import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect: '/login'},
    {path: '/manager', component: () => import('../views/Mananger.vue'), children: [
      {path: 'home', meta:{title: '主頁面'}, component: () => import('../views/Home.vue')},
      {path: 'test', meta:{title: '測試頁面'}, component: () => import('../views/Test.vue')},
      {path: 'data', meta:{title: '數據展示頁面'}, component: () => import('../views/Data.vue')},
      {path: 'employee', meta:{title: '員工信息頁面'}, component: () => import('../views/Employee.vue')},
      {path: 'admin', meta:{title: '管理員信息頁面'}, component: () => import('../views/Admin.vue')},
      {path: 'person', meta:{title: '個人信息頁面'}, component: () => import('../views/Person.vue')},
      {path: 'password', meta:{title: '修改密碼頁面'}, component: () => import('../views/Password.vue')},
    ]},
    {path: '/login',  meta:{title: '登錄頁面'}, component: () => import('../views/Login.vue')},
    {path: '/register', meta:{title: '註冊頁面'}, component: () => import('../views/Register.vue')},
    {path: '/404', meta:{title: '頁面未找到'}, component: () => import('../views/404.vue')},
    {path: '/:pathMatch(.*)', redirect: '/404' }
    
  ],
})

router.beforeEach((to, from, next) => {
  // 在路由切換前執行的邏輯
  document.title = to.meta.title || 'Default Title'; // 設置頁面標題
  console.log('正在導航到:', to.path);
  next(); // 繼續導航
});

export default router
