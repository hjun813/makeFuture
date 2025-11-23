// src/api/axios.ts
import axios, { type InternalAxiosRequestConfig } from 'axios';
import { useAuthStore } from '@/stores/authStore';

const apiClient = axios.create({
    baseURL: 'http://54.180.113.166:8080',
    headers: {
        'Content-Type': 'application/json',
    }
});

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