<script setup>
const props = defineProps({
  author: {
    type: String,
    required: true
  },
  authorImage: {
    type: String,
    default: ''
  },
  ageGroup: {
    type: String,
    required: true
  },
  gender: {
    type: String,
    required: true
  },
  date: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  },
  isPrivate: {
    type: String,
    default: false
  },
  attachedImage: {
    type: String,
    default: null // 첨부 이미지 URL
  }
});
</script>

<template>
  <div class="comment-item">
    <!-- 작성자 정보 -->
    <div class="comment-header">
      <img :src="authorImage" alt="작성자 프로필" class="profile-image" />
      <p class="author-info">
        <strong>{{ author }}</strong> - {{ ageGroup }} {{ gender }}
      </p>
      <p class="comment-date">{{ date }}</p>
    </div>

    <!-- 댓글 내용 -->
    <p v-if="isPrivate" class="private-comment">비밀 댓글입니다.</p>
    <p v-else class="comment-content">{{ content }}</p>

    <!-- 첨부 이미지 -->
    <div v-if="attachedImage" class="attached-image">
      <img :src="attachedImage" alt="첨부 이미지" />
    </div>
  </div>
</template>

<style scoped>
.comment-item {
  border-bottom: 1px solid #e0e0e0;
  padding: 10px 0;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.profile-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.author-info {
  font-weight: bold;
}

.comment-date {
  margin-left: auto;
  font-size: 0.85em;
  color: #888;
}

.private-comment {
  color: #aaa;
  font-style: italic;
}

.comment-content {
  white-space: pre-wrap; /* 줄바꿈 유지 */
}
</style>