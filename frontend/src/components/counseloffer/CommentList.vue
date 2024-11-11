<script setup>
import {useAuthStore} from "@/stores/auth.js";
import dayjs from "dayjs";
import router from "@/router/index.js";

const props = defineProps({
  comments: {
    type: Array,
    required: true
  }
});
const authStore = useAuthStore();
const userSeq = authStore.userSeq;

const goToMyPage = (userSeq) => {
  router.push(`/user/${userSeq}/mypage`);
};

</script>

<template>
  <!-- 댓글 리스트 -->
  <div class="comment-list">
    <div v-for="(comment, index) in comments" :key="index" class="comment-item border-bottom pb-3 mb-3">
      <div class="d-flex align-items-center">
        <img class="profile-img rounded-circle me-3" :src="comment.userProfilePath || '/default-profile.png'" alt="프로필"  @click="goToMyPage(comment.userSeq)" />
<!--        <img class="profile-img rounded-circle me-3" src="@/images/uploads/profile1.png" alt="프로필" />-->
        <div>
          <p @click="goToMyPage" class="username mb-1">{{ comment.userNickname }}</p>
          <p class="text-muted small">{{ dayjs(comment.regDate).format('YYYY-MM-DD HH:mm:ss') }}</p>
        </div>
      </div>
      <p class="comment-content mt-2">{{ comment.offerContent }}</p>
      <div v-if="comment.offerFilePath" class="comment-image mt-2">
        <img :src="comment.offerFilePath" alt="첨부 이미지" class="img-fluid rounded" />
      </div>
      <div class="comment-actions text-end">
        <button v-if="comment.userSeq===userSeq" class="btn btn-sm btn-outline-secondary me-2" @click="editComment(comment)">수정</button>
        <button v-if="comment.userSeq===userSeq" class="btn btn-sm btn-outline-danger" @click="deleteComment(comment.commentSeq)">삭제</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment-list {
  max-height: 300px;
  overflow-y: auto;
}

.comment-item {
  padding: 10px 0;
}

.profile-img {
  width: 40px;
  height: 40px;
}

.username {
  font-size: 16px;
  font-weight: bold;
}

.comment-content {
  font-size: 14px;
}

.comment-input textarea {
  resize: none;
}
</style>
