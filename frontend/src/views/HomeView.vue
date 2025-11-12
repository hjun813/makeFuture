<template>
  <div class="home-container">
    <div v-if="authStore.isAuthenticated">
      <h2>대시보드</h2>
      <p>My Job Dashboard에 오신 것을 환영합니다.</p>
      <p>
        이곳에서 당신의 채용 공고, 할 일, 그리고 학습 기록을 모두 관리해보세요.
      </p>
      </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();

</script>

<style scoped>
.home-container {
  width: 100%;
  max-width: 800px;
  padding: 2rem;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  text-align: center;
}

h2 {
  color: #35495e;
}

p {
  color: #555;
  line-height: 1.6;
}

.user-email {
  margin-top: 2rem;
  font-size: 0.9rem;
  color: #777;
}
</style>