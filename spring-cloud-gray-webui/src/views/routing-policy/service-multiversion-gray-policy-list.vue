<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.resource" placeholder="版本" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.delFlag" placeholder="Status" clearable class="filter-item" style="width: 130px" @change="handleFilter">
        <el-option :key="`ALL`" :label="`全部`" :value="`ALL`" />
        <el-option :key="`UNDELETE`" :label="`启用`" :value="`UNDELETE`" />
        <el-option :key="`DELELTED`" :label="`删除`" :value="`DELELTED`" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        Search
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        Add
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        Export
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="Service Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.moduleId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Version" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.resource }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Policy Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.policyId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Policy Alias" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.policyAlias }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="Instance Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.instanceId }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="Operator" prop="operator" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.operator }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Operate Time" prop="operateTime" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.operateTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <router-link :to="'/policy/grayPolicys/decision/'+row.policyId">
            <el-button size="mini" type="success">
              决策
            </el-button>
          </router-link>
          <el-button v-if="!row.delFlag" size="mini" type="danger" @click="handleDelete(row)">
            Delete
          </el-button>
          <el-button v-if="row.delFlag" size="mini" type="primary" @click="handleRecover(row)">
            恢复
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus] + '  ' + temp.id" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="Version" prop="resource">
          <el-input v-model="temp.resource" placeholder="版本" style="width: 200px;" />
        </el-form-item>
        <el-form-item label="Policy" prop="policyId">
          <el-select ref="" v-model="temp.policyId" placeholder="请选择">
            <el-option v-for="item in policyList" :key="item.id" :label="item.alias" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Confirm
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getDefaultNamespace } from '@/utils/ns'
import { fetchList, createRecord, deleteRecord, recoverRecord, getData } from '@/api/api-request'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        type: 'SERVICE_MULTI_VER_ROUTE',
        moduleId: this.$route.query.moduleId,
        resource: '',
        ns: this.$route.query.ns || getDefaultNamespace(),
        delFlag: 'UNDELETE'
      },
      policyList: [],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        namespace: '',
        type: '',
        moduleId: '',
        resource: '',
        policyId: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        policyId: [{ required: true, message: 'Policy is required', trigger: 'change' }],
        resource: [{ required: true, message: 'Version is required', trigger: 'change' }]
      },
      downloadLoading: false,
      tempRoute: {}
    }
  },
  created() {
    this.tempRoute = Object.assign({}, this.$route)
    this.getList()
    this.setTagsViewTitle()
    this.setPageTitle()
  },
  methods: {
    setPageTitle() {
      const title = '多版本灰度'
      document.title = `${title} - ${this.listQuery.moduleId}`
    },
    setTagsViewTitle() {
      const title = '多版本灰度'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.listQuery.moduleId}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
      console.log(route)
      console.log(this.$store.dispatch)
    },
    getList() {
      this.listLoading = true
      const params = Object.assign({}, this.listQuery)
      fetchList('/route/policy/page', params).then(response => {
        this.list = response.data.items
        this.total = response.data.total

        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 0.2 * 1000)
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        namespace: this.listQuery.ns,
        type: this.listQuery.type,
        moduleId: this.listQuery.moduleId,
        resource: this.listQuery.resource,
        policyId: undefined
      }
    },
    resetPolicyList() {
      const params = { 'namespace': this.temp.namespace }
      getData('/gray/policy/list', params).then(response => {
        this.policyList = response.data
      })
    },
    handleCreate() {
      this.resetTemp()
      this.resetPolicyList()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true

      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          createRecord('/route/policy/save', this.temp).then(response => {
            this.list.unshift(response.data)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('Confirm to remove the record?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        deleteRecord('/route/policy/?id=' + row.id).then(() => {
          this.dialogFormVisible = false
          /** for (const v of this.list) {
            if (v.id === row.id) {
              const index = this.list.indexOf(v)
              this.list.splice(index, 1)
              break
            }
          } **/
          this.getList()
          this.$notify({
            title: 'Success',
            message: 'Delete Successfully',
            type: 'success',
            duration: 2000
          })
        })
      })
    },
    handleRecover(row) {
      this.$confirm('Confirm to recover the record?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        recoverRecord('/route/policy/recover?id=' + row.id).then(() => {
          this.dialogFormVisible = false
          this.getList()
          this.$notify({
            title: 'Success',
            message: 'Delete Successfully',
            type: 'success',
            duration: 2000
          })
        })
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Service Id', 'Version', 'Policy Id', 'Policy Alias', 'Operator', 'Operate Time']
        const filterVal = ['moduleId', 'resource', 'policyId', 'policyAlias', 'operator', 'operateTime']
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>
