// src/api/axios.ts
import axios, { type InternalAxiosRequestConfig } from 'axios';
import { useAuthStore } from '@/stores/authStore';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // 백엔드 서버 주소
    headers: {
        'Content-Type': 'application/json',
    }
});

// Axios 요청 인터셉터
apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        
        const authStore = useAuthStore();
        const token = authStore.token;
        
        if (token && config.headers) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default apiClient;