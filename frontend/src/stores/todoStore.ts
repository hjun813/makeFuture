import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import apiClient from '@/api/axios';
import type { TodoCategory } from '@/types/todoTypes';

export interface Todo {
    id: number;
    content: string;
    isDone: boolean;
    category: TodoCategory;
}

export interface TodoCreate {
    content: string;
    category: TodoCategory;
}

export interface TodoUpdate {
    content: string;
}

export const useTodoStore = defineStore('todo', () => {
    const todos = ref<Todo[]>([]);
    const isLoading = ref(false);

    const todosToday = computed(() => 
        todos.value.filter(t => t.category === 'TODAY')
    );
    const todosWeek = computed(() =>
        todos.value.filter(t => t.category === 'WEEK')
    );
    const todosMonth = computed(() =>
        todos.value.filter(t => t.category === 'MONTH')
    );

    const fetchTodos = async () => {
        isLoading.value = true;
        try {
            const response = await apiClient.get<Todo[]>('/api/todos');
            todos.value = response.data;
        } catch (error) {
            console.error('Todo 목록 로딩 실패:', error);
            todos.value = [];
        } finally {
            isLoading.value = false;
        }
    };

    const createTodo = async (newTodo: TodoCreate): Promise<boolean> => {
        try {
            const response = await apiClient.post<Todo>('/api/todos', newTodo);
            todos.value.push(response.data);
            return true;
        } catch (error) {
            console.error('Todo 등록 실패:', error);
            return false;
        }
    };

    const updateTodoStatus = async (todoId: number, isDone: boolean) => {
        const todo = todos.value.find(t => t.id === todoId);
        if (todo) {
            todo.isDone = isDone;
        }

        try {
            await apiClient.patch(`/api/todos/${todoId}/status`, { isDone });
        } catch (error) {
            console.error('Todo 상태 업데이트 실패:', error);
            if (todo) todo.isDone = !isDone; 
            alert('상태 변경에 실패했습니다.');
        }
    };
    
    const updateTodoContent = async (todoId: number, newContent: string) => {
        const todo = todos.value.find(t => t.id === todoId);
        if (!todo) return;

        const oldContent = todo.content; 
        todo.content = newContent;

        try {
            await apiClient.put(`/api/todos/${todoId}`, { content: newContent });
        } catch (error) {
            console.error('Todo 내용 수정 실패:', error);
            todo.content = oldContent;
            alert('내용 수정에 실패했습니다.');
        }
    };

    const deleteTodo = async (todoId: number) => {
        const index = todos.value.findIndex(t => t.id === todoId);
        if (index === -1) return;
        
        const deletedTodoArray = todos.value.splice(index, 1);

        try {
            await apiClient.delete(`/api/todos/${todoId}`);
        } catch (error) {
            console.error('Todo 삭제 실패:', error);
            if (deletedTodoArray && deletedTodoArray[0]) {
                todos.value.splice(index, 0, deletedTodoArray[0]);
            }
            alert('삭제에 실패했습니다.');
        }
    };

    return {
        todos,
        isLoading,
        todosToday,
        todosWeek,
        todosMonth,
        fetchTodos,
        createTodo,
        updateTodoStatus,
        updateTodoContent,
        deleteTodo
    };
});