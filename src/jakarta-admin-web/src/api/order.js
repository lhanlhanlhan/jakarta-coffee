
import request from '@/utils/request'

// 拉取订单
export function fetchOrders(query) {
  return request({
    url: '/orders-api/admin/get-all-orders',
    method: 'get',
    params: query
  })
}

// 拉取订单详细资讯
export function fetchDetailedOrder(id) {
  return request({
    url: '/orders-api/admin/get-order',
    method: 'get',
    params: {
      order_id: id
    }
  })
}

// 删除订单
export function removeOrder(id) {
  return request({
    url: '/orders-api/admin/remove-order',
    method: 'post',
    params: {
      order_id: id
    }
  })
}
