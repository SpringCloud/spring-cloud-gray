<template>
  <div class="HttpMethod">
    <el-form
      ref="HttpMethod"
      :rules="rules"
      :model="infos"
      :inline="true"
    >
      <div class="selectBox">
        <el-form-item
          label="compareMode"
          prop="compareMode"
          label-width="120px"
        >
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
        <el-form-item
          label="method"
          prop="method"
        >
          <el-select
            v-model="infos.method"
            placeholder="请选择"
          >
            <el-option
              v-for="item in methodOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

      </div>
      <div class="infosBox">
        <el-form-item
          label="infos"
          prop="infos"
          label-width="120px"
          style="width:100%"
        >
          <el-input
            v-model="infos.infos"
            type="textarea"
            readonly
          />
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
        compareMode: '',
        method: '',
        infos: '{"compareMode":"","method":""}'
      },
      methodOptions: [{
        value: 'GET',
        label: 'GET'
      }, {
        value: 'HEAD',
        label: 'HEAD'
      }, {
        value: 'POST',
        label: 'POST'
      }, {
        value: 'PUT',
        label: 'PUT'
      }, {
        value: 'DELETE',
        label: 'DELETE'
      }, {
        value: 'CONNECT',
        label: 'CONNECT'
      }, {
        value: 'OPTIONS',
        label: 'OPTIONS'
      }, {
        value: 'TRACE',
        label: 'TRACE'
      }, {
        value: 'PATCH',
        label: 'PATCH'
      }, {
        value: 'MOVE',
        label: 'MOVE'
      }, {
        value: 'COPY',
        label: 'COPY'
      }, {
        value: 'LINK',
        label: 'LINK'
      }, {
        value: 'UNLINK',
        label: 'UNLINK'
      }, {
        value: 'WRAPPED',
        label: 'WRAPPED'
      }, {
        value: 'Extension-mothed',
        label: 'Extension-mothed'
      }],
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
        method: [{ required: true, message: 'method is required', trigger: 'change' }]

      }
    }
  },
  computed: {
    method() {
      return this.infos.method
    },
    compareMode() {
      return this.infos.compareMode
    }

  },
  watch: {
    method(a) {
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
      this.infos.infos = '{"compareMode":"' + this.infos.compareMode + '","method":"' + this.infos.method + '"}'
      this.$emit('sendInfos', this.infos.infos)
    },
    clear() {
      this.$refs.HttpMethod.clearValidate()
    },
    check() {
      let flag = false
      this.$refs.HttpMethod.validate((valid) => {
        flag = valid
      })
      return flag
    }
  }
}
</script>
<style >
.HttpMethod .selectBox .el-form-item {
  margin-right: 0;
  margin-left: 20px;
}
.HttpMethod .selectBox .el-form-item:first-child {
  margin: 0;
}
.HttpMethod .infosBox {
  width: 100%;
}
.HttpMethod .infosBox .el-form-item__content {
  width: 80%;
}
</style>
