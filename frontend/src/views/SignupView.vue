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
import { RouterLink, useRouter } from 'vue-router';

const email = ref('');
const username = ref('');
const password = ref('');
const authStore = useAuthStore();
const router = useRouter();


const handleSignup = async () => {
  const success = await authStore.signup({
    email: email.value,
    username: username.value,
    password: password.value
  });

 
  if (success) {
    alert('회원가입 성공! 로그인 페이지로 이동합니다.');
    router.push('/login');
  } else {
    alert('회원가입에 실패했습니다. (이메일 중복 등)');
  }
};
</script>

<style scoped>
/* ... */
</style>