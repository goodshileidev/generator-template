import { requestCreateBase64Image } from '@/common/api/file-manager';

export const createBase64Image = (params: {
  base64Image: string;
  fileName?: string;
  bizCategory?: string;
}) => {
  const url = params.base64Image.replace(/^data:image\/\w+;base64,/, '');
  return requestCreateBase64Image({
    base64Image: url,
    fileName: params.fileName
  }, params.bizCategory).then(res => {
    return res.data;
  })
}
