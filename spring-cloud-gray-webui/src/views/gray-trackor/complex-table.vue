<template>
  <div class="app-container">
    <div class="filter-container">
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
      <el-table-column label="Id" prop="id" width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="110px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Infos" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.infos }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="Service Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.serviceId }}</span>
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
      <el-table-column label="Actions" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="Service Id" prop="serviceId">
          <el-input v-model="temp.serviceId" disabled="true" />
        </el-form-item>
        <el-form-item
          label="Name"
          prop="type"
        >
          <div style="display:flex;align-items: center;">
            <el-select
              v-model="temp.type"
              placeholder="请选择"
              :disabled="dialogStatus=='update'"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-input
              v-if="temp.type === '自定义名称'"
              v-model="temp.name"
              style="margin-left:20px"
            />
          </div>

        </el-form-item>
        <el-form-item
          label="Infos"
          prop="infos"
        >
          <el-input
            v-model="temp.infos"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="Please input"
          />
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
import { fetchList, deleteTrackInfo, createTrackInfo, updateTrackInfo } from '@/api/gray-trackor'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  data() {
    const Typerules = (rule, value, callback) => {
      if (!value) {
        callback(new Error('Name is required'))
      } else {
        if (!this.temp.name && value === '自定义名称') {
          callback(new Error('Name is required'))
        } else {
          callback()
        }
      }
    }
    return {
      tableKey: 0,
      list: null,
      total: 0,
      options: [{
        value: 'HttpReceive',
        label: 'HttpReceive'
      }, {
        value: 'HttpHeader',
        label: 'HttpHeader'
      }, {
        value: 'HttpIP',
        label: 'HttpIP'
      }, {
        value: 'HttpMethod',
        label: 'HttpMethod'
      }, {
        value: 'HttpParameter',
        label: 'HttpParameter'
      }, {
        value: 'HttpURI',
        label: 'HttpURI'
      }, {
        value: '自定义名称',
        label: '自定义名称'
      }
      ],
      value: '',
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        serviceId: this.$route.query.serviceId || '',
        sort: 'id,desc'
      },
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        serviceId: this.$route.query.serviceId || '',
        instanceId: '',
        name: '',
        type: '',
        infos: ''
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
        serviceId: [{ required: true, message: 'Service Id is required', trigger: 'change' }],
        type: [{ required: true, trigger: 'change', validator: Typerules }]
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
      var title = '灰度追踪'
      if (this.listQuery.serviceId) {
        title = title + '-' + this.listQuery.serviceId
      }
      document.title = `${title}`
    },
    setTagsViewTitle() {
      var title = '灰度追踪'
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
      const serviceId = this.listQuery.serviceId
      this.temp = {
        id: undefined,
        serviceId: serviceId,
        instanceId: '',
        name: '',
        type: '',
        infos: ''
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
          this.temp.author = 'vue-element-admin'
          if (this.temp.type !== '自定义名称') {
            this.temp.name = this.temp.type
          }
          createTrackInfo(this.temp).then(response => {
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
      this.temp = Object.assign({}, row) // copy obj
      console.log(this.temp.name)
      if (this.checkName(this.temp.name)) {
        this.$set(this.temp, 'type', this.temp.name)
      } else {
        this.$set(this.temp, 'type', '自定义名称')
      }
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    checkName(str) {
      let flag = false
      this.options.forEach(item => {
        if (item.value === str) {
          flag = true
        }
      })
      return flag
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.type !== '自定义名称') {
            this.temp.name = this.temp.type
          }
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateTrackInfo(tempData).then(response => {
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
        deleteTrackInfo(row.id).then(() => {
          this.dialogFormVisible = false
          for (const v of this.list) {
            if (v.id === row.id) {
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
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Id', 'Name', 'Infos', 'Service Id', 'Instance Id']
        const filterVal = ['id', 'name', 'infos', 'serviceId', 'instanceId']
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
