<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        Search
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
      <!--<el-table-column label="Service Id" prop="serviceId" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.serviceId }}</span>
        </template>
      </el-table-column>-->
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
      <el-table-column label="Actions" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-if="row.grayStatus!='OPEN'" type="primary" size="mini" @click="createData(row)">
            Add
          </el-button>
          <el-dropdown trigger="click">
            <el-button size="mini" type="info" style="width:80px">
              实例状态
              <i class="el-icon-arrow-down" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="changeInstanceStatus(row, 'STARTING')">STARTING</el-dropdown-item>
              <el-dropdown-item @click.native="changeInstanceStatus(row, 'UP')">UP</el-dropdown-item>
              <el-dropdown-item @click.native="changeInstanceStatus(row, 'OUT_OF_SERVICE')">OUT_OF_SERVICE</el-dropdown-item>
              <el-dropdown-item @click.native="changeInstanceStatus(row, 'DOWN')">DOWN</el-dropdown-item>
              <el-dropdown-item @click.native="changeInstanceStatus(row, 'UNKNOWN')">UNKNOWN</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown trigger="click">
            <el-button size="mini" type="info" style="width:80px" class="list-button">
              灰度信息
              <i class="el-icon-arrow-down" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="showGrayInfos(row, 'SERVICES')">服务/实例</el-dropdown-item>
              <el-dropdown-item @click.native="showGrayInfos(row, 'TRACKS')">追踪</el-dropdown-item>
              <el-dropdown-item @click.native="showGrayInfos(row, 'POLICY')">策略</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog custom-class="el-dialog-cus" :title="grayInfo.type" :visible.sync="grayInfo.dialogVisible" :loading="grayInfo.loading">
      <pre>{{ grayInfo.content }}</pre>
      <div slot="footer" class="dialog-footer">
        <el-button @click="grayInfo.dialogVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="refershGrayInfos()">
          Refersh
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { fetchList, createInstance, tryChangeInstanceStatus } from '@/api/discovery-instance'
import waves from '@/directive/waves' // waves directive
import { getServiceAllInfos, getAllDefinitions, getClientInfos } from '@/api/gray-client'
import { parseTime } from '@/utils'

export default {
  name: 'ComplexTable',
  components: { },
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
      listLoading: true,
      listQuery: {
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
        grayStatus: 'OPEN',
        des: ''
      },
      grayInfo: {
        instanceId: '',
        type: '',
        content: '',
        dialogVisible: false,
        loading: false
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
      var title = '在线实例'
      if (this.listQuery.serviceId) {
        title = title + '-' + this.listQuery.serviceId
      }
      document.title = `${title}`
    },
    setTagsViewTitle() {
      var title = '在线实例'
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
        this.list = response.data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 0.2 * 1000)
      })
    },
    handleFilter() {
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
    createData(row) {
      this.$confirm('Confirm to add gray?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        const data = {
          serviceId: row.serviceId,
          instanceId: row.instanceId,
          host: row.host,
          port: row.port,
          grayStatus: 'OPEN'
        }
        createInstance(data).then(() => {
          for (const v of this.list) {
            if (v.instanceId === data.instanceId) {
              v.grayStatus = 'OPEN'
              break
            }
          }
          this.$notify({
            title: 'Success',
            message: 'Created Successfully',
            type: 'success',
            duration: 2000
          })
        })
      })
    },
    async changeInstanceStatus(row, status) {
      tryChangeInstanceStatus(row, status).then(() => {
        this.dialogFormVisible = false
        this.$notify({
          title: 'Success',
          message: '更新成功，稍等重新查看实例状态',
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
    showGrayInfos(row, type) {
      this.grayInfo.instanceId = row.instanceId
      this.grayInfo.type = type
      this.grayInfo.dialogVisible = true
      this.grayInfo.content = ''
      this.refershGrayInfos()
    },
    refershGrayInfos() {
      this.grayInfo.loading = true
      if (this.grayInfo.type === 'SERVICES') {
        getServiceAllInfos(this.listQuery.serviceId, this.grayInfo.instanceId).then(res => {
          this.grayInfo.content = res.data
          this.grayInfo.loading = false
        })
      } else if (this.grayInfo.type === 'TRACKS') {
        getAllDefinitions(this.listQuery.serviceId, this.grayInfo.instanceId).then(res => {
          this.grayInfo.content = res.data
          this.grayInfo.loading = false
        })
      } else {
        getClientInfos(this.listQuery.serviceId, this.grayInfo.instanceId, this.grayInfo.type).then(res => {
          this.grayInfo.content = res.data
          this.grayInfo.loading = false
        })
      }
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

<style>
  .el-dialog-cus{
    width: 80%;
  }
</style>
