<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';
import { Stomp } from '@stomp/stompjs';

const props = defineProps({
  chat: Object
});
const chatTitle = ref('');
const messages = ref([]);
const newMessage = ref('');
const messagesContainer = ref(null);
const stompClient = ref(null);
const sendUsername = ref(props.chat.sendUser.userNickname);
const receiveUsername = ref(props.chat.receiveUser.userNickname);
const isConnected = ref(false);

onMounted(() => {
  const chatId = props.chat.chatSeq;
  chatTitle.value = `채팅방 ${chatId}`;
  connectWebSocket();
});

onBeforeUnmount(() => {
  disconnect();
});

function connectWebSocket() {
  const websocket = new WebSocket("ws://localhost:8089/stomp/chat");
  stompClient.value = Stomp.over(websocket);

  stompClient.value.connect({}, (frame) => {
    isConnected.value = true;
    console.log("STOMP Connection established");

    // 메시지 수신
    stompClient.value.subscribe(`/sub/chat/room/${props.chat.chatSeq}`, (message) => {
      const content = JSON.parse(message.body);
      const msgClass = content.writer === sendUsername.value ? "sent" : "received";

      messages.value.push({
        id: Date.now(),
        sender: content.writer,
        text: content.message,
        class: msgClass
      });

      if (content.message.includes("나가셨습니다.")) {
        disconnect();
      }

      scrollToBottom();
    });

    // 방에 입장 메시지 전송
    stompClient.value.send('/pub/chat/enter', {}, JSON.stringify({
      roomId: props.chat.chatSeq,
      writer: sendUsername.value
    }));

  });

  stompClient.value.onclose = () => {
    isConnected.value = false;
    messages.value.push({
      id: Date.now(),
      sender: 'System',
      text: " 님과의 대화가 종료되었습니다.",
      class: "received"
    });
    console.log("WebSocket is closed.");
  };
}


function sendMessage() {
  if (newMessage.value.trim() && stompClient.value && stompClient.value.connected) {
    stompClient.value.send('/pub/chat/message', {}, JSON.stringify({
      chatRoomSeq: props.chat.chatSeq,
      message: newMessage.value,
      writer: sendUsername.value
    }));
    /*messages.value.push({
      id: Date.now(),
      sender: sendUsername.value,
      text: newMessage.value,
      class: "sent"
    });*/
    newMessage.value = '';
    scrollToBottom();
  }
}

function disconnect() {
  if (stompClient.value && stompClient.value.connected) {
    const exitMsg = `${sendUsername.value}: 님이 방을 나가셨습니다.`;
    stompClient.value.send('/pub/chat/message', {}, JSON.stringify({
      roomId: props.chat.chatSeq,
      message: exitMsg,
      writer: sendUsername.value
    }));
    stompClient.value.disconnect();
    isConnected.value = false;
    console.log("Disconnected from STOMP server");
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
    <button id="disconn" @click="disconnect">종료하기</button>

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
