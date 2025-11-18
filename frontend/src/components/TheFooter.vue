<template>
  <footer class="app-footer">
    <div class="footer-content">
      <p class="copyright">
        © {{ new Date().getFullYear() }} My Job Dashboard. All rights reserved.
      </p>
      <div class="creator-link">
        Created by 
        <RouterLink to="/about"><strong>HyunJun Kim</strong></RouterLink>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router';
</script>

<style scoped>
.app-footer {
  margin-top: auto; /* 내용을 밀어내고 항상 바닥에 위치 */
  background-color: #fff;
  border-top: 1px solid #e0e0e0;
  padding: 1.5rem 0;
  width: 100%;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: #888;
  font-size: 0.9rem;
}

.copyright {
  margin: 0;
}

.creator-link a {
  color: #42b883;
  text-decoration: none;
  margin-left: 0.3rem;
}

.creator-link a:hover {
  text-decoration: underline;
}
</style>