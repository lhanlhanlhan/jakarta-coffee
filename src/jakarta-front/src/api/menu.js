import request from '@/utils/request'

// 拉取菜单
export function fetchMenu(query) {
  return request({
    url: '/menus-api/admin/get-menu',
    method: 'get',
    params: query
  })
}

// 拉取详细菜单项目
export function fetchMenuItem(itemID) {
  return request({
    url: '/menus-api/admin/get-item',
    method: 'get',
    params: { 'id': itemID }
  })
}

// 创建菜单项目
export function createMenuItem(data) {
  return request({
    url: '/menus-api/admin/add-item',
    method: 'post',
    data
  })
}

// 更新菜单项目
export function updateMenuItem(data) {
  return request({
    url: '/menus-api/admin/set-item',
    method: 'post',
    data
  })
}

// 更新菜单项目
export function deleteMenuItem(itemId) {
  return request({
    url: '/menus-api/admin/remove-item',
    method: 'post',
    params: { 'id': itemId }
  })
}
