<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.serviceId" placeholder="Service Id" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="Instance Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.instanceId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Host" width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.host }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Port" width="90px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.port }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Gray Status" class-name="status-col" min-width="100px">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.grayStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Instance Status" min-width="110px">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.instanceStatus }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="Last Update" width="150px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.lastUpdateDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <router-link :to="'/gray/instance/policy?instanceId='+row.instanceId">
            <el-button size="mini" type="success">
              策略
            </el-button>
          </router-link>
          <el-button size="mini" type="danger" @click="handleDelete(row)">
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus] + '  ' + temp.instanceId" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="Service Id" prop="serviceId">
          <el-input v-model="temp.serviceId" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='create'" label="Instance Id" prop="instanceId">
          <el-input v-model="temp.instanceId" />
        </el-form-item>
        <el-form-item label="Host" prop="host">
          <el-input v-model="temp.host" />
        </el-form-item>
        <el-form-item label="Port" prop="port">
          <el-input v-model="temp.port" />
        </el-form-item>
        <el-form-item label="Gray Status" prop="grayStatus">
          <el-select v-model="temp.grayStatus" class="filter-item" placeholder="Please select">
            <el-option v-for="item in grayStatusOptions" :key="item" :label="item" :value="item" />
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
import { fetchList, deleteInstance, createInstance, updateInstance } from '@/api/gray-instance'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    grayStatusFilter(status) {
      const statusMap = {
        OPEN: 'success',
        CLOSE: 'danger'
      }
      return statusMap[status]
    },
    instanceStatusFilter(status) {
      const statusMap = {
        UP: 'success',
        STARTING: 'danger'
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
        page: 1,
        limit: 20,
        serviceId: this.$route.query.serviceId || ''
      },
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      grayStatusOptions: ['OPEN', 'CLOSE'],
      showReviewer: false,
      temp: {
        serviceId: this.$route.query.serviceId || '',
        instanceId: '',
        host: '',
        port: 0,
        grayStatus: 'OPEN'
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
        serviceId: [{ required: true, message: 'serviceId is required', trigger: 'blur' }],
        instanceId: [{ required: true, message: 'instanceId is required', trigger: 'blur' }],
        host: [{ required: true, message: 'host is required', trigger: 'blur' }],
        port: [{ required: true, message: 'port is required', trigger: 'blur' }],
        grayStatus: [{ required: true, message: 'grayStatus is required', trigger: 'change' }]
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
      var title = '灰度实例'
      if (this.listQuery.serviceId) {
        title = title + '-' + this.listQuery.serviceId
      }
      document.title = `${title}`
    },
    setTagsViewTitle() {
      var title = '灰度实例'
      if (this.listQuery.serviceId) {
        title = title + '-' + this.listQuery.serviceId
      }
      const route = Object.assign({}, this.tempRoute, { title: `${title}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
      console.log(route)
      console.log(this.$store.dispatch)
    },
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total

        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1 * 1000)
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.setTagsViewTitle()
      this.setPageTitle()
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
      const serviceId = this.listQuery.serviceId
      this.temp = {
        serviceId: serviceId,
        instanceId: '',
        host: '',
        port: 0,
        grayStatus: 'OPEN'
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
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          createInstance(this.temp).then(() => {
            this.list.unshift(this.temp)
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
          updateInstance(tempData).then(() => {
            for (const v of this.list) {
              if (v.instanceId === this.temp.instanceId) {
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
          })
        }
      })
    },
    handleDelete(row) {
      deleteInstance(row.instanceId).then(() => {
        this.dialogFormVisible = false
        for (const v of this.list) {
          if (v.instanceId === row.instanceId) {
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
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Service Id', 'Instance Id', 'Host', 'Port', 'Gray Status', 'Instance Status']
        const filterVal = ['serviceId', 'instanceId', 'host', 'port', 'grayStatus', 'instanceStatus']
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
