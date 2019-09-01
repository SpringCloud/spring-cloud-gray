<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.key" placeholder="Name or Account" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.status" placeholder="Status" clearable class="filter-item" style="width: 130px">
        <el-option :key="-1" :label="`全部`" :value="-1" />
        <el-option :key="0" :label="`禁用`" :value="0" />
        <el-option :key="1" :label="`启用`" :value="1" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        Search
      </el-button>
      <el-button v-if="isAdmin()" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
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
      <el-table-column label="User Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.userId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" prop="name" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Account" prop="account" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.account }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Roles" prop="roles" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.roles }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Status" prop="status" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.status | userStatusFilter }}</span>
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
      <el-table-column label="Actions" align="center" width="280" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-if="isAdmin()" type="primary" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <el-button v-if="isAdmin()" type="primary" size="mini" @click="handleResetPassword(row)">
            重置密码
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item v-if="dialogStatus=='create'" label="Account" prop="account">
          <el-input v-model="temp.account" />
        </el-form-item>
        <el-form-item v-if="dialogStatus=='create'" label="Password" prop="password">
          <el-input v-model="temp.password" :type="'password'" />
        </el-form-item>
        <el-form-item label="Name" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="Roles" prop="roles">
          <el-drag-select v-model="temp.roles" multiple placeholder="请选择">
            <el-option :key="'admin'" :label="'Admin'" :value="'admin'" />
            <el-option :key="'editor'" :label="'Editor'" :value="'editor'" />
          </el-drag-select>
          <!--<el-tooltip placement="top">
            <div slot="content">
              admin,editor
            </div>
            <div class="icon-item">
              <el-input v-model="temp.roles" />
            </div>
          </el-tooltip>-->
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="temp.status" placeholder="Status" clearable class="filter-item" style="width: 130px">
            <el-option :key="0" :label="`禁用`" :value="0" />
            <el-option :key="1" :label="`启用`" :value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" :disabled.sync="dialogFormConfirmDisabled" @click="dialogStatus==='create'?createData():updateData()">
          Confirm
        </el-button>
      </div>
    </el-dialog>

    <!-- 重置密码 -->
    <el-dialog :title="'Reset Password  ' + resPwdTemp.userId" :visible.sync="dialogResPwdFormVisible">
      <el-form ref="resPwdForm" :rules="rules" :model="resPwdTemp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="Password" prop="password">
          <el-input v-model="resPwdTemp.password" :type="'password'" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogResPwdFormVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" :disabled.sync="dialogResPwdFormConfirmDisabled" @click="resetPasswordData()">
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
import { mapGetters } from 'vuex'
import { fetchList, update, create, resetPassword } from '@/api/user'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import ElDragSelect from '@/components/DragSelect' // base on element-ui

export default {
  name: 'ComplexTable',
  components: { Pagination, ElDragSelect },
  directives: { waves },
  filters: {
    userStatusFilter(status) {
      const statusMap = {
        1: '启用',
        '0': '禁用'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        key: '',
        status: 1,
        page: 1,
        size: 10
      },
      importanceOptions: [1, 2, 3],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        name: '',
        account: '',
        status: 1,
        roles: [],
        userId: ''
      },
      resPwdTemp: {
        userId: '',
        passowrd: ''
      },
      dialogFormVisible: false,
      dialogFormConfirmDisabled: false,
      dialogResPwdFormVisible: false,
      dialogResPwdFormConfirmDisabled: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        account: [{ required: true, message: 'account is required', trigger: 'change' }],
        password: [{ required: true, message: 'password is required', trigger: 'change' }],
        name: [{ required: true, message: 'name is required', trigger: 'change' }],
        status: [{ required: true, message: 'status is required', trigger: 'change' }],
        roles: [{ required: true, message: 'roles is required', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'roles'
    ])
  },
  created() {
    this.getList()
  },
  methods: {
    isAdmin() {
      return this.roles.includes('admin')
    },
    getList() {
      this.listLoading = true
      // this.listQuery.page = this.listQuery.page - 1
      fetchList(this.listQuery).then(response => {
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
        name: '',
        account: '',
        status: 1,
        roles: '',
        userId: ''
      }
    },
    resetResPwdTemp() {
      this.resPwdTemp = {
        userId: '',
        password: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.dialogFormConfirmDisabled = false
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.dialogFormConfirmDisabled = true
          create(this.temp).then(response => {
            this.list.unshift(response.data)
            this.dialogFormVisible = false
            this.dialogFormConfirmDisabled = false
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
      this.resetTemp()
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.dialogFormConfirmDisabled = false
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464

          this.dialogFormConfirmDisabled = true
          update(tempData).then(response => {
            for (const v of this.list) {
              if (v.userId === this.temp.userId) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, response.data)
                break
              }
            }
            this.dialogFormVisible = false
            this.dialogFormConfirmDisabled = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          }).catch(err => {
            console.log(err)
            this.dialogFormConfirmDisabled = false
          })
        }
      })
    },
    handleResetPassword(row) {
      this.resetResPwdTemp()
      this.resPwdTemp = Object.assign({}, row) // copy obj
      this.dialogResPwdFormVisible = true
      this.dialogResPwdFormConfirmDisabled = false
      this.$nextTick(() => {
        this.$refs['resPwdForm'].clearValidate()
      })
    },
    resetPasswordData() {
      this.$refs['resPwdForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.resPwdTemp)
          this.dialogResPwdFormConfirmDisabled = true
          resetPassword(tempData).then(response => {
            this.dialogResPwdFormVisible = false
            this.dialogResPwdFormConfirmDisabled = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          }).catch(err => {
            console.log(err)
            this.dialogResPwdFormConfirmDisabled = false
          })
        }
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Service Id', 'User Id']
        const filterVal = ['serviceId', 'userId']
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'gray-service-list'
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
