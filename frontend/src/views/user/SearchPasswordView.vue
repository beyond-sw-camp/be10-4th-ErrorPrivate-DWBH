<script setup>
import SearchForm from "@/components/user/SearchForm.vue";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth.js";
import {ref} from "vue";
import axios from "axios";

const router = useRouter();
const authStore = useAuthStore();

const sendEmail = ref(false);
const haveEmail = ref(false);
const emailVerified = ref(false);
const emailToken = ref(null);

// 이메일 체크
const checkDuplicationEmail = async (email) => {
  try {
    if (email) {
      const response = await axios.get(`http://localhost:8089/api/v1/user/email-check/${email}`, {});

      // 이메일이 존재할 경우
      if (response.data === true) {
        haveEmail.value = true;
      } else{
        alert('없는 계정입니다!!');
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
  haveEmail.value = false;
  await checkDuplicationEmail(email);
  if (haveEmail.value)
    await sendVerificationCode(email);
}

// 이메일 인증 코드 확인
const verifyEmailCode = async (email, code) => {
  try {
    if (code === '' || code == null) {
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

const handlePasswordChange = async (formData) => {
  try {
    // 백엔드 서버로 비밀번호 데이터 전달
    const response = await axios.put('http://localhost:8089/api/v1/user/password', {
          userEmail: formData.userEmail,
          userPassword: formData.userPassword,
        }, {
          headers: {
            'Email-Verify-Header': emailToken.value,  // 헤더에 토큰 추가
          },
        }
    );

    alert(`비밀번호 변경 성공!!!`);
    await router.push('/');

  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert(`비밀번호 변경에 실패했습니다. 다시 시도해주세요`);
    } else {
      alert(`비밀번호 변경에 실패했습니다. 다시 시도해주세요`);
    }
  }
};

</script>

<template>
  <div class="search-view">
    <SearchForm @change="handlePasswordChange" @email="checkEmail" @confirm="verifyEmailCode"
                  :sendEmail="sendEmail" :emailVerified="emailVerified" :haveEmail="haveEmail" />
  </div>
</template>

<style scoped>
.search-view {
  background-color: #f4f1ea;
  display: flex;
  align-items: center;
}
</style>