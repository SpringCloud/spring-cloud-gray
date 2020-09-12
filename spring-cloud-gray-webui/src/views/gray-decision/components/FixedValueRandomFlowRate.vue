<template>
  <div class="FixedValueRandomFlowRate">
    <el-form
      ref="FixedValueRandomFlowRate"
      :rules="rules"
      :model="infos"
      :inline="true"
    >
      <div class="selectBox">
        <el-form-item label="type" prop="type" label-width="120px">
          <el-select
            v-model="infos.type"
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
        <el-form-item label="field" prop="field" label-width="120px">
          <el-input v-model="infos.field" />
        </el-form-item>
        <el-form-item label="salt" prop="salt" label-width="120px">
          <el-input v-model="infos.salt" />
        </el-form-item>
      </div>
      <div>
        <el-form-item label="rate" prop="rate" label-width="120px">
          <el-input v-model="infos.rate" type="tel" @input="setValue" />
        </el-form-item>
        <el-form-item label="value" prop="value" label-width="120px">
          <el-input v-model="infos.value" />
        </el-form-item>
        <el-form-item label="ignore case" prop="ignoreCase" label-width="120px">
          <el-select
            v-model="infos.ignoreCase"
            placeholder="请选择"
          >
            <el-option :key="`false`" :label="`false`" :value="`false`" />
            <el-option :key="`true`" :label="`true`" :value="`true`" />
          </el-select>
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
    const raterules = (rule, value, callback) => {
      if (!value) {
        callback(new Error('rate is required'))
      } else {
        if (value > 100) {
          callback(new Error('rate must be less than 100'))
        } else {
          callback()
        }
      }
    }
    return {
      infos: {
        field: '',
        salt: '',
        type: '',
        infos: '{"type":"","field":"","salt":"","rate":"","value":"","ignoreCase":""}',
        rate: '',
        value: '',
        ignoreCase: false
      },
      options: [{
        value: 'HttpHeader',
        label: 'HttpHeader'
      }, {
        value: 'HttpParameter',
        label: 'HttpParameter'
      }, {
        value: 'TrackAttribute',
        label: 'TrackAttribute'
      }, {
        value: 'HttpTrackHeader',
        label: 'HttpTrackHeader'
      }, {
        value: 'HttpTrackParameter',
        label: 'HttpTrackParameter'
      }],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        field: [{ required: true, message: 'field is required', trigger: 'blur' }],
        salt: [{ required: false, message: 'salt is required', trigger: 'blur' }],
        rate: [{ required: true, validator: raterules, trigger: 'blur' }],
        value: [{ required: true, message: 'value is required', trigger: 'blur' }],
        ignoreCase: [{ required: true, message: 'ignoreCase is required', trigger: 'blur' }]
      }
    }
  },
  computed: {
    field() {
      return this.infos.field
    },
    type() {
      return this.infos.type
    },
    salt() {
      return this.infos.salt
    },
    rate() {
      return this.infos.rate
    },
    value() {
      return this.infos.value
    },
    ignoreCase() {
      return this.infos.ignoreCase
    }

  },
  watch: {
    field(a) {
      if (a) {
        this.setInfos()
      }
    },
    type(a) {
      if (a) {
        this.setInfos()
      }
    },
    salt(a) {
      if (a) {
        this.setInfos()
      }
    },
    rate(a) {
      if (a) {
        this.setInfos()
      }
    },
    value(a) {
      if (a) {
        this.setInfos()
      }
    },
    ignoreCase(a) {
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
    setValue(target) {
      const val = this.NumberCheck(target)
      this.infos.rate = val
    },
    setInfos() {
      this.infos.infos = '{"type":"' + this.infos.type + '","field":"' + this.infos.field + '","salt":"' + this.infos.salt + '","rate":"' + this.infos.rate + '","value":"' + this.infos.value + '","ignoreCase":"' + this.infos.ignoreCase + '"}'
      console.log(this.infos.infos)
      this.$emit('sendInfos', this.infos.infos)
    },
    clear() {
      this.$refs.FixedValueRandomFlowRate.clearValidate()
    },
    check() {
      let flag = false
      this.$refs.FixedValueRandomFlowRate.validate((valid) => {
        flag = valid
      })
      return flag
    },
    NumberCheck(num) {
      let str = num
      const len1 = str.substr(0, 1)
      const len2 = str.substr(1, 1)
      // 如果第一位是0，第二位不是点，就用数字把点替换掉
      if (str.length > 1 && len1 === 0 && len2 !== '.') {
        str = str.substr(1, 1)
      }
      // 第一位不能是.
      if (len1 === '.') {
        str = ''
      }
      // 限制只能输入一个小数点
      if (str.indexOf('.') !== -1) {
        const str_ = str.substr(str.indexOf('.') + 1)
        if (str_.indexOf('.') !== -1) {
          str = str.substr(0, str.indexOf('.') + str_.indexOf('.') + 1)
        }
      }
      // 正则替换，保留数字和小数点
      str = str.replace(/[^\d^\.]+/g, '')

      return str
    }
  }
}
</script>
<style >
.FixedValueRandomFlowRate .selectBox .el-form-item{
    margin-right: 0;
margin-left: 20px;
}
.FixedValueRandomFlowRate .selectBox .el-form-item:first-child{
    margin: 0;
}
.FixedValueRandomFlowRate .infosBox{
    width: 100%;
}
.FixedValueRandomFlowRate .infosBox .el-form-item__content{
    width:80%;
}
</style>
