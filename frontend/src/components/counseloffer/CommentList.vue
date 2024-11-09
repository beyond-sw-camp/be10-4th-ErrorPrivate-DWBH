<script setup>
import CommentItem from './CommentItem.vue';
import { defineProps } from 'vue';

// 댓글 목록 데이터를 props로 받아서 각 댓글을 CommentItem에 전달
const props = defineProps({
  comments: {
    type: Array,
    required: true
  }
});
</script>

<template>
  <div class="comment-list">
    <CommentItem
        v-for="(comment, index) in comments"
        :key="index"
        :author="comment.author"
        :authorImage="comment.authorImage"
        :ageGroup="comment.ageGroup"
        :gender="comment.gender"
        :date="comment.date"
        :content="comment.content"
        :attachedImage="comment.attachedImage"
    />
  </div>
</template>

<style scoped>
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 15px; /* 댓글 간격 */
  padding: 10px 0;
  max-height: 500px; /* 최대 높이 지정하여 스크롤 가능 */
  overflow-y: auto; /* 댓글이 많을 경우 스크롤 */
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
}
</style>