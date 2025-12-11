/**
 * 拼接URL路径
 * @param parts
 */
export const concatUrls = (...parts: (string | undefined)[]): string => {
  return parts
    .filter((part): part is string => part !== null)
    .map(part => part.trim())
    .filter(part => part !== '')
    .join('/')
    .replace(/([^:]\/)\/+/g, '$1'); // 保留协议中的双斜杠，替换其他重复斜杠
};

export const defaultPagination = {
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showTotal: (total) => `Total ${total} items`,
  size: 'small',
  pageSizeOptions: [10, 50, 100, 500, 1000]
}

export const createPagination = (pagination) => {
  return {...defaultPagination, ...pagination}
}
