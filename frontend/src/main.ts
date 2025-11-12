// src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/authStore'

const app = createApp(App)

const pinia = createPinia()
app.use(pinia)

// 라우터가 네비게이션 가드에서 스토어를 사용하므로
// 라우터보다 먼저 authStore를 활성화해야 합니다.
const authStore = useAuthStore(pinia);
authStore.loadToken(); // 앱 시작 시 로컬 스토리지에서 토큰 로드

app.use(router)

app.mount('#app')