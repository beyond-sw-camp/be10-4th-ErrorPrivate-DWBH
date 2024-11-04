<script setup>
import { ref } from 'vue';
import ChatList from '@/components/chat/ChattingList.vue';
import ChatDetail from '@/components/chat/ChattingDetail.vue';

const isModalOpen = ref(false);
const isDetailOpen = ref(false);
const selectedChat = ref(null); // 선택한 채팅방 정보 저장

function openModal() {
  isModalOpen.value = true;
  isDetailOpen.value = false;
}

function closeModal() {
  isModalOpen.value = false;
  selectedChat.value = null;
}

function openChatDetail(chat) {
  selectedChat.value = chat;
  isDetailOpen.value = true;
}

function goBackToList() {
  isDetailOpen.value = false;
}

</script>

<template>
  <div class="chat-list-view">
    <!-- 모달 열기 버튼 -->
    <button @click="openModal">채팅 목록 열기</button>

    <!-- 채팅방 모달 -->
    <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <button class="close-button" @click="closeModal">X</button>

        <!-- 목록 또는 상세 화면 표시 -->
        <template v-if="!isDetailOpen">
          <!-- 채팅 목록 화면 -->
          <ChatList @selectChat="openChatDetail" />
        </template>
        <template v-else>
          <!-- 대화 상세 화면 -->
          <ChatDetail :chat="selectedChat" @goBack="goBackToList" />
        </template>
      </div>
    </div>
  </div>

</template>

<style scoped>
.chat-list-view {
  padding: 1rem;
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
}

.modal-content {
  background-color: white;
  padding: 0;
  border-radius: 5px; /* 모서리를 둥글게 */
  width: 320px; /* 좁고 길쭉한 모양 */
  height: 600px; /* 카톡처럼 길게 */
  position: relative;
  margin-top: 2rem;
  margin-right: 2rem;
  overflow-y: auto; /* 내용이 넘칠 때 스크롤 */
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: none;
  font-size: 1.2rem;
  cursor: pointer;
}

</style>
