import { createSlice } from "@reduxjs/toolkit";

const counterSlice = createSlice({
  name: "counter1",
  initialState: 0,
  reducers: {
    increment: (state) => state + 1,
    decrement: (state) => state - 1,
  },
});

export const { increment, decrement } = counterSlice.actions;
export const counterReducer = counterSlice.reducer;

// const [test, settest] = useState(0);

// settest(1);