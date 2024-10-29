<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const chats = ref([
  { id: 1, title: '채팅방 1', lastMessage: '마지막 메시지 1', unreadCount: 2 },
  { id: 2, title: '채팅방 2', lastMessage: '마지막 메시지 2', unreadCount: 0 },
  { id: 3, title: '채팅방 3', lastMessage: '마지막 메시지 3', unreadCount: 1 },
]);

const router = useRouter();

function openChat(chat) {
  router.push({ name: 'ChatRoom', params: { chatId: chat.id } });
}
</script>

<template>
  <div class="chat-list">
    <h2>채팅 목록</h2>
    <ul>
      <li
          v-for="chat in chats"
          :key="chat.id"
          @click="openChat(chat)"
          class="chat-item"
      >
        <div class="chat-info">
          <span class="chat-title">{{ chat.title }}</span>
          <span class="last-message">{{ chat.lastMessage }}</span>
        </div>
        <span class="unread-count" v-if="chat.unreadCount > 0">
          {{ chat.unreadCount }}
        </span>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.chat-list {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem;
}
.chat-item {
  display: flex;
  justify-content: space-between;
  padding: 0.8rem;
  cursor: pointer;
  border-bottom: 1px solid #ddd;
}
.chat-info {
  display: flex;
  flex-direction: column;
}
.chat-title {
  font-weight: bold;
}
.unread-count {
  background-color: #ff6b6b;
  color: white;
  border-radius: 50%;
  padding: 0.2rem 0.6rem;
  font-size: 0.8rem;
}
</style>
