<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";
import dayjs from "dayjs";
import "@/css/style.css";
import Pagination from "@/components/common/Pagination.vue";
import router from "@/router/index.js";
import CounselList from "@/components/counsel/CounselList.vue";

const counselHires = ref([]);
const paginationCounselHires = ref([]);
const counselTypes = ref([]);
const counselAgeRanges = ref([]);
const hopeAgeSeq = ref(0);
const hopeGender = ref("");
const hopeTypeSeq = ref(0);
const searchTitle = ref("");

const totalCount = ref(0); // 전체 개수
const currentPage = ref(1); // 현재 페이지
const pageSize = ref(10); // 페이지당 항목 수

const fetchCounselHires = async () => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/counselor-hire`, {
      params: {
        searchAgeSeq: hopeAgeSeq.value,
        searchGender: hopeGender.value,
        searchTypeSeq: hopeTypeSeq.value,
        searchTitle: searchTitle.value,
        page: currentPage.value - 1, // Spring은 0부터 시작
        size: pageSize.value // 페이지당 항목 수
      },
    });

    counselHires.value = response.data.counselorList.content;
    totalCount.value = counselHires.value.length;
    counselTypes.value = response.data.counselorTypeList;
    counselAgeRanges.value = response.data.counselorAgeList;

    paginate();
  } catch (error) {
    console.error('게시판 목록을 가져오는 중 오류 발생', error);
  }
};

// 현재 페이지에 따라 데이터 나누기
const paginate = () => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  console.log(start, end);
  paginationCounselHires.value = counselHires.value.slice(start, end);
};

const receivePagination = (page) => {
  currentPage.value = page.currentPage;
  pageSize.value = page.pageSize;

  fetchCounselHires();
};

const goCounselHireCreate = () => {
  router.push("/counsel/counselCreate"); // 등록 페이지로 이동
};

onMounted(() => {
  fetchCounselHires();
});
</script>

<template>
  <div class="container content">
    <div class="responsive-container mt-4">
      <!-- 제목 및 목록 버튼 -->
      <div class="title-container d-flex justify-content-between align-items-center">
        <h2 class="title-text">마음을 이어주는 다리</h2>
        <button class="btn btn-dark" @click="goCounselHireCreate">글쓰기</button>
      </div>

      <!-- 테이블 -->
      <div class="table-container">
        <!-- 필터 및 검색 -->
        <div class="filter-container row mb-3 justify-content-end">
          <div class="col-md-2">
            <select class="form-select" v-model="hopeAgeSeq">
              <option value="0" selected>희망 나이대</option>
              <option v-for="(counselAgeRange, index) in counselAgeRanges" :key="index" :value="counselAgeRange.counselorAgeRangeSeq">
                {{ counselAgeRange.counselorAgeRange }}
              </option>
            </select>
          </div>
          <div class="col-md-2">
            <select class="form-select" v-model="hopeGender">
              <option value="" selected>희망 성별</option>
              <option value="male">남자</option>
              <option value="female">여자</option>
            </select>
          </div>
          <div class="col-md-2">
            <select class="form-select" v-model="hopeTypeSeq">
              <option value="0" selected>희망 조언 유형</option>
              <option v-for="(counselorType, index) in counselTypes" :key="index" :value="counselorType.counselorTypeSeq">
                {{ counselorType.counselorType }}
              </option>
            </select>
          </div>
          <div class="col-md-3">
            <div class="input-group">
              <input type="text" class="form-control" placeholder="제목을 입력하세요" aria-label="Search Title" v-model="searchTitle"/>
              <button class="btn btn-outline-secondary" type="button" @click="fetchCounselHires">
                <i class="bi bi-search"></i>
              </button>
            </div>
          </div>
        </div>
        <CounselList :counselHires="paginationCounselHires" :currentPage="currentPage" :pageSize="pageSize" :totalCount="totalCount"/>
      </div>
      <Pagination :totalCount="totalCount" @sendPagination="receivePagination"/>
    </div>
  </div>
</template>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}

.table-container {
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  border: 2px solid #ccc;
  margin-bottom: 0;
}

.table-wrapper {
  overflow-x: auto;
}

.table {
  width: 100%;
  margin-bottom: 0;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 10px;
  vertical-align: middle;
  text-align: center;
}

.table th {
  background: #ffffff;
  font-weight: bold;
}

.btn.btn-dark {
  background-color: #333;
  color: #fff;
  border: none;
}

</style>
