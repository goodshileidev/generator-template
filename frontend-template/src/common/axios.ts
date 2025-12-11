import type {AxiosInstance} from 'axios'
import axios from "axios";
import {message} from 'antd'

export const baseUrl = '/api'
const instance: AxiosInstance = axios.create({
  baseURL: baseUrl,
  timeout: 600000,
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
  request.headers['token'] = token
  const projectId = window.localStorage.getItem('projectId')
  if (projectId && request.data) {
    if (request.url.indexOf("/create") > -1 || request.url.indexOf("/save") > -1 || request.url.indexOf("Save") > -1 || request.url.indexOf("Create") > -1) {
      request.data.projectId = projectId
    } else {
      request.data.projectIdCondition = projectId
    }
  }
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
  // 弹出显示info
  if (response.data.message !== null && response.data.message !== '') {
    if (response.data.code !== 200) {
      message.error(response.data.message)
    } else {
      // TODO 成功弹框不能放这里，放到页面ui部分
      // message.success(response.data.msg)
    }
  }
  let token = response.data.token
  // 从response取token并保存
  localStorage.setItem("token", token)
  return response
}, (commit: any) => {
  const response = commit.response
  console.debug("before response failed", response)
  // 处理错误
  if (response.status !== 200) {
    message.error(response.status + ":" + response.statusText)
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
