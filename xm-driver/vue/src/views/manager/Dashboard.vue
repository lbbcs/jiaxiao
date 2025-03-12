<template>
  <div>
    <div style="display: flex; grid-gap: 15px; margin-bottom: 10px">
      <div class="card item">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/预约.png" alt="" style="width: 60px; height: 60px">
        </div>
        <div style="flex: 1; font-size: 20px">
          <div style="margin-bottom: 5px">课程预约</div>
          <div style="font-weight: bold">{{ count.courseReserveCount }}</div>
        </div>
      </div>
      <div class="card item">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/车预约.png" alt="" style="width: 60px; height: 60px">
        </div>
        <div style="flex: 1; font-size: 20px">
          <div style="margin-bottom: 5px">车辆预约</div>
          <div style="font-weight: bold">{{ count.carReserveCount }}</div>
        </div>
      </div>
      <div class="card item">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/车辆.png" alt="" style="width: 60px; height: 60px">
        </div>
        <div style="flex: 1; font-size: 20px">
          <div style="margin-bottom: 5px">车辆总数</div>
          <div style="font-weight: bold">{{ count.carCount }}</div>
        </div>
      </div>
      <div class="card item">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/学员.png" alt="" style="width: 60px; height: 60px">
        </div>
        <div style="flex: 1; font-size: 20px">
          <div style="margin-bottom: 5px">学员总数</div>
          <div style="font-weight: bold">{{ count.userCount }}</div>
        </div>
      </div>
    </div>

    <div style="display: flex; grid-gap: 15px">
      <div id="pie" style="flex: 1; height: 500px" class="card"></div>
      <div id="bar" style="flex: 1; height: 500px" class="card"></div>
    </div>

  </div>
</template>

<script>
import * as echarts from 'echarts'
const pieOption = {
  title: {
    text: '课程预约统计图',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      center: ['50%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

const barOption = {
  title: {
    text: '车辆预约统计柱状图',
    subtext: '柱状图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
      smooth: true,
      itemStyle: {
        normal: {
          color: function(params) { // 根据索引返回对应的颜色
            let colorList = ['#ffaa2e','#32C5E9','#fa4c4c','#08b448','#FFDB5C','#ff9f7f','#fb7293','#E062AE','#E690D1','#e7bcf3']
            return colorList[params.dataIndex];
          }
        }
      },
    }
  ]
}
export default {
  name: "Dashboard",
  data() {
    return {
      count: {}
    }
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      this.$request.get('/count').then(res => {
        this.count = res.data || {}
      })
      // 柱状图
      let barDom = document.getElementById('bar');
      let barChart = echarts.init(barDom);

      // 饼图
      let pieDom = document.getElementById('pie');
      let pieChart = echarts.init(pieDom);


      this.$request.get('/barData').then(res => {
        barOption.xAxis.data = res.data?.map(v => v.name) || []
        barOption.series[0].data = res.data?.map(v => v.value) || []
        barChart.setOption(barOption)
      })

      this.$request.get('/pieData').then(res => {
        pieOption.series[0].data = res.data || []
        pieChart.setOption(pieOption)
      })
    }
  }
}
</script>

<style scoped>
.item {
  flex: 1;
  display: flex;
  align-items: center
}
</style>