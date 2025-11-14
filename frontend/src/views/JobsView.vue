<template>
  <div class="jobs-view-container">
    <h2>내 공고 관리</h2>
    
    <JobForm />

    <div class="job-list-container">
      <h3>전체 공고 ({{ jobStore.sortedJobs.length }}개)</h3>
      
      <div v-if="jobStore.isLoading && jobStore.jobs.length === 0" class="loading">
        공고 목록을 불러오는 중...
      </div>
      
      <div v-else-if="jobStore.jobs.length === 0" class="empty-list">
        등록된 공고가 없습니다. 새 공고를 등록해보세요!
      </div>

      <ul v-else class="job-list">
        <li v-for="job in jobStore.sortedJobs" :key="job.id" class="job-card">
          <div class="card-header">
            <h4>{{ job.jobTitle }}</h4>
            <span>@ {{ job.companyName }}</span>
          </div>
          <div class="card-body">
            <p v-if="job.deadline" class="deadline">
              마감일: {{ formatDate(job.deadline) }} ({{ getDDay(job.deadline) }})
            </p>
            <p v-if="job.jobType" class="job-type">{{ job.jobType }}</p>
          </div>
          <div class="card-footer">
            <a v-if="job.url" :href="job.url" target="_blank" class="link-button">공고 보기</a>
            <button @click="handleDelete(job.id)" class="delete-button">삭제</button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useJobStore } from '@/stores/jobStore';
import JobForm from '@/components/JobForm.vue'; // 2번에서 만든 폼

const jobStore = useJobStore();

// 컴포넌트가 마운트될 때 API를 호출하여 공고 목록을 가져옴
onMounted(() => {
  jobStore.fetchJobs();
});

const handleDelete = (jobId: number) => {
  if (confirm("이 공고를 정말 삭제하시겠습니까?")) {
    jobStore.deleteJob(jobId);
  }
};

// --- (유틸리티 함수) ---
const formatDate = (dateString: string | null): string => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('ko-KR');
};

const getDDay = (dateString: string | null): string => {
  if (!dateString) return 'D-??';
  const today = new Date();
  const deadline = new Date(dateString);
  today.setHours(0, 0, 0, 0); // 시간 제거
  deadline.setHours(0, 0, 0, 0);

  const diffTime = deadline.getTime() - today.getTime();
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  if (diffDays === 0) return 'D-Day';
  if (diffDays < 0) return `D+${Math.abs(diffDays)} (마감)`;
  return `D-${diffDays}`;
};
</script>

<style scoped>
.jobs-view-container {
  width: 100%;
  max-width: 900px;
}
.loading, .empty-list {
  text-align: center;
  padding: 2rem;
  color: #777;
}
.job-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1rem;
}
.job-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 1rem;
  display: flex;
  flex-direction: column;
}
.card-header {
  border-bottom: 1px solid #eee;
  padding-bottom: 0.5rem;
  margin-bottom: 1rem;
}
.card-header h4 {
  margin: 0;
}
.card-header span {
  font-size: 0.9rem;
  color: #555;
}
.card-body {
  flex-grow: 1;
}
.deadline {
  font-weight: bold;
  color: #d9534f;
}
.job-type {
  font-size: 0.8rem;
  background-color: #eee;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  display: inline-block;
}
.card-footer {
  margin-top: 1rem;
  display: flex;
  gap: 0.5rem;
}
.link-button, .delete-button {
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
}
.link-button {
  background-color: #0275d8;
  color: white;
}
.delete-button {
  background-color: #f0f0f0;
  color: #d9534f;
}
</style>