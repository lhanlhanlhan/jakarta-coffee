<template>
  <div class="app-container">
    <div style="margin-bottom:20px;">
      <div class="filter-container" style="margin-right: 10px;">
        <el-input v-model="listQuery.name" placeholder="Name" style="width: 140px; margin-right: 10px;" class="filter-item" @keyup.enter.native="handleFilter" />
        <el-select v-model="listQuery.cat" style="width: 200px; margin-right: 10px;" class="filter-item" @change="handleFilter">
          <el-option v-for="item in catMap" :key="item.key" :label="item.label" :value="item.key" />
        </el-select>
        <el-select v-model="listQuery.sort" style="width: 200px; margin-right: 10px;" class="filter-item" @change="handleFilter">
          <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
        </el-select>
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          Search
        </el-button>
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
          Add Item
        </el-button>
        <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
          Export Menu
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
        <el-table-column label="ID" prop="item_id" sortable="custom" align="center" :class-name="getSortClass('item_id')">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.item_id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Name" prop="name" sortable="custom" align="center" :class-name="getSortClass('name')">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleUpdate(row)">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Category" prop="category" sortable="custom" align="center" :class-name="getSortClass('category')">
          <template slot-scope="{row}" @click="handleUpdate(row)">
            <el-tag :type="row.category | catFilter">
              {{ catMapList[row.category] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="In-Stock" prop="in_stock" sortable="custom" align="center" :class-name="getSortClass('in_stock')">
          <template slot-scope="{row}" @click="handleUpdate(row)">
            <span>{{ row.in_stock }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Recently Sold" prop="recent_sold" sortable="custom" align="center" :class-name="getSortClass('recent_sold')">
          <template slot-scope="{row}" @click="handleUpdate(row)">
            <span>{{ row.recent_sold }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Price" prop="price" sortable="custom" align="center" :class-name="getSortClass('price')">
          <template slot-scope="{row}" @click="handleUpdate(row)">
            <span>$ {{ row.price.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Actions" align="center" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleUpdate(row)">
              Edit
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row, $index)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />-->
    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="140px" style="margin-left:50px; margin-right:50px;">
        <el-tabs v-model="tabItem">
          <el-tab-pane label="Basic Info" name="basic-info">
            <el-form-item v-if="dialogStatus === 'update'" label="Item ID">
              <el-input v-model="temp.item_id" :disabled="true" />
            </el-form-item>
            <el-form-item label="Item Name" prop="name">
              <el-input v-model="temp.name" />
            </el-form-item>
            <el-form-item label="Price" prop="price">
              <el-input v-model="temp.price" />
            </el-form-item>
            <el-form-item label="Category" prop="category">
              <el-select v-model="temp.category">
                <el-option v-for="item in catMap" :key="item.key" :label="item.label" :value="item.key" />
              </el-select>
            </el-form-item>
            <el-form-item label="In-Stock Amount" prop="in_stock">
              <el-input-number v-model="temp.in_stock" />
            </el-form-item>
            <el-form-item label="Recent Sold" prop="recent_sold">
              <el-input-number v-model="temp.recent_sold" />
            </el-form-item>
            <el-form-item label="Stars">
              <el-rate v-model="temp.stars" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="5" style="margin-top:8px;" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="Appendix" name="other-info">
            <el-form-item label="Description">
              <el-input v-model="temp.description" type="textarea" />
            </el-form-item>
            <el-form-item label="Item Picture">
              <pan-thumb :image="(!temp.photo_uri || temp.photo_uri === 'null') ? basicAvatarUrl : temp.photo_uri" />
              <el-button type="primary" icon="el-icon-upload" @click="imageCropperShow = true">
                Change Item Picture
              </el-button>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
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
    <!--记得把url改过来！-->
    <image-cropper
      v-show="imageCropperShow"
      :key="imageCropperKey"
      :width="300"
      :height="300"
      url="/files-api/upload"
      lang-type="en"
      @close="closeImageCropper"
      @crop-upload-success="cropSuccess"
    />
  </div>
</template>

<script>
import PanThumb from '@/components/PanThumb'
import ImageCropper from '@/components/ImageCropper'
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import { fetchMenu, fetchMenuItem, createMenuItem, updateMenuItem, deleteMenuItem } from '@/api/menu'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import { MessageBox } from 'element-ui'

export default {
  name: 'MenuList',
  directives: { waves, elDragDialog },
  components: { PanThumb, ImageCropper },
  filters: {
    catFilter(status) {
      const statusMap = {
        'DR': 'success',
        'SN': 'info',
        'SF': 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
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
        name: undefined,
        cat: undefined,
        sort: 'aname'
      },
      sortOptions: [
        { label: 'Name Ascending', key: 'aname' },
        { label: 'Name Descending', key: 'dname' },
        { label: 'ID Ascending', key: 'aitem_id' },
        { label: 'ID Descending', key: 'ditem_id' },
        { label: 'Category', key: 'acategory' },
        { label: 'In-Stock Ascending', key: 'ain_stock' },
        { label: 'In-Stock Descending', key: 'din_stock' },
        { label: 'Recent-Sold Ascending', key: 'arecent_sold' },
        { label: 'Recent-Sold Descending', key: 'drecent_sold' },
        { label: 'Price Descending', key: 'aprice' },
        { label: 'Price Descending', key: 'dprice' }
      ],
      catMap: [
        { label: 'Drinks', key: 'DR' },
        { label: 'Snacks', key: 'SN' },
        { label: 'Staple Foods', key: 'SF' }
      ],
      catMapList: { 'DR': 'Drinks', 'SN': 'Snacks', 'SF': 'Staple Foods' },
      temp: {
        item_id: '',
        name: '',
        category: '',
        in_stock: 0,
        stars: 3,
        price: 0.0,
        recent_sold: 0,
        description: '',
        photo_uri: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: { // 两个功能：edit及create的对话框标题
        update: 'Edit',
        create: 'Create New Menu Item'
      },
      rules: { // 这是对话框的内容的筛选条件
        name: [{ required: true, message: 'item name is required', trigger: 'blur' }],
        category: [{ required: true, message: 'item name is required', trigger: 'blur' }],
        in_stock: [{ type: 'number', required: true, message: 'in-stock amount is required', trigger: 'blur', min: 0 }],
        recent_sold: [{ type: 'number', required: true, message: 'recent sold amount is required', trigger: 'blur', min: 0 }],
        price: [{ required: true, message: 'price is required', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    cropSuccess(resData) {
      this.imageCropperShow = false
      this.imageCropperKey = this.imageCropperKey + 1
      this.$set(this.temp, 'photo_uri', resData.data.uri)
      // 提示
      this.$notify({
        title: 'Success',
        message: 'Image Updated Successfully',
        type: 'success',
        duration: 2000
      })
    },
    closeImageCropper() {
      this.imageCropperShow = false
    },
    getList() {
      this.listLoading = true
      fetchMenu(this.listQuery).then(response => {
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
          createMenuItem({
            name: this.temp.name,
            category: this.temp.category,
            in_stock: this.temp.in_stock,
            stars: this.temp.stars,
            price: this.temp.price,
            recent_sold: this.temp.recent_sold,
            description: this.temp.description,
            photo_uri: this.temp.photo_uri
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
    getDetailedData(id) {
      fetchMenuItem(id).then(response => {
        const { data } = response
        this.$set(this.temp, 'description', data.description)
        this.$set(this.temp, 'photo_uri', data.photo_uri || this.basicAvatarUrl)
        this.$set(this.temp, 'stars', data.stars)

        this.listLoading = false
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      // 从服务器获取详细资料
      this.getDetailedData(row.item_id)
      this.updatingItemId = row.item_id
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateMenuItem({
            number: this.temp.item_id,
            name: this.temp.name,
            category: this.temp.category,
            in_stock: this.temp.in_stock,
            stars: this.temp.stars,
            price: this.temp.price,
            recent_sold: this.temp.recent_sold,
            description: this.temp.description,
            photo_uri: this.temp.photo_uri
          }).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
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
    handleDelete(row, index) {
      MessageBox.confirm('Are you sure to delete the item \"' + row.name + '\"?', 'Delete User', {
        confirmButtonText: 'Delete',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        deleteMenuItem(row.item_id).then(() => {
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
        const tHeader = ['Client Name', 'Email', 'Phone']
        const filterVal = ['name', 'email', 'phone']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'Jakarta Clients - at ' + parseTime(new Date(), '{d}-{m}-{y} {h}:{i}')
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
