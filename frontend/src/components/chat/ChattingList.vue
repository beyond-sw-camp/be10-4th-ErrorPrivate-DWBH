<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const chats = ref([]);

const fetchChats = async () => {
  try {
    const response = await axios.get('http://localhost:8089/api/v1/user/chat');
    chats.value = response.data;
    console.log(chats.value);
  } catch (error) {
    console.error('채팅 목록을 가져오는 중 오류 발생:', error);
  }
};

const evaluateChat = (chatSeq, event) => {
  event.preventDefault();
  console.log(`Evaluating chat with seq: ${chatSeq}`);
  // 새로운 창에서 경로 열기
  const url = `/chat/${chatSeq}/evaluation/`;
  window.open(url, '_blank'); // 새 창에서 열기
};

onMounted(fetchChats);

</script>

<template>
  <div class="chat-list">
    <h2>채팅 목록</h2>
    <ul>
      <li
          v-for="chat in chats"
          :key="chat.chatSeq"
          @click="$emit('selectChat', chat)"
          :class="['chat-item', chat.readYn === 'N' ? 'unread' : 'read']"
      >
        <div class="chat-info">
          <div class="chat-details">
            <span class="chat-title">{{ chat.receiveUser.userNickname }}</span>
            <span class="last-message">안녕하세요.</span>
<!--            <span class="last-message">{{ chat.lastMessage }}</span>-->
            </div>
          <button class="evaluation-button" v-if="!chat.showEvaluation" @click.stop="(event) => evaluateChat(chat.chatSeq, event)">평가</button>
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
  justify-content: space-between;
}
.chat-title {
  font-weight: bold;
}

.chat-details {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.last-message {
  font-size: 0.9em;
  color: rgb(128, 128, 128);
}

.evaluation-button {
  margin-left: 150px;
  white-space: nowrap;
}

.chat-item.unread::marker {
  color: darkgreen; /* 읽지 않은 메시지가 있는 경우 */
}

.chat-item.read::marker {
  color: darkgray; /* 읽은 메시지일 경우 */
}

</style>
