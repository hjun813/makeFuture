
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api/axios';

// DTO 타입
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
    // 1. State
    const token = ref<string | null>(null);
    const user = ref<object | null>(null);
    const router = useRouter();

    // 2. Getters (Computed)
    const isAuthenticated = computed<boolean>(() => !!token.value);

    // 3. Actions
    
    /**
     * 앱 시작 시 로컬 스토리지에서 토큰 로드
     */
    const loadToken = () => {
        const storedToken = localStorage.getItem('authToken');
        if (storedToken) {
            token.value = storedToken;
        }
    };

    /**
     * 로그인
     */
    const login = async (credentials: LoginCredentials) => {
        try {
            const response = await apiClient.post<JwtResponse>('/api/auth/login', credentials);
            
            token.value = response.data.token;
            localStorage.setItem('authToken', token.value);
            
            alert('로그인 성공!');
            router.push('/'); // 대시보드로 이동
        } catch (error) {
            console.error('로그인 실패:', error);
            alert('이메일 또는 비밀번호를 확인하세요.');
        }
    };

    /**
     * 회원가입
     */
    const signup = async (userInfo: SignupUserInfo) => {
        try {
            await apiClient.post('/api/auth/signup', userInfo);
            alert('회원가입 성공! 로그인 페이지로 이동합니다.');
            router.push('/login');
        } catch (error) {
            console.error('회원가입 실패:', error);
            alert('회원가입에 실패했습니다. (이메일 중복 등)');
        }
    };

    /**
     * 로그아웃
     */
    const logout = () => {
        token.value = null;
        user.value = null;
        localStorage.removeItem('authToken');
        router.push('/login');
        alert('로그아웃 되었습니다.');
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