import axios from "axios";
const API = axios.create({
    baseURL: "http://localhost:8081/user",
    // withCredentials: true, // Uncomment if using cookies/auth
});

// 1. Register User
export const registerUser = (registerDto) =>
    API.post("/register", registerDto);

// Register verification (OTP)
export const verifyRegistration = (otpDto) =>
    API.post("/register/verification", otpDto);

// 2. Login User
export const loginUser = (loginRequestDto) =>
    API.post("/login", loginRequestDto);

// Login verification (OTP)
export const verifyLogin = (otpDto) =>
    API.post("/login/verification", otpDto);

// Set new password
export const setNewPassword = (setPasswordDto) =>
    API.post("/setNewPassword", setPasswordDto);

// 3. Initiate Password Reset
export const initiatePasswordChange = (passwordUpdateNotificationDto) =>
    API.post("/updPwd", passwordUpdateNotificationDto);

// Confirm Password Change (via OTP/code/DTO)
export const verifyPasswordUpdate = (passWordUpdateDto) =>
    API.put("/verifyUpd", passWordUpdateDto);

// 4. Enable 2FA
export const enable2Fa = (emailDto) =>
    API.post("/enable2fa", emailDto);