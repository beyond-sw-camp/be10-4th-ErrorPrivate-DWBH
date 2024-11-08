<script setup>
import {computed, onMounted, ref} from 'vue';
import ButtonSmallColor from "@/components/common/ButtonSmallColor.vue";
import ModalSmall from "@/components/common/ModalSmall.vue";
import {useRoute, useRouter} from "vue-router";
import {readEvaluation} from "@/util/evaluationApi.js";
import {useAuthStore} from "@/stores/auth.js";

// 평가 조회 api 호출 후 데이터
const {evaluationdata, readEvaluationData} = readEvaluation();

const authStore = useAuthStore();
// accessToken 이 있으면 로그인한 상태
const isLoggedIn = computed(() => !!authStore.accessToken);

const router = useRouter();
// 라우터로 온 데이터 받기
const route = useRoute();

// 평가 상태 여부
const isEvaluation = ref(false);

// 댓글 조회 받아오기 api 호출

// 평가 데이터
const evaluationItems = ref([
  {label: '따뜻한 소리에 만족하셨나요?', rating: 0, isStar: true},
  {label: '따뜻한 소리를 나눈 사람은 소통이 원활하였나요?', rating: 0, isStar: false},
  {label: '상담사가 친절하였나요?', rating: 0, isStar: true}
]);

// 별점을 설정하는 함수
const rate = (item, rating) => {
  item.rating = rating;
};



// 삭제 확인 모달창
const isModalVisible = ref(false);
const confirmModal = async () => {
  isModalVisible.value = false;
  // await exitUser();
  // await router.push(`/shoppinggroup`);
}

const closeModal = () => {
  isModalVisible.value = false;
}


// 평가를 저장하는 함수
const saveEvaluation = () => {
  // 실제 저장 로직 구현
  // 창닫기 & api 호출
  // alert('평가가 저장되었습니다!');
};

// 평가 작성 취소하는 함수
const exitEvaluation = () => {
  // 실제 저장 로직 구현
  // 창닫기 & api 호출
  // alert('평가가 저장되었습니다!');
};

// 평가를 수정하는 함수
const updateEvaluation = () => {
  // 실제 저장 로직 구현
  // 창닫기 & api 호출
  // alert('평가가 저장되었습니다!');
};

// 평가를 삭제하는 함수
const deleteEvaluation = () => {
  isModalVisible.value = true;
  // 실제 저장 로직 구현
  // 창닫기 & api 호출
  // alert('평가가 저장되었습니다!');
};

onMounted(() => {
  if (isEvaluation.value) {
    readEvaluationData();
  }
});
</script>>

<template>
  <div class="evaluation-container">
    <!-- Header 정보 -->
    <div class="header">
      <span>따뜻한 소리 나눔 / 평가</span>
    </div>

    <!-- 평가 항목 댓글 api 호출 필요 -->
    <div class="main-section">
      <div class="user-section">
        <div class="user-avatar">
          <img src="@/images/Error-pirate.jpg" alt="User Avatar"/>
        </div>
        <div class="user-details">
          <div class="user-top">
            <div class="user-name">행복한 사람</div>
            <div class="user-age">20대 남자</div>
          </div>
          <div class="user-message">
            안녕하세요! 저도 요즘 비슷한 고민을 하고 있어서 공감이 많이 되네요. 관계에서 오는 스트레스를 줄이는
            방법이 있을까요? 혹시 도움이 될 만한 조언이나 경험이 있으시다면 함께 나눠주시면 정말 감사하겠습니다.
            힘내시고, 저희도 함께 해결 방법을 찾아봐요!
          </div>
        </div>
        <div class="date">2024. 10.22 11:00:25</div>
      </div>

      <div class="evaluation-section">
        <div class="user-name">
          <span>평가항목</span>
          <ButtonSmallColor v-if="!isEvaluation" class="black-button" @click="deleteEvaluation">삭제</ButtonSmallColor>
        </div>

        <div class="evaluation-warning">
          소중한 평가를 남겨주셔서 감사합니다. 개인 의견을 바탕으로 더욱 나은 서비스를 제공하기 위해 노력하겠습니다.
        </div>

        <!-- 평가 항목 리스트 -->
        <div class="evaluation-items">
          <div class="evaluation-item" v-for="(item, index) in evaluationItems" :key="index">
            <span>{{ item.label }}</span>
            <div class="icons">
              <template v-if="item.isStar">
              <span
                  v-for="star in 5"
                  :key="star"
                  :class="['star', { filled: item.rating >= star }]"
                  @click="rate(item, star)"
              >★</span>
              </template>

              <template v-else>
                <div
                    v-for="icon in 5"
                    :key="icon"
                    :class="['icon', { filled: item.rating === icon }]"
                    @click="rate(item, icon)"
                >
                  <svg v-if="icon === 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                    <path
                        d="M256 48a208 208 0 1 1 0 416 208 208 0 1 1 0-416zm0 464A256 256 0 1 0 256 0a256 256 0 1 0 0 512zm72.4-118.5c9.7-9 10.2-24.2 1.2-33.9C315.3 344.3 290.6 328 256 328s-59.3 16.3-73.5 31.6c-9 9.7-8.5 24.9 1.2 33.9s24.9 8.5 33.9-1.2c7.4-7.9 20-16.4 38.5-16.4s31.1 8.5 38.5 16.4c9 9.7 24.2 10.2 33.9 1.2zM176.4 272c17.7 0 32-14.3 32-32c0-1.5-.1-3-.3-4.4l10.9 3.6c8.4 2.8 17.4-1.7 20.2-10.1s-1.7-17.4-10.1-20.2l-96-32c-8.4-2.8-17.4 1.7-20.2 10.1s1.7 17.4 10.1 20.2l30.7 10.2c-5.8 5.8-9.3 13.8-9.3 22.6c0 17.7 14.3 32 32 32zm192-32c0-8.9-3.6-17-9.5-22.8l30.2-10.1c8.4-2.8 12.9-11.9 10.1-20.2s-11.9-12.9-20.2-10.1l-96 32c-8.4 2.8-12.9 11.9-10.1 20.2s11.9 12.9 20.2 10.1l11.7-3.9c-.2 1.5-.3 3.1-.3 4.7c0 17.7 14.3 32 32 32s32-14.3 32-32z"/>
                  </svg>
                  <svg v-if="icon === 2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                    <path
                        d="M464 256A208 208 0 1 0 48 256a208 208 0 1 0 416 0zM0 256a256 256 0 1 1 512 0A256 256 0 1 1 0 256zM174.6 384.1c-4.5 12.5-18.2 18.9-30.7 14.4s-18.9-18.2-14.4-30.7C146.9 319.4 198.9 288 256 288s109.1 31.4 126.6 79.9c4.5 12.5-2 26.2-14.4 30.7s-26.2-2-30.7-14.4C328.2 358.5 297.2 336 256 336s-72.2 22.5-81.4 48.1zM144.4 208a32 32 0 1 1 64 0 32 32 0 1 1 -64 0zm192-32a32 32 0 1 1 0 64 32 32 0 1 1 0-64z"/>
                  </svg>
                  <svg v-if="icon === 3" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                    <path
                        d="M464 256A208 208 0 1 1 48 256a208 208 0 1 1 416 0zM256 0a256 256 0 1 0 0 512A256 256 0 1 0 256 0zM176.4 240a32 32 0 1 0 0-64 32 32 0 1 0 0 64zm192-32a32 32 0 1 0 -64 0 32 32 0 1 0 64 0zM184 328c-13.3 0-24 10.7-24 24s10.7 24 24 24l144 0c13.3 0 24-10.7 24-24s-10.7-24-24-24l-144 0z"/>
                  </svg>
                  <svg v-if="icon === 4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                    <path
                        d="M464 256A208 208 0 1 0 48 256a208 208 0 1 0 416 0zM0 256a256 256 0 1 1 512 0A256 256 0 1 1 0 256zm177.6 62.1C192.8 334.5 218.8 352 256 352s63.2-17.5 78.4-33.9c9-9.7 24.2-10.4 33.9-1.4s10.4 24.2 1.4 33.9c-22 23.8-60 49.4-113.6 49.4s-91.7-25.5-113.6-49.4c-9-9.7-8.4-24.9 1.4-33.9s24.9-8.4 33.9 1.4zM144.4 208a32 32 0 1 1 64 0 32 32 0 1 1 -64 0zm192-32a32 32 0 1 1 0 64 32 32 0 1 1 0-64z"/>
                  </svg>
                  <svg v-if="icon === 5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                    <path
                        d="M464 256A208 208 0 1 0 48 256a208 208 0 1 0 416 0zM0 256a256 256 0 1 1 512 0A256 256 0 1 1 0 256zm130.7 57.9c-4.2-13.6 7.1-25.9 21.3-25.9l212.5 0c14.2 0 25.5 12.4 21.3 25.9C369 368.4 318.2 408 258.2 408s-110.8-39.6-127.5-94.1zM144.4 192a32 32 0 1 1 64 0 32 32 0 1 1 -64 0zm192-32a32 32 0 1 1 0 64 32 32 0 1 1 0-64z"/>
                  </svg>
                </div>

              </template>
            </div>
          </div>
        </div>
      </div>

      <!-- 저장 버튼 -->
      <div class="button-section">
        <div v-if="!isEvaluation">
          <ButtonSmallColor class="black-button" @click="saveEvaluation">저장</ButtonSmallColor>
        </div>
        <div v-else-if="isEvaluation">
          <ButtonSmallColor class="gray-button" @click="exitEvaluation">취소</ButtonSmallColor>
          <ButtonSmallColor class="black-button" @click="updateEvaluation">수정</ButtonSmallColor>
        </div>
      </div>

    </div>
  </div>

  <!-- 삭제 확인 모달창 -->
  <ModalSmall :isVisible="isModalVisible" :message="'정말로 삭제하시겠습니까?'"
              @close="closeModal" @confirm="confirmModal"/>

</template>

<style scoped>
.evaluation-container {
  font-family: Arial, sans-serif;
  background-color: #f5f4ef;
  padding: 20px;
}

.header {
  display: flex;
  align-items: center;
  margin-top: 10px;
  padding: 20px;
  font-size: 20px;
  font-weight: bold;
}

.user-section {
  display: flex;
  flex-direction: row;
  align-items: center;
  flex: 1;
  padding-bottom: 20px;
  border-bottom: 3px solid #CCB997;
  margin: 20px;
}

.user-avatar img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.user-details {
  margin-left: 20px;
}

.user-top {
  display: flex;
  align-items: center;
  flex-direction: row;
}

.user-name {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-left: 30px;
  margin-right: 30px;
}

.user-name span {
  font-weight: bold;
  font-size: 20px;
}

.user-age {
  font-size: 20px;
  color: #666;
  margin-left: 30px;
}

.user-message {
  margin-top: 30px;
  font-size: 20px;
  color: #333;
}

.date {
  width: 30%;
  font-size: 15px;
  color: #888;
  text-align: right;
}

.main-section {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  background-color: #fff;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;

}

.evaluation-section {
  margin: 20px;
}

.evaluation-warning {
  display: flex;
  justify-content: center;
  font-size: 12px;
  color: #d9534f;
  margin-bottom: 20px;
}

.evaluation-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.evaluation-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  width: 60%;
}

.icons {
  display: flex;
  gap: 10px;
}

.star {
  font-size: 40px;
  cursor: pointer;
  color: #ddd;
}

.star.filled {
  color: #f1c40f;
}

.icon {
  width: 40px;
  height: 40px;
  cursor: pointer;
}

.icon.filled {
  fill: red;
}

.button-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

.black-button {
  background-color: #333;
  color: #fff;
  border: none;
  border-radius: 4px;
}

.gray-button {
  background-color: rgba(0, 0, 0, 0.39);
  color: #fff;
  border: none;
  border-radius: 4px;
}

</style>