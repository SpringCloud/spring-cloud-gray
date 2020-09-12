<template>
  <div class="TrackAttribute">
    <el-form
      ref="TrackAttribute"
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
        <el-form-item label="name" prop="name">
          <el-input v-model="infos.name" />
        </el-form-item>
        <el-form-item label="value" prop="value">
          <el-input v-model="infos.value" />
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
    return {
      infos: {
        value: '',
        name: '',
        compareMode: '',
        infos: '{"compareMode":"","name":"","value":""}'
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
      }],
      rules: {
        compareMode: [{ required: true, message: 'compareMode is required', trigger: 'change' }],
        name: [{ required: true, message: 'header is required', trigger: 'blur' }],
        value: [{ required: true, message: 'value is required', trigger: 'blur' }]
      }
    }
  },
  computed: {
    name() {
      return this.infos.name
    },
    compareMode() {
      return this.infos.compareMode
    },
    value() {
      return this.infos.value
    }
  },
  watch: {
    name(a) {
      this.setInfos()
    },
    compareMode(a) {
      this.setInfos()
    },
    value(a) {
      this.setInfos()
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
      this.infos.infos = '{"compareMode":"' + this.infos.compareMode + '","name":"' + this.infos.name + '","value":"' + this.infos.value + '"}'
      this.$emit('sendInfos', this.infos.infos)
    },
    clear() {
      this.$refs.TrackAttribute.clearValidate()
    },
    check() {
      let flag = false
      this.$refs.TrackAttribute.validate((valid) => {
        flag = valid
      })
      return flag
    }
  }
}
</script>
<style >
.TrackAttribute .selectBox .el-form-item{
    margin-right: 0;
margin-left: 20px;
}
.TrackAttribute .selectBox .el-form-item:first-child{
    margin: 0;
}
.TrackAttribute .infosBox{
    width: 100%;
}
.TrackAttribute .infosBox .el-form-item__content{
    width:80%;
}
</style>
