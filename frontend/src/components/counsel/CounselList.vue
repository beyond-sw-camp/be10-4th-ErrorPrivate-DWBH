<script setup>
import dayjs from "dayjs";
import router from "@/router/index.js";

const props = defineProps({
  counselHires: {
    type: Array,
    required: true
  },
  currentPage: {
    type: Number,
    required: true
  },
  pageSize: {
    type: Number,
    required: true
  },
  totalCount: {
    type: Number,
    required: true
  }
});

const goCounselHireDetail = (hireSeq) => {
  router.push(`/counsel/${hireSeq}`);
};
</script>

<template>
  <div class="table-wrapper">
    <div class="mb-3">총 게시글 수 : {{totalCount}}</div>
    <table class="table table-bordered text-center align-middle">
      <thead class="table-light">
      <tr>
        <th>No</th>
        <th>제목</th>
        <th>작성자</th>
        <th>희망 성별</th>
        <th>희망 나이대</th>
        <th>희망 조언 유형</th>
        <th>작성일</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(counselHire, index) in counselHires" :key="index">
        <td>{{ index + 1 + (currentPage - 1) * pageSize }}</td>
        <td style="cursor:pointer;" @click="goCounselHireDetail(counselHire.hireSeq)">{{ counselHire.hireTitle }}</td>
        <td>{{ counselHire.userNickname }}</td>
        <td>{{ counselHire.hireGender === "male" ? "남성" : "여성" }}</td>
        <td>
          <template v-if="counselHire.ageRanges.length === 0">
            무관
          </template>
          <template v-else>
            <template v-for="(ageRange, index) in counselHire.ageRanges" :key="index">
              {{ ageRange.counselorAgeRange }}&nbsp;
            </template>
          </template>
        </td>
        <td>
          <template v-if="counselHire.types.length === 0">
            무관
          </template>
          <template v-else>
            <template v-for="(type, index) in counselHire.types" :key="index">
              {{ type.counselorType }}&nbsp;
            </template>
          </template>
        </td>
        <td>{{ dayjs(counselHire.regDate).format("YYYY-MM-DD HH:mm:ss") }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>

</style>