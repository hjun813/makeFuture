<template>
  <div class="todo-view-container">
    <h2>나의 할 일 (Todo List)</h2>

    <form @submit.prevent="handleCreateTodo" class="todo-form">
      <input 
        v-model="newTodoContent" 
        type="text" 
        placeholder="새 할 일을 입력하세요..." 
        required 
      />
      <select v-model="newTodoCategory" required>
        <option value="TODAY">오늘</option>
        <option value="WEEK">이번 주</option>
        <option value="MONTH">이번 달</option>
      </select>
      <button type="submit">추가</button>
    </form>

    <div class="todo-tabs">
      <button 
        :class="{ active: currentTab === 'TODAY' }" 
        @click="currentTab = 'TODAY'">
        오늘 ({{ todoStore.todosToday.length }})
      </button>
      <button 
        :class="{ active: currentTab === 'WEEK' }" 
        @click="currentTab = 'WEEK'">
        이번 주 ({{ todoStore.todosWeek.length }})
      </button>
      <button 
        :class="{ active: currentTab === 'MONTH' }" 
        @click="currentTab = 'MONTH'">
        이번 달 ({{ todoStore.todosMonth.length }})
      </button>
    </div>

    <div class="todo-list-container">
      <div v-if="todoStore.isLoading" class="loading">목록을 불러오는 중...</div>
      
      <ul v-else-if="currentTodos.length > 0" class="todo-list">
        <li v-for="todo in currentTodos" :key="todo.id" :class="{ done: todo.isDone }">
          <input 
            type="checkbox" 
            :checked="todo.isDone" 
            @change="todoStore.updateTodoStatus(todo.id, !todo.isDone)"
            :disabled="editingTodoId === todo.id"
          />
          
          <div class="todo-item-content">
            <input 
              v-if="editingTodoId === todo.id"
              v-model="editingTodoText"
              @keyup.enter="saveEdit(todo.id)"
              @keyup.esc="cancelEdit"
              class="edit-input"
              v-focus
            />
            <span v-else class="todo-content" @dblclick="startEdit(todo)">
              {{ todo.content }}
            </span>
          </div>
          
          <div class="todo-buttons">
            <template v-if="editingTodoId === todo.id">
              <button @click="saveEdit(todo.id)" class="action-button save">✓</button>
              <button @click="cancelEdit" class="action-button cancel">×</button>
            </template>
            <template v-else>
              <button @click="startEdit(todo)" class="action-button edit">✎</button>
              <button @click="todoStore.deleteTodo(todo.id)" class="delete-button">×</button>
            </template>
          </div>
        </li>
      </ul>
      
      <div v-else class="empty-list">
        {{ currentTabName }} 할 일이 없습니다.
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, type Directive } from 'vue';
import { useTodoStore, type Todo } from '@/stores/todoStore';
import type { TodoCategory } from '@/types/todoTypes';

const todoStore = useTodoStore();

const newTodoContent = ref('');
const newTodoCategory = ref<TodoCategory>('TODAY');

const currentTab = ref<TodoCategory>('TODAY');

const currentTodos = computed((): Todo[] => {
  switch (currentTab.value) {
    case 'TODAY': return todoStore.todosToday;
    case 'WEEK': return todoStore.todosWeek;
    case 'MONTH': return todoStore.todosMonth;
    default: return [];
  }
});

const currentTabName = computed((): string => {
  switch (currentTab.value) {
    case 'TODAY': return '오늘';
    case 'WEEK': return '이번 주';
    case 'MONTH': return '이번 달';
    default: return '';
  }
});

const editingTodoId = ref<number | null>(null);
const editingTodoText = ref<string>('');

const vFocus: Directive<HTMLInputElement> = {
  mounted(el) {
    el.focus();
  }
};

onMounted(() => {
  if (todoStore.todos.length === 0) {
    todoStore.fetchTodos();
  }
});

const handleCreateTodo = async () => {
  if (newTodoContent.value.trim() === '') return;

  const success = await todoStore.createTodo({
    content: newTodoContent.value,
    category: newTodoCategory.value
  });

  if (success) {
    newTodoContent.value = '';
  } else {
    alert('할 일 추가에 실패했습니다.');
  }
};

const startEdit = (todo: Todo) => {
  editingTodoId.value = todo.id;
  editingTodoText.value = todo.content;
};

const saveEdit = (todoId: number) => {
  const newContent = editingTodoText.value.trim();
  if (newContent === '') {
    alert('내용은 비워둘 수 없습니다.');
    return;
  }
  todoStore.updateTodoContent(todoId, newContent);
  cancelEdit();
};

const cancelEdit = () => {
  editingTodoId.value = null;
  editingTodoText.value = '';
};
</script>

<style scoped>
.todo-view-container {
  width: 100%;
  max-width: 700px;
}

.todo-form {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  background: #fff;
  padding: 1rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}
.todo-form input[type="text"] {
  flex-grow: 1;
  border: 1px solid #ccc;
  padding: 0.75rem;
  border-radius: 4px;
}
.todo-form select {
  border: 1px solid #ccc;
  padding: 0 0.5rem;
  border-radius: 4px;
}
.todo-form button {
  background-color: #42b883;
  color: white;
  border: none;
  padding: 0 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.todo-tabs {
  display: flex;
  margin-bottom: 1rem;
}
.todo-tabs button {
  flex-grow: 1;
  padding: 1rem;
  border: none;
  background-color: #f0f0f0;
  cursor: pointer;
  font-size: 1rem;
  color: #555;
  border-bottom: 3px solid transparent;
}
.todo-tabs button.active {
  background-color: #fff;
  color: #42b883;
  border-bottom: 3px solid #42b883;
  font-weight: bold;
}

.todo-list-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  min-height: 200px;
  padding: 1rem;
}
.loading, .empty-list {
  text-align: center;
  padding: 3rem 0;
  color: #777;
}

.todo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.todo-list li {
  display: flex;
  align-items: center;
  padding: 1rem 0.5rem;
  border-bottom: 1px solid #f0f0f0;
}
.todo-list li:last-child {
  border-bottom: none;
}
.todo-list li.done .todo-content {
  text-decoration: line-through;
  color: #999;
}

.todo-list input[type="checkbox"] {
  margin-right: 1rem;
  transform: scale(1.2);
}
.todo-list input[type="checkbox"]:disabled {
  opacity: 0.5;
}

.todo-item-content {
  flex-grow: 1;
}
.todo-content {
  cursor: pointer;
}

.edit-input {
  width: 95%;
  padding: 0.5rem;
  border: 1px solid #42b883;
  border-radius: 4px;
  font-size: 1rem;
}

.todo-buttons {
  display: flex;
  gap: 0.25rem;
}

.delete-button {
  background: none;
  border: none;
  color: #ff6b6b;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0 0.5rem;
  visibility: hidden;
}
.todo-list li:hover .delete-button {
  visibility: visible;
}

.action-button {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0 0.5rem;
  border-radius: 4px;
}
.action-button.edit {
  color: #0275d8;
  visibility: hidden;
}
.todo-list li:hover .action-button.edit {
  visibility: visible;
}
.action-button.save {
  color: #42b883;
  font-weight: bold;
}
.action-button.cancel {
  color: #999;
}
</style>