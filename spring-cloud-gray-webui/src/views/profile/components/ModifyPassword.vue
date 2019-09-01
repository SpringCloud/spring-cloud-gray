<template>
  <el-form ref="modifyPwdForm" :rules="rules" :model="temp">
    <el-form-item label="Old Password" prop="oldPassword">
      <el-input v-model.trim="temp.oldPassword" :type="'password'" />
    </el-form-item>
    <el-form-item label="Password" prop="newPassword">
      <el-input v-model.trim="temp.newPassword" :type="'password'" />
    </el-form-item>
    <el-form-item label="Repeat Password" prop="repassword">
      <el-input v-model.trim="temp.repassword" :type="'password'" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">Update</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updatePassword } from '@/api/user'

export default {
  data() {
    return {
      temp: {
        oldPassword: '',
        newPassword: '',
        repassword: ''
      },
      rules: {
        oldPassword: [{ required: true, message: 'oldPassword is required', trigger: 'change' }],
        newPassword: [{ required: true, message: 'password is required', trigger: 'change' }],
        repassword: [{ required: true, message: 'repeat password is required', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  methods: {
    submit() {
      this.$refs['modifyPwdForm'].validate((valid) => {
        if (valid) {
          if (this.temp.newPassword.length < 6) {
            this.$notify({
              title: 'Failed',
              message: 'password length must be greater 6',
              type: 'warning',
              duration: 2000
            })
            return
          }
          if (this.temp.newPassword !== this.temp.repassword) {
            this.$notify({
              title: 'Failed',
              message: 'new password is not unanimous',
              type: 'warning',
              duration: 2000
            })
            return
          }
          updatePassword(this.temp).then(response => {
            this.$notify({
              title: 'Success',
              message: 'Operation is  Successfully',
              type: 'success',
              duration: 2000
            })
            setTimeout(() => {
              this.logout()
            }, 1.5 * 1000)
          })
        }
      })
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login`)
    }
  }
}
</script>
