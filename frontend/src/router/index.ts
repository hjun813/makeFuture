// src/router/index.ts
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import { useAuthStore } from '@/stores/authStore'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true } // 이 페이지는 인증이 필요함
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
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// 네비게이션 가드 (라우트 이동 전 체크)
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  
  if (!authStore.token) {
    authStore.loadToken();
  }

  // 1. 인증이 필요한 페이지('requiresAuth: true')에 접근하려 할 때
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    // 2. 인증되지 않았다면 로그인 페이지로 리디렉션
    next({ name: 'login' });
  } else {
    // 3. 그 외의 경우 (인증이 필요 없거나, 이미 인증된 경우)
    next();
  }
});

export default router