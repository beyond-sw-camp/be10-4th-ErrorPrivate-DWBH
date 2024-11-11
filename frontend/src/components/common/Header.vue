<script setup>
import {RouterLink} from "vue-router";
import {computed, onBeforeUnmount, onMounted, onUnmounted, reactive, ref} from "vue";
import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";
import router from "@/router/index.js";
import SideNotificationBar from "@/components/common/SideNotificationBar.vue";
import ChatList from '@/components/chat/ChattingList.vue';
import ChatDetail from '@/components/chat/ChattingDetail.vue';
import axios from "axios";
import {useAuthStore} from "@/stores/auth.js";

const authStore = useAuthStore();

const isSideNotificationBarOn = ref(false); // 기본 off 상태
const isModalOpen = ref(false);
const isDetailOpen = ref(false);
const selectedChat = ref(null);

const chat = ref(null);
const userNickname = ref(null);
const state = reactive({
  notificationList: [],
  isConfirmation: true,
});

// 알림 조회
const readNotificationList = async () => {
  try {
    const response = await axios.get('http://localhost:8089/api/v1/notification', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    });
    state.notificationList = response.data.notifications;
    state.isConfirmation = response.data.isConfirmation;
  } catch (error) {
    console.error("알림 가져오기 실패:", error);
  }
};

// 알림 상세 조회 (채팅방 입장)
const readNotification = async (notificationSeq) => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/notification/${notificationSeq}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    });
    chat.value = response.data
    console.log(chat.value);
  } catch (error) {
    console.error("알림 상세 조회 (채팅방 입장) 가져오기 실패:", error);
  }

  await readNotificationList(); // 알림 새로 조회
  isSideNotificationBarOn.value = !isSideNotificationBarOn.value; // 알림 창 닫기
  isModalOpen.value = true; // 채팅방으로 이동
  openChatDetail(chat.value);
};

// 유저 조회
const readUser = async () => {
  try {
    const userSeq = authStore.userSeq;
    const response = await axios.get(`http://localhost:8089/api/v1/user/${userSeq}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    });
    userNickname.value = response.data.userNickname;
  } catch (error) {
    console.error("알림 가져오기 실패:", error);
  }
};

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

onMounted( async () => {
  document.addEventListener('click', handleClickOutside);
  if (isLoggedIn.value) {
    await readNotificationList();
    await readUser();
  }
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});

function openModal() {
  isModalOpen.value = true;
  isDetailOpen.value = false;
}

function closeModal() {
  isModalOpen.value = false;
  selectedChat.value = null;
}

function openChatDetail(chat) {
  selectedChat.value = chat;
  isDetailOpen.value = true;
}

function goBackToList() {
  isDetailOpen.value = false;
  selectedChat.value = null;
}

</script>

<template>
  <div id="app">
    <header>
      <nav>
        <div class="logo">
          <RouterLink to="/" active-class="active" replace>
            <img src="@/images/ms-icon-310x310.png" alt="DWBH 로고"/>
          </RouterLink>
        </div>

        <div class="menu">
          <RouterLink to="/counsel" active-class="active" replace>
            Counsel
          </RouterLink>

          <template v-if="!isLoggedIn">
            <RouterLink to="/register" active-class="active" replace>
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
            <button @click="openModal" class="side-menubar-button">
              <img
                  src="@/images/chat.png"
                  alt="side menubar icon"
                  class="side-menubar"
              />
            </button>
            <!-- 사이드 알림바 토글 버튼-->
            <button @click.stop="toggleSideNotificationBar" class="side-menubar-button">
              <img
                  src="@/images/notification.png"
                  alt="side menubar icon"
                  class="side-menubar"
              />
              <span v-if="!state.isConfirmation" class="notification-badge"></span>
            </button>

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
        <SideNotificationBar :notifications="state.notificationList" :userNickname="userNickname"
                             @close="toggleSideNotificationBar" @selectChat="readNotification"/>
      </div>
    </transition>
  </div>


  <!-- 채팅방 모달 -->
  <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <button v-if="!selectedChat" class="close-button" @click="closeModal">X</button>

      <!-- 목록 또는 상세 화면 표시 -->
      <template v-if="!isDetailOpen">
        <!-- 채팅 목록 화면 -->
        <ChatList @selectChat="openChatDetail"/>
      </template>
      <template v-else>
        <!-- 대화 상세 화면 -->
        <ChatDetail v-if="selectedChat" :chat="selectedChat" @goBack="goBackToList"/>
      </template>
    </div>
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

.notification-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 10px;
  height: 10px;
  background-color: red;
  border-radius: 50%;
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

.side-menubar-button {
  background: none; /* 배경 제거 */
  border: none; /* 테두리 제거 */
  padding: 0; /* 기본 패딩 제거 */
  cursor: pointer; /* 마우스 포인터 커서 설정 */
  position: relative;
}

.side-menubar {
  width: 20px;
  height: auto;
  cursor: pointer;
}

.chat-list-view {
  padding: 1rem;
}

/* 모달 스타일 */
.modal-overlay {
  z-index: 1000;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
}

.modal-content {
  background-color: white;
  padding: 0;
  border-radius: 5px; /* 모서리를 둥글게 */
  width: 320px; /* 좁고 길쭉한 모양 */
  height: 600px; /* 카톡처럼 길게 */
  position: relative;
  margin-top: 6rem;
  margin-right: 6rem;
  overflow-y: auto; /* 내용이 넘칠 때 스크롤 */
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: none;
  font-size: 1.2rem;
  cursor: pointer;
}

</style>