<template>
  <div class="w" style="padding-bottom: 100px;">
    <y-shelf title="支付订单">
      <div slot="content">
        <div v-if="hasErr" class="box-inner order-info">
          <h3>提交订单成功</h3>
          <p class="payment-detail">请在 <span>24 小时内</span>完成支付，超时订单将自动取消。</p>
          <p class="payment-detail">敬请留意：你的订单内的个别餐点因当前缺货等原因下单失败，请阁下移步「订单历史」查询详情。</p>
        </div>
        <div v-else class="box-inner order-info">
          <h3>提交订单成功</h3>
          <p class="payment-detail">请在 <span>24 小时内</span>完成支付，超时订单将自动取消。</p>
          <p class="payment-detail">我们将在收到您的款项后即刻制作您的餐点</p>
        </div>
        <!--支付方式-->
        <div class="pay-type">
          <div class="p-title">支付方式</div>
          <div class="pay-item">
            <div :class="{ active:payType===1 }" @click="payType=1"><img src="/static/images/alipay@2x.png" alt=""></div>
            <div :class="{ active:payType===2 }" @click="payType=2"><img src="/static/images/weixinpay@2x.png" alt=""></div>
            <div :class="{ active:payType===3 }" @click="payType=3"><img src="/static/images/unionpaylogo.png" alt=""></div>
            <div :class="{ active:payType===4 }" @click="payType=4"><img src="/static/images/visalogo.png" alt=""></div>
            <div :class="{ active:payType===5 }" @click="payType=5"><img src="/static/images/masterlogo.png" alt=""></div>
            <div :class="{ active:payType===6 }" @click="payType=6"><img src="/static/images/jcblogo.png" alt=""></div>
            <div :class="{ active:payType===7 }" @click="payType=6"><img src="/static/images/americanexp.png" alt=""></div>
          </div>
        </div>
        <!--明细-->
        <div>
          <div class="box-inner">
            <div>
              <span>
                应付金额:
              </span>
              <em><span>$</span>{{ this.price.toFixed(2) }}</em>
              <y-button text="现在支付"
                        classStyle="main-btn"
                        style="width: 120px;height: 40px;font-size: 16px;line-height: 38px"
                        @btnClick="paySuc()"
              ></y-button>
            </div>
          </div>
        </div>
      </div>
    </y-shelf>
  </div>
</template>
<script>
  import YShelf from '../../components/shelf'
  import YButton from '../../components/YButton'
  export default {
    data () {
      return {
        hasErr: false,
        payType: 1,
        price: 0,
        // 以下不是我的！
        addList: {},
        addressId: 0,
        productId: '',
        num: ''
      }
    },
    methods: {
      // TODO - 点击了「现在支付」
      paySuc () {
        // TODO - 点击了「现在支付」
      }
    },
    created () {
      let params = this.$route.params
      if (params.price !== undefined) {
        this.hasErr = params.hasErr
        this.price = params.price
      } else {
        // 没有参数传入，请返回购物车
        this.$message(
          '您进入了非法页面！'
        )
        this.$router.replace({ path: 'cart' })
      }
    },
    components: {
      YShelf,
      YButton
    }
  }
</script>
<style lang="scss" scoped rel="stylesheet/scss">
  .w {
    padding-top: 39px;
  }

  .order-info {
    padding: 60px 0 55px;
    color: #333;
    background: #fff !important;
    h3 {
      padding-bottom: 14px;
      line-height: 36px;
      text-align: center;
      font-size: 36px;
      color: #212121;
    }
    .payment-detail {
      text-align: center;
      line-height: 24px;
      font-size: 14px;
      color: #999;
    }
  }

  /*支付类型*/
  .pay-type {
    margin: 0 auto;
    width: 90%;
    padding-bottom: 30px;
    .p-title {
      font-size: 18px;
      line-height: 40px;
      padding: 0 10px;
      position: relative;
      &:before {
        content: ' ';
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        border-bottom: 1px solid #ccc;
      }
    }

  }

  .pay-type {
    .pay-item {
      display: flex;
      align-items: center;
      div {
        margin-top: 20px;
        width: 175px;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid #E5E5E5;
        cursor: pointer;
        border-radius: 6px;
        margin-right: 10px;
        background: #FAFAFA;
        &.active {
          border-color: #6A8FE5;
          background: #fff;
        }
        img {
          display: block;
          height: 34px;
          margin: 8px auto;
        }
      }
    }
  }

  .box-inner {
    line-height: 60px;
    background: #f9f9f9;
    border-top: 1px solid #e5e5e5;
    box-sizing: border-box;
    > div {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      padding: 0 20px;
    }
    em {
      margin-left: 5px;
      font-size: 24px;
      color: #d44d44;
      font-weight: 700;
      margin-right: 10px;
      span {
        margin-right: 4px;
        font-size: 16px;

      }
    }
  }

  .confirm-detail {
    padding: 0 30px 25px;
    border-top: 1px solid #d5d5d5;
    .info-title {
      height: 14px;
      margin: 30px 0 17px;
      line-height: 14px;
      font-weight: bolder;
      color: #333;
    }
    .info-detail {
      line-height: 24px;
      color: #666;
    }
  }

  .confirm-table-title {
    padding: 3px 0 0 30px;
    border-top: 1px solid #D5D5D5;
    line-height: 54px;
    font-weight: bolder;
    color: #000;
    display: flex;
    justify-content: space-between;
    span {
      display: inline-block;
      width: 175px;
      text-align: center;
    }
  }

  .confirm-goods-table {
    border-top: 1px solid #D5D5D5;
    .cart-items {
      height: 80px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      a {
        color: #333;
      }
    }
    .n-b {
      display: flex;
      align-items: center;
      justify-content: center;
      > div {
        color: #626262;
        font-weight: 700;
        width: 175px;
        text-align: center;
      }
    }
  }

  .order-discount-line {
    padding: 22px 30px 53px;
    border-top: 1px solid #D5D5D5;
    line-height: 24px;
    text-align: right;
    font-size: 12px;
    &:first-child {
      line-height: 32px;
      text-align: right;
      font-size: 14px;
      font-weight: bolder;
    }
  }

  .name {
    width: 40%;
  }

  .name-cell {

  }
</style>
