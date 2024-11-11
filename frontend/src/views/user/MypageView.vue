<script setup>
import UserInfo from "@/components/user/UserInfo.vue";
import {useAuthStore} from "@/stores/auth.js";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import router from "@/router/index.js";
import CounselList from "@/components/counsel/CounselList.vue";
import Pagination from "@/components/common/Pagination.vue";

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
const userSeq = authStore.userSeq;

const readUser = async () => {
  try {
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

const counselList = ref([]);
const fetchCounselList = async () => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/counselor-hire/user/${userSeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });

    console.log(response.data);
    counselList.value = response.data.counselorList.content;
    hireTotalCount.value = counselList.value.length;
    console.log("hireCurrentPage",hireCurrentPage.value)
    counselListPaginate();
  } catch (error) {
    console.error("해당 사용자가 작성한 게시글 리스트 불러오기 실패 :", error);
  }
}
onMounted(  () => {
  readUser();
  fetchCounselList();
  fetchOfferList();
});

const hireTotalCount = ref(0); // 전체 개수
const hireCurrentPage = ref(1); // 현재 페이지
const hirePageSize = ref(5); // 페이지당 항목 수

const hireReceivePagination = (page) => {
  hireCurrentPage.value = page.currentPage;
  hirePageSize.value = page.pageSize;

  fetchCounselList();
};

const paginationCounselList = ref([]);
// 현재 페이지에 따라 데이터 나누기
const counselListPaginate = () => {
  const start = (hireCurrentPage.value - 1) * hirePageSize.value;
  const end = start + hirePageSize.value;
  console.log(start, end);
  paginationCounselList.value = counselList.value.slice(start, end);
};


const offerList = ref([]);
const offerTotalCount = ref(0);
const offerCurrentPage = ref(1);
const offerPageSize = ref(5);

const offerReceivePagination = (page) => {
  offerCurrentPage.value = page.currentPage;
  offerPageSize.value = page.pageSize;

  fetchOfferList();
};

const fetchOfferList = async () => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/hire-post/user/comment/${userSeq}`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });
    offerList.value = response.data.content;
    offerTotalCount.value = offerList.value.length;

  } catch (error) {
    console.error("해당 사용자가 작성한 댓글 리스트 불러오기 실패 :", error);
  }
}
</script>

<template>
  <div class="content">
    <div class="my-page-view">
      <div class="header">
        <span>마이페이지</span>
      </div>
      <div class="main-layout">

        <div class="user-info-form">
          <UserInfo :userData="userData" @update="updateUser" @delete="deleteUser"/>
        </div>
        <div class="right-section">
          <div class="bridge-of-heart">
            <CounselList :counselHires="paginationCounselList" :currentPage="hireCurrentPage" :pageSize="hirePageSize" :totalCount="hireTotalCount"/>

            <Pagination :totalCount="hireTotalCount" :pageSize="hirePageSize" :currentPage="hireCurrentPage" @sendPagination="hireReceivePagination"/>
          </div>
          <div class="warm-hand-sharing">
            <CounselList/>
            <Pagination :totalCount="offerTotalCount" :pageSize="offerPageSize" :currentPage="offerCurrentPage" @sendPagination="offerReceivePagination"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 오른쪽 섹션 (다리 및 따스한 손길 나눔) */
.right-section {
  flex: 2; /* 오른쪽 섹션이 더 넓게 보이도록 설정 */
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 다리 섹션 */
.bridge-of-heart {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  font-family: 'Arial', sans-serif;
}

.content {
  background-color: #f4f1ea;
}

.my-page-view {
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

/* 메인 레이아웃: 사용자 정보와 오른쪽 섹션을 나란히 배치 */
.main-layout {
  display: flex; /* 사용자 정보와 다리 섹션을 가로로 배치 */
  gap: 20px; /* 두 섹션 사이 간격 */
}

.user-info-form {
  flex: 1; /* 사용자 정보 카드가 적절한 비율로 차지 */
  display: flex;
  flex-direction: column;
  width: 40%;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

/* 따스한 손길 나눔 섹션 */
.warm-hand-sharing {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}
</style>