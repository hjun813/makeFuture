import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import JobsView from '../views/JobsView.vue'; 
import TodosView from '../views/TodosView.vue';
import AllJobsView from '@/views/AllJobsView.vue';
import { useAuthStore } from '@/stores/authStore'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true }
  },
  // ✅ 2. JobsView 라우트 추가 (인증 필요)
  {
    path: '/jobs',
    name: 'jobs',
    component: JobsView,
    meta: { requiresAuth: true }
  },
  {
    path: '/all-jobs',
    name: 'all-jobs',
    component: AllJobsView,
    meta: { requiresAuth: true } // (로그인한 사용자만 보도록 설정)
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/signup',
    name: 'signup',
    component: SignupView
  },
  {
    path: '/todos',
    name: 'todos',
    component: TodosView,
    meta: { requiresAuth: true }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  
  if (!authStore.token) {
    authStore.loadToken();
  }

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'login' });
  } else {
    next();
  }
});

export default router