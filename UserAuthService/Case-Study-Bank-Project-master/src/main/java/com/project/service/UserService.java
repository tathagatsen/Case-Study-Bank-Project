package com.project.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.dto.*;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.exception.UnauthorizedException;
import com.project.feign.AdminInterface;
import com.project.feign.CustomerInterface;
import com.project.feign.NotificationInterface;
import com.project.api.ApiResponse;
import com.project.model.AccountLock;
//import com.project.model.LoginHistory;
//import com.project.model.Role;
//import com.project.model.TemporaryToken;
//import com.project.model.TemporaryToken.Purpose;
import com.project.model.TwoFactorAuth;
import com.project.model.UserCredential;

import com.project.repository.AccountLockRepository;
//import com.project.repository.LoginHistoryRepository;

//import com.project.repository.TemporaryTokenRepository;
import com.project.repository.TwoFactorAuthRepository;
import com.project.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AccountLockRepository accountLockRepository;

	@Autowired
	private TwoFactorAuthRepository twoFactorAuthRepository;

	@Autowired
	private CustomerInterface customerInterface;

	@Autowired
	private NotificationInterface notificationInterface;
	
	@Autowired
	private AdminInterface adminInterface;
	
	
	boolean checker=false;
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
//	private static final Set<String> ALLOWED_ROLES = Set.of("CUSTOMER", "ADMIN", "SUPPORT");

//	UserService(EmailService emailService) {
//		this.emailService = emailService;
//	}

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
		if (lock.getFails() == null)
			lock.setFails(0);
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

//    private TemporaryToken createTempToken(UserCredential user, Purpose purpose, long minutesToLive) {
//        TemporaryToken token = new TemporaryToken();
//        token.setUser(user);
//        token.setPurpose(purpose);
//        token.setExpiryTime(LocalDateTime.now().plusMinutes(minutesToLive));
//        token.setToken(java.util.UUID.randomUUID().toString());
//        return temporaryTokenRepository.save(token);
//    }

//    private void recordLogin(UserCredential user, boolean success) {
//        LoginHistory h = new LoginHistory();
//        h.setUser(user);
//        h.setLoginTime(LocalDateTime.now());
//        h.setSuccess(success);
//        loginHistoryRepository.save(h);
//    }

	private long generate10DigitNumber() {
		Random random = new Random(System.currentTimeMillis());
		long number;
		int attempts = 0;
		do {
			number = 1_000_000_000L + Math.abs(random.nextLong()) % 9_000_000_000L;
			attempts++;
			if (attempts > 10)
				throw new RuntimeException("Failed to generate unique userId");
		} while (repository.findByUserId(number).isPresent());
		return number;
	}
	
//	private CustomerLoginDto buildLoginDto(UserCredential user, HttpServletRequest request) {
//	    CustomerLoginDto dto = new CustomerLoginDto();
//	    dto.setCustId(user.getUserId());
//
//	    if (request != null) {
//	        String ip = request.getHeader("X-Forwarded-For");
//	        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
//	        dto.setIpAddress(ip);
//
//	        String agent = request.getHeader("User-Agent");
//	        dto.setDeviceInfo(agent != null ? agent : "Unknown");
//	    }
//	    return dto;
//	}


	private void validateEmail(String email) {
		if (email == null || email.isBlank())
			throw new BadRequestException("Email is required");
		if (!email.contains("@"))
			throw new BadRequestException("Invalid email format");
	}

	private void validatePhone(String phone) {
		if (phone == null || phone.isBlank())
			throw new BadRequestException("Phone number is required");
		if (!phone.matches("\\d{10}"))
			throw new BadRequestException("Phone number must be 10 digits");
	}

	private void validatePassword(String pwd) {
		if (pwd == null || pwd.length() < 6)
			throw new BadRequestException("Password must be at least 6 characters");
	}

	// ---------- Register ----------

	@Transactional
	public boolean registerUser(RegisterDto registerDto) {
		validateEmail(registerDto.getEmail());
		validatePhone(registerDto.getPhoneNumber());
		validatePassword(registerDto.getPassword());

		if (repository.existsByEmail(registerDto.getEmail()))
			throw new ConflictException("Email already registered");
		if (repository.existsByPhoneNumber(registerDto.getPhoneNumber()))
			throw new ConflictException("Phone already registered");

		UserCredential user = new UserCredential();
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		user.setVerified(false);
		user.setAddress(registerDto.getAddress());
		user.setUserId(generate10DigitNumber());
		user.setFirstName(registerDto.getFirstName());
		user.setLastName(registerDto.getLastName());
		user.setPhoneNumber(registerDto.getPhoneNumber());
		user.setDob(registerDto.getDob());
		user.setRole((registerDto.getRole() == null || registerDto.getRole().isBlank()) ? "CUSTOMER"
				: registerDto.getRole().trim().toUpperCase());
		repository.save(user);
		AccountLock lock= getOrCreateLock(user);
		TwoFactorAuth tFactorAuth= getOrCreate2FA(user);
		user.setAccountLock(lock);
		user.setTwoFactorAuth(tFactorAuth);

		// Trigger OTP via Notification service
		UserOTPNotificationDto udto = new UserOTPNotificationDto();
		udto.setEmail(user.getEmail());
		udto.setCustomerId(user.getUserId());
		boolean otpNotificationDto= notificationInterface.registerCustomer(udto);
		if(!otpNotificationDto) {
			return false;
		}
		return true;
		}

	@Transactional
	public UserCredential verifyRegistrationOtp(OtpDto otpDto) {
		validateEmail(otpDto.getEmail());
		if (otpDto.getOtp() == null || otpDto.getOtp().isBlank())
			throw new BadRequestException("OTP is required");

//		UserCredential user = repository.findByEmail(otpDto.getEmail())
				UserCredential user = repository.findByEmail(otpDto.getEmail())
				
			    .orElseThrow(() -> {
			        // Delete the OTP or related record before throwing the exception
			        
			        return new ResourceNotFoundException("User not found");
			    });
		if(user.isVerified()) {
			throw new BadRequestException("User already verified");
		}
		// Verify OTP with Notification service
		UserVerificationNotificationDto vdto = new UserVerificationNotificationDto();
		vdto.setEmail(otpDto.getEmail());
		vdto.setOtp(otpDto.getOtp());
		boolean ok = notificationInterface.verifyOtp(vdto);
		if (!ok)
			throw new UnauthorizedException("Invalid or expired OTP");
		
		AccountLock lock = getOrCreateLock(user);
		lock.setActive(false);
		lock.setFails(0);
		lock.setLockedAt(null);
		lock.setUnlockAt(null);
		accountLockRepository.save(lock);

		if (user.getUserId() != 0) {
			UserCustomerDto userCustomerDto=new UserCustomerDto();
			userCustomerDto.setAddress(user.getAddress());
			userCustomerDto.setCustomerId(user.getUserId());
			userCustomerDto.setDob(user.getDob());
			userCustomerDto.setEmail(user.getEmail());
			userCustomerDto.setName(user.getFirstName()+" "+user.getLastName());
			userCustomerDto.setPhone(user.getPhoneNumber());
			ResponseEntity<UserCustomerDto> resp = customerInterface.registerCustomer(userCustomerDto);
	        if (!resp.getStatusCode().is2xxSuccessful()) {
	            throw new IllegalStateException("Customer registration failed with status: " + resp.getStatusCode());
	        }
		}
		return user;
		}

	// ---------- Login ----------
	@Transactional
	public boolean initiateLogin(LoginRequestDto loginRequestDto) {
		validateEmail(loginRequestDto.getEmail());
		UserCredential user = repository.findByEmail(loginRequestDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
			throw new UnauthorizedException("Account is locked until " + lock.getUnlockAt());
		}

		if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
			int fails = (lock.getFails() == null ? 0 : lock.getFails()) + 1;
			lock.setFails(fails);
			if (fails >= 3) {
				lock.setLockedAt(LocalDateTime.now());
				lock.setUnlockAt(LocalDateTime.now().plusMinutes(30));
				lock.setActive(true);
				lock.setReason("Too many failed attempts");
			}
			accountLockRepository.save(lock);
			throw new UnauthorizedException("Incorrect credentials");
		}
		
		// password OK
		TwoFactorAuth tfa = getOrCreate2FA(user);
		if (tfa.isEnabled()) {
			// Send OTP for login verification
			UserOTPNotificationDto udto = new UserOTPNotificationDto();
			udto.setEmail(user.getEmail());
			udto.setCustomerId(user.getUserId());
			notificationInterface.registerCustomer(udto);
			return true;
			} else {
			// login success without 2FA
			lock.setFails(0);
			lock.setActive(false);
			lock.setLockedAt(null);
			lock.setUnlockAt(null);
			accountLockRepository.save(lock);

			// Inform Customer service about login success
//			if (user.getUserId() != 0) {
////			    CustomerLoginDto loginDto = buildLoginDto(user, request);
//				CustomerLoginDto loginDto=new CustomerLoginDto();
//				loginDto.setCustId(user.getUserId());
//				loginDto.setDeviceInfo("LAPTOP");
//				loginDto.setIpAddress(null);
//			    customerInterface.loginCustomer(loginDto);
//			}

			return true;
		}
	}
	
	@Transactional
	public UserCredential verifyLoginOtp(OtpDto otpDto) {
		validateEmail(otpDto.getEmail());
		if (otpDto.getOtp() == null || otpDto.getOtp().isBlank())
			throw new BadRequestException("OTP is required");

		UserCredential user = repository.findByEmail(otpDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// verify with Notification service
		UserVerificationNotificationDto vdto = new UserVerificationNotificationDto();
		vdto.setEmail(otpDto.getEmail());
		vdto.setOtp(otpDto.getOtp());
		boolean ok = notificationInterface.verifyOtp(vdto);
		if (!ok)
			throw new UnauthorizedException("Invalid or expired OTP");
		checker=true;
		AccountLock lock = getOrCreateLock(user);
		lock.setActive(false);
		lock.setFails(0);
		lock.setLockedAt(null);
		lock.setUnlockAt(null);
		accountLockRepository.save(lock);

		if (user.getUserId() != 0) {
//			CustomerLoginDto loginDto = buildLoginDto(user, request);
			CustomerLoginDto loginDto=new CustomerLoginDto();
			loginDto.setCustId(user.getUserId());
			loginDto.setDeviceInfo("LAPTOP");
		    customerInterface.loginCustomer(loginDto);
		}

		return user;
	}

	// Inform Customer service about login success
	// ---------- Password Reset ----------
	@Transactional
	public boolean initiateChange(PasswordUpdateNotificationDto udto) {
		validateEmail(udto.getEmail());
		UserCredential user = repository.findByEmail(udto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		udto.setCustomerId(user.getUserId());
		notificationInterface.initiatePasswordChange(udto);
		return true;
	}

	@Transactional
	public UserCredential verifyUpdate(PassWordUpdateDto passWordUpdateDto) {
	    validateEmail(passWordUpdateDto.getEmail());
	    validatePassword(passWordUpdateDto.getNewPassword());
	    
	    if (passWordUpdateDto.getOtp() == null || passWordUpdateDto.getOtp().isBlank()) {
	        throw new BadRequestException("OTP is required");
	    }

	    UserCredential user = repository.findByEmail(passWordUpdateDto.getEmail())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    UserVerificationNotificationDto vdto = new UserVerificationNotificationDto();
	    vdto.setEmail(passWordUpdateDto.getEmail());
	    vdto.setOtp(passWordUpdateDto.getOtp());

	    PasswordUpdateNotificationCheckDto pDto = new PasswordUpdateNotificationCheckDto();
	    pDto.setEmail(passWordUpdateDto.getEmail());
	    pDto.setCustomerId(user.getUserId());

	    boolean check = notificationInterface.verifyOtp(vdto);

	    if (!check) {
	        pDto.setStatus("FAILED");
	        notificationInterface.sendPasswordUpdateStatus(pDto);
	        throw new UnauthorizedException("Incorrect OTP");
	    }

	    // ✅ OTP verified
	    checker=true;
	    
	    repository.save(user);

	    pDto.setStatus("SUCCESS");
	    notificationInterface.sendPasswordUpdateStatus(pDto);

	    return user;
	}
	
	@Transactional
	public boolean setNewPassword(SetPasswordDto setPasswordDto) {
		// TODO Auto-generated method stub
		validateEmail(setPasswordDto.getEmail());
		UserCredential user = repository.findByEmail(setPasswordDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		if(checker) {
			user.setPassword(passwordEncoder.encode(setPasswordDto.getNewPassword()));
			repository.save(user);
			checker=false;
			return true;
		}
		return false;
	}

	// ---------- 2FA ----------
	@Transactional
	public ApiResponse enable2Fa(EmailDto emailDto) {
		validateEmail(emailDto.getEmail());
		UserCredential user = repository.findByEmail(emailDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		TwoFactorAuth tfa = getOrCreate2FA(user);
		if (tfa.isEnabled())
			return new ApiResponse(true, "Two factor already enabled");

		tfa.setEnabled(true);
		twoFactorAuthRepository.save(tfa);
		return new ApiResponse(true, "Two factor enabled");
	}

	public boolean registerAdmin(AdminRequestDto adminDto) {
		// TODO Auto-generated method stub
		UserCredential user=new UserCredential();
		user.setEmail(adminDto.getEmail());
		user.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		user.setVerified(true);
		user.setAddress(adminDto.getAddress());
		user.setUserId(generate10DigitNumber());
		user.setFirstName(adminDto.getFirstName());
		user.setLastName(adminDto.getLastName());
		user.setPhoneNumber(adminDto.getPhoneNumber());
		user.setDob(adminDto.getDob());
		user.setRole("ADMIN");
		
		adminInterface.createAdmin(adminDto);
		
		return true;
	}
	@Transactional
	public boolean initiateLoginAdmin(LoginRequestDto loginRequestDto) {
	    validateEmail(loginRequestDto.getEmail());

	    UserCredential user = repository.findByEmail(loginRequestDto.getEmail())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    AccountLock lock = getOrCreateLock(user);

	    // auto-unlock if expired
	    if (lock.isActive() && lock.getUnlockAt() != null && LocalDateTime.now().isAfter(lock.getUnlockAt())) {
	        lock.setActive(false);
	        lock.setFails(0);
	        lock.setLockedAt(null);
	        lock.setUnlockAt(null);
	        accountLockRepository.save(lock);
	    }

	    if (lock.isActive()) {
	        throw new UnauthorizedException("Account is locked until " + lock.getUnlockAt());
	    }

	    if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
	        int fails = (lock.getFails() == null ? 0 : lock.getFails()) + 1;
	        lock.setFails(fails);
	        if (fails >= 3) {
	            lock.setLockedAt(LocalDateTime.now());
	            lock.setUnlockAt(LocalDateTime.now().plusMinutes(30));
	            lock.setActive(true);
	            lock.setReason("Too many failed attempts");
	        }
	        accountLockRepository.save(lock);
	        
	        throw new UnauthorizedException("Invalid credentials");
	    }

	    // login success
	    lock.setFails(0);
	    accountLockRepository.save(lock);
	    

	    return true;
	}


}