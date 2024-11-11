<<script setup>
import { defineProps } from 'vue';
import CommentList from '@/components/counseloffer/CommentList.vue';
import CommentForm from '@/components/counseloffer/CommentForm.vue';
import {useRoute} from "vue-router";

const props = defineProps({
  hireSeq: {
    type: Number,
    required: true
  }
});

// 댓글 작성 후 CommentList에 새로고침 요청하는 핸들러
</script>

<template>
  <div class="comment-section">
    <h2>댓글 섹션</h2>
    <CommentList :hireSeq="Number(props.hireSeq)" />
    <CommentForm :hireSeq="Number(props.hireSeq)" />
  </div>
</template>

<style scoped>
.comment-section {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-top: 20px;
  background-color: white;
}

</style>