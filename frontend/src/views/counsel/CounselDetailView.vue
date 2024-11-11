<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import dayjs from "dayjs";
import "@/css/style.css"
import {useRoute, useRouter} from "vue-router";
import CounselCommentSection from "@/components/counseloffer/CounselCommentSection.vue";
import {useAuthStore} from "@/stores/auth.js";

const router = useRouter();
// 라우터로 온 데이터 받기
const route = useRoute();

const hireSeq = route.params.hireSeq;

const counselHire = ref([]);
const counselorAgeRanges = ref([]);
const counselorTypes = ref([]);
const sendUserSeq = ref(0);

const authStore = useAuthStore();
const userSeq = authStore.userSeq;

const fetchCounselHires = async () => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/counselor-hire/${hireSeq}`);

    sendUserSeq.value = response.data.userSeq;
    counselHire.value = response.data;
    counselorAgeRanges.value = response.data.counselorAgeRanges;
    counselorTypes.value = response.data.counselorTypes;
  } catch (error) {
    console.error('게시판 상세조회 가져오는 중 오류 발생', error)
  }
}

onMounted(() => {
  fetchCounselHires()
})

// 목록으로 돌아가기
const goToList = () => {
  router.push("/counsel");
};
</script>

<template>
  <div class="container content">
    <div class="responsive-container mt-4">
      <div class="title-container d-flex justify-content-between align-items-center">
        <h2 class="title-text">마음을 이어주는 다리 / 상세조회</h2>
        <button class="btn btn-dark" @click="goToList">목록으로</button>
      </div>
      <!-- 상세 정보 섹션 -->
      <div class="detail-container mt-4 p-4 border rounded">
        <!-- 제목 -->
        <h3 class="detail-title">{{ counselHire.hireTitle }}</h3>
        <p class="text-muted author-text text-end">작성자 : {{ counselHire.userNickname }}</p>
        <!-- 내용 -->
        <p>
          {{ counselHire.hireContent }}
        </p>
        <table class="table mt-4">
          <tbody>
            <tr>
              <th scope="row">희망 상담사 나이</th>
              <td>
                <template v-if="counselorAgeRanges.length > 0" v-for="(counselorAgeRange, index) in counselorAgeRanges" :key="index">{{counselorAgeRange}}</template>
                <template v-else>무관</template>
              </td>
            </tr>
            <tr>
              <th scope="row">희망 성별</th>
              <td>{{ counselHire.hireGender }}</td>
            </tr>
            <tr>
              <th scope="row">희망 조언 타입</th>
              <td>
                <template v-if="counselorTypes.length > 0" v-for="(counselorType, index) in counselorTypes" :key="index">{{counselorType}}</template>
                <template v-else>무관</template>
              </td>
            </tr>
            <tr>
              <th scope="row">작성일</th>
              <td>{{ dayjs(counselHire.regDate).format("YYYY-MM-DD HH:mm:ss") }}</td>
            </tr>
            <tr v-if="counselHire.modDate !== null">
              <th scope="row">수정일</th>
              <td>{{ dayjs(counselHire.modDate).format("YYYY-MM-DD HH:mm:ss") }}</td>
            </tr>
          </tbody>
        </table>
        <div class="comment-actions text-end">
          <button v-if="sendUserSeq===userSeq" class="btn btn-sm btn-outline-secondary me-2" @click="editComment(comment)">수정</button>
          <button v-if="sendUserSeq===userSeq" class="btn btn-sm btn-outline-danger" @click="isDelete(comment.offerSeq, comment.hireSeq)">삭제</button>
        </div>
      </div>

      <!-- 댓글 -->
      <CounselCommentSection :hireSeq="hireSeq" :sendUserSeq="sendUserSeq" />
    </div>
  </div>
</template>

<style scoped>
.text-end {
  font-weight: bold;
}

.table th {
  width: 30%;
  font-weight: bold;
}

.table td {
  width: 70%;
}
</style>