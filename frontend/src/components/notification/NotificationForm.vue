<script setup>

// props 정의
const props = defineProps({
  notification: {
    type: Object,
    required: true
  },
});

// emit 정의
const emit = defineEmits(['selectChat']);

const selectChat = (notificationSeq) => {
  emit('selectChat', notificationSeq);
};
</script>

<template>
  <div class="notification-item" @click="selectChat">
    <div class="notification-header">
      <span>{{ new Date(notification.regDate).toLocaleDateString('ko-KR') }}</span>
    </div>
    <div class="notification-body">
      <span>{{ notification.userNickname }}님이 상담 채팅방을(를) 개설하였습니다.</span>
    </div>
  </div>
</template>

<style scoped>
.notification-item {
  border: 1px solid #ddd;
  padding: 16px;
  margin: 8px 0;
  border-radius: 8px;
  background-color: #f9f9f9;
}
.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.notification-body {
  margin-top: 8px;
  font-size: 14px;
}
.close-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
}
</style>