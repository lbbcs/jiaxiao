import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
      { path: 'user', name: 'User', meta: { name: '用户信息' }, component: () => import('../views/manager/User') },
      { path: 'userPerson', name: 'UserPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/UserPerson') },
      { path: 'coach', name: 'Coach', meta: { name: '教练信息' }, component: () => import('../views/manager/Coach') },
      { path: 'coachPerson', name: 'CoachPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/CoachPerson') },
      { path: 'course', name: 'Course', meta: { name: '课程信息' }, component: () => import('../views/manager/Course') },
      { path: 'userCourse', name: 'UserCourse', meta: { name: '课程预约' }, component: () => import('../views/manager/UserCourse') },
      { path: 'reserve', name: 'Reserve', meta: { name: '课程预约审核' }, component: () => import('../views/manager/Reserve') },
      { path: 'calendar', name: 'Calendar', meta: { name: '学车日历' }, component: () => import('../views/manager/Calendar') },
      { path: 'car', name: 'Car', meta: { name: '车辆管理' }, component: () => import('../views/manager/Car') },
      { path: 'userCar', name: 'UserCar', meta: { name: '车辆预约' }, component: () => import('../views/manager/UserCar') },
      { path: 'carReserve', name: 'CarReserve', meta: { name: '车辆预约审核' }, component: () => import('../views/manager/CarReserve') },
      { path: 'message', name: 'Message', meta: { name: '消息通知' }, component: () => import('../views/manager/Message') },
      { path: 'dashboard', name: 'Dashboard', meta: { name: '统计图表' }, component: () => import('../views/manager/Dashboard') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
