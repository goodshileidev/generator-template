import axios, {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios';
import {message, notification} from 'antd';
import {clearCurrentUser} from '@/common/util/user_storage';
import {getToken, removeToken, setToken} from '@/utils/jwt';

export const baseUrl = '/api';

export enum ErrorShowType {
  SILENT = 0,
  WARN_MESSAGE = 1,
  ERROR_MESSAGE = 2,
  NOTIFICATION = 3,
  REDIRECT = 9,
}

export interface ResponseStructure<T = any> {
  success?: boolean;
  code?: number;
  data?: T;
  message?: string;
  showType?: ErrorShowType;
}

const instance: AxiosInstance = axios.create({
  baseURL: baseUrl,
  timeout: 60000,
  responseType: 'json',
  headers: {
    'Content-Type': 'application/json',
  },
});

const redirectToLogin = () => {
  if (window.location.pathname !== '/user/login') {
    window.location.href = '/user/login';
  }
};

instance.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = getToken();
    if (token && config.headers) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    if (config.data && typeof config.data === 'object') {
      const language = window.localStorage.getItem('lang');
      if (language) {
        (config.data as Record<string, any>).language = language;
      }
    }
    return config;
  },
  (error) => Promise.reject(error),
);

instance.interceptors.response.use(
  (response: AxiosResponse<ResponseStructure>) => {
    const authHeader = response.headers?.authorization || response.headers?.Authorization;
    if (authHeader) {
      const token = authHeader.startsWith('Bearer ') ? authHeader.substring(7) : authHeader;
      setToken(token);
    }

    const skipErrorHandler = response.config?.skipErrorHandler === true;
    const payload = response.data;
    if (payload && payload.success === false && !skipErrorHandler) {
      const displayMessage = payload.message || 'Request failed';
      switch (payload.showType) {
        case ErrorShowType.WARN_MESSAGE:
          message.warning(displayMessage);
          break;
        case ErrorShowType.ERROR_MESSAGE:
          message.error(displayMessage);
          break;
        case ErrorShowType.NOTIFICATION:
          notification.error({
            message: `Error ${payload.code || ''}`.trim(),
            description: displayMessage,
          });
          break;
        default:
          message.error(displayMessage);
      }
      return Promise.reject(new Error(displayMessage));
    }

    return response;
  },
  (error) => {
    const skipErrorHandler = error?.config?.skipErrorHandler === true;
    if (skipErrorHandler) {
      return Promise.reject(error);
    }

    const {response} = error || {};
    if (response) {
      const {status, data} = response;
      if (status === 401 || status === 403) {
        message.error('Session expired, please login again');
        removeToken();
        clearCurrentUser();
        redirectToLogin();
        return Promise.reject(error);
      }
      const displayMessage = data?.message || `Request failed (${status})`;
      message.error(displayMessage);
    } else {
      message.error('Network error, please retry');
    }
    return Promise.reject(error);
  },
);

export default <T = any>(req: AxiosRequestConfig): Promise<ResponseStructure<T>> => {
  return instance(req).then((res) => res.data as ResponseStructure<T>);
};
