<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

function goToChatList() {
  router.push({ name: 'ChatList' });
}
</script>


<template>
  <div class="main">
    <h1>메인 페이지</h1>
    <button @click="goToChatList">채팅 목록으로 이동</button>
  </div>
</template>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
}
button {
  padding: 0.8rem 1.2rem;
  font-size: 1rem;
  cursor: pointer;
}
</style>
