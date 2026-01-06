import request from '@/common/axios';
import {queryResetPassword, querySendEmailVerifyCode} from '@/common/api/common/login';
import {clearCurrentUser} from '@/common/util/user_storage';
import {getDecodedToken, removeToken} from '@/utils/jwt';

export type LoginParams = {
  username: string;
  password: string;
  captchaId?: string;
  captchaCode?: string;
  language?: string;
};

export type LoginResult<T = any> = {
  code?: number;
  message?: string;
  success?: boolean;
  data?: T;
};

export type CaptchaPayload = {
  captchaId: string;
  image: string;
  expireAt: number;
};

export async function login(body: LoginParams) {
  return request<LoginResult>({
    url: '/login',
    method: 'POST',
    data: body,
    skipErrorHandler: true,
  });
}

export async function fetchCaptcha() {
  return request<{ data: CaptchaPayload }>({
    url: '/captcha',
    method: 'GET',
    params: {
      t: Date.now(),
    },
  });
}

export async function logout(params?: { username?: string }) {
  try {
    await request({
      url: '/logout',
      method: 'POST',
      data: params,
    });
  } finally {
    removeToken();
    clearCurrentUser();
  }
}

export async function getCurrentUser() {
  const decoded = getDecodedToken();
  if (decoded) {
    return {
      data: {
        ...decoded,
        operatorId: decoded.sub,
        username: decoded.username,
        displayName: decoded.displayName,
      },
    };
  }
  return request<{ data: any }>({
    url: '/currentUser',
    method: 'POST',
  });
}

export const sendEmailVerifyCode = (data: Record<string, any>) => {
  return querySendEmailVerifyCode(data);
};

export const resetPassword = (data: Record<string, any>) => {
  return queryResetPassword(data);
};
