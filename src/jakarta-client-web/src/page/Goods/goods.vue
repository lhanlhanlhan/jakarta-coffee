<template>
  <div class="goods">
    <div class="nav">
      <div class="w">
        <el-col :span="6">
          <el-input class="input-box" v-model="params.name" placeholder="搜寻关键字" clearable size="small" @keydown.enter.native="search" ref="search"></el-input>
        </el-col>

        <el-col :span="2" :offset="1">
          <el-input class="input-box" placeholder="最低价格" v-model="params.min" clearable size="small" />
        </el-col>
        <span style="margin: 0 5px"> - </span>
        <el-col :span="2">
          <el-input class="input-box" placeholder="最高价格" v-model="params.max" clearable size="small" />
        </el-col>
        <y-button text="搜寻" classStyle="main-btn" @btnClick="doSearch()" style="margin-left: 10px;"></y-button>
      </div>
    </div>
    <div class="nav">
      <div class="w">
        <a @click="sortNormally" :class="{ active:sortType===1 }">综合排序</a>
        <a @click="sortByValue(1)" :class="{ active:sortType===2 }">价格 ▲</a>
        <a @click="sortByValue(0)" :class="{ active:sortType===3 }">价格 ▼</a>
        <a @click="sortBySold(1)" :class="{ active:sortType===4 }">销量 ▲</a>
        <a @click="sortBySold(0)" :class="{ active:sortType===5 }">销量 ▼</a>

        <span style="margin: 0 5px" />

        <a @click="selectClass('all')" :class="{ active:itemClass === 1 }">全部 / All</a>
        <a @click="selectClass('drinks')" :class="{ active:itemClass === 2 }">饮品 / Beverage</a>
        <a @click="selectClass('snacks')" :class="{ active:itemClass === 3 }">点心 / Snacks</a>
        <a @click="selectClass('staple')" :class="{ active:itemClass === 4 }">主食 / Staples</a>
      </div>
    </div>

    <div class="goods-box w">
      <mall-goods v-for="(_item, i) in list" :key="i" :item="_item"></mall-goods>
    </div>
  </div>
</template>
<script>
  import { fetchMenu } from '../../api/goods'
  import mallGoods from '../../components/mallGoods'
  import YButton from '../../components/YButton'
  export default {
    data () {
      return {
        busy: false,
        timer: null,
        sortType: 1,
        itemClass: 1,
        windowHeight: null,
        windowWidth: null,
        params: {
          sort: undefined, // 排序方案
          kind: undefined, // 种类
          min: undefined,  // 最小价格
          max: undefined,
          name: '' // 名字
        },
        list: null,
        total: 0
      }
    },
    methods: {
      _fetchMenu () {
        let params = {
          name: this.params.name,
          sort: this.params.sort,
          cat: this.params.kind,
          min: this.params.min,
          max: this.params.max
        }
        fetchMenu(params).then(response => {
          const { data, size } = response
          this.list = data
          this.total = size
        })
      },
      // 按当前所确定之所有条件搜寻
      doSearch () {
        this.sortType = 1
        this._fetchMenu()
      },
      // 默认排序
      sortNormally () {
        this.sortType = 1
        this.params.sort = 'drecent_sold'
        this._fetchMenu()
      },
      // 价格排序
      sortByValue (ascending) {
        if (ascending) {
          this.sortType = 2
          this.params.sort = 'aprice'
        } else {
          this.sortType = 3
          this.params.sort = 'dprice'
        }
        this._fetchMenu()
      },
      // 销量排序
      sortBySold (ascending) {
        if (ascending) {
          this.sortType = 4
          this.params.sort = 'arecent_sold'
        } else {
          this.sortType = 5
          this.params.sort = 'drecent_sold'
        }
        this._fetchMenu()
      },
      // 类别排序
      selectClass (value) {
        switch (value) {
          case 'all':
            this.itemClass = 1
            this.params.kind = undefined
            break
          case 'drinks':
            this.itemClass = 2
            this.params.kind = 'DR'
            break
          case 'snacks':
            this.itemClass = 3
            this.params.kind = 'SN'
            break
          case 'staple':
            this.itemClass = 4
            this.params.kind = 'SF'
            break
        }
        this._fetchMenu()
      }
    },
    created () {
      this._fetchMenu()
    },
    mounted () {
      this.windowHeight = window.innerHeight
      this.windowWidth = window.innerWidth
    },
    components: {
      mallGoods,
      YButton
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/style/mixin";
  @import "../../assets/style/theme";

  .nav {
    height: 60px;
    line-height: 60px;
    > div {
      display: flex;
      align-items: center;
      a {
        padding: 0 15px;
        height: 100%;
        @extend %block-center;
        font-size: 12px;
        color: #999;
        &.active {
          color: #5683EA;
        }
        &:hover {
          color: #5683EA;
        }
      }
      input {
        @include wh(80px, 30px);
        border: 1px solid #ccc;
      }
      input + input {
        margin-left: 10px;
      }
    }
    .price-interval {
      padding: 0 0;
      @extend %block-center;
      input[type=number] {
        border: 1px solid #ccc;
        text-align: center;
        background: none;
        border-radius: 5px;
      }
    }
    .item-name {
      padding: 0 15px;
      @extend %block-center;
      input {
        border: 1px solid #ccc;
        text-align: center;
        background: none;
        border-radius: 5px;
        width: 200px;
      }
    }
  }
  .goods-box {
    > div {
      float: left;
      border: 1px solid #efefef;
    }
  }


</style>
