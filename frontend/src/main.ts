// src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/authStore'

const app = createApp(App)

const pinia = createPinia()
app.use(pinia)


const authStore = useAuthStore(pinia);
authStore.loadToken(); 

app.use(router)

app.mount('#app')