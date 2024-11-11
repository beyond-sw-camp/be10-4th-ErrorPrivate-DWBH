<script setup>
import {ref, onMounted, computed} from 'vue';
import axios from 'axios';
import CommentItem from './CommentItem.vue';
import {useAuthStore} from "@/stores/auth.js";

const props = defineProps({
  hireSeq: {
    type: Number,
    required: true
  }
});
// // 로그인한 사용자 ID 추출
// const getUserSeqFromToken = () => {
//   const token = localStorage.getItem("accessToken");
//   if (token) {
//     const payload = JSON.parse(atob(token.split(".")[1]));
//     return payload.sub; // userSeq를 의미하는 부분 추출
//   }
//   return null;
// };

const authStore = useAuthStore();
const userSeq = computed(() => authStore.userSeq);
const comments = ref([]);
const currentPage = ref(1); // 현재 페이지 번호
const pageSize = 10; // 한 페이지에 표시할 댓글 수
const sortOrder = "asc"; // 정렬 순서 (등록순)

console.log(authStore)
console.log(authStore.userSeq)
console.log(userSeq);

// API 호출 함수
const fetchComments = async () => {
  try {
    console.log("Fetching comments...");  // 호출 여부 확인
    const response = await axios.get(`http://localhost:8089/api/v1/hire-post/${props.hireSeq}/comment`, {
      params: {
        currentUserSeq: userSeq, // 로그인한 사용자의 ID
        sortOrder: sortOrder,
        page: currentPage.value - 1, // Spring Pageable에서 0부터 시작
        size: pageSize
      }
    });

    // API 응답 데이터가 있을 경우, DTO에 맞게 매핑하여 댓글 데이터 업데이트
    if (response.data && response.data.data) {
      comments.value = response.data.data.comments;
    }
    // if (response.data && response.data.data) {
    //   comments.value = response.data.data.comments.map((comment) => ({
    //     offerSeq: comment.offerSeq,
    //     hireSeq: comment.hireSeq,
    //     userSeq: comment.userSeq,
    //     offerContent: comment.offerContent || '', // 댓글 내용
    //     offerPrivateYn: comment.offerPrivateYn === 'Y', // 비밀 댓글 여부
    //     offerFilePath: getImagePath(comment.offerFilePath), // 댓글 첨부 이미지 경로
    //     regDate: comment.regDate,
    //     modDate: comment.modDate,
    //     userNickname: comment.userNickname || '알 수 없는 닉네임', // 닉네임
    //     userGender: comment.userGender || '알 수 없는 성별', // 성별
    //     userBirthday: calculateAgeGroup(comment.userBirthday), // 나이대 계산
    //     userStatus: comment.userStatus,
    //     userProfilePath: getImagePath(comment.userProfilePath), // 프로필 이미지 경로
    //     displayDate: getDisplayDate(comment.regDate, comment.modDate), // 표시할 날짜 (수정일 우선)
    //   }));
    // }
    else {
      console.error("댓글 데이터를 찾을 수 없습니다.");
    }
    console.log("Response data:", response.data);  // 서버에서 받은 데이터 확인
    comments.value = response.data.content || []; // 페이지 내용 부분만 저장
    console.log("Comments loaded:", comments.value);  // comments가 제대로 할당되었는지 확인
  } catch (error) {
    console.error("댓글 정보를 불러오는 중 에러가 발생했습니다:", error);
  }
};

// 초기 페이지 로드
onMounted(() => {
  fetchComments();
});

// // 날짜 형식을 포맷팅하는 함수
// const formatDate = (date) => {
//   const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
//   return new Date(date).toLocaleDateString('ko-KR', options);
// };

// 연령대 계산 함수
const calculateAgeGroup = (birthday) => {
  const age = new Date().getFullYear() - new Date(birthday).getFullYear();
  if (age < 20) return "10대";
  if (age < 30) return "20대";
  if (age < 40) return "30대";
  if (age < 50) return "40대";
  if (age < 60) return "50대";
  if (age < 70) return "60대";
  if (age < 80) return "70대";
  return "80대 이상";
};

const getDisplayDate = (regDate, modDate) => {
  if (modDate && new Date(modDate) > new Date(regDate)) {
    return modDate;
  }
  return regDate;
};

const getImagePath = (path) => {
  return path ? `../../../../uploads/${path}` : '';
};

// 이전 페이지 버튼
const fetchPreviousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    fetchComments();
  }
};

// 다음 페이지 버튼
const fetchNextPage = () => {
  currentPage.value++;
  fetchComments();
};


// 댓글 목록 데이터를 props로 받아서 각 댓글을 CommentItem에 전달
// const props = defineProps({
//   comments: {
//     type: Array,
//     required: true
//   }
// });
</script>

<template>
  <div class="comment-list-container">
<!--    <CommentItem-->
<!--        v-for="(comment, index) in comments"-->
<!--        :key="index"-->
<!--        :author="comment.author"-->
<!--        :authorImage="comment.authorImage"-->
<!--        :ageGroup="comment.ageGroup"-->
<!--        :gender="comment.gender"-->
<!--        :date="comment.date"-->
<!--        :content="comment.content"-->
<!--        :attachedImage="comment.attachedImage"-->
<!--    />-->
    <ul class="comment-list"  v-if="comments.length > 0">
<!--      <CommentItem-->
<!--          v-for="(comment, index) in comments"-->
<!--          :key="comment.index"-->
<!--          :author="comment.author"-->
<!--          :ageGroup="calculateAgeGroup(comment.userBirthday)"-->
<!--          :gender="comment.gender"-->
<!--          :date="comment.date"-->
<!--          :content="comment.content"-->
<!--          :isPrivate="comment.isPrivate"-->
<!--          :authorImage="getImagePath(comment.authorImage)"-->
<!--          :attachedImage="getImagePath(comment.attachedImage)"-->
<!--      />-->
      <CommentItem
          v-for="comment in comments"
          :key="comment.index"
          :author="comment.userNickname || '알수없는닉네임'"
          :ageGroup="calculateAgeGroup(comment.userBirthday)"
          :gender="comment.userGender || '알 수 없는 성별'"
          :date="getDisplayDate(comment.regDate, comment.modDate)"
          :content="comment.content || '내용못불러옴'"
          :isPrivate="comment.offerPrivateYn"
          :authorImage="comment.userProfilePath"
          :attachedImage="comment.offerFilePath"
      />
    </ul>
    <p v-else>댓글이 없습니다.</p> <!-- comments가 비어있을 경우 표시 -->
    <!-- 페이지네이션 버튼 -->
    <div class="pagination">
      <button @click="fetchPreviousPage" :disabled="currentPage === 1">이전</button>
      <button @click="fetchNextPage" :disabled="comments.length < pageSize">다음</button>
    </div>
  </div>
</template>

<style scoped>
/*.comment-list {
  display: flex;
  flex-direction: column;
  gap: 15px; !* 댓글 간격 *!
  padding: 10px 0;
  max-height: 500px; !* 최대 높이 지정하여 스크롤 가능 *!
  overflow-y: auto; !* 댓글이 많을 경우 스크롤 *!
  border-top: 1px solid #e0e0e0;
  border-bottom: 1px solid #e0e0e0;
}

.comment-list::-webkit-scrollbar {
  width: 8px;
}

.comment-list::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 4px;
}

.comment-list::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}*/
.comment-list-container {
  max-height: 300px; /* 스크롤바가 생길 최대 높이 */
  overflow-y: auto; /* 세로 스크롤바 */
  padding-right: 10px; /* 스크롤바와 내용 사이 간격 */
}

.comment-list {
  list-style-type: none; /* 기본 bullet 스타일 제거 */
  padding: 0;
  margin: 0;
}

/* 스크롤바 스타일 */
.comment-list-container::-webkit-scrollbar {
  width: 8px;
}

.comment-list-container::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 4px;
}

.comment-list-container::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}
</style>