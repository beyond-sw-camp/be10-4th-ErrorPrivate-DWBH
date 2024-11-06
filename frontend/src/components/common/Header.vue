<script setup>
import {RouterLink} from "vue-router";
import {computed, onBeforeUnmount, onMounted, ref} from "vue";
import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";
import router from "@/router/index.js";
import SideNotificationBar from "@/components/common/SideNotificationBar.vue";
import {useAuthStore} from "@/stores/auth.js";

const authStore = useAuthStore();
const isSideNotificationBarOn = ref(false); // 기본 off 상태

// accessToken 이 있으면 로그인한 상태
const isLoggedIn = computed(() => !!authStore.accessToken);
const handleLogout = () => {
  authStore.logout();
  router.push("/login");
};

// 사이드 알림바의 토글을 작동시키는 메소드
const toggleSideNotificationBar = () => {
  isSideNotificationBarOn.value = !isSideNotificationBarOn.value;
}

// // toggleSideMenuBar 메소드를 자식 컴포넌트(SideMenuBaer)에 전달
// provide("toggleSideMenuBar", toggleSideMenuBar);

// 사이드 알림바가 아닌 화면의 다른 부분을 클릭했을 때 사이드 알림바를 닫는 메소드
const handleClickOutside = (event) => {
  const sideMenu = document.querySelector('.side-menubar-container');
  if (sideMenu && !sideMenu.contains(event.target)) {
    isSideNotificationBarOn.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});

</script>

<template>
  <div id="app">
    <header>
      <nav>
        <div class="logo">
          <RouterLink to="/" active-class="active" replace>
            <img src="@/images/Error-pirate.jpg" alt="에러 해적단 로고"/>
          </RouterLink>
        </div>

        <div class="menu">
          <RouterLink to="/counsel" active-class="active" replace>
            Counsel
          </RouterLink>

          <template v-if="!isLoggedIn">
            <RouterLink to="/signup" active-class="active" replace>
              Sign Up
            </RouterLink>
            <RouterLink to="/login" active-class="active" replace>
              <ButtonSmallColor>Log In</ButtonSmallColor>
            </RouterLink>
          </template>

          <template v-if="isLoggedIn">
            <RouterLink to="/mypage" active-class="active" replace>
              My Page
            </RouterLink>
            <RouterLink to="/chat-list" active-class="active" replace>
              <!-- 채팅방 버튼-->
              <img
                  src="@/images/chat.png"
                  alt="side menubar icon"
                  class="side-menubar"
              />
            </RouterLink>
            <!-- 사이드 알림바 토글 버튼-->
            <img
                src="@/images/notification.png"
                alt="side menubar icon"
                class="side-menubar"
                @click.stop="toggleSideNotificationBar"
            />
            <ButtonSmallColor @click="handleLogout">Log Out</ButtonSmallColor>
          </template>
        </div>
      </nav>
    </header>

    <!-- 사이드 알림바 -->
    <transition name="slide">
      <div
          v-if="isSideNotificationBarOn"
          class="side-menubar-container"
          @click.stop
      >
        <SideNotificationBar @close="toggleSideNotificationBar"/>
      </div>
    </transition>
  </div>
</template>

<style scoped>

header {
  width: 100%; /* 화면 전체 너비 차지 */
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
}

nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  padding-left: 40px;
  padding-right: 40px;
  flex-wrap: nowrap; /* 화면이 줄어들어도 글씨 가로로 유지 */
}

a {
  white-space: nowrap;
  text-decoration: none;
  color: #333;
  padding: 10px 15px;
}

/* 이미지 스타일링 */
img {
  width: 100px; /* 너비를 150px로 고정 */
  height: auto; /* 비율을 유지하면서 높이 자동 조정 */
}

/* 반응형으로 조절하고 싶을 경우 */
img {
  max-width: 100%; /* 부모 요소에 맞춰 최대 크기를 설정 */
  height: auto; /* 높이를 자동으로 맞춤 */
}

/* 로고 */
.logo {

}

/* 메뉴 */
.menu {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 사이드 메뉴바 아이콘 */
.side-menubar {
  width: 20px;
  height: auto;
  cursor: pointer;
}

/* 반응형으로 조절하고 싶을 경우 */
.side-menubar {
  max-width: 100%; /* 부모 요소에 맞춰 최대 크기를 설정 */
  height: auto; /* 높이를 자동으로 맞춤 */
}


/* 슬라이드 애니메이션 */
.slide-enter-active, .slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from {
  transform: translateX(100%); /* 오른쪽에서 나타나기 */
}

.slide-leave-to {
  transform: translateX(100%); /* 오른쪽으로 사라지기 */
}

</style>