<script setup>
import { defineComponent, ref } from 'vue';
import CommentList from '@/components/counseloffer/CommentList.vue';
import CommentForm from '@/components/counseloffer/CommentForm.vue';

// export default defineComponent({
//   components: { CommentList, CommentForm },
//   setup() {
//     const comments = ref([
//       // 예시 데이터
//       { author: '김유지', ageGroup: '20대', gender: '여자', date: '2024.10.22', content: '예시 댓글 내용', isPrivate: false },
//       // 추가 댓글들...
//     ]);
//
//     const addComment = (newComment) => {
//       comments.value.push({
//         author: '익명', // 사용자 정보가 없으므로 익명 처리
//         ageGroup: '30대', // 예시 정보
//         gender: '여자',
//         date: new Date().toLocaleString(),
//         content: newComment.content,
//         isPrivate: newComment.isPrivate
//       });
//     };
//
//     return {
//       comments,
//       addComment
//     };
//   }
// });
</script>

<template>
  <div class="comment-section">
    <CommentList :comments="comments" />
<!--    <CommentForm @submit-comment="addComment" />-->
  </div>
</template>

<style scoped>

</style>