<script setup>
import { ref, onMounted, nextTick  } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const chatTitle = ref('');
const messages = ref([]);
const newMessage = ref('');
const messagesContainer = ref(null);

onMounted(() => {
  const chatId = route.params.chatId;
  chatTitle.value = `채팅방 ${chatId}`;
  messages.value = [
    { id: 1, sender: 'me', text: '안녕하세요!' },
    { id: 2, sender: 'other', text: '안녕하세요, 반가워요!' },
  ];
  scrollToBottom();
});

function sendMessage() {
  if (newMessage.value.trim()) {
    messages.value.push({
      id: Date.now(),
      sender: 'me',
      text: newMessage.value,
    });
    newMessage.value = '';
    scrollToBottom();
  }
}

function scrollToBottom() {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
}

</script>

<template>
  <div class="chat-room">
    <button class="back-button" @click="$emit('goBack')"> < </button>

    <h2>{{ chatTitle }}</h2>
    <div class="messages">
      <div
          v-for="message in messages"
          :key="message.id"
          :class="['message', message.sender === 'me' ? 'sent' : 'received']"
      >
        <span class="sender">{{ message.sender }}</span>
        <span class="text">{{ message.text }}</span>
      </div>
    </div>
    <div class="input-area">
      <input
          v-model="newMessage"
          @keydown.enter="sendMessage"
          placeholder="메시지를 입력하세요"
      />
      <button @click="sendMessage">전송</button>
    </div>
  </div>
</template>

<style scoped>
.chat-room {
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem;
}
.messages {
  height: 300px;
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 1rem;
  margin-bottom: 1rem;
}
.message {
  margin-bottom: 0.5rem;
}
.message.sent {
  text-align: right;
}
.sender {
  font-weight: bold;
}
.input-area {
  display: flex;
  gap: 0.5rem;
}
input {
  flex: 1;
  padding: 0.5rem;
}
button {
  padding: 0.5rem 1rem;
}
</style>
