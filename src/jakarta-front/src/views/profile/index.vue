<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="20">
        <el-col :span="6" :xs="24">
          <user-card :user="user" />
        </el-col>
        <el-col :span="18" :xs="24">
          <el-tabs v-model="activeTab" type="border-card">
            <el-tab-pane label="Account Settings" name="account">
              <account />
            </el-tab-pane>
            <el-tab-pane label="Password Settings" name="password">
              <password />
            </el-tab-pane>
          </el-tabs>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import UserCard from './components/UserCard'
import Account from './components/Account'
import Password from './components/Password'

export default {
  name: 'Profile',
  components: { UserCard, Account, Password },
  data() {
    return {
      user: {},
      activeTab: 'account'
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'email',
      'tel'
    ])
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      this.user = {
        name: this.name,
        email: this.email,
        tel: this.tel
      }
    }
  }
}
</script>
