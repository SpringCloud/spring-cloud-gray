<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.delFlag" placeholder="Status" clearable class="filter-item" style="width: 130px" @change="handleFilter">
        <el-option :key="`ALL`" :label="`全部`" :value="`ALL`" />
        <el-option :key="`UNDELETE`" :label="`启用`" :value="`UNDELETE`" />
        <el-option :key="`DELELTED`" :label="`删除`" :value="`DELELTED`" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        Refresh
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
      <el-table-column label="Id" prop="id" sortable="custom" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Infos" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.infos }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Policy Id" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.policyId }}</span>
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
      <el-table-column label="Actions" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="70%">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 90%; margin-left:50px;">
        <el-form-item v-if="dialogFormVisible" label="Name" prop="type">
          <div style="display:flex;align-items: center;">
            <el-select
              v-model="temp.type"
              placeholder="请选择"
              @change="changeSelect"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-input v-if="temp.type === '自定义名称'" v-model="temp.name" style="margin-left:30px;" />
          </div>
        </el-form-item>
        <div>
          <HttpMethod v-if="temp.type === 'HttpMethod'" ref="HttpMethod" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <HttpHeader v-if="temp.type === 'HttpHeader'" ref="HttpHeader" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <HttpParameter v-if="temp.type === 'HttpParameter'" ref="HttpParameter" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <HttpTrackHeader v-if="temp.type === 'HttpTrackHeader'" ref="HttpTrackHeader" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <HttpTrackParameter v-if="temp.type === 'HttpTrackParameter'" ref="HttpTrackParameter" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <TraceIp v-if="temp.type === 'TraceIp'" ref="TraceIp" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <TraceIps v-if="temp.type === 'TraceIps'" ref="TraceIps" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <TrackAttribute v-if="temp.type === 'TrackAttribute'" ref="TrackAttribute" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <TrackAttributes v-if="temp.type === 'TrackAttributes'" ref="TrackAttributes" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <FlowRate v-if="temp.type === 'FlowRate'" ref="FlowRate" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <Attribute v-if="temp.type === 'Attribute'" ref="Attribute" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <Attributes v-if="temp.type === 'Attributes'" ref="Attributes" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <FixedValueRandomFlowRate v-if="temp.type === 'FixedValueRandomFlowRate'" ref="FixedValueRandomFlowRate" :info="temp.infos" @sendInfos="sendInfos" @sendDecisionName="sendDecisionName" />
          <el-form-item v-if="temp.type === '自定义名称'" label="infos" prop="infos">
            <el-input v-model="temp.infos" />
          </el-form-item>
        </div>
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
import { recoverRecord } from '@/api/api-request'
import { fetchList, deleteDecision, createDecision, updateDecision } from '@/api/gray-decision'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ComplexTable',
  components: { Pagination,
    HttpMethod: () => import('./components/HttpMethod'),
    HttpHeader: () => import('./components/HttpHeader'),
    HttpParameter: () => import('./components/HttpParameter'),
    HttpTrackHeader: () => import('./components/HttpTrackHeader'),
    HttpTrackParameter: () => import('./components/HttpTrackParameter'),
    TraceIp: () => import('./components/TraceIp'),
    TraceIps: () => import('./components/TraceIps'),
    TrackAttribute: () => import('./components/TrackAttribute'),
    TrackAttributes: () => import('./components/TrackAttributes'),
    Attribute: () => import('./components/Attribute'),
    Attributes: () => import('./components/Attributes'),
    FlowRate: () => import('./components/FlowRate'),
    FixedValueRandomFlowRate: () => import('./components/FixedValueRandomFlowRate')
  },
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
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        policyId: this.$route.params.policyId || '',
        delFlag: 'UNDELETE'
      },
      options: [{
        value: 'HttpMethod',
        label: 'HttpMethod'
      }, {
        value: 'HttpHeader',
        label: 'HttpHeader'
      }, {
        value: 'HttpParameter',
        label: 'HttpParameter'
      }, {
        value: 'HttpTrackHeader',
        label: 'HttpTrackHeader'
      }, {
        value: 'HttpTrackParameter',
        label: 'HttpTrackParameter'
      }, {
        value: 'TraceIp',
        label: 'TraceIp'
      }, {
        value: 'TraceIps',
        label: 'TraceIps'
      }, {
        value: 'TrackAttribute',
        label: 'TrackAttribute'
      }, {
        value: 'TrackAttributes',
        label: 'TrackAttributes'
      }, {
        value: 'Attribute',
        label: 'Attribute'
      }, {
        value: 'Attributes',
        label: 'Attributes'
      }, {
        value: 'FlowRate',
        label: 'FlowRate'
      }, {
        value: 'FixedValueRandomFlowRate',
        label: 'FixedValueRandomFlowRate'
      }, {
        value: '自定义名称',
        label: '自定义名称'
      }],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        type: '',
        name: '',
        status: 'published'
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
        type: [{ required: true, trigger: 'change', validator: Typerules }],
        infos: [{ required: true, message: 'infos is required', trigger: 'blur' }]

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
    changeSelect() {
      this.temp.infos = ''
    },
    sendInfos(val) {
      this.temp.infos = val
    },
    sendDecisionName(name) {
      this.temp.name = name
    },
    setPageTitle() {
      const title = '灰度决策'
      document.title = `${title} - ${this.listQuery.policyId}`
    },
    setTagsViewTitle() {
      const title = '灰度决策'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.listQuery.policyId}` })
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
      const policyId = this.listQuery.policyId
      this.temp = {
        id: undefined,
        policyId: policyId,
        name: '',
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
          if (this.temp.type !== '自定义名称') {
            if (this.$refs[this.temp.type].check()) {
              this.temp.name = this.temp.type
              this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
              this.temp.author = 'vue-element-admin'
              createDecision(this.temp).then(response => {
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
          } else {
            this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
            this.temp.author = 'vue-element-admin'
            createDecision(this.temp).then(response => {
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
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.type !== '自定义名称') {
            if (this.$refs[this.temp.type].check()) {
              this.temp.name = this.temp.type
              const tempData = Object.assign({}, this.temp)
              tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
              updateDecision(tempData).then(response => {
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
          } else {
            const tempData = Object.assign({}, this.temp)
            tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
            updateDecision(tempData).then(response => {
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
        }
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
    handleUpdate(row) {
      this.temp = { ...row } // copy obj
      if (this.checkName(this.temp.name)) {
        this.$set(this.temp, 'type', this.temp.name)
        this.$nextTick(() => {
          this.$refs[this.temp.name].clear()
        })
      } else {
        this.$set(this.temp, 'type', '自定义名称')
      }
      console.log(this.temp)
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('Confirm to remove the record?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        deleteDecision(row.id).then(() => {
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
        recoverRecord(`/gray/decision/${row.id}/recover`).then(() => {
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
        const tHeader = ['Id', 'Name', 'Infos', 'Policy Id', 'Instance Id']
        const filterVal = ['id', 'name', 'infos', 'policyId', 'instanceId']
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
