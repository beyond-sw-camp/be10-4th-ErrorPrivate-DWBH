<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import dayjs from "dayjs";
import "@/css/style.css"
import Pagination from "@/components/common/Pagination.vue";
import router from "@/router/index.js";

const counselHires = ref([]);
const counselTypes = ref([]);
const counselAgeRanges = ref([]);
const hopeAgeSeq = ref(0);
const hopeGender = ref("");
const hopeTypeSeq = ref(0);
const searchTitle = ref("");

const totalCount = ref(0); // 전체 개수
const currentPage = ref(1); // 현재 페이지
const pageSize = ref(11); // 페이지당 항목 수

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
  } catch (error) {
    console.error('게시판 목록을 가져오는 중 오류 발생', error)
  }
}

const receivePagination = (page) => {
  currentPage.value = page.currentPage;
  pageSize.value = page.pageSize;

  fetchCounselHires();
}

const goCounselHireDetail = (counselSeq) => {
  router.push(`/counsel/${counselSeq}`);
};

onMounted(() => {
  fetchCounselHires()
})
</script>

<template>
  <div class="container content">
    <div class="responsive-container mt-4">
      <!-- 제목 -->
      <div class="title-container">
        <h2 class="title-text">마음을 이어주는 다리</h2>
      </div>
      <!-- 테이블 -->
      <div class="table-container">
        <!-- 필터 및 검색 -->
        <div class="filter-container row mb-3 justify-content-end">
          <div class="col-md-2">
            <select class="form-select" v-model="hopeAgeSeq">
              <option value=0 selected>희망 나이대</option>
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
              <option value=0 selected>희망 조언 유형</option>
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

        <div class="table-wrapper">
          <table class="table table-bordered text-center align-middle">
            <thead class="table-light">
            <tr>
              <th>No.</th>
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
              <td>{{ index + 1 }}</td>
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
      </div>
      <Pagination :totalCount="totalCount" @sendPagination="receivePagination"/>
    </div>
  </div>

</template>

<style scoped>
.responsive-container {
  width: 90vw; /* 화면 너비의 90% */
  height: 100vh; /* 화면 높이의 70% */
  max-width: 1600px; /* 너무 커지지 않도록 최대 크기 제한 */
  margin: 0 auto; /* 중앙 정렬 */
  padding: 20px; /* 내부 여백 */
  background-color: #ffffff; /* 배경색 */
  border-radius: 10px; /* 모서리 둥글게 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
}

.filter-container {
  margin-bottom: 20px;
}

.table-container {
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  border: 2px solid #ccc;
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
</style>