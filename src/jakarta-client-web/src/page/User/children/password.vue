<template>
  <div>
    <y-shelf title="设定密码">
      <div slot="content">
        <div class="info">
          <el-row>
            <el-col :span="10">
              <div class="avatar-box">
                <div class=img-box><img :src="basicAvatarUrl" alt=""></div>
                <div class="r-box">
                  <h3>{{name}}</h3>
                  <h5>{{email}}</h5>
                  <h6>{{tel}}</h6>
                  <y-button text="更换头像" classStyle="main-btn" style="margin: 0;" @btnClick="editAvatar()"></y-button>
                </div>
              </div>
              <div class="edit-avatar" v-if="editAvatarShow">
                <y-shelf title="设置头像">
                  <span slot="right">
              <span class="close" @click="editAvatarShow = false">
              <svg t="1501234940517" class="icon" style="" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="3014" xmlns:xlink="http://www.w3.org/1999/xlink"
                   width="20" height="20"><path
                d="M941.576 184.248l-101.824-101.824L512 410.176 184.248 82.424 82.424 184.248 410.168 512l-327.744 327.752 101.824 101.824L512 613.824l327.752 327.752 101.824-101.824L613.832 512z"
                fill="#cdcdcd" p-id="3015"></path></svg>
              </span>
            </span>
                  <div slot="content" class="content">
                    <div class="edit-l">
                      <div style="width: 100px;height: 100px;border: 1px solid #ccc;margin-bottom: 20px;overflow: hidden;">
                        <div class="show-preview"
                             :style="{'width': previews.w + 'px','height': previews.h + 'px','overflow': 'hidden','zoom':option.zoom}">
                          <div :style="previews.div">
                            <img :src="option.img"
                                 :style="previews.img"
                            >
                          </div>
                        </div>
                      </div>
                      <div style="padding: 10px 0 ">头像预览</div>
                      <div class="btn">
                        <a href="javascript:;">重新选择</a>
                        <input type="file" value="上传头像" @change="uploadImg($event)"></div>
                    </div>
                    <div class="edit-r">
                      <div>
                        <div class="big" id="cropper-target" v-if="option.img">
                          <vueCropper
                            :img="option.img"
                            @realTime="realTime"
                            ref="cropper"
                            :outputSize="example2.size"
                            :info="example2.info"
                            :canScale="example2.canScale"
                            :autoCrop="example2.autoCrop"
                            :autoCropWidth="example2.width"
                            :autoCropHeight="example2.height"
                            :fixed="example2.fixed"
                          ></vueCropper>
                        </div>
                      </div>

                    </div>
                    <div class="bootom-btn pa">
                      <y-button style="width: 140px;height: 40px;line-height: 40px"
                                text="取消"
                                @btnClick="editAvatarShow=false">
                      </y-button>
                      <y-button style="width: 140px;height: 40px;line-height: 40px"
                                text="确定"
                                classStyle="main-btn"
                                @btnClick="cropper">
                      </y-button>
                    </div>
                  </div>
                </y-shelf>
              </div>
            </el-col>
            <el-col :span="14">
              <el-form ref="userReq" :model="userReq" auto-complete="on" label-position="left">
                <el-form-item prop="name" label="用户名">
                  <el-input v-model.trim="userReq.name" :disabled="isDisabled" />
                </el-form-item>
                <el-form-item prop="email" label="Email">
                  <el-input v-model.trim="userReq.email" :disabled="isDisabled" />
                </el-form-item>
                <el-form-item prop="tel" label="电话号码">
                  <el-input v-model.trim="userReq.tel" :disabled="isDisabled" />
                </el-form-item>
                <y-button style="width: 100px; height: 40px; line-height: 40px"
                          text="更新个人资讯"
                          classStyle="disabled-btn"
                          @btnClick="updateInfo"
                          v-if="isDisabled">
                </y-button>
                <y-button style="width: 100px; height: 40px; line-height: 40px"
                          text="更新个人资讯"
                          classStyle="main-btn"
                          @btnClick="updateInfo"
                          v-else>
                </y-button>
                <y-button style="width: 100px; height: 40px; line-height: 40px"
                          text="重置所有"
                          classStyle="disabled-btn"
                          @btnClick="resetAll"
                          v-if="isDisabled">
                </y-button>
                <y-button style="width: 100px; height: 40px; line-height: 40px"
                          text="重置所有"
                          classStyle="default-btn"
                          @btnClick="resetAll"
                          v-else>
                </y-button>
              </el-form>
            </el-col>
          </el-row>
        </div>
      </div>
    </y-shelf>
  </div>
</template>
<script>
  import YButton from '/components/YButton'
  import YShelf from '/components/shelf'
  import vueCropper from 'vue-cropper'
  import {mapGetters, mapMutations} from 'vuex'
  import {MessageBox} from 'element-ui'

  export default {
    data () {
      return {
        isDisabled: true,
        userReq: {
          name: '',
          email: '',
          tel: ''
        },
        popupOpen: false,
        popupTitle: '管理收货地址',
        basicAvatarUrl: require('../../../../static/images/user-avatar.png'),
        imgSrc: '',
        editAvatarShow: false,
        cropContext: '',
        cropperImg: '',
        previews: {},
        option: {
          img: '',
          zoom: 0
        },
        fixedNumber: [1, 1],
        example2: {
          info: true,
          size: 1,
          canScale: false,
          autoCrop: true,
          // 只有自动截图开启 宽度高度才生效
          autoCropWidth: 300,
          autoCropHeight: 250,
          // 开启宽度和高度比例
          fixed: true
        }
      }
    },
    computed: {
      ...mapGetters([
        'name', 'email', 'tel'
      ])
    },
    created () {
      this.userReq.name = this.name
      this.userReq.email = this.email
      this.userReq.tel = this.tel
    },
    methods: {
      ...mapMutations([
        'RECORD_USERINFO'
      ]),
      updateInfo () {
        // TODO - 更新个人资讯
        // 扔给服务器处理！
        MessageBox.confirm('是否确认更新您的个人资讯？', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('user/updateInfo', this.userReq).then(() => {
            this.$message({
              message: '用户信息已经更新完毕',
              type: 'success',
              duration: 5 * 1000
            })
          }).catch(() => {})
        })
      },
      openEditing () {
        // 重置个人资讯
        this.isDisabled = !this.isDisabled
        if (this.isDisabled) { // 如果刚关闭了编辑
          this.userReq.name = this.name
          this.userReq.email = this.email
          this.userReq.tel = this.tel
        }
      },
      resetAll () {
        // 重置个人资讯
        this.userReq.name = this.name
        this.userReq.email = this.email
        this.userReq.tel = this.tel
      },
      uploadImg (e) {
        let file = e.target.files[0]
        if (!/\.(gif|jpg|jpeg|png|bmp|GIF|JPG|PNG)$/.test(e.target.value)) {
          alert('图片类型必须是.gif,jpeg,jpg,png,bmp中的一种')
          return false
        }
        let reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = (e) => {
          this.option.img = e.target.result
        }
      },
      cropper () {
        if (this.option.img) {
          this.$refs.cropper.getCropData((data) => {
            this.imgSrc = data
            // TODO - 上传图片
            this.$message('该功能尚未开发')
          })
        } else {
          alert('别玩我啊 先选照骗')
        }
      },
      editAvatar () {
        this.editAvatarShow = true
      },
      realTime (data) {
        this.previews = data
        this.option.zoom = 100 / data.w
      }
    },
    components: {
      YButton,
      YShelf,
      vueCropper
    }
  }
</script>
<style lang="scss" scoped>
  @import "../../../assets/style/mixin";
  .info {
    margin: 30px;
  }
  .avatar-box {
    height: 152px;
    display: flex;
    margin: 20px;
    border: #dadada solid 1px;
    line-height: 30px;
    align-items: center;
    border-radius: 5px;
    .img-box {
      @include wh(80px);
      border-radius: 5px;
      overflow: hidden;
      margin: 20px;
    }
    img {
      display: block;
      @include wh(100%)
    }
    .r-box {
      h3 {
        font-size: 18px;
        font-weight: 400;
        color: #333;
      }
    }
  }

  // 修改头像
  .edit-avatar {
    z-index: 9999;
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    @include wh(100%);
    background-color: rgba(0, 0, 0, .5);
    @extend %block-center;
    .content {
      display: flex;
      padding: 45px 100px 0;
    }
    > div {
      box-sizing: content-box;
      @include wh(700px, 500px);
      margin: 0;
    }
    .btn {
      width: 80px;
      height: 30px;
      margin-left: 10px;
      position: relative;
      text-align: center;
      line-height: 30px;
      text-shadow: rgba(255, 255, 255, .496094) 0 1px 0;
      border: 1px solid #E6E6E6;
      border-radius: 10px;
      &:hover {
      }
      a {
        color: #666;
        display: block;
        @include wh(100%);
      }
    }
    input[type=file] {
      position: absolute;
      right: 0;
      left: 0;
      top: 0;
      opacity: 0;
      width: 80px;
      height: 30px;
      cursor: pointer;
      box-sizing: border-box;
      border: 15px solid #000;
      overflow: hidden;
    }
    .edit-l {
      width: 100px;
      text-align: center;
    }
    .edit-r {
      margin-left: 10px;
      flex: 1;
      > div {
        border: 1px solid #ccc;
        width: 320px;
        height: 320px;
      }
    }
  }

  .image-container {
    width: 100px;
    height: 100px;
    border: 1px solid #ccc;
  }

  .close {
    position: absolute;
    right: 10px;
    top: 0;
    bottom: 0;
    padding: 0 10px;
    @extend %block-center;
    &:hover {
      svg {
        transition: all 1s;
        transform: rotate(360deg);
        transform-origin: 50% 50%;
      }
      path {
        fill: #8a8a8a;
      }
    }
  }

  .big {
    display: block;
    width: 320px;
    height: 320px;
  }

  .bootom-btn {
    padding: 0 15px;
    border-top: 1px solid #E6E6E6;
    bottom: 0;
    height: 60px;
    right: 0;
    left: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
