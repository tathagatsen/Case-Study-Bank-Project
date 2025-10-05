// src/redux/accountsSlice.js
import { createSlice } from "@reduxjs/toolkit";

// Example initial state for your accounts list
const initialState = {
  list: [],
  loading: false, // You can control this in UI with manual actions if desired
  error: null,
};

const accountsSlice = createSlice({
  name: "accounts",
  initialState,
  reducers: {
    // Replaces the accounts list with the new array (e.g., after fetching from API)
    replaceAccounts: (state, action) => {
      state.list = action.payload;
    },
    // Adds a new account object to the list
    addAccount: (state, action) => {
      state.list.push(action.payload);
    },
    // Optionally, reset error/loading if you want
    setAccountsError: (state, action) => {
      state.error = action.payload;
    },
    setAccountsLoading: (state, action) => {
      state.loading = action.payload;
    },
    // Optional: clear the list (e.g. on logout)
    clearAccounts: (state) => {
      state.list = [];
      state.error = null;
      state.loading = false;
    },
  },
});

export const {
  replaceAccounts,
  addAccount,
  setAccountsError,
  setAccountsLoading,
  clearAccounts,
} = accountsSlice.actions;

export const accountsReducer = accountsSlice.reducer;