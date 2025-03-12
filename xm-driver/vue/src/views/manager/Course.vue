<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入名称关键字查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation" v-if="user.role === 'ADMIN'">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" v-if="user.role === 'ADMIN'"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="descr" label="课程介绍" show-overflow-tooltip></el-table-column>
        <el-table-column prop="type" label="课程类型"></el-table-column>
        <el-table-column prop="during" label="课程时长(天)"></el-table-column>
        <el-table-column prop="time" label="上课时间"></el-table-column>
        <el-table-column prop="location" label="上课地点"></el-table-column>
        <el-table-column prop="coachId" label="教练ID"></el-table-column>
        <el-table-column prop="max" label="最大人数"></el-table-column>
        <el-table-column prop="status" label="课程状态">
          <template v-slot="scope">
            <el-tag type="success" v-if="scope.row.status === '可用'">可用</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '已约满'">已约满</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="listing" label="上架状态">
          <template v-slot="scope">
            <el-tag type="primary" v-if="scope.row.listing === '上架'">上架</el-tag>
            <el-tag type="info" v-if="scope.row.listing === '下架'">下架</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180" v-if="user.role === 'ADMIN'">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
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

    <el-dialog title="课程信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="form.name" placeholder="课程名称"></el-input>
        </el-form-item>
        <el-form-item label="课程介绍" prop="descr">
          <el-input type="textarea" v-model="form.descr" placeholder="课程介绍"></el-input>
        </el-form-item>
        <el-form-item label="课程类型" prop="type">
          <el-select v-model="form.type" style="width: 100%">
            <el-option v-for="(item, index) in ['理论课程', '实操课程']" :key="index" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程时长(天)" prop="during">
          <el-input v-model="form.during" placeholder="课程时长"></el-input>
        </el-form-item>
        <el-form-item label="上课时间" prop="time">
          <el-select v-model="form.time" style="width: 100%">
            <el-option v-for="item in ['9:00-11:00', '14:00-16:00', '18:00-20:00']" :key="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上课地点" prop="location">
          <el-input v-model="form.location" placeholder="上课地点"></el-input>
        </el-form-item>
        <el-form-item label="教练" prop="coachId">
          <el-select style="width: 100%" v-model="form.coachId">
            <el-option v-for="item in coachList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="最大人数" prop="max">
          <el-input-number :min="1" :max="50" v-model="form.max" placeholder="最大人数"></el-input-number>
        </el-form-item>
        <el-form-item label="上架状态" prop="listing">
          <el-select v-model="form.listing" style="width: 100%">
            <el-option v-for="item in ['上架', '下架']" :key="item" :value="item"></el-option>
          </el-select>
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
  name: "Course",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: [],
      coachList: []
    }
  },
  created() {
    this.load(1)
    this.loadCoach()
  },
  methods: {
    loadCoach() {
      this.$request.get('/coach/selectAll').then(res => {
        this.coachList = res.data || []
      })
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
            url: this.form.id ? '/course/update' : '/course/add',
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
        this.$request.delete('/course/delete/' + id).then(res => {
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
        this.$request.delete('/course/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/course/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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
      this.name = null
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