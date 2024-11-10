<script setup>
import {ref} from 'vue';
import InputBoxLong from "@/components/common/InputBoxLong.vue";
import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";

const email = ref('');
const pwd = ref('');
const errorMessage = ref('');

const emit = defineEmits(['submit']);

// 부모에게 errorMessage 받기
const props = defineProps({
  errorMessage: {
    type: String,
    default: ''
  }
});

const handleLogin = () => {
  if (!email.value || !pwd.value) {
    errorMessage.value = '이메일과 비밀번호를 입력하세요.';
  } else {
    errorMessage.value = '';
    emit('submit', email.value, pwd.value);
  }
};


</script>

<template>
  <div class="login-container">

    <div class="header">
      <span>Login</span>
    </div>

    <div class="login-form">

      <!-- 에러 메세지 표시 -->
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

      <div class="field-group">
        <!-- 이메일 필드 -->
        <span>이메일</span>
        <InputBoxLong
            v-model="email"
            placeholder="example.gmail.com"
            type="email"
        />
      </div>

      <!-- 비밀번호 필드 -->
      <div class="field-group">
        <span>비밀번호</span>
        <InputBoxLong
            v-model="pwd"
            placeholder="영문, 특수문자를 모두 포함하여 8자리 이상"
            type="password"
        />
      </div>

      <!-- 로그인 버튼 -->
      <ButtonSmallColor class="btn" @click="handleLogin">
        로그인하기
      </ButtonSmallColor>

      <div class="actions">
        <ButtonSmallColor class="btn" @click="handleLogin">
          <RouterLink to="/users/passwords/emails">비밀번호 찾기</RouterLink>
        </ButtonSmallColor>
        <ButtonSmallColor class="btn" >
          <RouterLink to="/register">회원가입</RouterLink>
        </ButtonSmallColor>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  align-items: center;
  width: 80%;
  margin: 0 auto;
  padding: 20px;
}

.header {
  display: flex;
  align-items: center;
  padding: 20px;
  font-size: 30px;
  font-weight: bold;
}

.login-form {
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


.field-group {
  width: 30%;
  margin-bottom: 20px;
}

.form-group p {
  text-align: left;
  font-weight: bold;
  font-size: 14px;
}

.btn {
  margin: 20px;
}

h2 {
  margin-bottom: 20px;
}

.actions {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 10px;
}

a {
  color: black;
  text-decoration: none;
}

.error {
  color: #53D9C1;
  font-weight: bold;
}
</style>