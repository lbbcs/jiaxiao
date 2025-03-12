<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="12" v-for="item in tableData" :key="item.id">
        <div class="card" style="margin-bottom: 10px">
          <div style="margin-bottom: 10px">
            <span style="font-size: 20px;">{{ item.name }}</span>
            <span style="position: relative; top: -2px; left: 3px">
              <el-tag type="success" v-if="item.type === '理论课程'">理论课程</el-tag>
              <el-tag type="danger" v-if="item.type === '实操课程'">实操课程</el-tag>
            </span>
          </div>
          <div style="color: #666; margin-bottom: 10px">{{ item.descr }}</div>
          <div style="color: #666; margin-bottom: 10px">
            <span style="margin-right: 20px">上课时间：{{ item.time }}</span>
            <span style="margin-right: 20px">上课时长：{{ item.during }}天</span>
            <span style="margin-right: 20px">上课地点：{{ item.location }}</span>
          </div>
          <div style="color: #666; margin-bottom: 10px">
            <span style="margin-right: 20px">教练员：{{ item.coachName }}</span>
            <span>当前状态：<strong :style="{ 'color': item.status === '可用' ? '#29a93e' : 'red' }">{{ item.status }}</strong></span>
          </div>
          <div>
            <el-button type="primary" :disabled="item.userReserve || item.status === '已约满'" @click="handleReserve(item.id)">预约</el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog title="课程预约" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" ref="formRef">
        <el-form-item label="备注" prop="comment">
          <el-input type="textarea" v-model="form.comment" placeholder="备注"></el-input>
        </el-form-item>
        <el-form-item label="开始日期" prop="start">
          <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="开始日期"
                          v-model="form.start" style="width: 100%"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="addReserve">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "UserCourse",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      fromVisible: false,
      form: {}
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    addReserve() {
      this.$request.post('/reserve/add', this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('预约成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    handleReserve(courseId) {
      this.fromVisible = true
      this.form = {courseId: courseId}
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/course/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          listing: '上架'
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
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>