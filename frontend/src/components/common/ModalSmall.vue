<script setup>
import {defineProps, defineEmits} from "vue";

// props 정의
const props = defineProps({
  message: String,  // 모달 내용
  confirmText: {
    type: String,
    default: '확인',
  },
  cancelText: {
    type: String,
    default: '취소'
  },
  isVisible: Boolean, // 모달 표시 여부
  isClosable:{
    type: Boolean,
    default: true,  // 모달을 닫을 수 있는지 여부
  },
});

// emit 정의
const emit = defineEmits(['close', 'confirm']);

const closeModal = () => {
  if (props.isClosable) {
    emit('close');
  }
};

const confirmModal = () => {
  emit('confirm');
};

</script>

<template>
  <!-- 모달이 보일 때만 렌더링 -->
  <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">

    <div class="modal-content">
      <!-- 모달 헤더 -->
      <div class="modal-header">
        <button class="close-button" @click="closeModal" v-if="isClosable">✕</button>
      </div>
      <!-- 모달 메시지 -->
      <div class="modal-body">
        <p>{{ message }}</p>
      </div>

      <!-- 모달 액션 버튼들 -->
      <div class="modal-footer">
        <button class="confirm-button" @click="confirmModal">{{ confirmText }}</button>
        <button class="cancel-button" @click="closeModal">{{ cancelText }}</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

/* 모달 콘텐츠 */
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 400px;
  width: 100%;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.close-button {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

/* 모달 내용 */
.modal-body {
  margin-bottom: 20px;
  text-align: center;
}

.modal-body p {
  color: #757575;
}

/* 모달 푸터 (액션 버튼들) */
.modal-footer {
  display: flex;
  justify-content: space-between;
}

.confirm-button {
  background-color: #000000;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.confirm-button:hover {
  background-color: #CCB997;
}

.cancel-button {
  background-color: rgba(0, 0, 0, 0.39);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.cancel-button:hover {
  background-color: #CCB997;
}
</style>