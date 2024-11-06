<script setup>
import {computed, onMounted, ref} from "vue";
import {RouterLink} from "vue-router";
import {useAuthStore} from "@/stores/auth.js";
import router from "@/router/index.js";

// import {fetchUserInfo} from "@/util/FetchUserInfo.js";
//
// // fetchUserInfo 메소드에서 데이터 가져오기
// const { userInfo, fetchUserInfoData } = fetchUserInfo();

const authStore = useAuthStore();
// accessToken 이 있으면 로그인한 상태
const isLoggedIn = computed(() => !!authStore.accessToken);

const handleLogout = () => {
  authStore.logout();
  router.push("/login");
};

// 마운트 시 회원 정보 가져오기
onMounted(() => {
  // if (isLoggedIn.value) {
  //   fetchUserInfoData();
  // }
});

</script>

<template>
  <div class="side-menu-bar">
    <div v-if="isLoggedIn" class="user-info">
<!--      <h3>{{ userInfo?.userName}} 님</h3>-->
<!--      <p>{{ userInfo?.userEmail}}</p>-->
    </div>
    <div v-else>
      <RouterLink v-if="!isLoggedIn" to="/login" active-class="active" replace>
        <h3>로그인 하기</h3>
      </RouterLink>
    </div>

    <hr class="menu-divider" />

    <ul class="menu-list">

      <li class="menu-item" v-if="isLoggedIn">
        <RouterLink to="/users/email" active-class="active">
<!--          <img src="@/assets/image/account_circle.png" alt="icon" class="icon" />-->
          <span>내 정보 보기</span>
        </RouterLink>
      </li>

      <li class="menu-item">
        <RouterLink to="/walkinggroups" active-class="active">
<!--          <img src="@/assets/image/diversity_1.png" alt="icon" class="icon" />-->
          <span>산책 모임 보기</span>
        </RouterLink>
      </li>

      <li class="menu-item">
        <RouterLink to="/registerwalkinggroups" active-class="active">
<!--          <img src="@/assets/image/groups.png" alt="icon" class="icon" />-->
          <span>산책 모임 목록 보기</span>
        </RouterLink>
      </li>

      <li class="menu-item">
        <RouterLink to="/shoppinggroup" active-class="active">
<!--          <img src="@/assets/image/local_mall.png" alt="icon" class="icon" />-->
          <span>공동구매 하기</span>
        </RouterLink>
      </li>

      <li class="menu-item">
        <RouterLink to="/board" active-class="active">
<!--          <img src="@/assets/image/content_paste.png" alt="icon" class="icon" />-->
          <span>게시판 보기</span>
        </RouterLink>
      </li>

      <li class="menu-item">
        <RouterLink to="/counseling" active-class="active">
<!--          <img src="@/assets/image/admin_meds.png" alt="icon" class="icon" />-->
          <span>상담하기</span>
        </RouterLink>
      </li>


      <li class="menu-divider"></li>

      <li class="menu-item">
        <div
            class="logout-menu-container"
            v-if="isLoggedIn"
            @click="handleLogout"
        >
<!--          <img src="@/assets/image/logout.png" alt="icon" class="icon" />-->
          <span>로그아웃</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<style scoped>

.side-menu-bar {
  background-color: #ffffff; /* 흰색 배경 */
  width: 250px;
  padding: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  position: fixed;
  right: 0;
  top: 50%;
  height: 70%;
  z-index: 1000;
  border-radius: 20px 0 0 20px; /* 왼쪽 상단과 하단에만 곡선 처리 */
  overflow-y: auto;
  font-family: Arial, sans-serif;
  transform: translateY(-50%);
}

.user-info {
  text-align: left;
  margin-bottom: 20px;
}

.user-info h3 {
  margin: 0;
  font-size: 18px;
}

.user-info p {
  margin: 5px 0;
  color: #555;
}

/* 기본 링크 스타일 제거 */
a {
  text-decoration: none; /* 밑줄 제거 */
  color: inherit; /* 텍스트 색상 유지 (상속받은 색상 사용) */
}

/* RouterLink가 활성화될 때의 기본 스타일 제거 */
.router-link-active {
  text-decoration: none !important; /* 활성화 시 밑줄 제거 */
  color: inherit !important; /* 색상 변경 제거 */
}

.menu-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  cursor: pointer;
  font-size: 16px;
  color: #333;
}

.icon {
  width: 15px;
  height: auto;
  margin-right: 10px;
}

.menu-divider{
  border: none;
  border-top: 1px solid #ddd; /* 옅은 회색 선 */
  margin: 10px 0; /* 메뉴 간 여백 */
}

</style>