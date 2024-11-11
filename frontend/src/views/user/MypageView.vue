<script setup>
import UserInfo from "@/components/user/UserInfo.vue";
import {useAuthStore} from "@/stores/auth.js";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import router from "@/router/index.js";
import {useRoute} from "vue-router";
import UserModifyForm from "@/components/user/UserModifyForm.vue";

const authStore = useAuthStore();
// 라우터로 온 데이터 받기
const route = useRoute();
const isMypage = ref(false);  // 마이페이지인지 프로필 조회인지 체크
const isModify = ref(false);  // 수정 선택 시

// 유저 정보 저장
const userData = reactive({
  filePath: null,
  userBirthday: null,
  userGender: null,
  userMbti: null,
  userNickname: null,
  userTemperature: null,
});
// 유저 정보 수정할 데이터
const userModifyData = reactive({
  userEmail: null,
  userNickname: null,
  userGender: null,
  userBirthday: null,
  userMbti: null,
  userTemperature: null,
  userRegDate: null,
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
  } catch (error) {
    console.error("유저 정보 가져오기 실패:", error);
  }
};

// 유저 정보 수정 조회
const readModifyUser = async () => {
  try {
    const userSeq = authStore.userSeq;
    const response = await axios.get(`http://localhost:8089/api/v1/user/${userSeq}/modify`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });
    userModifyData.userEmail = response.data.userEmail;
    userModifyData.userNickname = response.data.userNickname;
    userModifyData.userGender = response.data.userGender;
    userModifyData.userBirthday = response.data.userBirthday;
    userModifyData.userMbti = response.data.userMbti;
    userModifyData.userTemperature = response.data.userTemperature;
    userModifyData.userRegDate = response.data.userRegDate;
    isModify.value = true;
    console.log(response.data);
  } catch (error) {
    console.error("유저 수정 조회 실패:", error);
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

  } catch (error) {
    console.error("유저 수정 실패:", error);
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

onMounted(async  () => {
  if (route.params.userSeq == authStore.userSeq){
    isMypage.value = true;
  }
  await readUser();
});
</script>

<template>
  <div class="my-page-view">
    <div class="header">
      <span v-if="isMypage">마이페이지</span>
      <span v-else>프로필 조회</span>
    </div>
    <div class="user-info-form">
      <UserInfo :userData="userData" @update="readModifyUser" @delete="deleteUser"/>
      <UserModifyForm v-if="isModify" :userData="userModifyData"/>
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
  width: 35%;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}
</style>