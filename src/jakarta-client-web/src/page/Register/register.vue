<template>
  <div class="login-container">
    <div class="title-container">
      <h1 class="title">
        <el-image
          style="width: 200px; height: 70px"
          src="static/images/jakarta-text-white.png"
          fit="contain"></el-image>
      </h1>
      <h4 class="title-annotation">
        申请您的新 Jakarta ID
      </h4>
    </div>
    <el-form v-if="registering" ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <el-form-item prop="username">
        <el-input
          ref="username"
          v-model="loginForm.username"
          name="username"
          type="text"
          auto-complete="on"
          prefix-icon="el-icon-user"
          placeholder="用户名"
        />
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          ref="password"
          v-model="loginForm.password"
          placeholder="密码"
          name="password"
          auto-complete="on"
          show-password
          prefix-icon="el-icon-key"
        />
      </el-form-item>
      <el-form-item prop="again_pw">
        <el-input
          ref="again_pw"
          v-model="loginForm.again_pw"
          placeholder="确认密码"
          name="again_pw"
          auto-complete="on"
          show-password
          prefix-icon="el-icon-key"
        />
      </el-form-item>
      <el-collapse-transition>
        <div v-show="showingAnimation">
          <el-form-item prop="email">
            <el-input
              ref="email"
              v-model="loginForm.email"
              placeholder="电子邮件地址"
              name="email"
              auto-complete="on"
              prefix-icon="el-icon-message"
            />
          </el-form-item>
          <el-form-item prop="tel">
            <el-input
              ref="tel"
              v-model="loginForm.tel"
              placeholder="电话号码"
              name="tel"
              auto-complete="on"
              prefix-icon="el-icon-mobile-phone"
            />
          </el-form-item>
        </div>
      </el-collapse-transition>
      <el-button :loading="loading" type="primary" style="width:100%; margin-bottom:30px; background-color: #765d3f; border-color: #362215" @click.native.prevent="handleLogin">
        继续
      </el-button>
      <div class="tips">
        <span style="text-align: center;">点击「继续」表示　台端已阅读及签署<el-link type="warning">《账户私隐及服务协议》</el-link></span>
      </div>

    </el-form>
    <div v-else class="reg-succeeded-msg">
      <transition name="el-fade-in-linear">
        <div v-show="showingAnimation">
          <div>
            <h3 class="title">注册完成</h3>
            <h1 class="big-check"><span class="el-icon-success" /></h1>
            <el-form class="login-form">
              <el-button :loading="loading" type="primary" style="width:100%; margin-bottom:30px; background-color: #765d3f; border-color: #362215; position: relative;" @click.native.prevent="handleGotoLogin">
                使用Jakarta ID登入在线商城
              </el-button>
            </el-form>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>

import { removeStore } from '../../utils/storage'
export default {
  name: 'Register',
  data () {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('密码不可少于5字符'))
        this.showingAnimation = false
      } else {
        callback()
        this.showingAnimation = true
      }
    }
    const validateAgainPw = (rule, value, callback) => {
      if (value !== this.loginForm.password || value.length < 5) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    const validateTel = (rule, value, callback) => {
      const temp = /^(\+[0-9]{1,3})?[0-9*#]+$/
      if (!value || (!(temp).test(value))) {
        callback(new Error('电话号码是非法的'))
      } else {
        callback()
      }
    }
    return {
      showingAnimation: false,
      registering: true,
      loginForm: {
        username: '',
        password: '',
        again_pw: '',
        email: '',
        tel: ''
      },
      loginRules: {
        username: [{ required: true, message: '姓名必须至少1字符长', trigger: 'blur' }],
        password: [{ required: true, validator: validatePassword, trigger: 'blur' }],
        again_pw: [{ required: true, validator: validateAgainPw, trigger: 'blur' }],
        email: [{ type: 'email', required: true, message: '电子邮箱地址是非法的', trigger: 'blur' }],
        tel: [{ required: true, validator: validateTel, trigger: 'blur' }]
      },
      loading: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    async handleGotoLogin () {
      // 先尽可能登出
      await this.$store.dispatch('user/logout')
      // 清空本地购物车
      removeStore('buyCart')
      this.$router.push({path: '/login'})
    },
    handleLogin () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.showingAnimation = false
          this.$store.dispatch('user/register', this.loginForm).then(() => {
            this.loading = false
            this.registering = false
            setTimeout(() => {
              this.showingAnimation = true
            }, 850)
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;

    input {
      background: transparent;
      border: 0;
      -webkit-appearance: none;
      border-radius: 0;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #4b3c33;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  width: 100%;
  min-height: 800px;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 20px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin: 0 auto 0 10px;
    text-align: center;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 80px;
      color: $light_gray;
      margin: 50px auto 0 auto;
      text-align: center;
      font-weight: bold;
    }
    .title-annotation {
      font-size: 18px;
      color: $light_gray;
      margin: 0 auto 10px auto;
      text-align: center;
    }
  }

  .reg-succeeded-msg {
    position: relative;

    .big-check {
      font-size: 200px;
      color: #1ba784;
      margin: 10px 0 0 0;
      alignment: center;
      text-align: center;
      font-weight: bold;
    }
    .title {
      font-size: 40px;
      color: $light_gray;
      margin: 10px 0 0 0;
      text-align: center;
      font-weight: bold;
    }
    .title-annotation {
      font-size: 18px;
      color: $light_gray;
      margin: 30px auto 10px auto;
      text-align: center;
    }
  }
}
</style>
