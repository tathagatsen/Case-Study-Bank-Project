import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  email: "",
  firstName: "",
  lastName: "",
  phoneNumber: "",
  role: "",
  userId: null,
  verified: false
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    // Call this after successful verification/login
    setUser: (state, action) => {
      // action.payload should be the user object returned by the backend
      return { ...state, ...action.payload };
    },
    // Log out/reset
    clearUser: () => initialState,
  }
});

export const { setUser, clearUser } = userSlice.actions;
export const userReducer = userSlice.reducer;