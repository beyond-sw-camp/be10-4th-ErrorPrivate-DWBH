<script setup>

import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";
import {computed, ref, watch} from "vue";
import ModalSmall from "@/components/common/ModalSmall.vue";
import InputBoxLong from "@/components/common/InputBoxLong.vue";

// 부모에게 데이터 받기
const props = defineProps({
  userData: {
    type: Object,
    required: true
  },
  duplicationNickname: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['nickname', 'cancel', 'save']);

const errorMessage = ref('');
const userPassword = ref('');
const checkNickName = ref(false); // 닉네임 중복 체크 후 통과 시
const beforeNickName = ref(props.userData.userNickname);
const mpti = ["ISTJ", "ISFJ", "INFJ", "INTJ", "ISTP", "ISFP", "INFP", "INTP", "ESTP", "ESFP", "ENFP", "ENTP", "ESTJ", "ESFJ", "ENFJ", "ENTJ"];

// userData를 변경될 때마다 메시지 초기화
watch(props.userData, () => {
  errorMessage.value = '';
}, {deep: true});

// 저장 확인 모달창
const isModalVisible = ref(false);
const isSave = () => {
  isModalVisible.value = true;
}
const confirmModal = async () => {
  isModalVisible.value = false;
  submitForm();
}
const closeModal = () => {
  isModalVisible.value = false;
}

// 닉네임 중복 확인
const checkNickname = async () => {
  if (props.userData.userNickname === '' || props.userData.userNickname == null || props.userData.userNickname === beforeNickName.value) {
    alert('닉네임을 다시 입력해주세요!!');
    return;
  }

  emit('nickname', props.userData.userNickname);

  if(props.duplicationNickname){
    alert('닉네임을 다시 닉네임이 중복됩니다!!');
  } else{
    checkNickName.value = true;
    alert('사용가능한 닉네임입니다!!');
  }
};

// 성별 유효성 검사
const isGenderInvalid = computed(() => {
  const gender = props.userData.userGender.toLowerCase();
  return gender && gender !== 'male' && gender !== 'female';
});

// 폼 제출 핸들러
const submitForm = () => {

  if (!checkNickName.value) {
    errorMessage.value = '닉네임 중복 체크 해주세요!!';
    return;
  }

  // 비밀번호 패턴 검사
  const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*.,?])[A-Za-z\d!@#$%^&*.,?]{8,}$/;
  if (!passwordPattern.test(userPassword.value)) {
    errorMessage.value = '비밀번호는 영문, 특수문자를 포함한 8자 이상이어야 합니다.';
    return;
  }

  // 성별 유효성 검사
  if (isGenderInvalid.value) {
    errorMessage.value = '"male" 또는 "female"을 입력해 주세요';
    return;
  }

  // 폼 제출 로직 (회원 수정)
  handleModify();

};

const handleModify = () => {
  if (!props.userData.userNickname || !userPassword.value
      || !props.userData.userBirthday || !props.userData.userGender || !props.userData.userMbti
  ) {
    errorMessage.value = '빠진 부분이 없는지 확인해주세요!';
  } else {
    errorMessage.value = '';
    emit('save', {
      userNickname: props.userData.userNickname,
      userPassword: userPassword.value,
      userBirthday: props.userData.userBirthday,
      userGender: props.userData.userGender,
      userMbti: props.userData.userMbti,
    })
  }
};
</script>

<template>

  <div class="user-header">
    <span>정보 수정</span>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 이메일 :</span>
    </div>
    <div class="user-value">
      <span>{{ userData.userEmail }}</span>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 닉네임 :</span>
    </div>
    <div class="user-value">
      <InputBoxLong
          class="input-box"
          v-model="userData.userNickname"
          type="text"
          required
      />
      <ButtonSmallColor class="btn" @click="checkNickname">확인</ButtonSmallColor>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 비밀번호 :</span>
    </div>
    <div class="user-value">
      <InputBoxLong
          class="input-box"
          v-model="userPassword"
          placeholder="****"
          type="password"
          required
      />
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 성별 :</span>
    </div>
    <div class="user-value">
      <select class="input-box" v-model="userData.userGender" required>
        <option>male</option>
        <option>female</option>
      </select>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 생년월일 :</span>
    </div>
    <div class="user-value">
      <InputBoxLong
          class="input-box"
          v-model="userData.userBirthday"
          type="date"
          required
      />
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 MBTI :</span>
    </div>
    <div class="user-value">
      <select class="input-box" v-model="userData.userMbti" required>
        <option v-for="(type, index) in mpti" :key="index" :value="type">{{ type }}</option>
      </select>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 온도 :</span>
    </div>
    <div class="user-value">
      <span>{{ userData.userTemperature }} 도</span>
    </div>
  </div>

  <div class="user-field">
    <div class="user-info">
      <span>회원 가입일 :</span>
    </div>
    <div class="user-value">
      <span>{{ userData.regDate.split('T')[0] }}</span>
    </div>
  </div>

  <div class="user-field">
    <!-- 오류 메시지 표시 영역 -->
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>

  <div class="user-delete">
    <ButtonSmallColor class="btn-cancel" @click="$emit('cancel')">취소</ButtonSmallColor>
    <ButtonSmallColor class="btn-confirm" @click="isSave">저장</ButtonSmallColor>
  </div>

  <!-- 저장 확인 모달창 -->
  <ModalSmall :isVisible="isModalVisible" :message="'저장하시겠습니까?'"
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

.btn {
  width: 100px;
  height: 40px;
}

.btn-cancel {
  background-color: rgba(0, 0, 0, 0.39);
  color: white;
  width: 100px;
  height: 40px;
  margin: 10px;
}

.btn-confirm {
  background-color: #000000;
  color: white;
  width: 100px;
  height: 40px;
  margin: 10px;
}


.error {
  color: red;
  font-weight: bold;
}

.input-box{
  width: 100%;
  height: 40px;
}

</style>