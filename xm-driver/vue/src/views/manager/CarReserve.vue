<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入车辆名称查询" style="width: 200px" v-model="carName"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>


    <div class="operation" v-if="user.role === 'ADMIN'">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" v-if="user.role === 'ADMIN'"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="用户名称"></el-table-column>
        <el-table-column prop="carName" label="车辆名称"></el-table-column>
        <el-table-column prop="time" label="预约时间"></el-table-column>
        <el-table-column prop="useDate" label="使用日期"></el-table-column>
        <el-table-column prop="status" label="预约状态">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
            <el-tag type="info" v-if="scope.row.status === '待审核'">待审核</el-tag>
            <el-tag type="success" v-if="scope.row.status === '通过'">通过</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comment" label="备注"></el-table-column>
        <el-table-column prop="returnStatus" label="归还状态">
          <template v-slot="scope">
            <div style="margin-bottom: 5px">
              <el-tag type="danger" v-if="scope.row.returnStatus === '未归还'">未归还</el-tag>
              <el-tag type="success" v-if="scope.row.returnStatus === '已归还'">已归还</el-tag>
            </div>
            <div v-if="scope.row.returnStatus === '未归还' && user.role === 'USER'">
              <el-button size="mini" type="primary" @click="returnCar(scope.row)">归还</el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="审核" align="center" width="180" v-if="user.role === 'ADMIN'">
          <template v-slot="scope">
            <el-button v-if="scope.row.status === '待审核'" size="mini" type="success" plain @click="changeStatus(scope.row, '通过')">通过</el-button>
            <el-button v-if="scope.row.status === '待审核'" size="mini" type="danger" plain @click="changeStatus(scope.row, '拒绝')">拒绝</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="100">
          <template v-slot="scope">
<!--            <el-button v-if="user.role === 'USER' && scope.row.status === '待审核'" size="mini" type="danger" plain @click="changeStatus(scope.row, '已取消')">取消</el-button>-->
            <el-button v-if="user.role === 'USER'" size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
            <el-button v-if="user.role === 'ADMIN'" size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="车辆预约" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="用户ID"></el-input>
        </el-form-item>
        <el-form-item label="车辆ID" prop="carId">
          <el-input v-model="form.carId" placeholder="车辆ID"></el-input>
        </el-form-item>
        <el-form-item label="预约时间" prop="time">
          <el-input v-model="form.time" placeholder="预约时间"></el-input>
        </el-form-item>
        <el-form-item label="预约状态" prop="status">
          <el-input v-model="form.status" placeholder="预约状态"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="comment">
          <el-input v-model="form.comment" placeholder="备注"></el-input>
        </el-form-item>
        <el-form-item label="归还状态" prop="returnStatus">
          <el-input v-model="form.returnStatus" placeholder="归还状态"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>
<script>
export default {
  carName: "CarReserve",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      carName: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    returnCar(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.form.returnStatus = '已归还'
      this.$confirm('您确定归还吗？', '确认归还', {type: "warning"}).then(response => {
        this.$request.put('/carReserve/update', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('归还成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(e => {})
    },
    changeStatus(row, status) {
      this.form = JSON.parse(JSON.stringify(row))
      this.form.status = status
      this.$confirm('您确定'+status+'吗？', '确认', {type: "warning"}).then(response => {
        this.$request.put('/carReserve/update', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('审核成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(e => {})
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/carReserve/update' : '/carReserve/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
        this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
          this.$request.delete('/carReserve/delete/' + id).then(res => {
            if (res.code === '200') {   // 表示操作成功
              this.$message.success('操作成功')
              this.load(1)
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }).catch(() => {
        })
      },
      handleSelectionChange(rows) {   // 当前选中的所有的行数据
        this.ids = rows.map(v => v.id)
      },
      delBatch() {   // 批量删除
        if (!this.ids.length) {
          this.$message.warning('请选择数据')
          return
        }
        this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
          this.$request.delete('/carReserve/delete/batch', {data: this.ids}).then(res => {
            if (res.code === '200') {   // 表示操作成功
              this.$message.success('操作成功')
              this.load(1)
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }).catch(() => {
        })


    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/carReserve/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          carName: this.carName,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset() {
      this.carName = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>