<template>
  <div id="app-container">
    <header class="app-header">
      <h1>My Job Dashboard</h1>
      <nav v-if="authStore.isAuthenticated">
        <RouterLink to="/">대시보드</RouterLink>
        <button @click="handleLogout" class="logout-button">로그아웃</button>
      </nav>
    </header>

    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();

const handleLogout = () => {
  authStore.logout();
};
</script>

<style>
/* 전역 */
body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  margin: 0;
  background-color: #f4f7f6;
  color: #333;
}

#app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 헤더 스타일 */
.app-header {
  background-color: #ffffff;
  padding: 1rem 2rem;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.app-header h1 {
  margin: 0;
  color: #35495e;
}
.app-header nav {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.app-header nav a {
  text-decoration: none;
  color: #42b883;
  font-weight: bold;
}
.logout-button {
  background: none;
  border: 1px solid #ff6b6b;
  color: #ff6b6b;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

/* 메인 콘텐츠 */
.main-content {
  flex-grow: 1;
  padding: 2rem;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

/* 인증 폼 */
.auth-container {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}
.auth-container h2 {
  text-align: center;
  margin-top: 0;
}
.form-group {
  margin-bottom: 1.5rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}
.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
.auth-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
}
.auth-button:hover {
  background-color: #35495e;
}
.switch-auth {
  margin-top: 1.5rem;
  text-align: center;
  font-size: 0.9rem;
}
</style>