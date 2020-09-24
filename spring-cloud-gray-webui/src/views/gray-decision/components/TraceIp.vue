<template>
  <div class="TraceIp">
    <el-form
      ref="TraceIp"
      :rules="rules"
      :model="infos"
      :inline="true"
    >
      <div class="selectBox">

        <el-form-item label="ip" prop="ip" label-width="120px">
          <el-input v-model="infos.ip" />
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
        callback(new Error('ip is required'))
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
        ip: '',
        infos: '{"ip":""}'
      },
      options: [{
        value: 'GT',
        label: 'GT'
      }, {
        value: 'GTE',
        label: 'GTE'
      }, {
        value: 'LT',
        label: 'LT'
      }, {
        value: 'LTE',
        label: 'LTE'
      }, {
        value: 'EQUAL',
        label: 'EQUAL'
      }, {
        value: 'UNEQUAL',
        label: 'UNEQUAL'
      }, {
        value: 'CONTAINS_ALL',
        label: 'CONTAINS_ALL'
      }, {
        value: 'CONTAINS_ANY',
        label: 'CONTAINS_ANY'
      }, {
        value: 'NOT_CONTAINS_ANY',
        label: 'NOT_CONTAINS_ANY'
      }, {
        value: 'NOT_CONTAINS_ALL',
        label: 'NOT_CONTAINS_ALL'
      }],
      rules: {
        compareMode: [{ required: true, message: 'compareMode is required', trigger: 'change' }],
        ip: [{ required: true, validator: iprules, trigger: 'blur' }]

      }
    }
  },
  computed: {
    ip() {
      return this.infos.ip
    }

  },
  watch: {
    ip(a) {
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
      this.infos.infos = '{"ip":"' + this.infos.ip + '"}'
      this.$emit('sendInfos', this.infos.infos)
    },
    clear() {
      this.$refs.TraceIp.clearValidate()
    },
    check() {
      let flag = false
      this.$refs.TraceIp.validate((valid) => {
        flag = valid
      })
      return flag
    },
    isValidIP(ip) {
      /**
      const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
      return reg.test(ip)
       */
      return true
    }
  }
}
</script>
<style >
.TraceIp .selectBox .el-form-item{
    margin-right: 0;

}

.TraceIp .infosBox{
    width: 100%;
}
.TraceIp .infosBox .el-form-item__content{
    width:80%;
}
</style>
