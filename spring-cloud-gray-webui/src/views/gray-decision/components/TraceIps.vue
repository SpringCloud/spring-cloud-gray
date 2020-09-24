<template>
  <div class="TraceIps">
    <el-form
      ref="TraceIps"
      :rules="rules"
      :model="infos"
      :inline="true"
    >
      <div class="selectBox">
        <el-form-item label="compareMode" prop="compareMode" label-width="120px">
          <el-select
            v-model="infos.compareMode"
            placeholder="请选择"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="ips" prop="ips" label-width="120px">
          <el-input v-model="infos.ips" />
        </el-form-item>

      </div>
      <div class="infosBox">
        <el-form-item label="infos" prop="infos" label-width="120px" style="width:100%">
          <el-input v-model="infos.infos" type="textarea" readonly />
        </el-form-item>

      </div>
    </el-form>
  </div>
</template>

<script>

export default {
  name: 'HttpHeader',
  props: {
    info: {
      type: String,
      default: ''
    }
  },
  data() {
    const iprules = (rule, value, callback) => {
      if (!value) {
        callback(new Error('ips is required'))
      } else {
        if (!this.isValidIP(value)) {
          callback(new Error('Please enter the correct ip'))
        } else {
          callback()
        }
      }
    }
    return {
      infos: {
        compareMode: '',
        ips: '',
        infos: '{"compareMode":"","ips":""}'
      },
      options: [{
        value: 'CONTAINS_ANY',
        label: 'CONTAINS_ANY'
      }, {
        value: 'NOT_CONTAINS_ANY',
        label: 'NOT_CONTAINS_ANY'
      }],
      rules: {
        compareMode: [{ required: true, message: 'compareMode is required', trigger: 'change' }],
        ip: [{ required: true, validator: iprules, trigger: 'blur' }]

      }
    }
  },
  computed: {
    ips() {
      return this.infos.ips
    },
    compareMode() {
      return this.infos.compareMode
    }

  },
  watch: {
    ips(a) {
      if (a) {
        this.setInfos()
      }
    },
    compareMode(a) {
      if (a) {
        this.setInfos()
      }
    },
    info(a) {
      if (a) {
        if (this.info) {
          this.infos = { ...JSON.parse(this.info) }
          this.infos.infos = this.info
          console.log(this.infos)
        }
      }
    }

  },
  created() {
    if (this.info) {
      this.infos = { ...JSON.parse(this.info) }
      this.infos.infos = this.info
      console.log(this.infos)
    }
  },
  methods: {
    setInfos() {
      this.infos.infos = '{"compareMode":"' + this.infos.compareMode + ',"ips":"' + this.infos.ips + '"}'
      this.$emit('sendInfos', this.infos.infos)
    },
    clear() {
      this.$refs.TraceIps.clearValidate()
    },
    check() {
      let flag = false
      this.$refs.TraceIps.validate((valid) => {
        flag = valid
      })
      return flag
    },
    isValidIP(ips) {
      const ipList = ips.split(',')
      for (const ip of ipList) {
        const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
        if (!reg.test(ip)) {
          return false
        }
      }
      return true
    }
  }
}
</script>
<style >
.TraceIps .selectBox .el-form-item{
    margin-right: 0;

}

.TraceIps .infosBox{
    width: 100%;
}
.TraceIps .infosBox .el-form-item__content{
    width:80%;
}
</style>
