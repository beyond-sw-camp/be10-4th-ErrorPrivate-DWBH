<script setup>
import axios from "axios";
import {useRouter} from "vue-router";
import {ref} from "vue";
import {useAuthStore} from "@/stores/auth.js";
import RegisterForm from "@/components/user/RegisterForm.vue";

const router = useRouter();
const LoadingState = ref(false);
const authStore = useAuthStore();

const errorMessage = ref('');
const sendEmail = ref(false);
const duplicationEmail = ref(false);
const duplicationNickname = ref(false);
const emailVerified = ref(false);
const emailToken = ref(null);

// 이메일 중복 체크
const checkDuplicationEmail = async (email) => {
  try {
    if (email) {
      const response = await axios.get(`http://localhost:8089/api/v1/user/email-check/${email}`, {});

      // 이메일 중복일 경우
      if (response.data === true) {
        duplicationEmail.value = true;
      }

    }
  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert('정확한 이메일을 입력해주세요');
    } else {
      alert('정확한 이메일을 입력해주세요');
    }
  }
};

// 이메일 인증 코드 전송
const sendVerificationCode = async (email) => {
  try {
    if (email) {
      const response = await axios.post('http://localhost:8089/api/v1/user/email', {
        email: email,
      });
      // 인증 코드 전송 성공
      sendEmail.value = true;
    }
  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert('정확한 이메일을 입력해주세요');
    } else {
      alert('정확한 이메일을 입력해주세요');
    }
  }
};

const checkEmail = async (email) => {
  await checkDuplicationEmail(email);
  if (!duplicationEmail.value)
    await sendVerificationCode(email);
}

// 이메일 인증 코드 확인
const verifyEmailCode = async (email, code) => {
  try {
    if (code === '' || code == null) {
      errorMessage.value = '인증코드를 입력해 주세요';
      return;
    }

    const response = await axios.get('http://localhost:8089/api/v1/user/email', {
      params: {email, code},
      withCredentials: true,  // 쿠키와 인증 정보를 포함하여 요청
    });

    // 응답에서 'verifyToken' 헤더를 추출
    emailToken.value = response.headers['verifytoken'] || response.headers['verifyToken'];

    // 이메일 인증 성공
    emailVerified.value = true;
  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert('인증 실패');
    } else {
      alert('인증 실패');
    }
  }
};

// 닉네임 중복 체크
const checkDuplicationNickname = async (nickname) => {
  try {
    if (nickname) {
      const response = await axios.get(`http://localhost:8089/api/v1/user/nickname-check/${nickname}`, {});

      // 이메일 중복일 경우
      if (response.data === true) {
        duplicationNickname.value = true;
      }

    }
  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert('정확한 닉네임을 입력해주세요');
    } else {
      alert('정확한 닉네임을 입력해주세요');
    }
  }
};

const handleRegisterSubmit = async (formData) => {
  // 백엔드 요청 보낼 때 로딩 시작
  LoadingState.value = true;
  try {
    // 백엔드 서버로 회원가입 데이터 전달
    const response = await axios.post('http://localhost:8089/api/v1/user', {
          userEmail: formData.userEmail,
          userNickname: formData.userNickname,
          userPassword: formData.userPassword,
          userBirthday: formData.userBirthday,
          userGender: formData.userGender,
          userMbti: formData.userMbti,
        }, {
          headers: {
            'Email-Verify-Header': emailToken.value,  // 헤더에 토큰 추가
          },
        }
    );

    if (response.status === 201) {
      // 회원가입 성공 시 Pinia 스토어에 해당 이메일 저장
      authStore.registerEmail(formData.userEmail);

      await router.push('/');
    }
  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert(`회원가입에 실패했습니다. 다시 시도해주세요`);
    } else {
      alert(`회원가입에 실패했습니다. 다시 시도해주세요`);
    }
  } finally {
    LoadingState.value = false;  // 로딩 종료
  }
};
</script>

<template>
  <div class="register-view">
    <div v-if="LoadingState" class="loading-spinner">잠시만 기다려 주세요...</div>
    <RegisterForm v-else @submit="handleRegisterSubmit" @email="checkEmail" @confirm="verifyEmailCode"
                  @nickname="checkDuplicationNickname"
                  :errorMessage="errorMessage" :sendEmail="sendEmail" :emailVerified="emailVerified"
                  :duplicationEmail="duplicationEmail" :duplicationNickname="duplicationNickname"/>
  </div>
</template>

<style scoped>
.register-view {
  display: flex;
  align-items: center;
}

.loading-spinner {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #666;
}

</style>