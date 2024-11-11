<script setup>
import UserInfo from "@/components/user/UserInfo.vue";
import {useAuthStore} from "@/stores/auth.js";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import router from "@/router/index.js";
import CounselList from "@/components/counsel/CounselList.vue";
import Pagination from "@/components/common/Pagination.vue";
import CounselOffer from "@/components/counsel/CounselOfferList.vue";
import {useRoute} from "vue-router";
import UserModifyForm from "@/components/user/UserModifyForm.vue";

const authStore = useAuthStore();
// 라우터로 온 데이터 받기
const route = useRoute();
const isMypage = ref(false);  // 마이페이지인지 프로필 조회인지 체크
const isModify = ref(false);  // 수정 선택 시
const cancelModify = () => {
  isModify.value = false;
}
const duplicationNickname = ref(false); // 중복 체크

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
  regDate: null,
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
  } catch (error) {
    console.error("유저 정보 가져오기 실패:", error);
  }
};

// 유저 정보 수정 조회
const readModifyUser = async () => {
  try {
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
    userModifyData.regDate = response.data.regDate;
    isModify.value = true;
    console.log(response.data);
  } catch (error) {
    console.error("유저 수정 조회 실패:", error);
  }
};

// 닉네임 중복 체크
const checkDuplicationNickname = async (nickname) => {
  try {

    if (nickname) {
      const response = await axios.get(`http://localhost:8089/api/v1/user/nickname-check/${nickname}`, {});

      // 닉네임 중복일 경우
      if (response.data === true) {
        duplicationNickname.value = true;
      } else {
        duplicationNickname.value = false;
      }

    }
  } catch (error) {
    // 서버에서 전송된 에러 메세지 추출
    if (error.response.data.message) {
      alert('정확한 닉네임을 입력해주세요');
    } else {
      alert('정확한 닉네임을 입력해주세요');
    }
  }
};

// 유저 수정
const updateUser = async (userData) => {
  try {
    const formData = new FormData();
    formData.append('modifyUserRequest', new Blob([JSON.stringify({
      userNickname: userData.userNickname,
      userPassword: userData.userPassword,
      userBirthday: userData.userBirthday,
      userGender: userData.userGender,
      userMbti: userData.userMbti,
    })], { type: 'application/json' }));

    const response = await axios.put(`http://localhost:8089/api/v1/user`, formData, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
        "Content-Type": 'multipart/form-data',
      },
    });

    alert("수정 성공!!!");
    // '/' 경로로 이동하면서 새로고침 -> 헤더 갱신을 위해
    window.location.href = "/";
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
    console.log("hireCurrentPage", hireCurrentPage.value)
    counselListPaginate();
  } catch (error) {
    console.error("해당 사용자가 작성한 게시글 리스트 불러오기 실패 :", error);
  }
}

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
      params: {
        page: offerCurrentPage.value - 1, // Spring Boot Pageable의 page는 0부터 시작
        size: offerPageSize.value
      }, headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    });
    console.log(response.data);
    offerList.value = response.data.content;
    offerTotalCount.value = response.data.totalElements;

  } catch (error) {
    console.error("해당 사용자가 작성한 댓글 리스트 불러오기 실패 :", error);
  }
}

onMounted(() => {
  if (route.params.userSeq == authStore.userSeq) {
    isMypage.value = true;
  }
  readUser();
  fetchCounselList();
  fetchOfferList();
});

</script>

<template>
  <div class="content">
    <div class="my-page-view">
      <div class="header">
        <span v-if="isMypage">마이페이지</span>
        <span v-else>프로필 조회</span>
      </div>
      <div class="main-layout">

        <div class="user-info-form">
          <UserInfo :userData="userData" @update="readModifyUser" @delete="deleteUser"/>
        </div>
        <div class="right-section">
          <template v-if="!isModify">
            <div class="bridge-of-heart">
              <CounselList :counselHires="paginationCounselList" :currentPage="hireCurrentPage" :pageSize="hirePageSize"
                           :totalCount="hireTotalCount"/>

              <Pagination :totalCount="hireTotalCount" :pageSize="hirePageSize" :currentPage="hireCurrentPage"
                          @sendPagination="hireReceivePagination"/>
            </div>
            <div class="warm-hand-sharing">
              <CounselOffer :offerList="offerList" :currentPage="offerCurrentPage" :pageSize="offerPageSize"
                            :totalCount="offerTotalCount"/>
              <Pagination :totalCount="offerTotalCount" :pageSize="offerPageSize" :currentPage="offerCurrentPage"
                          @sendPagination="offerReceivePagination"/>
            </div>
          </template>
          <template v-else>
            <div class="bridge-of-heart">
              <UserModifyForm :userData="userModifyData" :duplicationNickname="duplicationNickname"
              @nickname="checkDuplicationNickname" @cancel="cancelModify" @save="updateUser"/>
            </div>
          </template>
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