<script setup>
import "@/css/style.css"
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
      <router-link to="/counsel">
        <button class="cta-btn">마음을 이어주는 다리 구경하기</button>
      </router-link>
    </div>
    <div class="image-section">
      <img src="@/images/main.png" alt="hands reaching for heart" />
    </div>
  </div>
</template>
