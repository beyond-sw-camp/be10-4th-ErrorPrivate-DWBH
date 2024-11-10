<script setup>

import {ref, defineEmits, computed} from "vue";

const props = defineProps({
  totalCount: { // 전체 개수
    type: Number,
    required: true,
  }
});

const emit = defineEmits(['sendPagination']);

const currentPage = ref(1); // 현재 페이지
const pageSize = ref(10); // 페이지당 항목 수
// 총 페이지 수 계산
const totalPages = computed(() => {
  return Math.ceil(props.totalCount / pageSize.value);
});

// 페이지 이동 함수
const goToPage = (page) => {
  if (page > 0 && page <= totalPages.value) {
    currentPage.value = page;
  }

  const pagination = {
    currentPage: currentPage.value,
    pageSize: pageSize.value
  }

  emit('sendPagination', pagination); // 이벤트와 데이터를 emit
};
</script>

<template>
  <div class="pagination-container">
    <ul class="pagination">
      <!-- 이전 페이지 버튼 -->
      <li
          class="page-item"
          :class="{ disabled: currentPage === 1 }"
          @click="goToPage(currentPage - 1)"
      >
        <span class="page-link">이전</span>
      </li>

      <!-- 페이지 번호 버튼 -->
      <li
          v-for="page in totalPages"
          :key="page"
          class="page-item"
          :class="{ active: page === currentPage }"
          @click="goToPage(page)"
      >
        <span class="page-link">{{ page }}</span>
      </li>

      <!-- 다음 페이지 버튼 -->
      <li
          class="page-item"
          :class="{ disabled: currentPage === totalPages }"
          @click="goToPage(currentPage + 1)"
      >
        <span class="page-link">다음</span>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination {
  display: flex;
  list-style: none;
  padding: 0;
}

.page-item {
  margin: 0 5px;
}

.page-item.disabled .page-link {
  color: #ccc;
  pointer-events: none;
  cursor: not-allowed;
}

.page-item.active .page-link {
  font-weight: bold;
  color: #fff;
  background-color: #007bff;
  border-radius: 5px;
}

.page-link {
  display: block;
  padding: 5px 10px;
  border: 1px solid #ddd;
  color: #007bff;
  cursor: pointer;
  text-decoration: none;
}

.page-link:hover {
  background-color: #f1f1f1;
}
</style>