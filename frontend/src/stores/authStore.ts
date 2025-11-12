
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

import apiClient from '@/api/axios';

interface LoginCredentials {
    email: string;
    password: string;
}

interface SignupUserInfo {
    email: string;
    username: string;
    password: string;
}

interface JwtResponse {
    token: string;
}


export const useAuthStore = defineStore('auth', () => {
    const token = ref<string | null>(null);
    const user = ref<object | null>(null);

    const isAuthenticated = computed<boolean>(() => !!token.value);

    const loadToken = () => {
        const storedToken = localStorage.getItem('authToken');
        if (storedToken) {
            token.value = storedToken;
        }
    };

    const login = async (credentials: LoginCredentials): Promise<boolean> => {
        try {
            const response = await apiClient.post<JwtResponse>('/api/auth/login', credentials);
            token.value = response.data.token;
            localStorage.setItem('authToken', token.value);
            return true;
        } catch (error) {
            console.error('로그인 실패:', error);
            return false;
        }
    };


    const signup = async (userInfo: SignupUserInfo): Promise<boolean> => {
        try {
            await apiClient.post('/api/auth/signup', userInfo);
            return true; // 성공
        } catch (error) {
            console.error('회원가입 실패:', error);
            return false; // 실패
        }
    };

    const logout = () => {
        token.value = null;
        user.value = null;
        localStorage.removeItem('authToken');
        window.location.href = '/login';
    };

    return {
        token,
        user,
        isAuthenticated,
        loadToken,
        login,
        signup,
        logout
    };
});