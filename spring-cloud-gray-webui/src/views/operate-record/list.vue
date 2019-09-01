<template>
  <div class="app-container">
    <el-card>
      <div class="filter-container">
        <el-form ref="queryForm" :rules="rules" :model="listQuery">
          <el-row>
            <el-col :span="8">
              <el-form-item label-width="120px" prop="startTime" label="Start Time" class="postInfo-container-item">
                <el-date-picker v-model="listQuery.startTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" placeholder="Select start date and time" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="120px" prop="endTime" label="End Time" class="postInfo-container-item">
                <el-date-picker v-model="listQuery.endTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" placeholder="Select end date and time" />
              </el-form-item>
            </el-col>
            <el-col :span="8" class="postInfo-container-item">
              <div style="padding-left: 80px">
                <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                  Search
                </el-button>
                <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
                  Export
                </el-button>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label-width="120px" label="Operator Id" prop="operator">
                <el-input v-model="listQuery.operator" placeholder="Operator Id" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="120px" label="ApiRes Code" prop="apiResCode">
                <el-input v-model="listQuery.apiResCode" placeholder="ApiRes Code" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="120px" label="Operate State" prop="operateState">
                <el-select v-model="listQuery.operateState" placeholder="结果" clearable class="filter-item" style="width: 200px">
                  <el-option :key="-1" :label="`全部`" :value="-1" />
                  <el-option :key="0" :label="`失败`" :value="0" />
                  <el-option :key="1" :label="`成功`" :value="1" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label-width="130px" label="Request Handler" prop="handler">
                <el-input v-model="listQuery.handler" placeholder="Request Handler" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="120px" label="URI" prop="uri">
                <el-input v-model="listQuery.uri" placeholder="URI" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="120px" label="IP" prop="ip">
                <el-input v-model="listQuery.ip" placeholder="IP" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>

    <el-card style="margin-bottom:20px; margin-top:20px;">
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
        <el-table-column label="IP" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.ip }}</span>
          </template>
        </el-table-column>
        <el-table-column label="URI" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.uri }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Http Method" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.httpMethod }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Query String" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.queryString }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Request Handler" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.handler }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Headler Args" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.headlerArgs }}</span>
          </template>
        </el-table-column>
        <el-table-column label="ApiRes Code" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.apiResCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Operate State" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.operateState | operateStateFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Operate Time" prop="operateTime" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.operateTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Operator Id" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.operator }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getList" />
    </el-card>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { fetchList } from '@/api/operate-record'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const statusMap = {
  1: '成功',
  0: '失败'
}

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    operateStateFilter(status) {
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
        startTime: '',
        endTime: '',
        operator: '',
        apiResCode: '',
        handler: '',
        uri: '',
        ip: '',
        operateState: 1,
        page: 1,
        size: 20
      },
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      rules: {
        startTime: [{ required: true, message: 'Start time is required', trigger: 'change' }],
        endTime: [{ required: true, message: 'End time is required', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  created() {
    const now = new Date()
    const endTime = parseTime(now)
    const startTime = parseTime(now.setHours(now.getHours() - 4))
    this.listQuery.endTime = endTime
    this.listQuery.startTime = startTime
    this.getList()
  },
  methods: {
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
      this.$refs['queryForm'].validate((valid) => {
        if (valid) {
          this.listQuery.page = 1
          this.getList()
        }
      })
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
