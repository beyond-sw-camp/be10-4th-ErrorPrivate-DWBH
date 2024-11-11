<script setup>
import {useRouter} from "vue-router";
import {ref} from "vue";
import axios from 'axios';
import {useAuthStore} from "@/stores/auth.js";
import LoginForm from "@/components/user/LoginForm.vue";

const router = useRouter();
const errorMessage = ref('');

const authStore = useAuthStore();

const handleLoginSubmit = async (email, password) => {
  try {
    const response = await axios.post('http://localhost:8089/api/v1/login', {
      email: email,
      password: password
    },{
      withCredentials: true
    });

    if (response.status === 200) {
      // 헤더에서 토큰 추출
      authStore.login(response.headers.token);

      router.push('/'); // 로그인 성공 시 홈 화면으로 이동

    }
  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert(`로그인 정보가 올바르지 않습니다.`);
      location.reload();  // 새로고침
    } else {
      alert(`로그인 정보가 올바르지 않습니다.`);
    }
  }
};

</script>

<template>
  <div class="login-view">
    <LoginForm @submit="handleLoginSubmit" :errorMessage="errorMessage"/>
  </div>
</template>

<style scoped>

.login-view {
  display: flex;
  align-items: center;
}
</style>