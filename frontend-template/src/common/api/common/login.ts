import request from '@/common/axios';

export const updateOperatorInfo = (body: Record<string, any>) => {
  return request({
    url: '/account/updateInfo',
    method: 'post',
    data: body,
  });
};

export const updateOperatorPassword = (body: Record<string, any>) => {
  return request({
    url: '/account/updatePassword',
    method: 'post',
    data: body,
  });
};

export const queryResetPassword = (body: Record<string, any>) => {
  return request({
    url: '/account/resetPassword',
    method: 'post',
    data: body,
  });
};

export const querySendEmailVerifyCode = (body: Record<string, any>) => {
  return request({
    url: '/account/sendEmailVerifyCode',
    method: 'post',
    data: body,
  });
};
