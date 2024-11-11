<script setup>

import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";
import {computed, ref} from "vue";
import ModalSmall from "@/components/common/ModalSmall.vue";

// 부모에게 데이터 받기
const props = defineProps({
  userData: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['update', 'delete']);

// 나이 계산
const calculateAge = computed(() => {
  const today = new Date();
  const birthDate = new Date(props.userData.userBirthday);

  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDifference = today.getMonth() - birthDate.getMonth();

  if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
    age--;
  }

  return age;
});

// 삭제 확인 모달창
const isModalVisible = ref(false);
const isDelete = () => {
  isModalVisible.value = true;
}
const confirmModal = async () => {
  isModalVisible.value = false;

  emit('delete');
}
const closeModal = () => {
  isModalVisible.value = false;
}

</script>

<template>

  <div class="user-header">
    <span>내 정보</span>
    <ButtonSmallColor @click="$emit('update')">정보 수정</ButtonSmallColor>
  </div>

  <div class="user-img">
    <img src="@/images/profile-image.jpg" alt="사진 없음">
  </div>
  <div class="user-age">
    <span>{{ calculateAge }} 세</span>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>닉네임 :</span>
    </div>
    <div class="user-value">
      <span>{{ userData.userNickname }}</span>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>MBTI :</span>
    </div>
    <div class="user-value">
      <span>{{ userData.userMbti }}</span>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>성별 :</span>
    </div>
    <div class="user-value">
      <span>{{ userData.userGender }}</span>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>매너온도 :</span>
    </div>
    <div class="user-value">
      <b-progress :max="100" :precision="1" :animated="true" show-value class="temperature">
        <b-progress-bar :value="userData.userTemperature" variant="danger">
          <span><strong>{{ userData.userTemperature }} 도</strong></span>
        </b-progress-bar>
      </b-progress>
    </div>
  </div>

  <div class="user-delete">
    <ButtonSmallColor class="btn-delete" @click="isDelete">회원삭제</ButtonSmallColor>
  </div>

  <!-- 삭제 확인 모달창 -->
  <ModalSmall :isVisible="isModalVisible" :message="'정말로 탈퇴하시겠습니까?'"
              @close="closeModal" @confirm="confirmModal"/>

</template>

<style scoped>
.user-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  font-size: 20px;
  font-weight: bold;
}

.user-field {
  display: flex;
  flex-direction: row;
  width: 100%;
  margin-bottom: 10px;
  justify-content: space-around;
  align-items: center;
}

.user-img {
  display: flex;
  justify-content: center;
}

.user-img img {
  width: 50%;
}

.user-age {
  display: flex;
  width: 80%;
  justify-content: end;
  margin-bottom: 10px;
}

.user-info {
  display: flex;
  justify-content: center;
  width: 50%;
  padding: 10px;
}

.user-value {
  display: flex;
  width: 50%;
  padding: 10px;
}

.user-delete {
  display: flex;
  justify-content: end;
  margin-top: 20px;
  padding: 10px;
}

.btn-delete {
  width: 100px;
  height: 40px;
}

.temperature {
  display: flex;
  width: 100%;
  margin: 0 auto;
}

</style>