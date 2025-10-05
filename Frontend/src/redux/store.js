import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { useDispatch } from "react-redux";
import { counterReducer } from "./counter";
import { accountsReducer } from "./Slice/AccountSlice";
import { userReducer } from "./Slice/User";
import storage from "redux-persist/lib/storage"; // defaults to localStorage for web
import persistReducer from "redux-persist/es/persistReducer";
import persistStore from "redux-persist/es/persistStore";
import {
  FLUSH,
  REHYDRATE,
  PAUSE,
  PERSIST,
  PURGE,
  REGISTER,
} from "redux-persist";

const persistConfig = {
  key: "root",
  storage,
  // whitelist: ["user"],  // persist only 'user' slice
  // blacklist: ["temp"],  // blacklist slices if needed
};

const rootReducer = combineReducers({
  counter: counterReducer,
  accounts: accountsReducer,
  user: userReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }),
});

export default store;

export const persistor = persistStore(store);

// Custom hook for dispatch
export const useAppDispatch = () => useDispatch();
