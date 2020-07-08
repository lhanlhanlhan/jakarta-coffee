import request from '@/utils/request'

// 拉取管理员列表api
export function fetchAdminList(query) {
  return request({
    url: '/users-api/admin/get-admin-users',
    method: 'get',
    params: query
  })
}

// 拉取用户列表api
export function fetchClientList(query) {
  return request({
    url: '/users-api/admin/get-client-users',
    method: 'get',
    params: query
  })
}

export function createAdmin(data) {
  return request({
    url: '/users-api/admin/add-admin',
    method: 'post',
    data
  })
}

export function createClient(data) {
  return request({
    url: '/users-api/admin/add-client',
    method: 'post',
    data
  })
}

// 更新一个客户的所有信息
export function updateClient(data) {
  return request({
    url: '/users-api/admin/set-client-info',
    method: 'post',
    data
  })
}

// 删掉一个客户
export function deleteClient(name) {
  return request({
    url: '/users-api/admin/remove-client',
    method: 'post',
    params: { 'name': name }
  })
}
