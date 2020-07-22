import request from '../utils/request'

// 获取热门菜单 - LH
export const fetchHot = (limit) => {
  return request({
    url: '/menus-api/client/get-hot',
    method: 'get',
    params: {
      limit: limit
    }
  })
}

// 获取菜单 - LH
export const fetchMenu = (query) => {
  return request({
    url: '/menus-api/client/get-menu',
    method: 'get',
    params: query
  })
}

// 获取某个产品 - LH
export const fetchItemInfo = (id) => {
  return request({
    url: '/menus-api/client/get-item',
    method: 'get',
    params: {
      id: id
    }
  })
}
