<template>
  <div class="app-container">
    <div style="margin-bottom:20px;">
      <div class="filter-container" style="margin-right: 10px;">
        <el-input v-model="listQuery.customer_name" placeholder="Customer Name" style="width: 180px; margin-right: 10px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.email" placeholder="Email" style="width: 140px; margin-right: 10px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.phone" placeholder="Telephone" style="width: 140px; margin-right: 10px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          Search
        </el-button>
        <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
          Export Order
        </el-button>
      </div>
    </div>
    <div>
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column label="Order ID" prop="item_id" sortable align="center">
          <template slot-scope="{row}">
            <span>{{ row.order_id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Customer Name" prop="name" sortable align="center">
          <template slot-scope="{row}">
            <span>{{ row.customer_name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Create Time" prop="category" sortable align="center">
          <template slot-scope="{row}">
            <span>{{ row.create_time * 1000 | timeFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Total Income" prop="in_stock" sortable align="center">
          <template slot-scope="{row}">
            <span>$ {{ row.total_price.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Actions" align="center" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              View Detail
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row, $index)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--  检视更多有关order的信息  -->
    <el-dialog
      title="Detail of Order"
      :visible.sync="dialogFormVisible"
      width="80%"
    >
      <el-form label-position="left" class="detail-of-order">
        <el-form-item label="Order ID">
          <span>{{ temp.order_id }}</span>
        </el-form-item>
        <el-form-item label="Customer">
          <span>{{ temp.customer_name }}</span>
        </el-form-item>
        <el-form-item label="Order Created Time">
          <span>{{ temp.create_time }}</span>
        </el-form-item>
        <el-form-item label="Total Price">
          <span>$ {{ temp.total_price }}</span>
        </el-form-item>
        <el-form-item :label="`Order Items (${temp.items.length})`">
          <el-table
            :data="temp.items"
            height="250"
            highlight-current-row
          >
            <el-table-column label="Item ID" prop="item_id" align="center">
              <template slot-scope="{row}">
                <span>{{ row.item_id }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Item Name" prop="item_name" align="center">
              <template slot-scope="{row}">
                <span>{{ row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Count" prop="item_count" align="center">
              <template slot-scope="{row}">
                <span>{{ row.item_count }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Subtotal" prop="subtotal" align="center">
              <template slot-scope="{row}">
                <span>{{ row.t_price }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Category" prop="cat" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.category | catFilter">
                  {{ catMapList[row.category] }}
                </el-tag>
              </template>
            </el-table-column>

          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          Done
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import { MessageBox } from 'element-ui'
import { fetchDetailedOrder, fetchOrders, removeOrder } from '@/api/order'

export default {
  name: 'OrderList',
  directives: { waves },
  filters: {
    catFilter(status) {
      const statusMap = {
        'DR': 'success',
        'SN': 'info',
        'SF': 'danger'
      }
      return statusMap[status]
    },
    timeFilter(timestamp) {
      return parseTime(new Date(timestamp), '{d}/{m}/{y} {h}:{i}:{s}')
    }
  },
  data() {
    return {
      catMapList: { 'DR': 'Drinks', 'SN': 'Snacks', 'SF': 'Staple Foods' },
      basicAvatarUrl: require('@/views/resources/basic-avatar.png'),
      imageCropperShow: false,
      imageCropperKey: 0,
      updatingItemId: '',
      tabItem: 'basic-info',
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        customer_name: undefined,
        email: undefined,
        phone: undefined
      },
      temp: {
        order_id: '',
        customer_name: '',
        create_time: 0,
        total_price: 0.0,
        items: []
      },
      dialogFormVisible: false,
      dialogStatus: '',
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取订单列表
    getList() {
      this.listLoading = true
      fetchOrders(this.listQuery).then(response => {
        const { data, size } = response
        this.list = data
        this.total = size
        this.listLoading = false
      })
    },
    // 查看细节
    handleViewDetail(row) {
      this.temp = Object.assign({}, row) // copy obj to temp
      this.getDetailedData(row)
    },
    // 获取详细的订单资讯
    getDetailedData(row) {
      fetchDetailedOrder(row.order_id).then(response => {
        this.$set(this.temp, 'order_id', row.order_id)
        this.$set(this.temp, 'customer_name', row.customer_name)
        this.$set(this.temp, 'create_time', row.create_time)
        this.$set(this.temp, 'total_price', row.total_price)
        this.$set(this.temp, 'items', response.data)
        this.dialogFormVisible = true
        this.listLoading = false
      })
    },
    handleFilter() {
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    resetTemp() {
      this.temp = {
        order_id: '',
        customer_name: '',
        create_time: 0,
        total_price: 0.0,
        items: []
      }
    },
    // 消去订单
    handleDelete(row, index) {
      MessageBox.confirm('Are you sure to delete order whose id is \"' + row.order_id + '\"?', 'Delete Order', {
        confirmButtonText: 'Delete',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        // 删除订单
        removeOrder(row.order_id).then(() => {
          this.$notify({
            title: 'Success',
            message: 'Removed item \"' + row.name + '\".',
            type: 'success',
            duration: 2000
          })
          this.list.splice(index, 1)
        })
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Order ID', 'Customer Name', 'Create time', 'Total Price']
        const filterVal = ['order_id', 'customer_name', 'create_time', 'total_price']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'Jakarta Orders - at ' + parseTime(new Date(), '{d}-{m}-{y} {h}:{i}')
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        return v[j]
      }))
    }
  }
}
</script>

<style>
  .detail-of-order {
    font-size: 0;
  }
  .detail-of-order label {
    color: #99a9bf;
  }
  .detail-of-order .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
  }
</style>
