<script setup>

import dayjs from "dayjs";
import ModalSmall from "@/components/common/ModalSmall.vue";
import CounselCommentSection from "@/components/counseloffer/CounselCommentSection.vue";
import {useAuthStore} from "@/stores/auth.js";
import router from "@/router/index.js";
import {ref} from "vue";

const authStore = useAuthStore();
const userSeq = authStore.userSeq;

const props = defineProps({
  hireSeq : {
    type: Number,
    require: true
  }, counselHire : {
    type: Object,
    require: true
  },
  sendUserSeq: {
    type: Number,
    require: true
  },
  counselorAgeRanges: {
    type: Array,
    require: true
  },
  counselorTypes: {
    type: Array,
        require: true
  }
})

// 목록으로 돌아가기
const goToList = () => {
  router.push("/counsel");
};

const isModalVisible = ref(false);
function isDelete() {
  isModalVisible.value = true;
}

const closeModal = () => {
  isModalVisible.value = false;
}

const deleteHire = async () => {
  try {
    const response = await axios.delete(`http://localhost:8089/api/v1/counselor-hire/${hireSeq}`)

    isModalVisible.value = false;
    alert("마음을 이어주는 다리 삭제가 완료되었습니다.");
    goToList();
  } catch(error) {
    alert("마음을 이어주는 다리 삭제에 실패하였습니다.")
    console.error(error);
  }
}

const emit = defineEmits(["openModifyForm"]); // 부모에게 이벤트를 전달

const modifyForm = (counselHire) => {
  emit("openModifyForm", counselHire); // counselHire 데이터를 부모에게 전달
};
</script>

<template>
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
        <button v-if="sendUserSeq===userSeq" class="btn btn-sm btn-outline-secondary me-2" @click="modifyForm(counselHire)">수정</button>
        <button v-if="sendUserSeq===userSeq" class="btn btn-sm btn-outline-danger" @click="isDelete()">삭제</button>
      </div>
    </div>

    <!-- 댓글 -->
    <CounselCommentSection :hireSeq="hireSeq" :sendUserSeq="sendUserSeq" />
    <!-- 삭제 확인 모달창 -->
    <ModalSmall :isVisible="isModalVisible" :message="'정말로 삭제하시겠습니까?'"
                @close="closeModal" @confirm="deleteHire"/>
  </div>
</template>

<style scoped>

</style>