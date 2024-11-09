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

const handleRegisterSubmit = async (formData) => {
  // 백엔드 요청 보낼 때 로딩 시작
  LoadingState.value = true;
  try {
    // 백엔드 서버로 회원가입 데이터 전달
    const response = await axios.post('http://localhost:8089/user', {
      userEmail: formData.userEmail,
      userNickname: formData.userNickname,
      userPassword: formData.userPassword,
      userBirthday: formData.userBirthday,
      userGender: formData.userGender,
      userMbti: formData.userMbti,
    });

    if (response.status === 201) {
      console.log('회원가입 성공');
      // 회원가입 성공 시 Pinia 스토어에 해당 이메일 저장
      authStore.registerEmail(formData.userEmail);

      router.push('/users/emails/codes');
    }
  } catch(error) {

    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = '다시 시도해주세요';
      console.log('회원가입 실패: ', error);
    }
  } finally {
    LoadingState.value = false;  // 로딩 종료
  }
};
</script>

<template>
  <div class="register-view">
    <div v-if="LoadingState" class="loading-spinner">잠시만 기다려 주세요...</div>
    <RegisterForm v-else @submit="handleRegisterSubmit" :errorMessage="errorMessage"/>
    <RouterView />
  </div>
</template>

<style scoped>
.register-view {
  margin-top: 40px;  /* 폼 위쪽 여백 추가 */
  margin-bottom: 100px; /* 폼 아래쪽 여백 추가 */
  display: flex;
  justify-content: center; /* 화면 가운데에 폼을 정렬 */
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