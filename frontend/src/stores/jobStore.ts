import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import apiClient from '@/api/axios';

export interface Job {
    id: number;
    companyName: string;
    jobTitle: string;
    url: string | null;
    deadline: string | null;
    jobType: string | null;
}


export interface JobCreate {
    companyName: string;
    jobTitle: string;
    url?: string;
    deadline?: string;
    jobType?: string;
}

// ---------------------------

export const useJobStore = defineStore('job', () => {
 
    const jobs = ref<Job[]>([]);
    const allJobs = ref<Job[]>([]);
    const isLoading = ref(false); 

    const sortedJobs = computed(() => {
       
        return [...jobs.value].sort((a, b) => {
            if (!a.deadline) return 1;
            if (!b.deadline) return -1;
            return new Date(a.deadline).getTime() - new Date(b.deadline).getTime();
        });
    });

    const sortedAllJobs = computed(() => {
        return [...allJobs.value].sort((a, b) => {
            if (!a.deadline) return 1;
            if (!b.deadline) return -1;
            return new Date(a.deadline).getTime() - new Date(b.deadline).getTime();
        });
    });

    const fetchJobs = async () => {
        isLoading.value = true;
        try {
            const response = await apiClient.get<Job[]>('/api/jobs');
            jobs.value = response.data;
        } catch (error) {
            console.error('공고 목록 로딩 실패:', error);
            jobs.value = []; 
        } finally {
            isLoading.value = false;
        }
    };

    const fetchAllJobs = async () => {
        isLoading.value = true;
        try {
            const response = await apiClient.get<Job[]>('/api/jobs/all');
            allJobs.value = response.data;
        } catch (error) {
            console.error('전체 공고 목록 로딩 실패:', error);
            allJobs.value = [];
        } finally {
            isLoading.value = false;
        }
    };

    const createJob = async (newJobData: JobCreate): Promise<boolean> => {
        isLoading.value = true;
        try {
            const response = await apiClient.post<Job>('/api/jobs', newJobData);

            jobs.value.unshift(response.data);
            return true; // 성공
        } catch (error) {
            console.error('공고 등록 실패:', error);
            return false; // 실패
        } finally {
            isLoading.value = false;
        }
    };


    const deleteJob = async (jobId: number) => {
        
        const originalJobs = [...jobs.value];
        jobs.value = jobs.value.filter(job => job.id !== jobId);

        try {
            await apiClient.delete(`/api/jobs/${jobId}`);

        } catch (error) {
            console.error('공고 삭제 실패:', error);

            jobs.value = originalJobs; 
            alert('공고 삭제에 실패했습니다.');
        }
    };

    return {
        jobs,
        allJobs,
        isLoading,
        sortedJobs,
        sortedAllJobs,
        fetchJobs,
        fetchAllJobs,
        createJob,
        deleteJob
    };
});