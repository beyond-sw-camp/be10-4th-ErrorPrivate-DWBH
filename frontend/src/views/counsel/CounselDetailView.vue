<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import "@/css/style.css"
import {useRoute, useRouter} from "vue-router";
import CounselDetail from "@/components/counsel/CounselDetail.vue";

const router = useRouter();
// 라우터로 온 데이터 받기
const route = useRoute();

const hireSeq = route.params.hireSeq;

const counselHire = ref([]);
const counselorAgeRanges = ref([]);
const counselorTypes = ref([]);
const sendUserSeq = ref(0);
const isModifyVisible = ref(false);

const fetchCounselHires = async () => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/counselor-hire/${hireSeq}`);

    sendUserSeq.value = response.data.userSeq;
    counselHire.value = response.data;
    counselorAgeRanges.value = response.data.counselorAgeRanges;
    counselorTypes.value = response.data.counselorTypes;
  } catch (error) {
    console.error('게시판 상세조회 가져오는 중 오류 발생', error)
  }
}

onMounted(() => {
  fetchCounselHires()
})

const modifyCounselHire = ref(null);
function modifyForm(counselHire) {
  isModifyVisible.value = true;
}
</script>

<template>
  <div class="container content">
    <CounselDetail v-if="!isModifyVisible"
        :hireSeq="hireSeq"
        :counselHire="counselHire"
        :counselorAgeRanges="counselorAgeRanges"
        :counselorTypes="counselorTypes"
        :sendUserSeq="sendUserSeq"/>

  </div>
</template>

<style scoped>
.text-end {
  font-weight: bold;
}

.table th {
  width: 30%;
  font-weight: bold;
}

.table td {
  width: 70%;
}
</style>