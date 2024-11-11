<script setup>
import { defineComponent, ref } from 'vue';
import { defineProps } from 'vue';
import CommentList from '@/components/counseloffer/CommentList.vue';
import CommentForm from '@/components/counseloffer/CommentForm.vue';
import {useRoute} from "vue-router";

// const route = useRoute();
// const hireSeq = Number(route.params.hireSeq); // URL 파라미터로부터 hireSeq를 가져옴
const props = defineProps({
  hireSeq: {
    type: Number,
    required: true
  }
});

// 댓글 작성 후 CommentList에 새로고침 요청하는 핸들러
// const handleCommentSubmitted = () => {
//   // CommentList 컴포넌트에 새로고침 요청을 전달
//   comments.value = [...comments.value]; // 이 방법으로 상태가 변경되었다고 알림
// };
</script>

<template>
<!--  <div class="comment-section">-->
<!--    <CommentList :comments="comments" />-->
<!--&lt;!&ndash;    <CommentForm @submit-comment="addComment" />&ndash;&gt;-->
<!--  </div>-->
  <div class="comment-section">
    <h2>댓글 섹션</h2>
    <CommentList :hireSeq="Number(props.hireSeq)" />
<!--    <CommentForm :hireSeq="Number(props.hireSeq)" @comment-submitted="handleCommentSubmitted" />-->
    <CommentForm :hireSeq="Number(props.hireSeq)" />
  </div>
</template>

<style scoped>
.comment-section {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-top: 20px;
  width: 800px;
  background-color: white;
}

</style>