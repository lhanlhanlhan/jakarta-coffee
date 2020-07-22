<template>
  <div class="login v2">
    <div class="wrapper">
      <div class="dialog dialog-shadow" style="display: block; margin-top: -362px;">
        <div class="title" v-if="loginPage">
          <h4>使用您的 Jakarta ID 登录</h4></div>
        <div v-if="loginPage" class="content">
          <ul class="common-form">
            <li class="username border-1p">
              <div class="input">
                <el-input type="text" v-model="ruleForm.userName" placeholder="账号" />
              </div>
            </li>
            <li>
              <div class="input">
                <el-input v-model="ruleForm.userPwd" @keyup.enter="login" placeholder="密码" show-password/>
              </div>
            </li>
            <li style="text-align: right" class="pr">
              <span class="pa" style="top: 0;left: 0;color: #d44d44">{{ ruleForm.errMsg }}</span>
              <a href="javascript:;" style="padding: 0 5px" @click="gotoRegister">注册 Jakarta ID</a>
            </li>
          </ul>
          <!--登陆-->
          <div>
            <y-button text="登录" :classStyle="isLoginOk" @btnClick="login" class="btn"></y-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import YFooter from '/common/footer'
  import YButton from '/components/YButton'
  // import { addCartBatch } from '/api/goods'
  import { getStore } from '../../utils/storage'

  export default {
    data () {
      return {
        cart: [],
        loginPage: true,
        redirect: undefined,
        ruleForm: {
          userName: '',
          userPwd: '',
          errMsg: ''
        },
        registered: {
          userName: '',
          userPwd: '',
          userPwd2: '',
          errMsg: ''
        }
      }
    },
    computed: {
      // 可点击注册
      isRegOk () {
        const {userPwd, userPwd2, userName} = this.registered
        return userPwd && userPwd2 && userName ? 'main-btn' : 'disabled-btn'
      },
      isLoginOk () {
        const {userPwd, userName} = this.ruleForm
        return userPwd && userName ? 'main-btn' : 'disabled-btn'
      }
    },
    methods: {
      // 登陆时将本地的购物车合并添加到用户购物车
      login_addCart () {
        let cartArr = []
        let locaCart = JSON.parse(getStore('buyCart'))
        if (locaCart && locaCart.length) {
          cartArr = locaCart.map(item => {
            return {
              'productId': item.productId,
              'productNum': item.productNum
            }
          })
        }
        this.cart = cartArr
      },
      login () {
        const { userName, userPwd } = this.ruleForm
        if (!userName || !userPwd) {
          this.ruleForm.errMsg = '账号或者密码不能为空!'
        } else {
          this.$store.dispatch('user/login', this.ruleForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        }
      },
      // 前去注册页面
      gotoRegister () {
        this.$router.push({path: '/register'})
      }
    },
    mounted () {
      this.login_addCart()
    },
    components: {
      YFooter,
      YButton
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  * {
    box-sizing: content-box;
  }

  .login {
    overflow-x: hidden;
    overflow-y: hidden;
    .input {
      height: 50px;
      display: flex;
      align-items: center;
      input {
        font-size: 16px;
        width: 100%;
        height: 100%;
        padding: 10px 15px;
        box-sizing: border-box;
        border: 1px solid #ccc;
        border-radius: 6px;
      }
    }
    .wrapper {
      background: url(/static/images/bg_9b9dcb65ff.png) repeat;
      background-size: 100px;
      min-height: 800px;
      min-width: 630px;
    }
    .btn {
      margin: 0;
      width: 100%;
      height: 48px;
      font-size: 18px;
      line-height: 48px
    }
  }

  .v2 .dialog {
    width: 450px;
    border: 1px solid #dadada;
    border-radius: 5px;
    top: 50%;
    left: 50%;
    margin-left: -225px;
    position: absolute;
    .title {
      overflow: visible;
      position: relative;
      background: url(/static/images/jakarta-text.png) no-repeat center;
      background-size: 200px;
      height: 80px;
      margin: 23px 0 50px;
      padding: 75px 0 0;
      box-shadow: none;
      h4 {
        padding: 0;
        text-align: center;
        border-bottom: 1px solid #dcdcdc;
        position: absolute;
        bottom: 0;
        width: 100%;
        margin: 0;
        border-bottom: 0;
        box-shadow: none;
        line-height: 1em;
        height: auto;
        color: #333;
        font-weight: 400;
      }
    }
    .content {
      padding: 0 40px 22px;
      height: auto;
      .common-form {
        li {
          clear: both;
          margin-bottom: 15px;
          position: relative;
        }
      }
    }
  }

  .dialog-shadow, .v2 .bbs .dialog-shadow, .v2 .dialog-shadow {
    -webkit-box-shadow: 0 9px 30px -6px rgba(0, 0, 0, .2), 0 18px 20px -10px rgba(0, 0, 0, .04), 0 18px 20px -10px rgba(0, 0, 0, .04), 0 10px 20px -10px rgba(0, 0, 0, .04);
    -moz-box-shadow: 0 9px 30px -6px rgba(0, 0, 0, .2), 0 18px 20px -10px rgba(0, 0, 0, .04), 0 18px 20px -10px rgba(0, 0, 0, .04), 0 10px 20px -10px rgba(0, 0, 0, .04);
    box-shadow: 0 9px 30px -6px rgba(0, 0, 0, .2), 0 18px 20px -10px rgba(0, 0, 0, .04), 0 18px 20px -10px rgba(0, 0, 0, .04), 0 10px 20px -10px rgba(0, 0, 0, .04);
  }

  @media screen and (min-width: 737px), screen and (-webkit-max-device-pixel-ratio: 1.9) and (max-width: 736px) and (min-device-width: 737px) {
    .wrapper {
      background: url(/static/images/con-bg_04f25dbf8e.jpg) repeat-x;
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }
    .dialog {
      background: url(/static/images/dialog-gray-bg.png) #fff bottom repeat-x;
      border-radius: 12px;
      display: none;
      margin: -163px 0 0 -218px;
      width: 436px;
      position: fixed;
      left: 50%;
      top: 50%;
    }
    .dialog .title h4 {
      border-bottom: #d1d1d1 solid 1px;
      box-shadow: 0 2px 6px #d1d1d1;
      color: #666;
      font-size: 20px;
      height: 61px;
      line-height: 61px;
      padding: 0 0 0 35px;
    }
    .common-form li {
      clear: both;
      margin-bottom: 15px;
      position: relative;
    }
  }

  .registered {
    h4 {
      padding: 0;
      text-align: center;
      color: #666;
      border-bottom: 1px solid #dcdcdc;
      -webkit-box-shadow: none;
      -moz-box-shadow: none;
      box-shadow: none;
      font-weight: 700;
      font-size: 20px;
      height: 60px;
      line-height: 60px;
    }
  }

</style>
