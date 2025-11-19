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
import { RouterLink, useRouter } from 'vue-router'; 
import { useToast } from "vue-toastification";

const email = ref('');
const password = ref('');
const authStore = useAuthStore();
const router = useRouter(); 
const toast = useToast();


const handleLogin = async () => {
  const success = await authStore.login({
    email: email.value,
    password: password.value
  });


  if (success) {
    toast.success('환영합니다! 로그인 되었습니다.');
    router.push('/');
  } else {
    toast.error('이메일 또는 비밀번호를 확인하세요.');
  }
};
</script>

<style scoped>
/* ... */
</style>