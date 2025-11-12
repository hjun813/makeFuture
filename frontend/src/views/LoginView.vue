<template>
  <div class="auth-container">
    <h2>로그인</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="email">이메일</label>
        <input id="email" v-model="email" type="email" required />
      </div>
      <div class="form-group">
        <label for="password">비밀번호</label>
        <input id="password" v-model="password" type="password" required />
      </div>
      <button type="submit" class="auth-button">로그인</button>
      <p class="switch-auth">
        계정이 없으신가요? <RouterLink to="/signup">회원가입</RouterLink>
      </p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { RouterLink } from 'vue-router';

const email = ref('');
const password = ref('');
const authStore = useAuthStore();

const handleLogin = () => {
  authStore.login({
    email: email.value,
    password: password.value
  });
};
</script>

<style scoped>
/* (스타일은 7번 App.vue의 <style> 블록을 참고하세요) */
</style>