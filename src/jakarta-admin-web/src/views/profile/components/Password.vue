<template>
  <div>
    <el-form ref="passwordReq" :model="passwordReq" :rules="passwordRules" auto-complete="on" label-position="left">
      <el-form-item prop="old" label="Old Password">
        <el-input
          key="password"
          ref="old-pass"
          v-model="passwordReq.old"
          name="old-pass"
          type="password"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>
      <el-form-item prop="newPass" label="New Password">
        <el-input
          key="password"
          ref="new-pass"
          v-model="passwordReq.newPass"
          name="new-pass"
          type="password"
          tabindex="2"
          auto-complete="on"
        />
      </el-form-item>
      <el-form-item prop="again" label="Re-Enter New Password">
        <el-input
          key="password"
          ref="again-pass"
          v-model="passwordReq.again"
          name="again-pass"
          type="password"
          tabindex="3"
          auto-complete="on"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click.native.prevent="submit">Update</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Password',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (!value || value.length < 5) {
        callback(new Error('the password can not be less than 5 characters'))
      } else {
        callback()
      }
    }
    const validateAgainPassword = (rule, value, callback) => {
      if (!value || value !== this.passwordReq.newPass) {
        callback(new Error('the two passwords are not matching'))
      } else {
        callback()
      }
    }
    return {
      passwordReq: {
        old: '',
        newPass: '',
        again: '',
        name: ''
      },
      passwordRules: {
        old: [{ required: true, trigger: 'blur', validator: validatePassword }],
        newPass: [{ required: true, trigger: 'blur', validator: validatePassword }],
        again: [{ required: true, trigger: 'blur', validator: validateAgainPassword }]
      },
      redirect: undefined
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.passwordReq.name = this.name
  },
  methods: {
    submit() {
      this.$refs.passwordReq.validate(valid => {
        if (valid) {
          // 扔给服务器处理！
          this.$store.dispatch('user/updatePass', this.passwordReq).then(() => {
            this.$message({
              message: 'Password has been updated successfully',
              type: 'success',
              duration: 5 * 1000
            })
          }).catch(() => {})
        } else {
          console.log('error submit!')
          return false
        }
      })
    }
  }
}
</script>

