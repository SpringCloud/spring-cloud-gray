<template>
  <div class="HttpParameter">
    <el-form
      ref="HttpParameter"
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
        <el-form-item label="values" prop="values">
          <el-input v-model="infos.values" />
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
        values: '',
        name: '',
        compareMode: '',
        infos: '{"compareMode":"","name":"","values":""}'
      },
      options: [{
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
        name: [{ required: true, message: 'header is required', trigger: 'blur' }],
        values: [{ required: true, message: 'values is required', trigger: 'blur' }]
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
    values() {
      return this.infos.values
    }
  },
  watch: {
    name(a) {
      if (a) {
        this.setInfos()
      }
    },
    compareMode(a) {
      if (a) {
        this.setInfos()
      }
    },
    values(a) {
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
      this.infos.infos = '{"compareMode":"' + this.infos.compareMode + '","name":"' + this.infos.name + '","values":"' + this.infos.values + '"}'
      this.$emit('sendInfos', this.infos.infos)
    },
    clear() {
      this.$refs.HttpParameter.clearValidate()
    },
    check() {
      let flag = false
      this.$refs.HttpParameter.validate((valid) => {
        flag = valid
      })
      return flag
    }
  }
}
</script>
<style >
.HttpParameter .selectBox .el-form-item{
    margin-right: 0;
margin-left: 20px;
}
.HttpParameter .selectBox .el-form-item:first-child{
    margin: 0;
}
.HttpParameter .infosBox{
    width: 100%;
}
.HttpParameter .infosBox .el-form-item__content{
    width:80%;
}
</style>
