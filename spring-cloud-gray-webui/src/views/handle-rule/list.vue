<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select ref="" v-model="listQuery.namespace" placeholder="请选择">
        <el-option v-for="item in nsList" :key="item.code" :label="item.name" :value="item.code" />
      </el-select>

      <!--<el-input v-model="listQuery.type" placeholder="Type" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
      <!--<el-input v-model="listQuery.moduleId" placeholder="Module Id" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
      <!--<el-input v-model="listQuery.resource" placeholder="Resource" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->

      <el-select v-model="listQuery.type" placeholder="Type" clearable class="filter-item" style="width: 130px" @change="handleFilter">
        <el-option :key="`mock_server_client_response`" :label="`MOCK_SERVER_CLIENT_RESPONSE`" :value="`mock_server_client_response`" />
        <el-option :key="`mock_application_response`" :label="`MOCK_APPLICATION_RESPONSE`" :value="`mock_application_response`" />
      </el-select>

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
      <el-table-column label="Id" prop="serviceId" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Type" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Module Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.moduleId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Resource" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.resource }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Handle Option" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.handleOptionAlias }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Matching Policys" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.matchingPolicys }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Order" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.order }}</span>
        </template>
      </el-table-column>
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
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <router-link :to="`/policy/handle/action?handleId=${row.id}`">
            <el-button size="mini" type="success">
              动作
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
        <el-form-item v-if="dialogStatus=='create'" label="Type" prop="type">
          <el-select v-model="temp.type" placeholder="Type" clearable>
            <el-option :key="`mock_server_client_response`" :label="`MOCK_SERVER_CLIENT_RESPONSE`" :value="`mock_server_client_response`" />
            <el-option :key="`mock_application_response`" :label="`MOCK_APPLICATION_RESPONSE`" :value="`mock_application_response`" />
          </el-select>
        </el-form-item>
        <el-form-item label="Module Id" prop="moduleId">
          <el-input v-model="temp.moduleId" />
        </el-form-item>
        <el-form-item label="Resource" prop="resource">
          <el-input v-model="temp.resource" />
        </el-form-item>
        <el-form-item label="Matching Policys" prop="matchingPolicyIds">
          <el-select ref="" v-model="temp.matchingPolicyIds" multiple placeholder="请选择">
            <el-option v-for="item in policyList" :key="item.id" :label="item.alias" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="Handle Option" prop="handleOption">
          <el-select ref="" v-model="temp.handleOption" placeholder="请选择">
            <el-option v-for="item in handleList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="Order" prop="order">
          <el-input v-model="temp.order" />
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
import { fetchList, deleteRecord, recoverRecord, createRecord, updateRecord, getData } from '@/api/api-request'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

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
        namespace: getDefaultNamespace(),
        type: '',
        moduleId: '',
        resource: '',
        delFlag: 'UNDELETE'
      },
      policyList: [],
      handleList: [],
      nsList: [],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: '',
        moduleId: '',
        resource: '',
        matchingPolicyIds: [],
        namespace: '',
        handleOption: '',
        order: '',
        type: ''
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
        moduleId: [{ required: true, message: 'Module Id is required', trigger: 'change' }],
        resource: [{ required: true, message: 'Resource Id is required', trigger: 'change' }],
        handleOption: [{ required: true, message: 'Handle Option Id is required', trigger: 'change' }],
        order: [{ required: true, message: 'Order Id is required', trigger: 'change' }],
        matchingPolicyIds: [{ required: true, message: 'Matching Policys Id is required', trigger: 'change' }],
        type: [{ required: true, message: 'Type is required', trigger: 'change' }]
      },
      downloadLoading: false,
      tempRoute: {}
    }
  },
  created() {
    this.tempRoute = Object.assign({}, this.$route)
    this.getNamespaceList()
    this.getList()
  },
  methods: {
    getNamespaceList() {
      getData('/namespace/listAll').then(response => {
        this.nsList = response.data
      })
    },
    getList() {
      this.listLoading = true
      fetchList('/handleRule/page', this.listQuery).then(response => {
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
        id: '',
        moduleId: '',
        resource: '',
        matchingPolicyIds: [],
        handleOption: '',
        order: '',
        namespace: this.listQuery.namespace,
        type: this.listQuery.type
      }
    },
    resetPolicyList() {
      const params = { 'namespace': this.temp.namespace }
      getData('/gray/policy/list', params).then(response => {
        this.policyList = response.data
      })
    },
    resetHandleList() {
      const params = { 'namespace': this.temp.namespace }
      getData('/handle/listAll', params).then(response => {
        this.handleList = response.data
      })
    },
    handleCreate() {
      this.resetTemp()
      this.resetPolicyList()
      this.resetHandleList()
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
          createRecord('/handleRule/', this.temp).then(response => {
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
    handleUpdate(row) {
      this.temp = {
        id: row.id,
        moduleId: row.moduleId,
        resource: row.resource,
        matchingPolicyIds: [],
        handleOption: row.handleOption,
        order: row.order,
        namespace: row.namespace,
        type: row.type
      }
      this.resetPolicyList()
      this.temp.matchingPolicys = undefined
      this.temp.matchingPolicyIds = []
      for (var x = 0; x < row.matchingPolicys.length; x++) {
        const policy = row.matchingPolicys[x]
        this.temp.matchingPolicyIds.push(policy.policyId)
      }

      this.resetHandleList()
      if (!row.handleOption === false) {
        this.temp.handleOption = parseInt(row.handleOption)
      }

      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateRecord('/handleRule/', tempData).then(response => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, response.data)
                break
              }
            }
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
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
        deleteRecord('/handleRule/' + row.id).then(() => {
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
    handleRecover(row) {
      this.$confirm('Confirm to recover the record?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        recoverRecord('/handleRule/' + row.id + '/recover').then(() => {
          this.dialogFormVisible = false
          this.getList()
          this.$notify({
            title: 'Success',
            message: 'Recover Successfully',
            type: 'success',
            duration: 2000
          })
        })
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Id', 'Alias', 'Instance Id']
        const filterVal = ['id', 'alias', 'instanceId']
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
