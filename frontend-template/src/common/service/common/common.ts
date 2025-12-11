import {requestLockData, requestUnlockData} from "@/common/api/common/common";

/**
 *  解锁数据
 */
export const lockData = (data: Record<string, string | number | null | undefined>) => {
  const params = JSON.parse(JSON.stringify(data))

  console.debug("LockData->param-converted", params)
  return requestLockData(params).then((response) => {
    let data = response.data
    if (data) {

    }
    console.debug("LockData->detail-converted", data)
    return response
  });
}

/**
 *  解锁数据
 */
export const unlockData = (data: Record<string, string | number | null | undefined>) => {
  const params = JSON.parse(JSON.stringify(data))

  console.debug("UnLockData->param-converted", params)
  return requestUnlockData(params).then((response) => {
    let data = response.data
    if (data) {

    }
    console.debug("UnLockData->detail-converted", data)
    return response
  });
}

