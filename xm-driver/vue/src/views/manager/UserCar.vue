<template>
  <div>
    <div class="search" style="margin-bottom: 10px">
      <el-input placeholder="请输入车辆名称关键字查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <el-row :gutter="10">
      <el-col :span="6" v-for="item in tableData" :key="item.id">
        <div>
          <img :src="item.img" alt="" style="width: 100%; height: 200px; border-radius: 5px">
          <div style="margin: 5px 0;">
            <span style="font-size: 20px; margin-right: 10px">{{ item.name }}</span>
            <strong style="color: #0ac46f" v-if="item.status === '可用'">{{ item.status }}</strong>
            <strong style="color: #0b65ec" v-if="item.status === '使用中'">{{ item.status }}</strong>
            <strong style="color: orange" v-if="item.status === '维修中'">{{ item.status }}</strong>
          </div>
          <div style="margin-bottom: 20px; color: #666; display: flex; align-items: center">
            <div style="flex: 1">{{ item.brand }} · {{ item.no }} · {{ item.color }}· {{ item.seats }}座</div>
            <el-button type="primary" :disabled="item.status !== '可用'" @click="handleReserve(item.id)">预约车辆</el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <div style="margin: 15px 0">
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

    <el-dialog title="车辆预约" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" ref="formRef">
        <el-form-item label="备注" prop="comment">
          <el-input type="textarea" v-model="form.comment" placeholder="备注"></el-input>
        </el-form-item>
        <el-form-item label="使用日期" prop="useDate">
          <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="使用日期"
                          v-model="form.useDate" style="width: 100%"></el-date-picker>
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
  name: "UserCar",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      fromVisible: false,
      form: {},
      name: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleReserve(carId) {
      this.form = { carId: carId }
      this.fromVisible = true
    },
    addReserve() {
      this.$request.post('/carReserve/add', this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('预约成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/car/selectPage', {
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