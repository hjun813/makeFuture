<template>
  <div class="dashboard-container">
    <section class="welcome-section">
      <h2>오늘도 좋은 하루에요~!</h2>
      <p>오늘도 취업 성공을 향해 한 걸음 더 나아가세요.</p>
    </section>

    <div class="dashboard-grid">
      
      <div class="widget-card job-widget">
        <div class="widget-header">
          <h3>🚨 마감 임박 공고</h3>
          <RouterLink to="/jobs" class="more-link">더보기 ></RouterLink>
        </div>
        <div class="widget-body">
          <div v-if="jobStore.isLoading" class="loading">로딩 중...</div>
          <ul v-else-if="upcomingJobs.length > 0" class="simple-list">
            <li v-for="job in upcomingJobs" :key="job.id">
              <div class="job-info">
                <span class="company">{{ job.companyName }}</span>
                <span class="title">{{ job.jobTitle }}</span>
              </div>
              <span class="d-day">{{ getDDay(job.deadline) }}</span>
            </li>
          </ul>
          <div v-else class="empty-state">
            <p>등록된 공고가 없습니다.</p>
            <RouterLink to="/jobs" class="action-btn">공고 등록하기</RouterLink>
          </div>
        </div>
      </div>

      <div class="widget-card todo-widget">
        <div class="widget-header">
          <h3>✅ 오늘의 할 일 ({{ completedTodosCount }}/{{ todayTodos.length }})</h3>
          <RouterLink to="/todos" class="more-link">관리 ></RouterLink>
        </div>
        <div class="widget-body">
          <div v-if="todoStore.isLoading" class="loading">로딩 중...</div>
          
          <div v-if="todayTodos.length > 0" class="progress-bar-bg">
            <div class="progress-bar-fill" :style="{ width: progressPercentage + '%' }"></div>
          </div>

          <ul v-if="todayTodos.length > 0" class="simple-list todo-list">
            <li v-for="todo in todayTodos.slice(0, 5)" :key="todo.id" :class="{ done: todo.isDone }">
              <input 
                type="checkbox" 
                :checked="todo.isDone" 
                @change="todoStore.updateTodoStatus(todo.id, !todo.isDone)"
              />
              <span>{{ todo.content }}</span>
            </li>
          </ul>
          <div v-else class="empty-state">
            <p>오늘 예정된 할 일이 없습니다.</p>
            <RouterLink to="/todos" class="action-btn">할 일 추가하기</RouterLink>
          </div>
        </div>
      </div>

      <div class="widget-card history-widget">
        <div class="widget-header">
          <h3>🗓️ 최근 활동 기록</h3>
          <RouterLink to="/histories" class="more-link">전체 보기 ></RouterLink>
        </div>
        <div class="widget-body">
          <div v-if="historyStore.isLoading" class="loading">로딩 중...</div>
          <ul v-else-if="recentHistories.length > 0" class="timeline-preview">
            <li v-for="history in recentHistories" :key="history.id">
              <span class="date">{{ formatDate(history.createdAt) }}</span>
              <p class="content">{{ history.content }}</p>
            </li>
          </ul>
          <div v-else class="empty-state">
            <p>아직 작성된 기록이 없습니다.</p>
            <RouterLink to="/histories" class="action-btn">기록 남기기</RouterLink>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { RouterLink } from 'vue-router';
import { useJobStore } from '@/stores/jobStore';
import { useTodoStore } from '@/stores/todoStore';
import { useHistoryStore } from '@/stores/historyStore';

const jobStore = useJobStore();
const todoStore = useTodoStore();
const historyStore = useHistoryStore();

// 1. 데이터 로드 (모든 데이터를 병렬로 호출)
onMounted(() => {
  Promise.all([
    jobStore.fetchJobs(),
    todoStore.fetchTodos(),
    historyStore.fetchHistories()
  ]);
});

// 2. [Job] 마감 임박 공고 계산 (마감일 지난 것 제외, 마감일순 정렬, 상위 3개)
const upcomingJobs = computed(() => {
  const now = new Date();
  now.setHours(0, 0, 0, 0);
  
  return jobStore.jobs
    .filter(job => job.deadline && new Date(job.deadline) >= now) // 지난 공고 제외
    .sort((a, b) => new Date(a.deadline!).getTime() - new Date(b.deadline!).getTime()) // 오름차순
    .slice(0, 3); // 상위 3개
});

// 3. [Todo] 오늘 할 일 계산
const todayTodos = computed(() => todoStore.todosToday);

const completedTodosCount = computed(() => 
  todayTodos.value.filter(t => t.isDone).length
);

const progressPercentage = computed(() => {
  if (todayTodos.value.length === 0) return 0;
  return Math.round((completedTodosCount.value / todayTodos.value.length) * 100);
});

// 4. [History] 최근 기록 3개
const recentHistories = computed(() => {
  return historyStore.histories.slice(0, 3);
});

// --- 유틸리티 함수 ---
const getDDay = (dateString: string | null): string => {
  if (!dateString) return '';
  const today = new Date();
  const deadline = new Date(dateString);
  today.setHours(0, 0, 0, 0);
  deadline.setHours(0, 0, 0, 0);
  const diffTime = deadline.getTime() - today.getTime();
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays === 0 ? 'D-Day' : `D-${diffDays}`;
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return `${date.getMonth() + 1}/${date.getDate()}`;
};
</script>

<style scoped>
.dashboard-container {
  width: 100%;
  max-width: 1200px; /* 넓은 화면 대응 */
  margin: 0 auto;
}

.welcome-section {
  margin-bottom: 2rem;
}
.welcome-section h2 {
  color: #35495e;
  margin-bottom: 0.5rem;
}
.welcome-section p {
  color: #666;
}

/* 그리드 레이아웃 */
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

/* 위젯 카드 공통 스타일 */
.widget-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  height: 320px; /* 높이 고정 */
}

.widget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 0.5rem;
}

.widget-header h3 {
  margin: 0;
  font-size: 1.1rem;
  color: #35495e;
}

.more-link {
  font-size: 0.85rem;
  color: #999;
  text-decoration: none;
}
.more-link:hover {
  color: #42b883;
}

.widget-body {
  flex-grow: 1;
  overflow-y: auto; /* 내용 많으면 스크롤 */
}

/* 로딩 & 빈 상태 */
.loading, .empty-state {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #888;
  font-size: 0.9rem;
  text-align: center;
}
.action-btn {
  margin-top: 0.5rem;
  font-size: 0.85rem;
  color: #42b883;
  text-decoration: underline;
  cursor: pointer;
}

/* [위젯 1] Job List 스타일 */
.simple-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.simple-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f5f5f5;
}
.simple-list li:last-child {
  border-bottom: none;
}
.job-info {
  display: flex;
  flex-direction: column;
}
.company {
  font-size: 0.8rem;
  color: #888;
}
.title {
  font-weight: 500;
  color: #333;
}
.d-day {
  background-color: #fff0f0;
  color: #d9534f;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: bold;
}

/* [위젯 2] Todo List 스타일 */
.progress-bar-bg {
  background-color: #eee;
  height: 8px;
  border-radius: 4px;
  margin-bottom: 1rem;
  overflow: hidden;
}
.progress-bar-fill {
  background-color: #42b883;
  height: 100%;
  transition: width 0.5s ease;
}
.todo-list li {
  justify-content: flex-start;
  gap: 0.5rem;
}
.todo-list li.done span {
  text-decoration: line-through;
  color: #bbb;
}

/* [위젯 3] History Timeline 스타일 */
.timeline-preview {
  list-style: none;
  padding: 0;
  margin: 0;
  border-left: 2px solid #eee;
  margin-left: 0.5rem;
}
.timeline-preview li {
  position: relative;
  padding-left: 1.5rem;
  margin-bottom: 1rem;
}
.timeline-preview li::before {
  content: '';
  position: absolute;
  left: -5px;
  top: 6px;
  width: 8px;
  height: 8px;
  background-color: #42b883;
  border-radius: 50%;
}
.timeline-preview .date {
  font-size: 0.75rem;
  color: #999;
  display: block;
  margin-bottom: 0.2rem;
}
.timeline-preview .content {
  margin: 0;
  font-size: 0.95rem;
  color: #555;
  /* 긴 텍스트 말줄임 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>