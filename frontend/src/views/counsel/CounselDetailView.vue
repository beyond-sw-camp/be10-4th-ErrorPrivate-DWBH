<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import dayjs from "dayjs";
import "@/css/style.css"
import {useRoute, useRouter} from "vue-router";

  const router = useRouter();
  // 라우터로 온 데이터 받기
  const route = useRoute();

  const chatSeq = route.params.chatSeq;

  const counselHire = ref([]);

  const fetchCounselHires = async () => {
    try {
      const response = await axios.get(`http://localhost:8089/api/v1/counselor-hire/${chatSeq}`);
      console.log(response.data);
      counselHire.value = response.data;

    } catch (error) {
      console.error('게시판 상세조회 가져오는 중 오류 발생', error)
    }
  }

  onMounted(() => {
    fetchCounselHires()
  })
</script>

<template>
  <div class="container content">
  </div>
</template>

<style scoped>
</style>