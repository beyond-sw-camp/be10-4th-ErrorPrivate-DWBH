
<script setup>
import { ref, computed, defineProps } from 'vue';
import {useAuthStore} from "@/stores/auth.js";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  offerSeq: {
    type: Number,
    required: true
  },
  hireSeq: {
    type: Number,
    required: true
  },
  userSeq: {
    type: Number,
    required: true
  },
  offerContent: {
    type: String,
    required: true
  },
  offerPrivateYn: {
    type: String, // 'Y' or 'N' Enum 타입을 문자열로 설정
    required: true
  },
  offerFilePath: {
    type: String,
    required: false,
    default: '@/images/images.jpeg'
  },
  regDate: {
    type: String,
    required: true
  },
  modDate: {
    type: String,
    required: false
  },
  userNickname: {
    type: String,
    required: true
  },
  userGender: {
    type: String,
    required: true
  },
  userBirthday: {
    type: String,
    required: true
  },
  userStatus: {
    type: String,
    required: true
  },
  userProfilePath: {
    type: String,
    required: false,
    default: '@/images/profile-image.jpg'
  },
  postOwnerSeq: {
    type: Number,
    required: true
  }
});

const authStore = useAuthStore();
const currentUserSeq = authStore.userSeq;

// emit 정의
const emit = defineEmits(['commentDeleted']);

const showMenu = ref(false);
const showDeleteConfirm = ref(false);

const goToMyPage = () => {
  router.push({ name: 'MyPage', params: { userSeq: props.userSeq } });
};

// 본인이 작성한 댓글인지 확인
const isAuthor = computed(() => props.userSeq === currentUserSeq);

// 비밀 댓글 여부 확인: 본인이 작성한 댓글이거나 공개 댓글이면 내용 표시
const canViewContent = computed(() => {
  const isAuthor = props.userSeq === currentUserSeq; // 댓글 작성자와 현재 사용자가 동일한지 확인
  const isPostOwner = props.postOwnerSeq === currentUserSeq; // 글 작성자와 현재 사용자가 동일한지 확인
  const isPublicComment = props.offerPrivateYn !== 'Y'; // 공개 댓글 여부

  return isPublicComment || isAuthor || isPostOwner;
});

// 몇 년생인지 확인하여 나이대를 계산하는 computed
const ageGroup = computed(() => {
  // userBirthday에서 연도 추출
  const birthYear = new Date(props.userBirthday).getFullYear();
  const currentYear = new Date().getFullYear();
  const age = currentYear - birthYear;

  // 나이대를 10대로 묶어서 계산 (예: 25살이면 20대)
  const ageGroup = Math.floor(age / 10) * 10;

  return `${ageGroup}대`;
});

// 성별을 한국어로 표시
const formattedGender = computed(() => {
  return props.userGender === 'male' ? '남자' : '여자';
});

// 사용자 활성화 상태 확인
const isActiveUser = computed(() => props.userStatus === 'activate');

// 사용자 닉네임 표시
const displayName = computed(() => {
  return isActiveUser.value ? props.userNickname : '탈퇴된 회원';
});


// 날짜 형식을 포맷팅하여 표시할 날짜 계산
const formattedDate = computed(() => {
  const regDateObj = new Date(props.regDate);
  const modDateObj = props.modDate ? new Date(props.modDate) : null;

  // 수정일이 작성일보다 나중인 경우 수정일을 표시, 그렇지 않으면 작성일을 표시
  const displayDate = modDateObj && modDateObj > regDateObj ? modDateObj : regDateObj;

  return displayDate.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
});

// 이미지 경로 설정
const authorImagePath = computed(() => props.userProfilePath ? `http://localhost:8089${props.userProfilePath}` : '@/images/profile-image.jpg');
const attachedImagePath = computed(() => props.offerFilePath ? `http://localhost:8089${props.offerFilePath}` : '@/images/profile-image.jpg');

// 삭제확인 모달
const confirmDelete = () => {
  showDeleteConfirm.value = true;
};

// 댓글 삭제 요청
const deleteComment = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    await axios.delete(`http://localhost:8089/api/v1/hire-post/${props.hireSeq}/comment/${props.offerSeq}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    alert("댓글이 삭제되었습니다.");
    emit('commentDeleted', props.offerSeq);
    showDeleteConfirm.value = false;
    window.location.reload();
  } catch (error) {
    console.error("댓글 삭제 중 오류 발생:", error);
    if (error.response.status === 404) {
      alert('이미 삭제된 댓글이거나 삭제된 글의 댓글입니다.');
    }
    alert("댓글 삭제에 실패했습니다.");
  }
};

const editComment = async () => {
};
</script>

<template>
  <li class="comment-item">
    <!-- 작성자 정보 -->
    <div class="comment-header">
      <img :src="authorImagePath" alt="작성자 프로필" class="profile-image"  @click="goToMyPage" />
      <span class="author-info"  @click="goToMyPage" v-if="isActiveUser"><strong>{{ displayName }}</strong></span>

        <!-- 탈퇴된 회원일 경우 -->
        <p v-else class="deactivated-user">탈퇴된 회원</p>
      <p class="author-info" v-if="isActiveUser">{{ ageGroup }} {{ formattedGender }}
      </p>
      <p class="comment-date">{{ formattedDate }}</p>

      <!-- 현재 사용자와 작성자가 같을 때만 표시되는 메뉴 -->
      <div v-if="isAuthor" class="comment-menu">
        <div @click="showMenu = !showMenu">...</div>
        <div v-if="showMenu" class="menu-options">
          <div @click="editComment">수정</div>
          <div @click="confirmDelete">삭제</div>
        </div>
      </div>
    </div>

    <!-- 댓글 내용 (비밀 댓글일 경우 표시 제한) -->
    <p v-if="!canViewContent" class="private-comment">비밀 댓글입니다.</p>
    <p v-else class="comment-content">{{ offerContent }}</p>

    <!-- 첨부 이미지 (첨부 이미지가 있을 경우에만 표시) -->
    <div v-if="offerFilePath" class="attached-image">
      <img :src="attachedImagePath" alt="첨부 이미지" />
    </div>

    <!-- 삭제 확인 모달 -->
    <div v-if="showDeleteConfirm" class="delete-confirm">
      <p>정말로 삭제하시겠습니까?</p>
      <button @click="deleteComment">확인</button>
      <button @click="showDeleteConfirm = false">취소</button>
    </div>
  </li>
</template>

<style scoped>
.comment-item {
  border-bottom: 1px solid #e0e0e0;
  padding: 10px 0;
  position: relative;
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
  display: inline-block;
}

.comment-menu {
  position: relative;
  display: inline-block;
  margin-left: 8px; /* 작성일과 약간의 간격 추가 */
}

.menu-options {
  position: absolute;
  right: 0;
  top: 100%;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 10;
  width: 60px;
}

.menu-options div {
  display: block;
  width: 100%;
  padding: 4px 8px;
  text-align: center;
  border: none;
  background: none;
  font-size: 0.9em;
  cursor: pointer;
  border-radius: 4px; /* 기본 둥근 모양 */
}

.menu-options div:hover {
  background-color: beige;
  border-radius: 12px;
}

.private-comment {
  color: #aaa;
  font-style: italic;
}

.comment-content {
  white-space: pre-wrap;
}

.attached-image img {
  max-width: 100%;
  margin-top: 10px;
  border-radius: 5px;
}

.delete-confirm {
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 10px;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  border-radius: 5px;
  text-align: center;
}

.delete-confirm button {
  margin: 5px;
}

.profile-image,
.author-info {
  cursor: pointer;
}

.deactivated-user {
  color: #888;
  font-style: italic;
}

</style>
