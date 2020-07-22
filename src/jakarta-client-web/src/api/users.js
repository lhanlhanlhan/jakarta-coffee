import request from '../utils/request'

// LH - 用户登录！
export const login = (data) => {
  return request({
    // 改成项目的登录api
    url: '/users-api/client-login',
    method: 'post',
    data
  })
}

// LH - 更新信息！
export function updateInfo (data) {
  return request({
    // 改成项目的更新api
    url: '/users-api/client/set-my-info',
    method: 'post',
    data
  })
}

// LH - 获取用户个人资讯
export function getInfo (token) {
  return request({
    // 改成项目的获取用户信息api
    url: '/users-api/client/get-my-info',
    method: 'get'
    // token会被自己加上去，不用管！
  })
}

// TODO - LH - 获取用户收件地址
export function getAddrList () {
  return request({
    // 改成项目的获取用户信息api
    url: '/users-api/client/get-my-addr',
    method: 'get'
    // token会被自己加上去，不用管！
  })
}

// LH - 登出
export function logout (token) {
  return request({
    url: '/users-api/client/logout',
    method: 'post'
    // token会被自己加上去，不用管！
  })
}

// LH - 注册
export function register (data) {
  return request({
    url: '/users-api/register',
    method: 'post',
    // token会被自己加上去，不用管！
    data
  })
}
