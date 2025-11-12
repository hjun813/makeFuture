<template>
  <div class="auth-container">
    <h2>회원가입</h2>
    <form @submit.prevent="handleSignup">
      <div class="form-group">
        <label for="email">이메일</label>
        <input id="email" v-model="email" type="email" required />
      </div>
      <div class="form-group">
        <label for="username">사용자 이름</label>
        <input id="username" v-model="username" type="text" required />
      </div>
      <div class="form-group">
        <label for="password">비밀번호</label>
        <input id="password" v-model="password" type="password" required />
      </div>
      <button type="submit" class="auth-button">회원가입</button>
      <p class="switch-auth">
        이미 계정이 있으신가요? <RouterLink to="/login">로그인</RouterLink>
      </p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { RouterLink } from 'vue-router';

const email = ref('');
const username = ref('');
const password = ref('');
const authStore = useAuthStore();

const handleSignup = () => {
  authStore.signup({
    email: email.value,
    username: username.value,
    password: password.value
  });
};
</script>

<style scoped>
/* (스타일은 7번 App.vue의 <style> 블록을 참고하세요) */
</style>