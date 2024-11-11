<script setup>
import dayjs from "dayjs";
import router from "@/router/index.js";

const props = defineProps({
  offerList: {
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
          <th>댓글 내용</th>
          <th>비밀댓글 여부</th>
          <th>작성일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(counselOffer, index) in offerList" :key="index">
          <td>{{ index + 1 + (currentPage - 1) * pageSize }}</td>
          <td style="cursor:pointer;" @click="goCounselHireDetail(counselOffer.hireSeq)">{{ counselOffer.offerContent }}</td>
          <td>{{ counselOffer.offerPrivateYn }}</td>
          <td>{{ dayjs(counselOffer.regDate).format("YYYY-MM-DD HH:mm:ss") }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>

</style>