<script setup>

</script>


<template>
  <div class="container content">
    <div class="text-section">
      <h1>마음을 이어주는 다리,</h1>
      <p>
        서로의 이야기에 귀 기울이며<br />
        따뜻한 위로와 공감을 나누는 공간입니다.<br />
        당신의 고민을 함께 나누고,<br />
        서로의 마음을 이어가며<br />
        희망과 용기를 찾는 여정에 동행합니다.
      </p>
      <button class="cta-btn">마음을 이어주는 다리 구경하기</button>
    </div>
    <div class="image-section">
      <img src="@/images/main.png" alt="hands reaching for heart" />
    </div>
  </div>
</template>

<style scoped>


.content {
  display: flex;
  align-items: center;
  justify-content: center; /* 가운데 정렬 */
  max-width: 16000px; /* 최대 너비 설정 */
  margin: 0 auto; /* 가운데 정렬 */
  gap: 40px; /* 텍스트와 이미지 간의 간격 조절 */
  height: 100vh; /* 화면 전체 높이 설정 */
  box-sizing: border-box;
  user-select: none;
}

.text-section {
  max-width: 500px;
}

.text-section h1 {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 20px;
}

.text-section p {
  line-height: 1.6;
  margin-bottom: 20px;
}

.cta-btn {
  padding: 10px 20px;
  border: none;
  background-color: #bfa68b;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.image-section {
  width: 300px;
  height: 300px;
  background-color: #bfa68b;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-section img {
  max-width: 100%;
  max-height: 100%;
}

button {
  padding: 0.8rem 1.2rem;
  font-size: 1rem;
  cursor: pointer;
}

</style>
