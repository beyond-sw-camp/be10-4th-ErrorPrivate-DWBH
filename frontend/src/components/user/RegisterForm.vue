<script setup>
import {computed, ref, watch} from 'vue';
import InputBoxLong from "@/components/common/InputBoxLong.vue";
import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";

const formData = ref({
  userEmail: '',
  userNickname: '',
  userPassword: '',
  userBirthday: '',
  userGender: '',
  userMbti: '',
});

const confirmPassword = ref('');
const errorMessage = ref('');
const verificationCode = ref(''); // 이메일 인증 코드
const isClickNickName = ref(false);
const isButtonDisabled = ref(false) // 이메일 발송 버튼 비활성화 여부
const mpti = ["ISTJ", "ISFJ", "INFJ", "INTJ", "ISTP", "ISFP", "INFP", "INTP", "ESTP", "ESFP", "ENFP", "ENTP", "ESTJ", "ESFJ", "ENFJ", "ENTJ"];

// 비밀번호 일치 여부 확인
const isPasswordMatch = computed(() => formData.value.userPassword === confirmPassword.value && formData.value.userPassword !== '');

const emit = defineEmits(['submit', 'email', 'confirm', 'nickname']);

// 부모에게 errorMessage 받기
const props = defineProps({
  errorMessage: {
    type: String,
    default: ''
  },
  sendEmail: {
    type: Boolean,
    default: false
  },
  emailVerified: {
    type: Boolean,
    default: false
  },
  duplicationEmail: {
    type: Boolean,
    default: false
  },
  duplicationNickname: {
    type: Boolean,
    default: false
  }
});

// formData가 변경될 때마다 메시지 초기화
watch(formData, () => {
  errorMessage.value = '';
}, {deep: true});

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

// 닉네임 중복 확인
const checkNickname = async () => {
  if (formData.value.userNickname === '' || formData.value.userNickname == null) {
    errorMessage.value = '닉네임을 입력해주세요';
    return;
  }

  emit('nickname', formData.value.userNickname);

  isClickNickName.value = true;
};

// 성별 유효성 검사
const isGenderInvalid = computed(() => {
  const gender = formData.value.userGender.toLowerCase();
  return gender && gender !== 'male' && gender !== 'female';
});

// `formData.userNickname`이 변경될 때 `isClickNickName`을 false로 설정
watch(() => formData.value.userNickname, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    isClickNickName.value = false;
  }
});

// 폼 제출 핸들러
const submitForm = () => {

  if (!props.emailVerified) {
    errorMessage.value = '이메일 인증이 필요합니다.';
    return;
  }

  // 비밀번호 패턴 검사
  const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*.,?])[A-Za-z\d!@#$%^&*.,?]{8,}$/;
  if (!passwordPattern.test(formData.value.userPassword)) {
    errorMessage.value = '비밀번호는 영문, 특수문자를 포함한 8자 이상이어야 합니다.';
    return;
  }

  // 성별 유효성 검사
  if (isGenderInvalid.value) {
    errorMessage.value = '"male" 또는 "female"을 입력해 주세요.';
    return;
  }

  // 폼 제출 로직 (회원가입 처리)
  handleRegister();

};

const handleRegister = () => {
  if (!formData.value.userEmail || !formData.value.userNickname ||
      !formData.value.userPassword || !formData.value.userBirthday || !formData.value.userGender || !formData.value.userMbti
  ) {
    errorMessage.value = '빠진 부분이 없는지 확인해주세요!';
  } else {
    errorMessage.value = '';
    emit('submit', {
      userEmail: formData.value.userEmail,
      userNickname: formData.value.userNickname,
      userPassword: formData.value.userPassword,
      userBirthday: formData.value.userBirthday,
      userGender: formData.value.userGender,
      userMbti: formData.value.userMbti,
    })
  }
};

</script>

<template>
  <div class="register-container">

    <div class="header">
      <span>Join</span>
    </div>

    <div class="register-form">
      <!-- 오류 메시지 표시 영역 -->
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

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
          <span v-if="duplicationEmail" class="error">이메일이 중복됩니다.</span>
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
        <span>비밀번호</span>
        <InputBoxLong
            class="input-box"
            v-model="formData.userPassword"
            placeholder="영문=, 특수문자를 모두 포함하여 8자리 이상"
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

      <div class="field-group">
        <!-- 닉네임-->
        <span>닉네임</span>
        <div>
          <InputBoxLong
              class="input-box"
              v-model="formData.userNickname"
              placeholder="예) 길똥"
              type="text"
              required
          />
          <template v-if="isClickNickName">
            <span v-if="duplicationNickname" class="error">닉네임이 중복됩니다.</span>
            <span v-else class="message">사용가능한 닉네임입니다.</span>
          </template>
        </div>
        <ButtonSmallColor class="small-button" @click="checkNickname">확인</ButtonSmallColor>
      </div>


      <div class="field-group">
        <!-- 성별  -->
        <span>성별</span>
        <select class="input-box" v-model="formData.userGender" required>
          <option>male</option>
          <option>female</option>
        </select>
      </div>


      <div class="field-group">
        <!-- 생년월일 필드 -->
        <span>생년월일</span>
        <InputBoxLong
            class="input-box"
            v-model="formData.userBirthday"
            placeholder="ex) 2000-01-01"
            type="date"
            required
        />
      </div>

      <div class="field-group">
        <!-- MBTI 필드 -->
        <span>MBTI</span>
        <select class="input-box" v-model="formData.userMbti" required>
          <option v-for="(type, index) in mpti" :key="index" :value="type">{{ type }}</option>
        </select>
      </div>

      <!-- 회원가입 버튼 -->
      <ButtonSmallColor class="btn" @click="submitForm">
        가입하기
      </ButtonSmallColor>

    </div>
  </div>

</template>

<style scoped>
.register-container {
  align-items: center;
  width: 80%;
  margin: 0 auto;
  padding: 20px;
}

.register-form {
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