<template>
  <div class="layout-container">
    <y-header :showNav="false"></y-header>
    <div class="w">
      <div class="content">
        <div class="account-sidebar">
          <div class="avatar gray-box ">
            <div>
              <img :src="this.basicAvatarUrl">
              <h5>{{ this.name }}</h5></div>
            <div class="box-inner">
              <ul class="account-nav">
                <li v-for="(item, i) in nav" :key='i' :class="{ current:item.name === title }"
                    @click="tab(item)">
                  <a> {{ item.name }} </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="account-content">
          <router-view></router-view>
        </div>
      </div>
    </div>
    <y-footer></y-footer>

  </div>

</template>
<script>
  import YFooter from '/common/footer'
  import YHeader from '/common/header'
  import {mapGetters, mapState} from 'vuex'
  export default {
    data () {
      return {
        title: '我的主页',
        basicAvatarUrl: require('../../../static/images/user-avatar.png'),
        nav: [
          { name: '个人资讯', path: 'information' },
          { name: '历史订单', path: 'orderList' },
          { name: '收件地址管理', path: 'addressList' },
          { name: '客户支援', path: 'support' },
          { name: '优惠服务', path: 'coupon' }
        ],
        editAvatar: true
      }
    },
    computed: {
      ...mapState(['userInfo']),
      ...mapGetters([
        'name', 'email', 'tel'
      ])
    },
    methods: {
      tab (e) {
        this.$router.push({path: '/user/' + e.path})
      }
    },
    created () {
      let path = this.$route.path.split('/')[2]
      this.nav.forEach(item => {
        if (item.path === path) {
          this.title = item.name
        }
      })
    },
    components: {
      YFooter,
      YHeader
    },
    watch: {
      $route (to) {
        let path = to.path.split('/')[2]
        this.nav.forEach(item => {
          if (item.path === path) {
            this.title = item.name
          }
        })
      }
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/style/mixin";

  .w {
    padding-top: 40px;
  }

  .content {
    display: flex;
    height: 100%;
  }

  .account-sidebar {
    width: 210px;
    border-radius: 6px;
    .avatar {
      padding-top: 20px;
      border-radius: 10px;
      text-align: center;
      img {
        width: 168px;
        height: 168px;
      }
      h5 {
        font-size: 18px;
        line-height: 48px;
        font-weight: 700;
      }
    }
    .account-nav {
      padding-top: 15px;
      li {
        position: relative;
        height: 48px;
        border-top: 1px solid #EBEBEB;
        line-height: 48px;
        &:hover {
          a {
            position: relative;
            z-index: 1;
            height: 50px;
            background-color: #98AFEE;
            line-height: 50px;
            color: #FFF;
          }

        }
        a {
          display: block;
        }
        &.current {
          a {
            position: relative;
            z-index: 1;
            height: 50px;
            background-color: #98AFEE;
            line-height: 50px;
            color: #FFF;
          }
        }

      }
    }
  }

  .account-content {
    margin-left: 20px;
    flex: 1;
  }


</style>
