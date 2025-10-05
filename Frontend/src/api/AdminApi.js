import axios from "axios";

const API = axios.create({ baseURL: "http://localhost:8086/" });

export const deactivateCustomer = (customerId) =>
  API.put(`/deactivation/` + customerId);

export const getRecentActivities = () =>
  API.get('/activity/recent');

