<script setup>
import { ref } from 'vue';
import axios from 'axios';

const props = defineProps({
  hireSeq: {
    type: Number,
    required: true
  }
});
console.log("Received hireSeq:", props.hireSeq);


// console.log("userSeq:" ,userSeq);

const newComment = ref({
  offerContent: '',
  offerPrivateYn: false
});
const offerFile = ref(null); // 첨부 파일

// 사진 첨부 버튼 클릭 시 파일 선택
const handleFileSelect = (event) => {
  offerFile.value = event.target.files[0];
};

// 비밀 댓글 여부 토글
const togglePrivateComment = () => {
  newComment.value.offerPrivateYn = !newComment.value.offerPrivateYn;
  console.log(newComment.value.offerPrivateYn)
};

const submitComment = async () => {
  try {
    const token = localStorage.getItem('token');

    const formData = new FormData();
    formData.append("offerContent", newComment.value.offerContent);
    formData.append("offerPrivateYn", newComment.value.offerPrivateYn ? 'Y' : 'N');

    if (offerFile.value) {
      formData.append("file", offerFile.value);
    }

    // hireSeq를 URL에 포함하여 POST 요청
    await axios.post(`/api/v1/hire-post/${props.hireSeq}/comment`, formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'multipart/form-data', // 파일 업로드 시 Content-Type 설정
      },
    });

    // 댓글 제출 후 폼 초기화
    newComment.value.offerContent = '';
    newComment.value.offerPrivateYn = false;
    offerFile.value = null;

    // 댓글 목록 새로고침 로직 추가 필요


  } catch (error) {
    console.error("댓글 작성 중 오류 발생:", error);
  }
};
</script>

<template>
  <div class="comment-form">
    <div class="comment-input-wrapper">
      <!-- contenteditable div를 textarea처럼 사용 -->
<!--      <div-->
<!--          contenteditable="true"-->
<!--          class="comment-textarea"-->
<!--          @input="(e) => newComment.value.offerContent = e.target.innerText"-->
<!--          aria-placeholder="따뜻한 손길을 나눌 내용을 입력해 주세요"-->
<!--      ></div>-->
    <textarea v-model="newComment.offerContent"
              placeholder="따뜻한 손길 나눔 내용을 입력해 주세요."
              class="comment-textarea"></textarea>
<!--    <label>-->
<!--      <input type="checkbox" v-model="newComment.offerPrivateYn" /> 비밀 댓글로 작성-->
<!--    </label>-->
<!--    <div class="comment-options">-->
      <!-- 사진 첨부 아이콘 -->
      <label>
        <input type="file" @change="handleFileSelect" accept="image/*" style="display: none" />
        <span class="icon-camera">📷</span>
      </label>

      <!-- 비밀 댓글 토글 아이콘 -->
      <span class="icon-lock" @click="togglePrivateComment">
          🔒 <span v-if="newComment.offerPrivateYn">(비밀 댓글)</span>
      </span>
<!--      </div>-->

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