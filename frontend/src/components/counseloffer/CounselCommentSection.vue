<script setup>
import "@/css/style.css"
import {defineProps, onMounted, ref} from 'vue';
import CommentList from '@/components/counseloffer/CommentList.vue';
import CommentForm from '@/components/counseloffer/CommentForm.vue';
import axios from "axios";
import {useAuthStore} from "@/stores/auth.js";

const props = defineProps({
  hireSeq: {
    type: Number,
    required: true
  },
  sendUserSeq: {
    type: Number,
    required: true
  }
});

const authStore = useAuthStore();
const userSeq = authStore.userSeq;

const currentPage = ref(1); // 현재 페이지 번호
const pageSize = 5; // 한 페이지에 표시할 댓글 수
const sortOrder = ref("asc"); // 정렬 순서 (asc: 등록순, desc: 최신순)


const comments = ref([]); // 댓글 데이터 저장

// 댓글 데이터를 백엔드 API에서 가져오는 함수
const fetchComments = async () => {
  try {

    // API 요청을 보낼 때 현재 페이지, 정렬 순서, 로그인 사용자 ID를 함께 보냄
    const response = await axios.get(`http://localhost:8089/api/v1/hire-post/${props.hireSeq}/comment`, {
      params: {
        // currentUserSeq: localStorage.getItem("userSeq"), // 로그인한 사용자의 ID
        currentUserSeq: userSeq || null, // 로그인한 사용자의 ID
        sortOrder: sortOrder.value,
        page: currentPage.value - 1, // Spring Pageable에서 0부터 시작
        size: pageSize
      }, headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`
      }
    });

    comments.value = response.data.content; // Page 내용 부분만 저장
    console.log(comments.value);
  } catch (error) {
    console.error("댓글 정보를 불러오는 중 에러가 발생했습니다:", error);
  }
};

// 초기 페이지 로드
onMounted(() => {
  fetchComments();
});
</script>

<template>
  <div class="comment-section mt-4">
    <h4 class="section-title">따뜻한 손길 나눔</h4>
    <hr class="divider" />
    <CommentList v-if="comments.length > 0" :comments="comments" :sendUserSeq="props.sendUserSeq"/>
    <p v-else class="text-muted">아직 댓글이 없습니다. 첫 댓글을 작성해보세요!</p>
    <CommentForm :hireSeq="props.hireSeq" />
  </div>
</template>

<style scoped>
.comment-section {
  background-color: #ffffff;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.section-title {
  font-weight: bold;
}

.divider {
  margin: 10px 0;
}
</style>