<template>
  <div class="dashboard-container">
    <el-row>
      <el-col :span="3">
        <svg-icon icon-class="thejaback" style="width: 150px; height: 150px"/>
      </el-col>
      <el-col :span="21">
        <el-card shadow="never">
          <div class="dashboard-text"><b>Welcome, {{ name }}</b></div>
          <div class="dashboard-text-annotation">You have: 0 messages, 0 to-dos.</div>
          <div class="dashboard-text-time"><b>Last Login: </b> {{ new Date() }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="10" style="margin-top:20px;">
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" style="">
            <b>Break news</b>
          </div>
          <div class="component-item" style="height:330px;">
            <el-image :src="imageURL">
            </el-image>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <b>Notepad</b>
          </div>
          <div class="component-item" style="height:330px">
            <markdown-editor ref="markdownEditor" v-model="content" :options="{hideModeSwitch:true,previewStyle:'tab'}" height="330px" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import MarkdownEditor from '@/components/MarkdownEditor'

export default {
  name: 'Dashboard',
  components: { MarkdownEditor },
  data() {
    return {
      content: `# What a nice day it is!
## A nice day starts with a cup of Jakarta Coffee
      `,
      imageURL: require('@/views/resources/coffeeicon.png')
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
  .dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
  &-text-annotation {
    font-size: 20px;
    line-height: 36px;
    color: #2b2f3a;
  }
  &-text-time {
    font-size: 10px;
    line-height: 36px;
    color: #5a5e66;
  }
}
</style>
