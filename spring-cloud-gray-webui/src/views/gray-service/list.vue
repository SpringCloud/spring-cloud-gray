<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select ref="" v-model="listQuery.namespace" placeholder="请选择">
        <el-option v-for="item in nsList" :key="item.code" :label="item.name" :value="item.code" />
      </el-select>

      <!--<el-input v-model="listQuery.title" placeholder="Service Id" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />-->
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
      <el-table-column label="Service Id" prop="serviceId" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.serviceId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Service Name" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.serviceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Context Path" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.contextPath }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实例数" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.instanceNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="灰度数" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.grayInstanceNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.describe }}</span>
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
          <el-dropdown trigger="click">
            <el-button size="mini" type="primary" style="width:80px" class="list-button">
              编辑
              <i class="el-icon-arrow-down" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(row)">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown trigger="click">
            <el-button size="mini" type="info" style="width:80px" class="list-button">
              实例
              <i class="el-icon-arrow-down" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <router-link :to="`/gray/instance?ns=${row.namespace}&serviceId=${row.serviceId}`"><el-dropdown-item>实例列表</el-dropdown-item></router-link>
              <router-link :to="'/gray/service/discovery-instances/'+row.serviceId+'?serviceId='+row.serviceId"><el-dropdown-item>在线实例</el-dropdown-item></router-link>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown trigger="click">
            <el-button size="mini" type="danger" style="width:80px" class="list-button">
              灰度
              <i class="el-icon-arrow-down" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <router-link :to="`/routingPolicy/serviceGrayPolicys?ns=${row.namespace}&resource=${row.serviceId}`"><el-dropdown-item>服务灰度</el-dropdown-item></router-link>
              <router-link :to="`/routingPolicy/serviceMultiVersionGrayPolicys?ns=${row.namespace}&moduleId=${row.serviceId}`"><el-dropdown-item>多版本灰度</el-dropdown-item></router-link>
            </el-dropdown-menu>
          </el-dropdown>
          <router-link :to="'/gray/trackor?serviceId='+row.serviceId">
            <el-button size="mini" type="success" class="list-button">
              追踪
            </el-button>
          </router-link>
          <router-link :to="'/gray/service/authority?serviceId='+row.serviceId">
            <el-button size="mini" type="success" class="list-button">
              权限
            </el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus] + '  ' + temp.serviceId" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item v-if="dialogStatus==='create'" label="Service Id" prop="serviceId">
          <el-input v-model="temp.serviceId" />
        </el-form-item>
        <el-form-item label="Service Name" prop="serviceName">
          <el-input v-model="temp.serviceName" />
        </el-form-item>
        <el-form-item label="Context Path" prop="contextPath">
          <el-input v-model="temp.contextPath" />
        </el-form-item>
        <el-form-item label="Describe" prop="describe">
          <el-input v-model="temp.describe" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
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
import { getData } from '@/api/api-request'
import { fetchList, createService, updateService, deleteService } from '@/api/gray-service'
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
        namespace: getDefaultNamespace(),
        page: 1,
        size: 10
      },
      nsList: [],
      importanceOptions: [1, 2, 3],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        namespace: '',
        serviceName: '',
        serviceId: '',
        contextPath: '',
        describe: ''
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
        serviceId: [{ required: true, message: 'serviceId is required', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getNamespaceList()
  },
  methods: {
    getNamespaceList() {
      getData('/namespace/listAll').then(response => {
        this.nsList = response.data
      })
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
      }).finally(r => {
        this.listLoading = false
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
        namespace: this.listQuery.namespace,
        serviceId: '',
        serviceName: '',
        contextPath: '',
        describe: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createService(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          }).finally(r => {
            this.listLoading = false
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
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
          updateService(tempData).then(() => {
            for (const v of this.list) {
              if (v.serviceId === this.temp.serviceId) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.temp)
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
          }).finally(r => {
            this.listLoading = false
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
        deleteService(row.serviceId).then(() => {
          this.dialogFormVisible = false
          for (const v of this.list) {
            if (v.serviceId === row.serviceId) {
              const index = this.list.indexOf(v)
              this.list.splice(index, 1)
              break
            }
          }
          this.$notify({
            title: 'Success',
            message: 'Delete Successfully',
            type: 'success',
            duration: 2000
          })
        }).finally(r => {
          this.listLoading = false
        })
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Service Id', 'Service Name', '实例数', '描述']
        const filterVal = ['serviceId', 'serviceName', 'instanceNumber', 'describe']
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

<style lang="scss">
  .list-button {
    margin-top: 5px;
  }
</style>
