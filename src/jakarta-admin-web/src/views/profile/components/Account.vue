<template>
  <el-form ref="userReq" :model="userReq" :rules="userReqRules" auto-complete="on" label-position="left">
    <el-form-item prop="name" label="Name">
      <el-input v-model.trim="userReq.name" />
    </el-form-item>
    <el-form-item prop="email" label="Email">
      <el-input v-model.trim="userReq.email" />
    </el-form-item>
    <el-form-item prop="tel" label="Telephone">
      <el-input v-model.trim="userReq.tel" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">Update</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  data() {
    const validateTel = (rule, value, callback) => {
      const temp = /^(\+[0-9]{1,3})?[0-9*#]+$/
      if (!value || (!(temp).test(value))) {
        callback(new Error('not a valid telephone number'))
      } else {
        callback()
      }
    }
    return {
      userReq: {
        name: '',
        email: '',
        tel: ''
      },
      userReqRules: {
        name: [{ required: true, trigger: 'blur' }],
        email: [{ type: 'email', required: true, message: 'a valid E-mail address is required', trigger: 'blur' }],
        tel: [{ required: true, trigger: 'blur', validator: validateTel }]
      },
      redirect: undefined
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
    submit() {
      this.$refs.userReq.validate(valid => {
        if (valid) {
          // 扔给服务器处理！
          this.$store.dispatch('user/updateInfo', this.userReq).then(() => {
            this.$message({
              message: 'User information has been updated successfully',
              type: 'success',
              duration: 5 * 1000
            })
          }).catch(() => {})
        } else {
          console.log('error submit!')
          return false
        }
      })
    },
    getUser() {
      this.userReq.name = this.name
      this.userReq.email = this.email
      this.userReq.tel = this.tel
    }
  }
}
</script>
