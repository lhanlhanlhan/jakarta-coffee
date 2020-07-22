<template>
  <div class="app-container">
    <div style="margin-bottom: 20px;">
      <div class="filter-container" style="margin-right: 10px;">
        <el-input v-model="listQuery.name" placeholder="Name" style="width: 140px; margin-right: 10px" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.email" placeholder="Email" style="width: 140px; margin-right: 10px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.phone" placeholder="Telephone" style="width: 140px; margin-right: 10px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-select v-model="listQuery.sort" style="width: 200px; margin-right: 10px;" class="filter-item" @change="handleFilter">
          <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
        </el-select>
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          Search
        </el-button>
        <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">
          Add Admin
        </el-button>
        <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
          Export Table
        </el-button>
      </div>
    </div>
    <div>
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
        <el-table-column label="Name" prop="name" sortable="custom" align="center" :class-name="getSortClass('name')">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Email" prop="email" sortable="custom" align="center" :class-name="getSortClass('email')">
          <template slot-scope="{row}">
            <span>{{ row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Telephone" prop="phone" align="center">
          <template slot-scope="{row}">
            <span>{{ row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column label="User Class" class-name="status-col" align="center">
          <template>
            <el-tag>
              Administrator
            </el-tag>
          </template>
        </el-table-column>
        <!-- 我认为管理员不应互相伤害！ -->
        <!--        <el-table-column label="Actions" align="center" class-name="small-padding fixed-width">-->
        <!--          <template slot-scope="{row,$index}">-->
        <!--            <el-button type="primary" size="mini" @click="handleUpdate(row)">-->
        <!--              Edit-->
        <!--            </el-button>-->
        <!--            <el-button size="mini" type="danger" @click="handleDelete(row, $index)">-->
        <!--              Delete-->
        <!--            </el-button>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
      </el-table>
    </div>
    <!--    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" style="margin-left:50px; margin-right:50px;">
        <el-form-item label="Administrator Username" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='create'" label="Password" prop="password">
          <!-- 只有create模式才准创建密码！ -->
          <el-input v-model="temp.password" type="password" />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='create'" label="Your Password" prop="password">
          <!-- 要create一个管理员，必须键入当前管理员密码！ -->
          <el-input v-model="temp.currPassword" type="password" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="temp.phone" />
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
  </div>
</template>

<script>
import { fetchAdminList, createAdmin } from '@/api/list-user'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import { mapGetters } from 'vuex'

export default {
  name: 'AdminList',
  directives: { waves },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (!value || value.length < 5) {
        callback(new Error('the password can not be less than 5 characters'))
      } else {
        callback()
      }
    }
    const validateTel = (rule, value, callback) => {
      const temp = /^(\+[0-9]{1,3})?[0-9*#]+$/
      if (!value || (!(temp).test(value))) {
        callback(new Error('not a valid telephone number'))
      } else {
        callback()
      }
    }
    return {
      currAdminUser: '',
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        name: undefined,
        email: undefined,
        phone: undefined,
        sort: 'aname'
      },
      sortOptions: [
        { label: 'Name Ascending', key: 'aname' },
        { label: 'Name Descending', key: 'dname' },
        { label: 'Email Ascending', key: 'aemail' },
        { label: 'Email Descending', key: 'demail' },
        { label: 'Phone Ascending', key: 'aphone' },
        { label: 'Phone Descending', key: 'dphone' }
      ],
      temp: {
        name: '',
        email: '',
        phone: '',
        password: undefined,
        currPassword: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: { // 两个功能：edit及create的对话框标题
        update: 'Edit',
        create: 'Create Administrator User'
      },
      rules: { // 这是对话框的内容的筛选条件
        name: [{ required: true, message: 'username is required', trigger: 'blur' }],
        password: [{ required: true, validator: validatePassword, trigger: 'blur' }],
        email: [{ type: 'email', required: true, trigger: 'blur' }],
        phone: [{ required: true, validator: validateTel, trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  created() {
    this.getList()
    this.currAdminUser = this.name
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchAdminList(this.listQuery).then(response => {
        const { data, size } = response
        this.list = data
        this.total = size

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
    sortChange(data) {
      const { prop, order } = data
      this.sortBy(order, prop)
    },
    sortBy(order, prop) {
      if (order === 'ascending') {
        this.listQuery.sort = `a${prop}`
      } else {
        this.listQuery.sort = `a${prop}`
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        name: '',
        email: '',
        phone: '',
        password: undefined
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
          createAdmin({
            username: this.temp.name,
            password: this.temp.password,
            email: this.temp.email,
            telephone: this.temp.phone,
            admin: true,
            operator: this.currAdminUser,
            key: this.temp.currPassword
          }).then(() => {
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
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Administrator Name', 'Email', 'Phone']
        const filterVal = ['name', 'email', 'phone']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'Jakarta Administrator - at ' + parseTime(new Date(), '{d}-{m}-{y} {h}:{i}')
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        return v[j]
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `a${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
