<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';
import { Stomp } from '@stomp/stompjs';
import { v4 as uuidv4 } from 'uuid';
import dayjs from 'dayjs';

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

onMounted(async () => {
  const chatId = props.chat.chatSeq;
  chatTitle.value = `채팅방 ${chatId}`;

  await loadChatHistory(chatId);

  connectWebSocket();
});

async function loadChatHistory(chatId) {
  try {
    const response = await fetch(`http://localhost:8089/api/v1/user/chat/message/${chatId}`);
    const data = await response.json();
    messages.value = data.map((message) => ({
      chatMessageSeq: message.chatMessageSeq,
      chatRoomSeq: message.chatRoomSeq,
      senderNickName: sendUsername,
      sendSeq: props.chat.sendUser.userSeq,
      receiveSeq: props.chat.receiveUser.userSeq,
      text: message.message,
      type: message.type == "ENTER" ? "ENTER" : message.senderNickName == sendUsername ? "sent" : "received", // ENTER 구분 추가
      regDate: message.regDate,
      readYn: message.readYn
    }));
    scrollToBottom();
  } catch (error) {
    console.error("Failed to load chat history:", error);
  }
}

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
    stompClient.value.subscribe(`/sub/chat/talk/${props.chat.chatSeq}`, (message) => {
      const content = JSON.parse(message.body);
      let msgType;
      if (content.message.includes("입장")) {
        msgType = "ENTER";
      } else {
        msgType = content.writer === sendUsername ? "sent" : "received";
      }

      messages.value.push({
        chatMessageSeq: uuidv4(),
        chatRoomSeq: props.chat.chatSeq,
        senderNickName: sendUsername,
        sendSeq: props.chat.sendUser.userSeq,
        receiveSeq: props.chat.receiveUser.userSeq,
        text: content.message,
        type: msgType,
        regDate: new Date().toLocaleTimeString(),
        readYn: "N"
      });

      if (content.message.includes("나가셨습니다.")) {
        disconnect();
      }

      scrollToBottom();
    });

    // 방에 입장 메시지 전송
    stompClient.value.send(`/pub/chat/enter/${props.chat.chatSeq}`, {}, JSON.stringify({
      chatMessageSeq: uuidv4(),
      chatRoomSeq: props.chat.chatSeq,
      writer: receiveUsername
    }));

  });

  stompClient.value.onclose = () => {
    isConnected.value = false;
    messages.value.push({
      chatMessageSeq: uuidv4(),
      chatRoomSeq: props.chat.chatSeq,
      sender: sendUsername,
      message: " 님과의 대화가 종료되었습니다.",
      type: "exit"
    });
    console.log("WebSocket is closed.");
  };
}


function sendMessage() {
  if (newMessage.value.trim() && stompClient.value && stompClient.value.connected) {
    stompClient.value.send(`/pub/chat/talk/${props.chat.chatSeq}`, {}, JSON.stringify({
      roomId: props.chat.chatSeq,
      message: newMessage.value,
      writer: sendUsername
    }));

    messages.value.push({
      chatMessageSeq: uuidv4(),
      chatRoomSeq: props.chat.chatSeq,
      senderNickName: sendUsername,
      sendSeq: props.chat.sendUser.userSeq,
      receiveSeq: props.chat.receiveUser.userSeq,
      text: newMessage.value,
      type: "sent",
      regDate: new Date().toLocaleTimeString(),
      readYn: "N"
    });
    newMessage.value = '';
    scrollToBottom();
  }

}

function disconnect() {
  if (stompClient.value && stompClient.value.connected) {
    stompClient.value.send(`/pub/chat/exit/${props.chat.chatSeq}`, {}, JSON.stringify({
      roomId: props.chat.chatSeq,
      message: `${sendUsername}: 님이 방을 나가셨습니다.`,
      writer: sendUsername
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

function formatDate(regDate) {
  return dayjs(regDate).format('YYYY-MM-DD HH:mm'); // 원하는 형식으로 변환
}

</script>

<template>
  <div class="chat-room">
    <button class="chat-room-button back" @click="$emit('goBack')"> < </button>
    <button class="chat-room-button disconn" id="disconn" @click="disconnect"> {{ chat.showEvaluation ? '평가하기' : '종료하기' }}</button>
    <hr style="border: 1px solid #262627; margin: 10px 0;" />

    <div class="messages" ref="messagesContainer">
      <div
          v-for="message in messages"
          :key="message.chatMessageSeq"
          :class="['message', message.type]"
      >
        <span class="sender">{{ message.sender }}</span> <br/>
        <span class="text">{{ message.text }}</span>
      </div>
    </div>
    <div class="input-area">
      <input class="sendMessage-area"
          v-model="newMessage"
          @keydown.enter="sendMessage"
          placeholder="메시지를 입력하세요"
          :disabled="!isConnected"
      />
      <button class="chat-room-button submit-btn" @click="sendMessage" :disabled="!isConnected"> 전송</button>
    </div>
  </div>
</template>

<style scoped>

.chat-room-button.back {
  all: unset;
  height: 38px;
  cursor: pointer;
  font-size: 20px;
  font-weight: bold;
}

.chat-room-button.disconn {
  width: 85px;
  height: 38px;
  background-color: #262627;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 15px;
  font-weight: bold;
  float : right;
}

.sendMessage-area {
  height: 40px;
  border-style: solid;
  border-radius: 5px;
}
.chat-room-button.submit-btn {
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
}

.chat-room {
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem;
}

.messages {
  margin-top: 10px;
  height: 415px;
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 1rem;
  margin-bottom: 1rem;
}

.message {
  margin-bottom: 0.5rem;
}

/* 입장 메시지 스타일 (가운데 정렬) */
.message.ENTER {
  align-self: center;
  text-align: center;
  color: #333333;
  font-size: 0.9rem;
}

.message.sent {
  align-self: flex-start;
  text-align: right;
  color: black;
}

.message.received {
  align-self: flex-end;
  text-align: left;
  color: black;
}

/* 공통 닉네임 스타일 */
.sender {
  font-weight: bold;
  margin-bottom: 0.2rem;
}

/* 보낸 사람 닉네임 스타일 */
.sender-sent {
  font-weight: bold;
  color: blue; /* 보낸 사람은 파란색 */
  text-decoration: underline
}

/* 받은 사람 닉네임 스타일 */
.sender-received {
  font-weight: bold;
  color: green; /* 받은 사람은 초록색 */
  text-decoration: underline;
}

/* 날짜 스타일 */
.date {
  font-size: 0.8rem;
  color: #333333;
  margin-top: 0.2rem;
}

.input-area {
  display: flex;
  gap: 0.5rem;
}

input {
  flex: 1;
  padding: 0.5rem;
}

</style>
