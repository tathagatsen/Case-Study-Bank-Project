package com.project.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.LoginRequestDto;
import com.project.dto.PassWordUpdateDto;
import com.project.dto.RegisterDto;
import com.project.dto.EmailDto;
import com.project.model.AccountLock;
import com.project.model.LoginHistory;
//import com.project.model.Role;
import com.project.model.TemporaryToken;
import com.project.model.TemporaryToken.Purpose;
import com.project.model.TwoFactorAuth;
import com.project.model.UserCredential;
import com.project.model.Role.RoleType;
import com.project.repository.AccountLockRepository;
import com.project.repository.LoginHistoryRepository;
import com.project.repository.RoleRepository;
import com.project.repository.TemporaryTokenRepository;
import com.project.repository.TwoFactorAuthRepository;
import com.project.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

	private final EmailService emailService;

	@Autowired
	private UserRepository repository;

	@Autowired
	private OtpService otpService;

	@Autowired
	private PasswordEncoder passwordEncoder;

//	@Autowired
//	private RoleRepository roleRepository;

	@Autowired
	private LoginHistoryRepository loginHistoryRepository;

	@Autowired
	private AccountLockRepository accountLockRepository;

	@Autowired
	private TwoFactorAuthRepository twoFactorAuthRepository;

	@Autowired
	private TemporaryTokenRepository temporaryTokenRepository;
//	
//	 	@PostConstruct
//	    public void initRoles() {
//	        ensureRoleExists("CUSTOMER");
//	        ensureRoleExists("ADMIN");
//	        ensureRoleExists("SUPPORT");
//	    }
//
//	    private void ensureRoleExists(String roleName) {
//	        roleRepository.findByRoleName(roleName)
//	                .orElseGet(() -> {
//	                    Role role = new Role();
//	                    role.setRoleName(roleName.toString());
//	                    return roleRepository.save(role);
//	                });
//	    }
	private static final Set<String> ALLOWED_ROLES = Set.of("CUSTOMER", "ADMIN", "SUPPORT");

	UserService(EmailService emailService) {
		this.emailService = emailService;
	}

	private AccountLock getOrCreateLock(UserCredential user) {
        AccountLock lock = accountLockRepository.findByUser(user);
        if (lock == null) {
            lock = new AccountLock();
            lock.setUser(user);
            lock.setFails(0);
            lock.setActive(false);
            accountLockRepository.save(lock);
        }
        // normalize null fails
        if (lock.getFails() == null) lock.setFails(0);
        return lock;
    }

    private TwoFactorAuth getOrCreate2FA(UserCredential user) {
        TwoFactorAuth tfa = twoFactorAuthRepository.findByUser(user);
        if (tfa == null) {
            tfa = new TwoFactorAuth();
            tfa.setUser(user);
            tfa.setEnabled(false);
            twoFactorAuthRepository.save(tfa);
        }
        return tfa;
    }

    private TemporaryToken createTempToken(UserCredential user, Purpose purpose, long minutesToLive) {
        TemporaryToken token = new TemporaryToken();
        token.setUser(user);
        token.setPurpose(purpose);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(minutesToLive));
        token.setToken(java.util.UUID.randomUUID().toString());
        return temporaryTokenRepository.save(token);
    }

    private void recordLogin(UserCredential user, boolean success) {
        LoginHistory h = new LoginHistory();
        h.setUser(user);
        h.setLoginTime(LocalDateTime.now());
        h.setSuccess(success);
        loginHistoryRepository.save(h);
    }

    private long generate10DigitNumber() {
        return 1_000_000_000L + (long) (new Random().nextDouble() * 9_000_000_000L);
    }

	// ---------- Register ----------

	@Transactional
	public String registerUser(RegisterDto registerDto) {
		if (repository.findByEmail(registerDto.getEmail()).isPresent()) {
			throw new RuntimeException("Email already registered");
		}

		UserCredential user = new UserCredential();
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		user.setVerified(false);
//        Role roleEntity = roleRepository.findByRoleName(registerDto.getRole())
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//        user.setRole(roleEntity);
// // if Role is entity, ensure frontend sends roleId or you fetch by RoleType
//		String role = Optional.ofNullable(registerDto.getRole()).map(String::trim).map(String::toUpperCase)
//				.orElse("CUSTOMER");
//
//		if (!ALLOWED_ROLES.contains(role)) {
//			throw new RuntimeException("Invalid role: " + role);
//		}
		user.setRole(registerDto.getRole());
		user.setFirstName(registerDto.getFirstName());
		user.setLastName(registerDto.getLastName());
		user.setPhoneNumber(registerDto.getPhoneNumber());

		// init account lock & 2FA rows
		repository.save(user);
		getOrCreateLock(user);
		getOrCreate2FA(user);

		// send OTP + create token limited for registration verification
		String otp = otpService.generateOtp();
		user.setOtp(otp);
		repository.save(user);

		createTempToken(user, Purpose.EMAIL_VERIFY, 10); // e.g., 10 minutes
		otpService.sendOtpEmail(user.getEmail(), otp, "Registration");
		return "Registration initiated. Please verify OTP.";
	}

	// Shared for /register/verification and /login/verification
	@Transactional
	public void verifyOtp(String email, String otp, boolean isLoginFlow) {
		UserCredential user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// token & purpose gate
		Purpose purpose = isLoginFlow ? Purpose.EMAIL_VERIFY : Purpose.EMAIL_VERIFY; // same purpose here
		TemporaryToken token = temporaryTokenRepository.findTopByUserAndPurposeOrderByExpiryTimeDesc(user, purpose)
				.orElseThrow(() -> new RuntimeException("No OTP token found or already used"));

		if (LocalDateTime.now().isAfter(token.getExpiryTime())) {
			temporaryTokenRepository.delete(token); // expire it
			throw new RuntimeException("OTP expired");
		}

		if (user.getOtp() == null || !user.getOtp().equals(otp)) {
			recordLogin(user, false);
			throw new RuntimeException("Invalid OTP");
		}

		// success
		user.setOtp(null);
		repository.save(user);
		temporaryTokenRepository.delete(token);

		if (!isLoginFlow) {
			// registration verification
			if (user.isVerified()) {
				throw new RuntimeException("User already verified");
			}
			user.setVerified(true);
			user.setUserId(generate10DigitNumber());
			repository.save(user);
		} else {
			// login verification success
			recordLogin(user, true);
			// Optionally: reset lock fails
			AccountLock lock = getOrCreateLock(user);
			lock.setActive(false);
			lock.setFails(0);
			lock.setLockedAt(null);
			lock.setUnlockAt(null);
			accountLockRepository.save(lock);
		}
	}

	// ---------- Login ----------

	@Transactional
	public void initiateLogin(LoginRequestDto loginRequestDto) {
		UserCredential user = repository.findByEmail(loginRequestDto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		AccountLock lock = getOrCreateLock(user);

		// auto-unlock if time passed
		if (lock.isActive() && lock.getUnlockAt() != null && LocalDateTime.now().isAfter(lock.getUnlockAt())) {
			lock.setActive(false);
			lock.setFails(0);
			lock.setLockedAt(null);
			lock.setUnlockAt(null);
			accountLockRepository.save(lock);
		}

		if (lock.isActive()) {
			throw new RuntimeException("Account is locked until " + lock.getUnlockAt());
		}

		// password check
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
			int fails = (lock.getFails() == null ? 0 : lock.getFails()) + 1;
			lock.setFails(fails);

			if (fails >= 3) { // threshold
				lock.setLockedAt(LocalDateTime.now());
				lock.setUnlockAt(LocalDateTime.now().plusMinutes(30)); // or 2, as you had
				lock.setActive(true);
			}
			accountLockRepository.save(lock);

			recordLogin(user, false);
			throw new RuntimeException("Incorrect credentials");
		}

		// password OK
		TwoFactorAuth tfa = getOrCreate2FA(user);
		if (tfa.isEnabled()) {
			String otp = otpService.generateOtp();
			user.setOtp(otp);
			repository.save(user);

			createTempToken(user, Purpose.EMAIL_VERIFY, 5);
			otpService.sendOtpEmail(user.getEmail(), otp, "Login");

			// Do not record success yet — record after OTP verification
		} else {
			// no 2FA: login success
			recordLogin(user, true);
			// reset lock state
			lock.setFails(0);
			lock.setActive(false);
			lock.setLockedAt(null);
			lock.setUnlockAt(null);
			accountLockRepository.save(lock);
		}
	}

	// ---------- Password Reset ----------

	@Transactional
	public void initiateChange(EmailDto emailDto) {
		UserCredential user = repository.findByEmail(emailDto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String otp = otpService.generateOtp();
		user.setOtp(otp);
		repository.save(user);

		createTempToken(user, Purpose.PASSWORD_RESET, 10);
		otpService.sendOtpEmail(emailDto.getEmail(), otp, "Password Reset");
	}

	@Transactional
	public void verifyUpdate(PassWordUpdateDto passWordUpdateDto) {
		UserCredential user = repository.findByEmail(passWordUpdateDto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		// fetch the latest valid reset token
		TemporaryToken token = temporaryTokenRepository
				.findTopByUserAndPurposeOrderByExpiryTimeDesc(user, Purpose.PASSWORD_RESET)
				.orElseThrow(() -> new RuntimeException("No password reset token found"));

		if (LocalDateTime.now().isAfter(token.getExpiryTime())) {
			temporaryTokenRepository.delete(token);
			throw new RuntimeException("OTP expired");
		}

		if (user.getOtp() == null || !user.getOtp().equals(passWordUpdateDto.getOtp())) {
			throw new RuntimeException("Incorrect OTP");
		}

		// Update password
		user.setPassword(passwordEncoder.encode(passWordUpdateDto.getNewPassword()));
		user.setOtp(null);
		repository.save(user);

		// Invalidate token
		temporaryTokenRepository.delete(token);
	}

	// ---------- 2FA ----------

	@Transactional
	public String enable2Fa(EmailDto twoFADto) {
		UserCredential user = repository.findByEmail(twoFADto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		TwoFactorAuth tfa = getOrCreate2FA(user);
		if (tfa.isEnabled())
			return "Two factor already enabled";

		tfa.setEnabled(true);
		twoFactorAuthRepository.save(tfa);
		return "Two factor enabled";
	}
}
