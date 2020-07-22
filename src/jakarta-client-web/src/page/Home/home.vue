<template>
  <div>
    <!--implemented-->
    <div class="banner">
      <div class="bg" ref="bg"
           @mouseover="bgOver($refs.bg)"
           @mousemove="bgMove($refs.bg,$event)"
           @mouseout="bgOut($refs.bg)">
        <el-carousel :interval="5000" arrow="never" height="350px" style="border-radius: 5px">
          <el-carousel-item v-for="item in bannersA" :key="item">
            <el-image
              :src="item"
              fit="cover" />
          </el-carousel-item>
        </el-carousel>
<!--        <span class="text b">美好的一天 🌞<br/>从一杯 Jakarta® 开始 ☕</span>-->
      </div>
    </div>
    <section class="w mt30 clearfix">
      <y-shelf title="热销商品">
        <div slot="content" class="hot">
          <mall-goods :item="item" v-for="(item, i) in hot" :key="i" />
        </div>
      </y-shelf>
    </section>
    <div class="banner">
      <div class="bg" ref="bg2"
           @mouseover="bgOver($refs.bg2)"
           @mousemove="bgMove($refs.bg2, $event)"
           @mouseout="bgOut($refs.bg2)">
        <el-carousel :interval="5000" arrow="never" height="350px" style="border-radius: 5px">
          <el-carousel-item v-for="item in bannersB" :key="item">
            <el-image
              :src="item"
              fit="cover" />
          </el-carousel-item>
        </el-carousel>
        <!--        <span class="text b">美好的一天 🌞<br/>从一杯 Jakarta® 开始 ☕</span>-->
      </div>
    </div>
    <section class="w mt30 clearfix">
      <y-shelf title="当季推荐">
        <div slot="content" class="hot">
          <mall-goods :item="item" v-for="(item, i) in hot" :key="i" />
        </div>
      </y-shelf>
    </section>
    <div class="banner">
      <div class="bg" ref="bg1"
           @mouseover="bgOver($refs.bg1)"
           @mousemove="bgMove($refs.bg1,$event)"
           @mouseout="bgOut($refs.bg1)">
        <el-carousel :interval="5000" arrow="never" height="450px" style="border-radius: 5px">
          <el-carousel-item>
            <el-image
              :src="cardBanner"
              fit="scale-down"/>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>
<script>
  import YShelf from '/components/shelf'
  import product from '/components/product'
  import mallGoods from '/components/mallGoods'
  import {fetchHot} from '../../api/goods'
  export default {
    data () {
      return {
        cardBanner: require('../../../static/carousels/card.png'),
        bannersA: [
          require('../../../static/carousels/1.png'),
          require('../../../static/carousels/2.png')
        ],
        bannersB: [
          require('../../../static/carousels/3.png'),
          require('../../../static/carousels/4.png')
        ],
        bgOpt: {
          offsetLeft: 0,
          offsetTop: 0,
          offsetWidth: 0,
          offsetHeight: 0
        },
        hot: []
      }
    },
    methods: {
      // LH - 获取热门菜单
      getHotList () {
        fetchHot(20).then(result => {
          this.hot = result.data
        })
      },
      // 鼠标移入
      bgOver (event) {
        // 获取移入时的位置
        const {offsetLeft, offsetTop, offsetWidth, offsetHeight} = event
        this.bgOpt.offsetLeft = offsetLeft
        this.bgOpt.offsetTop = offsetTop
        this.bgOpt.offsetWidth = offsetWidth
        this.bgOpt.offsetHeight = offsetHeight
      },
      // 鼠标移动
      bgMove (dom, eve) {
        const {offsetLeft, offsetTop, offsetWidth, offsetHeight} = this.bgOpt
        let X, Y
        let mouseX = eve.pageX - offsetLeft
        let mouseY = eve.pageY - offsetTop
        if (mouseX > offsetWidth / 2) {
          X = mouseX - (offsetWidth / 2)
        } else {
          X = mouseX - (offsetWidth / 2)
        }
        if (mouseY > offsetHeight / 2) {
          Y = offsetHeight / 2 - mouseY
        } else {
          Y = offsetHeight / 2 - mouseY
        }
        dom.style['-webkit-transform'] = `rotateY(${X / 50}deg) rotateX(${Y / 50}deg)`
        dom.style.transform = `rotateY(${X / 50}deg) rotateX(${Y / 50}deg)`
      },
      // 鼠标移除
      bgOut (dom) {
        dom.style.transform = 'rotateY(0deg) rotateX(0deg)'
        dom.style['-webkit-transform'] = 'rotateY(0deg) rotateX(0deg)'
      }
    },
    created () {
      this.getHotList()
    },
    components: {
      YShelf,
      product,
      mallGoods
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  .banner, .banner span, .banner div {
    transition: all .3s;
    -webkit-transition: all .3s;
    transition-timing-function: linear;
    -webkit-transition-timing-function: linear;
  }

  .banner {
    perspective: 3000px;
    position: relative;
    z-index: 19;
  }

  .bg {
    position: relative;
    width: 1220px;
    height: 350px;
    margin: 20px auto;
    border-radius: 8px;
    transform-style: preserve-3d;
    -webkit-transform-origin: 50% 50%;
    -webkit-transform: rotateY(0deg) rotateX(0deg);
  }

  .text {
    position: absolute;
    top: 20%;
    right: 10%;
    font-size: 30px;
    color: #fff;
    text-align: right;
    font-weight: lighter;
  }

  .copyright {
    position: absolute;
    bottom: 10%;
    right: 10%;
    font-size: 10px;
    color: #fff;
    text-align: right;
    font-weight: lighter;
  }

  .a {
    -webkit-transform: translateZ(40px);
  }

  .b {
    -webkit-transform: translateZ(20px);
  }

  .c {
    -webkit-transform: translateZ(0px);
  }

  .sk_item {
    width: 170px;
    height: 225px;
    padding: 0 14px 0 15px;
    > div {
      width: 100%;
    }
    a {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      transition: all .3s;
      &:hover {
        transform: translateY(-5px);
      }
    }
    img {
      width: 130px;
      height: 130px;
      margin: 17px 0;
    }
    .sk_item_name {
      color: #999;
      display: block;
      max-width: 100%;
      _width: 100%;
      overflow: hidden;
      font-size: 12px;
      text-align: left;
      height: 32px;
      line-height: 16px;
      word-wrap: break-word;
      word-break: break-all;
    }
    .sk_item_price {
      padding: 3px 0;
      height: 25px;
    }
    .price_new {
      font-size: 18px;
      font-weight: 700;
      margin-right: 8px;
      color: #f10214;
    }
    .price_origin {
      color: #999;
      font-size: 12px;
    }
  }

  .box {
    overflow: hidden;
    position: relative;
    z-index: 0;
    margin-top: 29px;
    box-sizing: border-box;
    border: 1px solid rgba(0, 0, 0, .14);
    border-radius: 8px;
    background: #fff;
    box-shadow: 0 3px 8px -6px rgba(0, 0, 0, .1);

  }

  ul.box {
    display: flex;
    li {
      flex: 1;
      img {
        display: block;
        width: 305px;
        height: 200px;
      }
    }
  }

  .mt30 {
    margin-top: 30px;
  }

  .mt60 {
    margin-top: 60px;
  }

  .hot {
    display: flex;
    > div {
      flex: 1;
      width: 25%;
    }
  }

  .floors {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    .imgbanner {
      width: 50%;
      height: 430px;
    }
    img {
      display: block;
      width: 100%;
      height: 100%;
    }
  }

</style>
