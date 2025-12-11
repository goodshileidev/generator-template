import type {AxiosInstance} from 'axios'
import axios from "axios";
import {message} from 'antd'

export const baseUrl = '/api'
const instance: AxiosInstance = axios.create({
  baseURL: baseUrl,
  timeout: 5000,
  responseType: 'json',
  headers: {
    'Content-Type': 'application/json',
  },
})
instance.interceptors.request.use((request: any) => {
  // 在请求发送之前做一些处理，比如token
  let token = localStorage.getItem("token")
  if (token === null) {
    token = "test"
  }

  request.headers.set('token', token)
  console.debug("before request", request)
  return request
}, (error: any) => {
  // 处理错误
  return Promise.reject(error)
})

/**
 * 在响应返回之前做一些处理
 */
instance.interceptors.response.use((response: any) => {
  console.debug("before response succeed", response)
  if (response.data.message !== null && response.data.message !== '') {
    if (response.data.code !== 200) {
      message.error(response.data.message)
      // 弹出显示error
    } else {
      message.success(response.data.message)
      // 弹出显示info
    }
  }
  let token = response.data.token
  // 从response取token并保存
  localStorage.setItem("token", token)
  return response
}, (response: any) => {
  console.debug("before response failed", response)
  // 处理错误
  if (response.data.code !== 200) {
    if (response.data.message !== null && response.data.message !== '') {
      message.error(response.data.message)
    }
  }
  let token = response.data.token
  // 从response取token并保存
  localStorage.setItem("token", token)
  return Promise.reject(response)
})

// 异常token过期 重定向到 login
export default (req: any) => {
  return instance(req).then((res: any) => {
    return res.data
  })
}
