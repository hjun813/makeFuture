// src/types/router.d.ts
import 'vue-router';

declare module 'vue-router' {
  interface RouteMeta {
    requiresAuth?: boolean; // 인증 필요 여부
  }
}