<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import "@/css/style.css"
import {useRoute, useRouter} from "vue-router";
import CounselDetail from "@/components/counsel/CounselDetail.vue";
import CounselUpdateView from "@/views/counsel/CounselUpdateView.vue";

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

const openModifyForm = (counselHire) => {
  modifyCounselHire.value = counselHire; // 수정할 데이터 저장
  isModifyVisible.value = true; // 수정 창 열림 상태로 변경
};

const closeModifyForm = () => {
  isModifyVisible.value = false; // 수정 창 닫음
  modifyCounselHire.value = null; // 데이터 초기화
};
</script>

<template>
  <div class="container content">
    <CounselDetail v-if="!isModifyVisible"
        :hireSeq="hireSeq"
        :counselHire="counselHire"
        :counselorAgeRanges="counselorAgeRanges"
        :counselorTypes="counselorTypes"
        :sendUserSeq="sendUserSeq"
        @openModifyForm="openModifyForm"/>
    <CounselUpdateView v-else :modifyCounselHire="modifyCounselHire"/>
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