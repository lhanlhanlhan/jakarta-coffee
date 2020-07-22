import request from '../utils/request'

// 获取购物车 - LH
export const fetchCart = () => {
  return request({
    url: '/carts-api/client/get-cart',
    method: 'get'
  })
}

// 改变购物车 - LH
export const changeCart = (param) => {
  return request({
    url: '/carts-api/client/edit-cart',
    method: 'post',
    params: param
  })
}

// 删除购物车项目 - LH
export const delFromCart = (param) => {
  return request({
    url: '/carts-api/client/del-from-cart',
    method: 'post',
    params: param
  })
}

// 加入购物车项目 - LH
export const addToCart = (param) => {
  return request({
    url: '/carts-api/client/add-cart',
    method: 'post',
    params: param
  })
}
