import { defineStore } from 'pinia';
import { ref } from 'vue';
import apiClient from '@/api/axios';


export interface History {
    id: number;
    content: string;
    category: string | null;
    createdAt: string; 
}


export interface HistoryCreate {
    content: string;
    category?: string;
}


export interface HistoryUpdate {
    content: string;
    category?: string;
}
// ---------------------------

export const useHistoryStore = defineStore('history', () => {

    const histories = ref<History[]>([]);
    const isLoading = ref(false);

    
    // 목록 조회
    const fetchHistories = async () => {
        isLoading.value = true;
        try {
            const response = await apiClient.get<History[]>('/api/histories');
            histories.value = response.data;
        } catch (error) {
            console.error('히스토리 목록 로딩 실패:', error);
            histories.value = [];
        } finally {
            isLoading.value = false;
        }
    };

    // 등록

    const createHistory = async (newHistory: HistoryCreate): Promise<boolean> => {
        try {
            const response = await apiClient.post<History>('/api/histories', newHistory);
            histories.value.unshift(response.data); 
            return true;
        } catch (error) {
            console.error('히스토리 등록 실패:', error);
            return false;
        }
    };

    // 삭제
    const deleteHistory = async (historyId: number) => {
        const index = histories.value.findIndex(h => h.id === historyId);
        if (index === -1) return;
        
        const deletedHistoryArray = histories.value.splice(index, 1);

        try {
            await apiClient.delete(`/api/histories/${historyId}`);
        } catch (error) {
            console.error('히스토리 삭제 실패:', error);
            if (deletedHistoryArray && deletedHistoryArray[0]) {
                histories.value.splice(index, 0, deletedHistoryArray[0]);
            }
            alert('삭제에 실패했습니다.');
        }
    };
    
    // 수정
    const updateHistory = async (historyId: number, updatedData: HistoryUpdate) => {
        try {
            const response = await apiClient.put<History>(`/api/histories/${historyId}`, updatedData);
            const index = histories.value.findIndex(h => h.id === historyId);
            if (index !== -1) {
                histories.value[index] = response.data;
            }
        } catch (error) {
            console.error('히스토리 수정 실패:', error);
            alert('수정에 실패했습니다.');
        }
    };

    return {
        histories,
        isLoading,
        fetchHistories,
        createHistory,
        deleteHistory,
        updateHistory
    };
});