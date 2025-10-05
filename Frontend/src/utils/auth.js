// src/utils/auth.js
export function getAuthEmailFromCookie() {
  const match = document.cookie.match(new RegExp('(^| )pqr_auth=([^;]+)'));
  return match ? decodeURIComponent(match[2]) : null;
}

export function isUserAuthenticated() {
  return !!getAuthEmailFromCookie();
}

export function logout() {
  document.cookie = 'pqr_auth=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
  // navigate to login or homepage
}