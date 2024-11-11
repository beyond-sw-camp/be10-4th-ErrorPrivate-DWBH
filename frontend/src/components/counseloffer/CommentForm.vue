<script setup>
import { ref } from 'vue';
import axios from 'axios';
import {useAuthStore} from "@/stores/auth.js";

const props = defineProps({
  hireSeq: {
    type: Number,
    required: true
  }
});

const authStore = useAuthStore();
const userSeq = authStore.userSeq;

const newComment = ref({
  offerContent: '',
  offerPrivateYn: false
});
const offerFilePath = ref(null); // 첨부 파일

// 사진 첨부 버튼 클릭 시 파일 선택
const handleFileSelect = (event) => {
  offerFilePath.value = event.target.files[0];
  if (offerFilePath.value) {
    imageFile.value = offerFilePath.value;
    previewImage.value = URL.createObjectURL(offerFilePath.value);
  }
};

// 비밀 댓글 여부 토글
const togglePrivateComment = () => {
  newComment.value.offerPrivateYn = !newComment.value.offerPrivateYn;
};

const submitComment = async () => {
  try {

    const formData = new FormData();
    const requestData = {
      userSeq: userSeq,
      offerContent: newComment.value.offerContent,
      offerPrivateYn: newComment.value.offerPrivateYn ? 'Y' : 'N'
    };

    // request 파트 추가 - JSON 직렬화 후 append
    formData.append("request", new Blob([JSON.stringify(requestData)], { type: 'application/json' }));

    if (offerFilePath.value) {
      formData.append("file", offerFilePath.value);
    }

    // hireSeq를 URL에 포함하여 POST 요청
      await axios.post(`http://localhost:8089/api/v1/hire-post/${props.hireSeq}/comment`, formData, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
        'Content-Type': 'multipart/form-data', // 파일 업로드 시 Content-Type 설정
      },
    });

    window.location.reload();

    // 댓글 제출 후 폼 초기화
    newComment.value.offerContent = '';
    newComment.value.offerPrivateYn = false;

    offerFilePath.value = null;

  } catch (error) {
    console.error("댓글 작성 중 오류 발생:", error);
    if (error.response.status === 409) {
      // 서버에서 ALREADY_COMMENTED(HttpStatus.CONFLICT, "OFFER_ERROR_003") 반환한 경우
      alert('이미 댓글을 작성하였습니다. 한 게시글에는 한 개의 댓글만 작성할 수 있습니다.');
    } else if (error.response.status === 400) {
      alert('이미지 사진만 첨부할 수 있습니다');
    } else if (error.response.status === 403) {
      alert('자신의 글에는 댓글을 작성할 수 없습니다');
    }
  }
};

const imageFile = ref(null);
const previewImage = ref(null);
</script>

<template>
  <div class="comment-form">
    <div class="comment-input-wrapper">
    <textarea v-model="newComment.offerContent"
              placeholder="따뜻한 손길 나눔 내용을 입력해 주세요."
              class="comment-textarea"></textarea>
      <div class="image-upload mt-2">
        <div v-if="previewImage" class="preview-image mt-2">
          <img :src="previewImage" alt="미리보기" class="img-fluid rounded" />
        </div>
      </div>
      <label>
        <input type="file" @change="handleFileSelect" accept="image/*" style="display: none" />
        <span class="icon-camera">📷</span>
      </label>

      <!-- 비밀 댓글 토글 아이콘 -->
      <span class="icon-lock" @click="togglePrivateComment">
          🔒 <span v-if="newComment.offerPrivateYn">(비밀 댓글)</span>
      </span>
      <button @click="submitComment" class="submit-button">입력</button>
    </div>
  </div>
</template>

<style scoped>
.comment-form {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  border: #f4f1ea 1px solid
}

.comment-textarea {
  width: 100%;
  min-height: 80px;
  margin-bottom: 10px;
  resize: none; /* 크기 조절 막기 */

}

.comment-options {
  display: flex;
  align-items: center;
}

.comment-form {
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  width: 100%;
}

.comment-input-wrapper {
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  width: 100%;
  background-color: #f9f9f9;
  position: relative;
}

.comment-textarea {
  flex-grow: 1;
  outline: none;
  border: none;
  padding: 5px;
  min-height: 50px;
  max-height: 100px;
  overflow-y: auto;
  resize: none;
  color: #333;
  background-color: transparent;
}

.comment-textarea::before {
  content: attr(placeholder);
  color: #aaa; /* 플레이스홀더 색상 */
  position: absolute;
  top: 10px;
  left: 10px;
  pointer-events: none;
}

.icon-camera, .icon-lock {
  cursor: pointer;
  font-size: 20px;
  margin-left: 8px;
}

.submit-button {
  padding: 5px 15px;
  background-color: #d3b18a;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  position: absolute;
  right: 10px;
  bottom: 10px;
}
</style>