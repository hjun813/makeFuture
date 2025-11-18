<template>
  <form class="job-form" @submit.prevent="handleSubmit">
    <h3>새 공고 등록하기</h3>
    <div class="form-grid">
      <div class="form-group">
        <label for="companyName">회사명*</label>
        <input id="companyName" v-model="formData.companyName" type="text" required />
      </div>
      <div class="form-group">
        <label for="jobTitle">직무*</label>
        <input id="jobTitle" v-model="formData.jobTitle" type="text" required />
      </div>
      <div class="form-group">
        <label for="url">공고 URL</label>
        <input id="url" v-model="formData.url" type="url" />
      </div>
      <div class="form-group">
        <label for="deadline">마감일</label>
        <div class="deadline-input-group">
          <input 
            id="deadline" 
            v-model="formData.deadline" 
            type="date" 
            :disabled="isNoDeadline" 
          />
          <label class="checkbox-label">
            <input 
              type="checkbox" 
              v-model="isNoDeadline" 
              @change="handleNoDeadlineChange" 
            />
            채용시 마감
          </label>
        </div>
      </div>
      <div class="form-group">
        <label for="jobType">유형</label>
        <select id="jobType" v-model="formData.jobType">
          <option value="">선택</option>
          <option value="신입 및 인턴">신입 및 인턴</option>
          <option value="체험형 인턴">체험형 인턴</option>
          <option value="채용연계 인턴">채용연계 인턴</option>
          <option value="기타">기타</option>
        </select>
      </div>
    </div>
    <button type="submit" class="submit-button" :disabled="jobStore.isLoading">
      {{ jobStore.isLoading ? '등록 중...' : '등록하기' }}
    </button>
  </form>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useJobStore, type JobCreate } from '@/stores/jobStore';

const jobStore = useJobStore();

// 폼 데이터를 관리할 ref
const formData = ref<JobCreate>({
  companyName: '',
  jobTitle: '',
  url: '',
  deadline: '', // type="date"는 YYYY-MM-DD 문자열을 반환
  jobType: ''
});

const isNoDeadline = ref(false);
const handleNoDeadlineChange = () => {
  if (isNoDeadline.value) {
    formData.value.deadline = ''; // 날짜 초기화
  }
};

const handleSubmit = async () => {
  // 빈 문자열 대신 undefined로 보내도록 처리 (선택)
  const dataToSubmit: JobCreate = {
    ...formData.value,
    url: formData.value.url || undefined,
    deadline: formData.value.deadline || undefined,
    jobType: formData.value.jobType || undefined,
  };

  const success = await jobStore.createJob(dataToSubmit);

  if (success) {
    alert('공고가 등록되었습니다.');
    // 폼 초기화
    formData.value = {
      companyName: '',
      jobTitle: '',
      url: '',
      deadline: '',
      jobType: ''
    };
  } else {
    alert('공고 등록에 실패했습니다.');
  }
};
</script>

<style scoped>
.job-form {
  background: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 2rem;
}
.deadline-input-group {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  font-size: 0.9rem;
  cursor: pointer;
  white-space: nowrap;
}
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}
.form-group {
  display: flex;
  flex-direction: column;
}
.form-group label {
  margin-bottom: 0.5rem;
  font-weight: 500;
  font-size: 0.9rem;
}
.form-group input,
.form-group select {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.submit-button {
  margin-top: 1.5rem;
  width: 100%;
  padding: 0.75rem;
  background-color: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
}
.submit-button:disabled {
  background-color: #ccc;
}
</style>