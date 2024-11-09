<script setup>
import {RouterLink} from "vue-router";
import {computed, onBeforeUnmount, onMounted, onUnmounted, reactive, ref} from "vue";
import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";
import router from "@/router/index.js";
import SideNotificationBar from "@/components/common/SideNotificationBar.vue";
import {useAuthStore} from "@/stores/auth.js";
import ChatList from '@/components/chat/ChattingList.vue';
import ChatDetail from '@/components/chat/ChattingDetail.vue';
import axios from "axios";

const authStore = useAuthStore();
const isSideNotificationBarOn = ref(false); // 기본 off 상태
const isModalOpen = ref(false);
const isDetailOpen = ref(false);
const selectedChat = ref(null);

const notifications = reactive([]);  // 알림을 저장하는 Vue reactive 배열
const checkInterval = 30000; // 30초
let eventSource = null;

const setupEventSource = () => {
  if (isLoggedIn.value) {
    const token = authStore.accessToken;
    eventSource = new EventSource(`http://localhost:8089/api/v1/notification/subscribe?token=${token}`);

    eventSource.addEventListener('notification', (event) => {
      const notificationData = JSON.parse(event.data); // DTO 데이터를 파싱하여 사용
      notifications.push(notificationData); // 알림 데이터를 Vue 상태에 추가
    });

    eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error);
      eventSource.close();
    };
  }
};

// 알림 조회
const readNotificationList = async () => {
  try {
    const response = await axios.get('http://localhost:8089/api/v1/notification', {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });
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

let intervalId;
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
  setupEventSource();

  // 매 30초 마다 알림을 가져오는 간격 설정
  intervalId = setInterval(readNotificationList, checkInterval);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
  if (eventSource) {
    eventSource.close();
  }
});

onUnmounted(() => {
  clearInterval(intervalId); // 컴포넌트가 언마운트될 때 간격 해제
})


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

    <!-- ChatView 모달 -->
    <transition>
      <div
          v-if="isModalOpen"
          class="modal-overlay"
          @click="closeModal"
      >
        <div class="modal-content" @click.stop>
          <button class="close-button" @click="closeModal">X</button>
          <ChatView/>
        </div>
      </div>
    </transition>

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


  <!-- 채팅방 모달 -->
  <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <button class="close-button" @click="closeModal">X</button>

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