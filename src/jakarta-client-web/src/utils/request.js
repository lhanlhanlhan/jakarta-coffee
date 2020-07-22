import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '../store'
import { getToken } from './auth'

// create an axios instance
const service = axios.create({
  // 这个项目好像已经有全局代理了！在./config/index.js里！
  // baseURL: '127.0.0.1:8080', // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // if the custom code is not 200, it is judged as an error.
    if (res.code !== 200) {
      switch (res.code) {
        // 401 Unauthorized - HAN
        case 401:
          // to re-login
          MessageBox.confirm('你已被系统登出账户，请重新登录', '告示', {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            store.dispatch('user/resetToken').then(() => {
              location.reload()
            })
          })
          break
        default:
          Message({
            message: res.message || '发生了未知错误',
            type: 'error',
            duration: 5 * 1000
          })
          return Promise.reject(new Error(res.message || '发生了未知错误'))
      }
    } else {
      return res
    }
  },
  error => {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
