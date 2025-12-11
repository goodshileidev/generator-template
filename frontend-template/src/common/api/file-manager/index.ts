import request from '@/common/axios';

export const requestCreateBase64Image = (body: {
  base64Image: string;
  fileName?: string;
}, bizCategory: string = 'page_layout') => {
  return request({
    url: `/common/file-manager/create-cut-image/${bizCategory}`,
    method: 'post',
    data: body
  })
}
