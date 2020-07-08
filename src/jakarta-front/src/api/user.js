import request from '@/utils/request'

// 用户登录！
export function login(data) {
  return request({
    // 改成项目的登录api
    url: '/users-api/admin-login',
    method: 'post',
    data
  })
}

// 更新信息！
export function updateInfo(data) {
  return request({
    // 改成项目的更新api
    url: '/users-api/admin/set-my-info',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    // 改成项目的获取用户信息api
    url: '/users-api/admin/get-my-info',
    method: 'get'
    // token会被自己加上去，不用管！
  })
}

export function logout(token) {
  return request({
    url: '/users-api/admin/logout',
    method: 'post'
    // token会被自己加上去，不用管！
  })
}
