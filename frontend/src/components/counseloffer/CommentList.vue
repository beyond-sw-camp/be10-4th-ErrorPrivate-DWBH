<script setup>
import {ref, onMounted, watch} from 'vue';
import axios from 'axios';
import CommentItem from './CommentItem.vue';
import {useAuthStore} from "@/stores/auth.js";

const props = defineProps({
  hireSeq: {
    type: Number,
    required: true
  }
});

const authStore = useAuthStore();
const userSeq = authStore.userSeq;

const comments = ref([]); // 댓글 데이터 저장
const currentPage = ref(1); // 현재 페이지 번호
const pageSize = 10; // 한 페이지에 표시할 댓글 수
const sortOrder = ref("asc"); // 정렬 순서 (asc: 등록순, desc: 최신순)

// 댓글 데이터를 백엔드 API에서 가져오는 함수
const fetchComments = async () => {
  try {

    // 토큰을 확인하여 로그인한 사용자라면 헤더에 추가
    const token = localStorage.getItem("accessToken");
    const headers = token ? { Authorization: `Bearer ${token}` } : {};

    // API 요청을 보낼 때 현재 페이지, 정렬 순서, 로그인 사용자 ID를 함께 보냄
    const response = await axios.get(`http://localhost:8089/api/v1/hire-post/${props.hireSeq}/comment`, {
      params: {
        // currentUserSeq: localStorage.getItem("userSeq"), // 로그인한 사용자의 ID
        currentUserSeq: userSeq || null, // 로그인한 사용자의 ID
        sortOrder: sortOrder.value,
        page: currentPage.value - 1, // Spring Pageable에서 0부터 시작
        size: pageSize
      },
      headers: headers // 헤더에 Authorization 추가 또는 빈 헤더 전달
    });

    // API 응답 데이터가 있을 경우, DTO에 맞게 매핑하여 댓글 데이터 업데이트
    if (response.data) {
      comments.value = response.data.content; // Page 내용 부분만 저장
      console.log(response.data.content)
    } else {
      console.error("댓글 데이터를 찾을 수 없습니다.");
    }
  } catch (error) {
    console.error("댓글 정보를 불러오는 중 에러가 발생했습니다:", error);
  }
};

// 초기 페이지 로드
onMounted(() => {
  fetchComments();
});

watch(
    [() => props.hireSeq, () => localStorage.getItem("userSeq")],
    ([newHireSeq, newUserSeq]) => {
      if (newHireSeq && newUserSeq) {
        fetchComments();
      }
    },
    {immediate: true}
);

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

const handleCommentDeleted = (deletedOfferSeq) => {
  // 삭제된 댓글을 comments 배열에서 제거합니다.
  comments.value = comments.value.filter(comment => comment.key !== deletedOfferSeq);
};
</script>

<template>
  <div class="comment-list-container">
    <ul class="comment-list" v-if="comments.length > 0">
      <CommentItem
          v-for="comment in comments"
          :key="comment.offerSeq"
          :offerSeq="comment.offerSeq"
          :hireSeq="comment.hireSeq"
          :userSeq="comment.userSeq"
          :offerContent="comment.offerContent"
          :offerPrivateYn="comment.offerPrivateYn"
          :offerFilePath="comment.offerFilePath"
          :regDate="comment.regDate"
          :modDate="comment.modDate"
          :userNickname="comment.userNickname || '알 수 없는 닉네임'"
          :userGender="comment.userGender || '알 수 없는 성별'"
          :userBirthday="comment.userBirthday"
          :userStatus="comment.userStatus"
          :userProfilePath="comment.userProfilePath"
          :postOwnerSeq="comment.postOwnerSeq"
          @commentDeleted="handleCommentDeleted"
      />
    </ul>

    <!-- 페이지네이션 버튼 -->
    <div class="pagination">
      <button @click="fetchPreviousPage" :disabled="currentPage.value === 1">이전</button>
      <button @click="fetchNextPage" :disabled="comments.length < pageSize">다음</button>
    </div>
  </div>
</template>

<style scoped>
.comment-list-container {
  max-height: 300px;
  overflow-y: auto;
}

.comment-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

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
