<template>
  <div class="jobs-view-container">
    <h2>전체 공고 ({{ jobStore.sortedAllJobs.length }}개)</h2>
    
    <div v-if="jobStore.isLoading && jobStore.allJobs.length === 0" class="loading">
      전체 공고 목록을 불러오는 중...
    </div>
    
    <div v-else-if="jobStore.allJobs.length === 0" class="empty-list">
      등록된 공고가 없습니다.
    </div>

    <ul v-else class="job-list">
      <li v-for="job in jobStore.sortedAllJobs" :key="job.id" class="job-card">
        <div class="card-header">
          <h4>{{ job.jobTitle }}</h4>
          <span>@ {{ job.companyName }}</span>
        </div>
        <div class="card-body">
          <p v="if" class="deadline">
            마감일: {{ formatDate(job.deadline) }} ({{ getDDay(job.deadline) }})
          </p>
          <p v="if" class="job-type">{{ job.jobType }}</p>
        </div>
        <div class="card-footer">
          <a v-if="job.url" :href="job.url" target="_blank" class="link-button">공고 보기</a>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useJobStore } from '@/stores/jobStore';

const jobStore = useJobStore();

// 컴포넌트가 마운트될 때 API를 호출
onMounted(() => {
  jobStore.fetchAllJobs(); // ✅ '내 공고'가 아닌 '전체 공고' 호출
});

// --- (JobsView.vue와 동일한 유틸리티 함수) ---
const formatDate = (dateString: string | null): string => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('ko-KR');
};

const getDDay = (dateString: string | null): string => {
  if (!dateString) return 'D-??';
  const today = new Date();
  const deadline = new Date(dateString);
  today.setHours(0, 0, 0, 0);
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

.loading,
.empty-list {
  text-align: center;
  padding: 3rem 2rem;
  color: #777;
  font-size: 1.1rem;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.job-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  /* 280px 크기를 기준으로 반응형 그리드 */
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
}

.job-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.job-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.card-header {
  border-bottom: 1px solid #eee;
  padding-bottom: 0.75rem;
  margin-bottom: 1rem;
}

.card-header h4 {
  margin: 0 0 0.25rem 0;
  color: #35495e;
}

.card-header span {
  font-size: 0.9rem;
  color: #555;
  font-weight: 500;
}

.card-body {
  flex-grow: 1; /* 카드의 높이가 달라도 푸터가 항상 하단에 위치하도록 함 */
}

.deadline {
  font-weight: bold;
  color: #d9534f; /* 마감일 강조 (빨간색 계열) */
  margin: 0 0 0.5rem 0;
}

.job-type {
  font-size: 0.8rem;
  font-weight: 500;
  background-color: #eef;
  color: #42b883;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
  display: inline-block;
}

.card-footer {
  margin-top: 1rem;
  display: flex;
  gap: 0.5rem;
}

.link-button {
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  font-weight: bold;
  font-size: 0.9rem;
  background-color: #42b883;
  color: white;
  text-align: center;
}

.link-button:hover {
  background-color: #35495e;
}
</style>