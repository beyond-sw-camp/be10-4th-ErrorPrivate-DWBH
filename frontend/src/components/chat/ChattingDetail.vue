<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const chatTitle = ref('');
const messages = ref([]);
const newMessage = ref('');
const messagesContainer = ref(null);
const websocket = ref(null);
const username = ref('me'); // 사용자 이름 가져와야 함
const isConnected = ref(false);

onMounted(() => {
  const chatId = route.params.chatId;
  chatTitle.value = `채팅방 ${chatId}`;
  connectWebSocket();
});

onBeforeUnmount(() => {
  disconnect();
});

function connectWebSocket() {
  websocket.value = new WebSocket("ws://localhost:8089/ws/chat");

  websocket.value.onopen = () => {
    isConnected.value = true;
    const entryMsg = `${username.value}: 님이 입장하셨습니다.`;
    websocket.value.send(entryMsg);
  };

  websocket.value.onmessage = (event) => {
    const data = event.data;
    const [sessionId, message] = data.split(":");

    const msgClass = sessionId === username.value ? "sent" : "received";
    messages.value.push({
      id: Date.now(),
      sender: sessionId,
      text: message,
      class: msgClass
    });

    if (message && message.includes("나가셨습니다.")) {
      disconnect();
    }

    scrollToBottom();
  };

  websocket.value.onclose = () => {
    isConnected.value = false;
    messages.value.push({
      id: Date.now(),
      sender: username.value,
      text: " 님과의 대화가 종료되었습니다.",
      class: "received"
    });
    console.log("WebSocket is closed.");
  };
}

function sendMessage() {
  if (newMessage.value.trim() && websocket.value && websocket.value.readyState === WebSocket.OPEN) {
    const msg = `${username.value}: ${newMessage.value}`;
    websocket.value.send(msg);
    messages.value.push({
      id: Date.now(),
      sender: username.value,
      text: newMessage.value,
      class: "sent"
    });
    newMessage.value = '';
    scrollToBottom();
  }
}

function disconnect() {
  isConnected.value = false;
  if (websocket.value && websocket.value.readyState === WebSocket.OPEN) {
    const exitMsg = `${username.value}: 님이 방을 나가셨습니다.`;
    websocket.value.send(exitMsg);
    websocket.value.close();
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
    <button class="back-button" @click="$emit('goBack')"> <</button>
    <button id="disconn" @click="disconnect">Disconnect</button>

    <h2>{{ chatTitle }}</h2>
    <div class="messages" ref="messagesContainer">
      <div
          v-for="message in messages"
          :key="message.id"
          :class="['message', message.class]"
      >
        <span class="sender">{{ message.sender }}</span> <br/>
        <span class="text">{{ message.text }}</span>
      </div>
    </div>
    <div class="input-area">
      <input
          v-model="newMessage"
          @keydown.enter="sendMessage"
          placeholder="메시지를 입력하세요"
          :disabled="!isConnected"
      />
      <button @click="sendMessage" :disabled="!isConnected">전송</button>
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
  color: blue;
}

.message.received {
  text-align: left;
  color: green;
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
