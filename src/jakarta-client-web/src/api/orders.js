import request from '../utils/request'

// 获取用户的所有订单 - LH
export const fetchOrders = () => {
  return request({
    url: '/orders-api/client/get-all-orders',
    method: 'get'
  })
}

// 提交订单 - LH
export const submitOrder = (data) => {
  return request({
    url: '/orders-api/client/new-order',
    method: 'post',
    data
  })
}

// 删除订单 - LH
export const deleteOrder = (data) => {
  return request({
    url: '/orders-api/client/new-order',
    method: 'post',
    data
  })
}
