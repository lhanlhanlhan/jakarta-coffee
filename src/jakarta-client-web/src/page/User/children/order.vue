<template>
  <div>
    <y-shelf title="我的订单">
      <div slot="content">
        <div v-if="total">
          <div v-for="(item, i) in list" :key="i">
            <div class="gray-sub-title cart-title">
              <div class="first">
                <div>
                  <span class="order-id">订单号： <a>{{ item.order_id }}</a></span>
                  <span class="date"> 完成于 {{item.create_time | formatTimestamp}} </span>
                </div>
                <div class="f-bc">
                  <span class="price">单价</span>
                  <span class="num">数量</span>
<!--                  <span class="operation">操作</span>-->
                </div>
              </div>
              <div class="last">
                <span class="sub-total">实付金额</span>
                <span class="order-detail">状态</span>
              </div>
            </div>
            <div class="pr">
              <div class="cart" v-for="(good, j) in item.items" :key="j">
                <div class="cart-l" :class="{ bt:j > 0 }">
                  <div class="car-l-l">
                    <div class="img-box">
                      <img
                      :src = "good.photo_uri === 'null' ? basicFoodUrl : good.photo_uri"
                      alt="" />
                    </div>
                    <div class="ellipsis">{{ good.name }}</div>
                  </div>
                  <div class="cart-l-r">
                    <div>$ {{(good.t_price / good.item_count).toFixed(2)}}</div>
                    <div class="num">{{good.item_count}}</div>
<!--                    <div class="type"><a @click="delOrder(item.order_id, i)" v-if="j < 1"-->
<!--                                         class="del-order">删除此订单</a>-->
<!--                    </div>-->
                  </div>
                </div>
                <div class="cart-r">
                  <span></span>
                  <span></span>
                </div>
              </div>
              <div class="prod-operation pa" style="right: 0;top: 0;">
                <div class="total">$ {{ item.total_price }}</div>
                <div class="status"> {{'已确认'}}  </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <div style="padding: 100px 0;text-align: center">
            你还未有创建过订单
          </div>
        </div>
      </div>
    </y-shelf>

  </div>
</template>
<script>
  import { fetchOrders } from '../../../api/orders'
  import YShelf from '/components/shelf'
  export default {
    data () {
      return {
        basicFoodUrl: require('../../../../static/images/coffeeicon.png'),
        list: undefined,
        total: 0
      }
    },
    methods: {
      getOrderList () {
        fetchOrders().then(result => {
          const { data, size } = result
          console.log(data)
          this.list = data
          this.total = size
        })
      },
      // TODO - 删除订单
      delOrder (orderId, i) {
        this.orderList.splice(i, 1)
      }
    },
    filters: {
      formatTimestamp (timestamp) {
        // 对Date的扩展，将 Date 转化为指定格式的String
        // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
        // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
        // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
        // (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
        const _formatDate = (date, fmt) => {
          let o = {
            'M+': date.getMonth() + 1,                 // 月份
            'd+': date.getDate(),                    // 日
            'h+': date.getHours(),                   // 小时
            'm+': date.getMinutes(),                 // 分
            's+': date.getSeconds()                 // 秒
          }
          if (/(y+)/.test(fmt)) {
            fmt = fmt
              .replace(RegExp.$1, (date.getFullYear() + '')
                .substr(4 - RegExp.$1.length))
          }
          for (let k in o) {
            if (new RegExp('(' + k + ')').test(fmt)) {
              fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
            }
          }
          return fmt
        }
        let date = new Date(timestamp * 1000)
        return _formatDate(date, 'dd/MM/yyyy hh:mm:ss')
      }
    },
    created () {
      this.getOrderList()
    },
    components: {
      YShelf
    }
  }
</script>
<style lang="scss" scoped>
  @import "../../../assets/style/mixin";

  .gray-sub-title {
    height: 38px;
    padding: 0 24px;
    background: #EEE;
    border-top: 1px solid #DBDBDB;
    border-bottom: 1px solid #DBDBDB;
    line-height: 38px;
    font-size: 12px;
    color: #666;
    display: flex;
    span {
      display: inline-block;
      height: 100%;
    }
    .first {
      display: flex;
      justify-content: space-between;
      flex: 1;
      .f-bc {
        > span {
          width: 112px;
          text-align: center;
        }
      }
    }
    .last {
      width: 230px;
      text-align: center;
      display: flex;
      border-left: 1px solid #ccc;
      span {
        flex: 1;
      }
    }
  }

  .bt {
    border-top: 1px solid #EFEFEF;
  }

  .date {
    padding-left: 6px;
  }

  .order-id {
    margin-left: 5px;
  }

  .cart {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    &:hover {
      .del-order {
        display: block;
      }
    }
    .del-order {
      display: none;
    }
    .cart-l {
      display: flex;
      align-items: center;
      flex: 1;
      padding: 15px 0;
      justify-content: space-between;
      position: relative;
      &:before {
        position: absolute;
        content: ' ';
        right: -1px;
        top: 0;
        width: 1px;
        background-color: #EFEFEF;
        height: 100%;
      }
      .ellipsis {
        margin-left: 20px;
        width: 220px;
      }
      .img-box {
        border: 1px solid #EBEBEB;
      }
      img {
        display: block;
        @include wh(80px);
      }
      .cart-l-r {
        display: flex;
        > div {
          text-align: center;
          width: 112px;
        }
      }
      .car-l-l {
        display: flex;
        align-items: center;
      }
    }
    .cart-r {
      width: 230px;
      display: flex;
      span {
        text-align: center;
        flex: 1;
      }
    }
  }

  .prod-operation {
    height: 110px;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 254px;
    div {
      width: 100%;
      text-align: center;
    }
    div:last-child {
      padding-right: 24px;
    }
  }
</style>
