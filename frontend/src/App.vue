<script setup>
import Header from '@/components/common/Header.vue';
</script>

<template>
  <Header />
  <router-view />
</template>

<style scoped>
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%; /* 화면 전체 높이 설정 */
}
</style>
