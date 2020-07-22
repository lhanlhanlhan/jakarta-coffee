import Vue from 'vue'
import Router from 'vue-router'
const Index = resolve => require(['/page/index'], resolve)
const Login = resolve => require(['/page/Login/login'], resolve)
const Home = resolve => require(['/page/Home/home'], resolve)
const GoodS = resolve => require(['/page/Goods/goods'], resolve)
const goodsDetails = resolve => require(['/page/Goods/goodsDetails'], resolve)
const Cart = resolve => require(['/page/Cart/cart'], resolve)
const order = resolve => require(['../page/Order/order'], resolve)
const user = resolve => require(['../page/User/user'], resolve)
const orderList = resolve => require(['../page/User/children/order'], resolve)
const information = resolve => require(['../page/User/children/information'], resolve)
const addressList = resolve => require(['../page/User/children/addressList'], resolve)
const coupon = resolve => require(['/page/User/children/coupon'], resolve)
const support = resolve => require(['/page/User/children/support'], resolve)
const checkout = resolve => require(['/page/Checkout/checkout'], resolve)
const register = resolve => require(['/page/Register/register'], resolve)
const payment = resolve => require(['/page/Order/payment'], resolve)
const paysuccess = resolve => require(['/page/Order/paysuccess'], resolve)

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/',
    component: Index,
    name: 'index',
    redirect: '/home',
    children: [
      { path: 'home', component: Home, meta: { title: '主页' } },
      { path: 'goods', component: GoodS, meta: { title: '浏览菜单' } },
      { path: 'goodsDetails', name: 'goodsDetails', component: goodsDetails, meta: { title: '商品详情' } }
    ]
  },
  {
    path: '/404',
    component: () => import('../page/404'),
    hidden: true
  },
  { path: '/login', name: 'login', component: Login, meta: { title: '登入' } },
  { path: '/cart', name: 'cart', component: Cart, meta: { title: '购物车' } },
  {
    path: '/order',
    name: 'order',
    component: order,
    children: [
      { path: 'paysuccess', name: 'paysuccess', component: paysuccess, meta: { title: '支付成功' } },
      { path: 'payment', name: 'payment', component: payment, meta: { title: '付款' } }
    ]
  },
  {
    path: '/user',
    name: 'user',
    component: user,
    redirect: '/user/information',
    children: [
      { path: 'information', name: 'Account Information', component: information, meta: { title: '个人资讯' } },
      { path: 'orderList', name: 'Order History', component: orderList, meta: { title: '历史订单' } },
      { path: 'addressList', name: 'Ship Addresses', component: addressList, meta: { title: '地址管理' } },
      { path: 'coupon', name: 'My Coupons', component: coupon, meta: { title: '我的优惠' } },
      { path: 'support', name: 'Support', component: support, meta: { title: '客户支援' } }
    ]
  },
  { path: '/checkout', name: 'checkout', component: checkout, meta: { title: '确认订单' } },
  { path: '/register', name: 'register', component: register, meta: { title: '注册账户' } },
  { path: '*', redirect: '/404' }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  // scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
