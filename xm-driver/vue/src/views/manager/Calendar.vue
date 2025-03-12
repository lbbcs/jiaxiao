<template>
  <div>
    <el-calendar>
      <template
          slot="dateCell"
          slot-scope="{date, data}">
        <div style="margin-bottom: 10px">{{ data.day.split('-').slice(1).join('-') }}</div>
        <div v-for="item in reserveList" :key="item.id" v-if="item.start === data.day" style="color: #666; margin-bottom: 20px">
          <div style="color: #409EFF; margin-bottom: 5px">课程名称：{{ item.courseName }}</div>
          <div style="color: #0ac46f; margin-bottom: 5px">上课时间：{{ item.courseTime }}</div>
          <div style="color: #ffa640; margin-bottom: 5px">上课地点：{{ item.courseLocation }}</div>
          <div style="color: #0d4d7a; margin-bottom: 5px">教练：{{ item.coachName }}</div>
        </div>
      </template>
    </el-calendar>
  </div>
</template>

<script>
export default {
  name: "Calendar",
  data() {
    return {
      reserveList: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.$request.get('/reserve/selectUser').then(res => {
        this.reserveList = res.data || []
      })
    }
  }
}
</script>

<style scoped>
/deep/.el-calendar-day {
  height: auto;  /* 必须设置自动高度*/
  min-height: 85px;
}
</style>