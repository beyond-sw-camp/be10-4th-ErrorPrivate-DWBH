<script setup>
import { defineComponent, ref } from 'vue';

// export default defineComponent({
//   emits: ['submit-comment'],
//   setup(props, { emit }) {
//     const newComment = ref({
//       content: '',
//       isPrivate: false
//     });
//
//     const submitComment = () => {
//       emit('submit-comment', newComment.value);
//       newComment.value = { content: '', isPrivate: false };
//     };
//
//     return {
//       newComment,
//       submitComment
//     };
//   }
// });
</script>

<template>
  <div class="comment-form">
    <textarea v-model="newComment.content" placeholder="따뜻한 손길 나눔 내용을 입력해 주세요."></textarea>
    <label>
      <input type="checkbox" v-model="newComment.isPrivate" /> 비밀 댓글로 작성
    </label>
    <button @click="submitComment">입력</button>
  </div>
</template>

<style scoped>

</style>