import request from "@/common/axios";

/**
 *  锁定数据
 */
export const requestLockData = (body: Record<string, string | number | null | undefined>) => {
  return request({
    url: '/common/lock',
    method: 'post',
    data: body
  })
}

/**
 *  解锁数据
 */
export const requestUnlockData = (body: Record<string, string | number | null | undefined>) => {
  return request({
    url: '/common/unlock',
    method: 'post',
    data: body
  })
}
