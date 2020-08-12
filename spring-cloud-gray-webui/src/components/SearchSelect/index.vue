<template>
  <div class="common-wrapper">
    <el-select v-model="valueMeta" multiple collapse-tags placeholder="请选择标签">
      <div class="el-input" style="width:90%;margin-left:5%;">
        <input v-model="dropDownValue" type="text" placeholder="请输入" class="el-input__inner" @keyup="dropDownSearch">
      </div>
      <el-option v-for="item in optionsMetaShow" :value="item" />
    </el-select>
  </div>
</template>
<script>
export default {
  data() {
    return {
      dropDownValue: '',
      optionsMetaAll: ['黄金糕', '双皮奶', '蚵仔煎', '双皮奶2', '龙须面', '北京烤鸭'],
      optionsMetaShow: ['黄金糕', '双皮奶', '蚵仔煎', '双皮奶2', '龙须面', '北京烤鸭'],
      valueMeta: []
    }
  },
  methods: {
    dropDownSearch() {
      var _this = this
      _this.valueMeta = []
      _this.optionsMetaShow = _this.optionsMetaAll.filter(_this.filterSearch)
    },
    filterSearch(item) {
      return item.includes(this.dropDownValue)
    }
  }
}
</script>
<style lang="">
  .el-scrollbar {
    display: block !important;
  }
</style>
