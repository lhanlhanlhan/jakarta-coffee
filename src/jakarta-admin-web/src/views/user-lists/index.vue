<template>
  <div class="app-container">
    <div>
      <el-row :gutter="20">
        <el-col>
          <el-tabs v-model="activeTab" type="border-card">
            <el-tab-pane label="Administrators" name="admin-list">
              <admin-list />
            </el-tab-pane>
            <el-tab-pane label="Clients" name="client-list">
              <client-list />
            </el-tab-pane>
          </el-tabs>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import AdminList from './components/admin-list'
import ClientList from './components/client-list'
import Cookies from 'js-cookie'

export default {
  name: 'UserLists',
  components: { AdminList, ClientList },
  lastSelected: undefined,
  data() {
    return {
      activeTab: Cookies.get('lastSelectedList') || 'admin-list',
      toggleTab: () => {
        console.log(this.activeTab)
        Cookies.set('lastSelectedList', this.activeTab)
      }
    }
  }
}
</script>
