<script setup>
import {useAuthStore} from "@/stores/auth.js";
import dayjs from "dayjs";
import router from "@/router/index.js";
import axios from "axios";
import ModalSmall from "@/components/common/ModalSmall.vue";
import {ref} from "vue";

const props = defineProps({
  comments: {
    type: Array,
    required: true
  }
});

const authStore = useAuthStore();
const userSeq = authStore.userSeq;
const editHireSeq = ref(0);
const editOfferSeq = ref(0); // 현재 수정 중인 댓글 ID
const editedContent = ref(''); // 수정 중인 댓글 내용
const isModalVisible = ref(false);
const deleteOfferSeq = ref(0);
const deleteHireSeq = ref(0);
const imageFile = ref(null);
const previewImage = ref(null);
const offerFilePath = ref(null);

const goToMyPage = (userSeq) => {
  router.push(`/user/${userSeq}/mypage`);
};

const confirmModal = async () => {
  isModalVisible.value = false;

  await deleteComment();
}

const closeModal = () => {
  isModalVisible.value = false;
}

const isDelete = (offerSeq, hireSeq) => {
  isModalVisible.value = true;
  deleteOfferSeq.value = offerSeq;
  deleteHireSeq.value = hireSeq;
}

// 댓글 삭제 요청
const deleteComment = async () => {
  try {
    console.log(deleteOfferSeq.value);
    const token = localStorage.getItem('accessToken');
    await axios.delete(`http://localhost:8089/api/v1/hire-post/${deleteHireSeq.value}/comment/${deleteOfferSeq.value}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    alert("댓글이 삭제되었습니다.");
    isModalVisible.value = false;
    window.location.reload();
  } catch (error) {
    console.error("댓글 삭제 중 오류 발생:", error);
    if (error.response.status === 404) {
      alert('이미 삭제된 댓글이거나 삭제된 글의 댓글입니다.');
    }
    alert("댓글 삭제에 실패했습니다.");
  }
};

// 사진 첨부 버튼 클릭 시 파일 선택
const handleFileSelect = (event) => {
  offerFilePath.value = event.target.files[0];
  if (offerFilePath.value) {
    imageFile.value = offerFilePath.value;
    previewImage.value = URL.createObjectURL(offerFilePath.value);
  }
};

function editComment(comment) {
  editHireSeq.value = comment.hireSeq;
  editOfferSeq.value = comment.offerSeq;
  editedContent.value = comment.offerContent;
}

function cancelEdit() {
  editOfferSeq.value = 0;
}

const saveEdit = async (comment) => {
  try {

    const formData = new FormData();
    const requestData = {
      userSeq: userSeq,
      offerContent: editedContent.value,
      offerPrivateYn: comment.offerPrivateYn
    };

    formData.append("request", new Blob([JSON.stringify(requestData)], { type: 'application/json' }));
    if (offerFilePath.value) {
      formData.append("file", offerFilePath.value);
    }

    // 요청 보내기
    await axios.put(
        `http://localhost:8089/api/v1/hire-post/${editHireSeq.value}/comment/${editOfferSeq.value}`,
        formData, // 두 번째 인자로 formData 전달
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
            'Content-Type': 'multipart/form-data', // Content-Type 설정
          },
        }
    );

    alert("댓글이 수정되었습니다.");
    window.location.reload();
  } catch (error) {
    console.error("댓글 수정 중 오류 발생:", error);

    alert("댓글 수정에 실패했습니다.");
  }
}
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
      <template v-if="editOfferSeq == comment.offerSeq">
        <div class="comment-input-wrapper">
          <textarea v-model="editedContent" class="form-control mt-2" rows="2"></textarea>
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
          <span class="icon-lock" @click="togglePrivateComment">🔒</span>
        </div>

        <div class="text-end mt-2">
          <button class="btn btn-sm btn-outline-secondary me-2" @click="saveEdit(comment)">저장</button>
          <button class="btn btn-sm btn-outline-danger" @click="cancelEdit">취소</button>
        </div>
      </template>
      <template v-else>
        <p class="comment-content mt-2">{{ comment.offerContent }}</p>
        <div v-if="comment.offerFilePath" class="comment-image mt-2">
          <img :src="comment.offerFilePath" alt="첨부 이미지" class="img-fluid rounded" />
        </div>
        <div class="comment-actions text-end">
          <button v-if="comment.userSeq===userSeq" class="btn btn-sm btn-outline-secondary me-2" @click="editComment(comment)">수정</button>
          <button v-if="comment.userSeq===userSeq" class="btn btn-sm btn-outline-danger" @click="isDelete(comment.offerSeq, comment.hireSeq)">삭제</button>
        </div>
      </template>
      <!-- 삭제 확인 모달창 -->
      <ModalSmall :isVisible="isModalVisible" :message="'정말로 삭제하시겠습니까?'"
                  @close="closeModal" @confirm="confirmModal"/>
    </div>
  </div>
</template>

<style scoped>
.comment-input-wrapper {
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  width: 100%;
  background-color: #f9f9f9;
  position: relative;
}
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
