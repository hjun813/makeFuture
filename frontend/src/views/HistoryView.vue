<template>
  <div class="history-view-container">
    <h2>나의 히스토리 🗓️</h2>
    <p class="description">오늘 한 일, 배운 것, 느낀 점을 간단히 기록해 보세요.</p>

    <form @submit.prevent="handleCreateHistory" class="history-form">
      <textarea
        v-model="newHistoryContent"
        placeholder="오늘의 히스토리를 입력하세요..."
        rows="3"
        required
      ></textarea>
      <div class="form-footer">
        <input
          v-model="newHistoryCategory"
          type="text"
          placeholder="카테고리 (예: 알고리즘, 프로젝트)"
        />
        <button type="submit">기록하기</button>
      </div>
    </form>

    <div class="history-list-container">
      <h3>기록 목록</h3>
      <div v-if="historyStore.isLoading" class="loading">
        기록을 불러오는 중...
      </div>
      <div v-else-if="historyStore.histories.length === 0" class="empty-list">
        아직 작성된 히스토리가 없습니다.
      </div>

      <ul v-else class="timeline">
        <li v-for="history in historyStore.histories" :key="history.id" class="timeline-item">
          <div class="timeline-dot"></div>
          <div class="timeline-content">
            <div class="timeline-header">
              <span class="timeline-date">{{ formatDateTime(history.createdAt) }}</span>
              <span v-if="history.category" class="category-tag">{{ history.category }}</span>
              <button @click="historyStore.deleteHistory(history.id)" class="delete-button" title="삭제">
                🗑️
              </button>
            </div>
            <p class="timeline-text">{{ history.content }}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useHistoryStore } from '@/stores/historyStore';

const historyStore = useHistoryStore();

const newHistoryContent = ref('');
const newHistoryCategory = ref('');

// 컴포넌트 마운트 시 API 호출
onMounted(() => {
  if (historyStore.histories.length === 0) {
    historyStore.fetchHistories();
  }
});

// 새 히스토리 생성 핸들러
const handleCreateHistory = async () => {
  if (newHistoryContent.value.trim() === '') return;

  const success = await historyStore.createHistory({
    content: newHistoryContent.value,
    category: newHistoryCategory.value || undefined, 
  });

  if (success) {
    newHistoryContent.value = '';
    newHistoryCategory.value = '';
  } else {
    alert('히스토리 등록에 실패했습니다.');
  }
};

// 날짜 포맷팅 유틸리티
const formatDateTime = (dateString: string): string => {
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  };
  return new Date(dateString).toLocaleString('ko-KR', options);
};
</script>

<style scoped>
.history-view-container {
  width: 100%;
  max-width: 800px;
}
.description {
  color: #555;
  margin-top: -1rem;
  margin-bottom: 1.5rem;
}

/* 폼 스타일 */
.history-form {
  background: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 2rem;
}
.history-form textarea {
  width: 100%;
  border: 1px solid #ccc;
  padding: 0.75rem;
  border-radius: 4px;
  font-size: 1rem;
  font-family: inherit;
  resize: vertical;
  min-height: 80px;
  box-sizing: border-box; /* padding 포함 */
}
.form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
}
.form-footer input[type="text"] {
  border: 1px solid #ccc;
  padding: 0.75rem;
  border-radius: 4px;
  width: 60%;
}
.form-footer button {
  background-color: #42b883;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

/* 목록 컨테이너 (로딩, 빈 목록) */
.history-list-container {
  margin-top: 2rem;
}
.loading, .empty-list {
  text-align: center;
  padding: 3rem 0;
  color: #777;
  font-size: 1.1rem;
  background-color: #fff;
  border-radius: 8px;
}

/* 타임라인 스타일 */
.timeline {
  list-style: none;
  padding: 0;
  margin: 0;
  position: relative;
}
/* 타임라인 중앙선 */
.timeline::before {
  content: '';
  position: absolute;
  top: 10px;
  left: 10px;
  bottom: 10px;
  width: 4px;
  background-color: #f0f0f0;
  border-radius: 2px;
}

.timeline-item {
  display: flex;
  position: relative;
  margin-bottom: 1.5rem;
}

.timeline-dot {
  position: absolute;
  top: 8px;
  left: 2px;
  width: 16px;
  height: 16px;
  background-color: #42b883;
  border-radius: 50%;
  border: 3px solid #fff;
  z-index: 1;
}

.timeline-content {
  flex-grow: 1;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 1rem 1.5rem;
  margin-left: 30px; /* dot + 여백 */
}

.timeline-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 0.5rem;
  margin-bottom: 0.75rem;
}

.timeline-date {
  font-size: 0.9rem;
  font-weight: 500;
  color: #555;
}

.category-tag {
  font-size: 0.8rem;
  font-weight: 500;
  background-color: #eee;
  color: #35495e;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
}

.delete-button {
  background: none;
  border: none;
  color: #ff6b6b;
  font-size: 1.2rem;
  cursor: pointer;
  visibility: hidden;
}
.timeline-item:hover .delete-button {
  visibility: visible;
}

.timeline-text {
  margin: 0;
  white-space: pre-wrap; /* 줄바꿈 반영 */
  word-break: break-word;
}
</style>