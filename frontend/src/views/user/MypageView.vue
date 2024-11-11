<script setup>
import UserInfo from "@/components/user/UserInfo.vue";
import {useAuthStore} from "@/stores/auth.js";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import router from "@/router/index.js";

const authStore = useAuthStore();

// 유저 정보 저장
const userData = reactive({
  filePath: null,
  userBirthday: null,
  userGender: null,
  userMbti: null,
  userNickname: null,
  userTemperature: null,
});

// 유저 조회
const readUser = async () => {
  try {
    const userSeq = authStore.userSeq;
    const response = await axios.get(`http://localhost:8089/api/v1/user/${userSeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });
    userData.filePath = response.data.filePath;
    userData.userBirthday = response.data.userBirthday;
    userData.userGender = response.data.userGender;
    userData.userMbti = response.data.userMbti;
    userData.userNickname = response.data.userNickname;
    userData.userTemperature = response.data.userTemperature;
    console.log(response.data);
  } catch (error) {
    console.error("유저 정보 가져오기 실패:", error);
  }
};

// 유저 수정
const updateUser = async () => {
  try {
    const response = await axios.put(`http://localhost:8089/api/v1/user`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });
    authStore.logout();
    alert("회원이 탈퇴되었습니다.");
    await router.push("/");
  } catch (error) {
    console.error("유저 삭제 실패:", error);
  }
};

// 유저 삭제
const deleteUser = async () => {
  try {
    const response = await axios.delete(`http://localhost:8089/api/v1/user`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });
    authStore.logout();
    alert("회원이 탈퇴되었습니다.");
    await router.push("/");
  } catch (error) {
    console.error("유저 삭제 실패:", error);
  }
};

onMounted(  () => {
  readUser();
});
</script>

<template>
  <div class="my-page-view">
    <div class="header">
      <span>마이페이지</span>
    </div>
    <div class="user-info-form">
      <UserInfo :userData="userData" @update="updateUser" @delete="deleteUser"/>
    </div>

  </div>
</template>

<style scoped>

.my-page-view {
  display: flex;
  flex-direction: column;
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

.user-info-form {
  display: flex;
  flex-direction: column;
  width: 40%;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}
</style>