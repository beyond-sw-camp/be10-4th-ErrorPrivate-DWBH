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

const emit = defineEmits(['submit', 'email', 'confirm']);

// 부모에게 errorMessage 받기
const props = defineProps({
  errorMessage: {
    type: String,
    default: ''
  },
  emailVerified: {
    type: Boolean,
    default: false
  }
});


const verificationCode = ref(''); // 이메일 인증 코드
// 비밀번호 일치 여부 확인
const isPasswordMatch = computed(() => formData.value.userPassword === confirmPassword.value);

// formData가 변경될 때마다 메시지 초기화
watch(formData, () => {
  errorMessage.value = '';
}, {deep: true});

// 이메일 인증 코드 전송
const sendVerificationCode = async () => {
  if (formData.value.userEmail) {

    const response = await axios.post('https://matzipapi.huichan.kr/user/api/v1/auth/mail-verification', {
      userEmail: formData.value.userEmail,
      userName: formData.value.userName,
    });
    console.log(response);

    emit('email', {
      userEmail: formData.value.userEmail,
    })

  } else {
    errorMessage.value = '이메일을 모두 입력해 주세요.';
  }
};

// 이메일 인증 코드 확인
const verifyEmailCode = async () => {
  if (verificationCode.value === '' || verificationCode.value == null) {
    errorMessage.value = '인증코드를 입력해 주세요';
    return;
  }

  const response = await axios.post('https://matzipapi.huichan.kr/user/api/v1/auth/chkEmailCode', {
    userEmail: formData.value.userEmail,
    verificationCode: verificationCode.value,
  });
  console.log(response);

  emit('confirm', {
    userEmail: formData.value.userEmail,
    verificationCode: verificationCode.value,
  })
};

// 성별 유효성 검사
const isGenderInvalid = computed(() => {
  const gender = formData.value.userGender.toLowerCase();
  return gender && gender !== 'male' && gender !== 'female';
});


// 폼 제출 핸들러
const submitForm = () => {
  if (!props.emailVerified) {
    errorMessage.value = '이메일 인증이 필요합니다.';
    return;
  }

  // 비밀번호 패턴 검사
  const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*.,?])[A-Za-z\d!@#$%^&*.,?]{10,}$/;
  if (!passwordPattern.test(formData.value.userPassword)) {
    errorMessage.value = '비밀번호는 영문, 숫자, 특수문자를 포함한 10자 이상이어야 합니다.';
    return;
  }

  // 비밀번호 확인
  if (formData.value.userPassword !== confirmPassword) {
    errorMessage.value = '비밀번호와 비밀번호 확인이 일치하지 않습니다.';
    return;
  }

  // 성별 유효성 검사
  if(isGenderInvalid){
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
  <div class="register-form">
    <h2>JOIN</h2>

    <!-- 이메일 필드 -->
    <div class="field-group">
      <p>이메일</p>
      <InputBoxLong
          v-model="formData.userEmail"
          placeholder="example@gmail.com"
          type="email"
          required
      />
      <ButtonSmallColor class="small-button" @click="sendVerificationCode">인증</ButtonSmallColor>
    </div>

    <!-- 이메일 인증 필드 -->
    <div class="field-group">
      <p>이메일 인증</p>
      <InputBoxLong
          v-model="verificationCode"
          placeholder="인증 코드를 입력하세요"
          type="text"
          required
      />
      <ButtonSmallColor class="small-button" @click="verifyEmailCode">확인</ButtonSmallColor>
      <p v-if="props.emailVerified" class="message">이메일 인증이 완료되었습니다.</p>
    </div>

    <div class="field-group">
      <!-- 비밀번호 필드 -->
      <p>비밀번호</p>
      <InputBoxLong
          v-model="formData.userPassword"
          placeholder="영문대소문자, 특수문자를 모두 포함하여 8자리 이상"
          type="password"
          required
      />
    </div>

    <div class="field-group">
      <!-- 비밀번호 필드 -->
      <p>비밀번호 확인</p>
      <InputBoxLong
          v-model="confirmPassword"
          placeholder="영문대소문자, 특수문자를 모두 포함하여 8자리 이상"
          type="password"
          required
      />
      <p v-if="!isPasswordMatch && confirmPassword">비밀번호가 일치하지 않습니다.</p>
      <p v-else>비밀번호가 일치합니다.</p>
    </div>


    <div class="field-group">
      <!-- 닉네임-->
      <p>닉네임</p>
      <InputBoxLong
          v-model="formData.userNickname"
          placeholder="예) 길똥"
          type="text"
          required
      />
    </div>


    <div class="field-group">
      <!-- 성별  -->
      <p>성별</p>
      <InputBoxLong
          v-model="formData.userGender"
          placeholder="male or female"
          type="text"
          required
      />
    </div>


    <div class="field-group">
      <!-- 생년월일 필드 -->
      <p>생년월일</p>
      <InputBoxLong
          v-model="formData.userBirthday"
          placeholder="ex) 2000-01-01"
          type="date"
          required
      />
    </div>

    <div class="field-group">
      <!-- MBTI 필드 -->
      <p>MBTI</p>
      <InputBoxLong
          v-model="formData.userMbti"
          placeholder="MBTI를 입력해주세요"
          type="text"
          required
      />
    </div>

    <!-- 오류 메시지 표시 영역 -->
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

    <!-- 회원가입 버튼 -->
    <ButtonSmallColor class="btn" @click="submitForm">
      가입하기
    </ButtonSmallColor>

  </div>
</template>

<style scoped>
.register-form {
  align-items: center;
  width: 500px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.field-group {
  width: 80%;
}

.field-group > * {
  margin-bottom: 3px;
}

.small-button {
  width: 60px;
  height: 30px;
}

.btn {
  margin-top: 20px;
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