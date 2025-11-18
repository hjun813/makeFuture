<template>
  <div class="about-container">
    <div class="profile-card">
      <h2>Profile</h2>
      
      <div class="profile-header">
        <h3>김현준 (Kim HyunJun)</h3>
        <p class="role">FullStack Developer</p>
      </div>

      <div class="section">
        <h4>📢 INTRO</h4>
        <p>
            <strong>"흩어진 취업 정보와 나의 노력을 한곳에."</strong><br/><br/>
            <strong>MakeFuture</strong>는 취업 준비생이 겪는 정보 관리의 비효율을 해결하기 위해 개발한 
            개인 맞춤형 취업 비서 서비스입니다.<br><br>
            단순한 일정 관리를 넘어, 채용 공고 스크랩부터 학습 기록까지 
            취업 준비의 전 과정을 기록하여 시각적으로 관리할 수 있도록 설계했습니다.
        </p>
      </div>

      <div class="section">
        <h4>🛠️ Tech Stack</h4>
        <div class="tech-badges">
          <span class="badge backend">Spring Boot</span>
          <span class="badge backend">Java</span>
          <span class="badge frontend">Vue.js</span>
          <span class="badge frontend">TypeScript</span>
        </div>
      </div>

      <div class="section">
        <h4>📫 Contact Me</h4>
        <ul class="contact-list">
          <li>Email: hyeonjun0899@gmail.com</li>
          <li>GitHub: <a href="https://github.com/hjun813" target="_blank">https://github.com/hjun813</a></li>
          <li>My Blog: <a href="https://velog.io/@hjun813/posts" target="_blank">https://velog.io/@hjun813/posts</a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>

<style scoped>
.about-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.profile-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 2.5rem;
  text-align: center;
}

.profile-card h2 {
  color: #35495e;
  margin-bottom: 2rem;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 1rem;
}

.profile-header {
  margin-bottom: 2rem;
}

.profile-header h3 {
  font-size: 1.5rem;
  margin: 0.5rem 0;
}

.role {
  color: #42b883;
  font-weight: bold;
  font-size: 1.1rem;
}

.section {
  margin-bottom: 2rem;
  text-align: left;
}

.section h4 {
  color: #555;
  margin-bottom: 0.8rem;
  font-size: 1.1rem;
}

.section p {
  line-height: 1.6;
  color: #666;
}

/* 기술 스택 배지 스타일 */
.tech-badges {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.badge {
  padding: 0.4rem 0.8rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.badge.backend {
  background-color: #e8f5e9; /* 연한 초록 */
  color: #2e7d32;
}

.badge.frontend {
  background-color: #e3f2fd; /* 연한 파랑 */
  color: #1565c0;
}

/* 연락처 리스트 스타일 */
.contact-list {
  list-style: none;
  padding: 0;
}

.contact-list li {
  margin-bottom: 0.5rem;
  color: #666;
}

.contact-list a {
  color: #42b883;
  text-decoration: none;
}
.contact-list a:hover {
  text-decoration: underline;
}
</style>