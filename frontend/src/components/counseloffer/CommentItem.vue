<script setup>
import {computed, defineProps} from 'vue';

const props = defineProps({
  author: {
    type: String,
    required: true
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
    required: true
  },
  authorImage: {
    type: String,
    required: true
  },
  attachedImage: {
    type: String,
    required: false
  }
});

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
};


// 이미지 경로가 'uploads/' 하위에 있을 경우 상대 경로를 구성하는 헬퍼 함수
// const getImagePath = (path) => {
//   return path ? `../../../../uploads/${path}` : '';
// };

const formattedDate = computed(() => formatDate(props.date));
const authorImagePath = computed(() => getImagePath(props.authorImage));
const attachedImagePath = computed(() => getImagePath(props.attachedImage));
</script>

<template>
<!--  <div class="comment-item">-->
<!--    &lt;!&ndash; 작성자 정보 &ndash;&gt;-->
<!--    <div class="comment-header">-->
<!--      <img :src="authorImage" alt="작성자 프로필" class="profile-image" />-->
<!--      <p class="author-info">-->
<!--        <strong>{{ author }}</strong> - {{ ageGroup }} {{ gender }}-->
<!--      </p>-->
<!--      <p class="comment-date">{{ date }}</p>-->
<!--    </div>-->

<!--    &lt;!&ndash; 댓글 내용 &ndash;&gt;-->
<!--    <p v-if="isPrivate" class="private-comment">비밀 댓글입니다.</p>-->
<!--    <p v-else class="comment-content">{{ content }}</p>-->

<!--    &lt;!&ndash; 첨부 이미지 &ndash;&gt;-->
<!--    <div v-if="attachedImage" class="attached-image">-->
<!--      <img :src="attachedImage" alt="첨부 이미지" />-->
<!--    </div>-->
<!--  </div>-->

  <li class="comment-item">
    <!-- 작성자 정보 -->
    <div class="comment-header">
      <img :src="authorImagePath" alt="작성자 프로필" class="profile-image" />
      <p class="author-info">
        <strong>{{ author }}</strong> - {{ ageGroup }} {{ gender }}
      </p>
      <p class="comment-date">{{ formattedDate }}</p>
    </div>

    <!-- 댓글 내용 -->
    <p v-if="isPrivate === 'Y'" class="private-comment">비밀 댓글입니다.</p>
    <p v-else class="comment-content">{{ content }}</p>

    <!-- 첨부 이미지 -->
    <div v-if="attachedImage" class="attached-image">
      <img :src="attachedImagePath" alt="첨부 이미지" />
    </div>
  </li>
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

.attached-image img {
  max-width: 100%;
  margin-top: 10px;
  border-radius: 5px;
}
</style>