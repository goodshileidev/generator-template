/**
 * Minimal JWT helper utilities for storing and decoding tokens on the client.
 */

const STORAGE_KEY = 'jwt_token';

export const getToken = (): string | null => {
  if (typeof window === 'undefined') {
    return null;
  }
  return window.localStorage.getItem(STORAGE_KEY);
};

export const setToken = (token: string) => {
  if (typeof window === 'undefined') {
    return;
  }
  window.localStorage.setItem(STORAGE_KEY, token);
};

export const removeToken = () => {
  if (typeof window === 'undefined') {
    return;
  }
  window.localStorage.removeItem(STORAGE_KEY);
};

const base64UrlDecode = (segment: string): string => {
  try {
    const base64 = segment.replace(/-/g, '+').replace(/_/g, '/');
    const padded = base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '=');
    const binary = atob(padded);
    // Handle unicode characters
    const chars = Array.from(binary).map((c) => c.charCodeAt(0));
    return decodeURIComponent(
      chars
        .map((code) => `%${code.toString(16).padStart(2, '0')}`)
        .join(''),
    );
  } catch (error) {
    console.error('[jwt] Failed to decode token segment', error);
    return '';
  }
};

export const getDecodedToken = (): Record<string, any> | null => {
  const token = getToken();
  if (!token) {
    return null;
  }
  const parts = token.split('.');
  if (parts.length !== 3) {
    return null;
  }
  try {
    const payload = base64UrlDecode(parts[1]);
    return JSON.parse(payload);
  } catch (error) {
    console.error('[jwt] Failed to parse token payload', error);
    return null;
  }
};

export type DecodedJwt = ReturnType<typeof getDecodedToken>;
