<script setup>
import { ref } from 'vue';

const chats = ref([
  { id: 1, title: '채팅방 1', lastMessage: '마지막 메시지 1', unreadCount: 2 },
  { id: 2, title: '채팅방 2', lastMessage: '마지막 메시지 2', unreadCount: 0 },
  { id: 3, title: '채팅방 3', lastMessage: '마지막 메시지 3', unreadCount: 1 },
]);

</script>

<template>
  <div class="chat-list">
    <h2>채팅 목록</h2>
    <ul>
      <li
          v-for="chat in chats"
          :key="chat.id"
          @click="$emit('selectChat', chat)"
          :class="['chat-item', chat.unreadCount > 0 ? 'unread' : 'read']"
      >
        <div class="chat-info">
          <span class="chat-title">{{ chat.title }}</span>
          <span class="last-message">{{ chat.lastMessage }}</span>
        </div>
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

.chat-item.unread::marker {
  color: darkgreen; /* 읽지 않은 메시지가 있는 경우 */
}

.chat-item.read::marker {
  color: darkgray; /* 읽은 메시지일 경우 */
}

</style>
