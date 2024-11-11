<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {useAuthStore} from "@/stores/auth.js";

const chats = ref([]);
const emit = defineEmits(['selectChat']);
const token = useAuthStore().accessToken;

const fetchChats = async () => {
  try {
    const response = await axios.get('http://localhost:8089/api/v1/user/chat',
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
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

const truncateMessage = (message) => {
  return message.length > 15 ? message.substring(0, 15) + '...' : message;
};

onMounted(fetchChats);

</script>

<template>
  <div class="chat-list">
    <h2>채팅 목록 &#9660;</h2>
    <ul>
      <li
          v-for="chat in chats"
          :key="chat.chatSeq"
          @click="emit('selectChat', chat)"
          :class="['chat-item', chat.readYn === 'N' ? 'unread' : 'read']"
      >
        <div class="chat-info">
          <img
              class="profile-image"
              src="@/images/profile-image.jpg"
              alt="프로필 이미지"
          />
          <div class="chat-details">
            <span class="chat-title">{{ chat.receiveUser.userNickname }}</span>
            <span v-if="!chat.showEvaluation" class="last-message">{{ truncateMessage(chat.lastMessage) }}</span>
            <span v-else class="last-message">종료된 채팅방 입니다.</span>
            </div>
          <button class="evaluation-button" v-if="chat.showEvaluation" @click.stop="(event) => evaluateChat(chat.chatSeq, event)">평가</button>
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.6rem;
  cursor: pointer;
  border-bottom: 1px solid #ddd;
  position: relative; /* 마커 위치를 위한 설정 */
}

.chat-item::before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%; /* 동그랗게 만들기 */
  background-color: darkgreen; /* 읽지 않은 메시지가 있는 경우 */
  position: absolute;
  left: -20px; /* 리스트 아이템 왼쪽에 위치 */
  top: 50%;
  transform: translateY(-50%);
}

.chat-item.read::before {
  background-color: darkgray; /* 읽은 메시지일 경우 */
}

.chat-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex: 1;
}
.chat-title {
  font-weight: bold;
}

.chat-details {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.profile-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #ddd;
  margin-right: 10px;
}

.last-message {
  font-size: 0.8em;
  color: rgb(128, 128, 128);
}

.evaluation-button {
  width: 58px;
  height: 40px;
  background-color: #CCB997;
  color: #262627;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 15px;
  font-weight: bold;
  float : right;
  white-space: nowrap;
  margin-top: 13px;
  max-height: 35px;
  max-width: 48px;
}

</style>
