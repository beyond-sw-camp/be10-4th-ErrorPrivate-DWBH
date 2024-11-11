<script setup>
import {computed, onMounted, ref} from "vue";
import {RouterLink} from "vue-router";
import {useAuthStore} from "@/stores/auth.js";
import router from "@/router/index.js";
import NotificationForm from "@/components/notification/NotificationForm.vue";

const authStore = useAuthStore();
// accessToken 이 있으면 로그인한 상태
const isLoggedIn = computed(() => !!authStore.accessToken);

// props 정의
const props = defineProps({
  notifications: {
    type: Array,
    required: true
  },
  userNickname:{
    type: String,
    required: true
  }
});

// emit 정의
const emit = defineEmits(['selectChat']);

const handleLogout = () => {
  authStore.logout();
  router.push("/login");
};

const selectChat = (notificationSeq) => {
  emit('selectChat', notificationSeq);
};

</script>

<template>
  <div class="side-menu-bar">
    <div class="user-info">
      <p>{{ userNickname }} 님</p>
      <p>{{ authStore.userEmail }}</p>
    </div>

    <hr class="menu-divider"/>
    <NotificationForm v-for="notification in notifications" :key="notification.notificationSeq"
                      :notification="notification" @selectChat="selectChat(notification.notificationSeq)"/>
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

.menu-divider {
  border: none;
  border-top: 5px solid #CCB997;
  margin: 10px 0; /* 메뉴 간 여백 */
}

</style>