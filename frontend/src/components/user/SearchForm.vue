<script setup>

import InputBoxLong from "@/components/common/InputBoxLong.vue";
import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";
import {computed, ref, watch} from "vue";

const formData = ref({
  userEmail: '',
  userPassword: '',
});

const errorMessage = ref('');
const confirmPassword = ref('');
const verificationCode = ref(''); // 이메일 인증 코드
const isButtonDisabled = ref(false) // 이메일 발송 버튼 비활성화 여부

// 비밀번호 일치 여부 확인
const isPasswordMatch = computed(() => formData.value.userPassword === confirmPassword.value && formData.value.userPassword !== '');

const emit = defineEmits(['change', 'email', 'confirm']);

const props = defineProps({
  sendEmail: {
    type: Boolean,
    default: false
  },
  emailVerified: {
    type: Boolean,
    default: false
  },
  haveEmail: {
    type: Boolean,
    default: false
  },
});

// 이메일 인증 코드 전송
const sendVerificationCode = async () => {
  isButtonDisabled.value = true; // 버튼 비활성화

  // 5초 후에 버튼 활성화
  setTimeout(() => {
    isButtonDisabled.value = false;
  }, 5000);

  if (formData.value.userEmail) {
    emit('email', formData.value.userEmail);
  } else {
    errorMessage.value = '이메일을 모두 입력해 주세요.';
  }
};

// 이메일 인증 코드 확인
const verifyEmailCode = async () => {
  if (verificationCode.value === '' || verificationCode.value == null) {
    errorMessage.value = '인증 코드 입력해주세요';
    return;
  }

  emit('confirm',
      formData.value.userEmail,
      verificationCode.value,
  );
};

// 비밀 번호 변경 핸들러
const changePassword = () => {

  // 비밀번호 패턴 검사
  const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*.,?])[A-Za-z\d!@#$%^&*.,?]{8,}$/;
  if (!passwordPattern.test(formData.value.userPassword)) {
    errorMessage.value = '비밀번호는 영문, 특수문자를 포함한 8자 이상이어야 합니다.';
    return;
  }

  if (!formData.value.userEmail || !formData.value.userPassword) {
    errorMessage.value = '빠진 부분이 없는지 확인해주세요!';
  } else {
    errorMessage.value = '';
    emit('change', {
      userEmail: formData.value.userEmail,
      userPassword: formData.value.userPassword,
    })
  }

};

</script>

<template>
  <div class="search-container">

    <div class="header">
      <span>패스워드 찾기</span>
    </div>

    <div class="search-form">

      <!-- 이메일 필드 -->
      <div class="field-group">
        <span>이메일</span>
        <div>
          <InputBoxLong
              v-model="formData.userEmail"
              placeholder="example@gmail.com"
              type="email"
              required
          />
          <span v-if="sendEmail">인증 메일이 발송되었습니다.</span>
          <span v-if="!haveEmail" class="error">이메일을 확인해주세요.</span>
        </div>
        <ButtonSmallColor class="small-button" @click="sendVerificationCode" :disabled="isButtonDisabled" >발송</ButtonSmallColor>
      </div>

      <!-- 이메일 인증 필드 -->
      <div class="field-group">
        <span>이메일 인증</span>
        <div>
          <InputBoxLong
              class="input-box"
              v-model="verificationCode"
              placeholder="인증 코드를 입력하세요"
              type="text"
              required
          />
          <span v-if="props.emailVerified" class="message">이메일 인증이 완료되었습니다.</span>
        </div>
        <ButtonSmallColor class="small-button" @click="verifyEmailCode">확인</ButtonSmallColor>
      </div>

      <div class="field-group">
        <!-- 비밀번호 필드 -->
        <span>신규 비밀번호</span>
        <InputBoxLong
            class="input-box"
            v-model="formData.userPassword"
            placeholder="영문, 특수문자를 모두 포함하여 8자리 이상"
            type="password"
            required
        />
      </div>

      <div class="field-group">
        <!-- 비밀번호 필드 -->
        <span>비밀번호 확인</span>
        <div>
          <InputBoxLong
              class="input-box"
              v-model="confirmPassword"
              placeholder="영문, 특수문자를 모두 포함하여 8자리 이상"
              type="password"
              required
          />
          <span v-if="!isPasswordMatch" class="error">비밀번호가 일치하지 않습니다.</span>
          <span v-else class="message">비밀번호가 일치합니다.</span>
        </div>
      </div>

      <!-- 변경 버튼 -->
      <ButtonSmallColor class="btn" @click="changePassword">
        변경
      </ButtonSmallColor>

    </div>
  </div>
</template>

<style scoped>
.search-container {
  align-items: center;
  width: 80%;
  margin: 0 auto;
  padding: 20px;
}

.search-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.header {
  display: flex;
  align-items: center;
  padding: 20px;
  font-size: 30px;
  font-weight: bold;
}

.field-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 20px;
  width: 70%;
}

.field-group > span {
  width: 130px;
}

.input-box {
  width: 500px;
  height: 40px;
}

.small-button {
  width: 60px;
  height: 30px;
  margin-left: 20px;
}

.error {
  color: red;
  font-weight: bold;
}

.message {
  color: green;
  font-weight: bold;
}

</style>