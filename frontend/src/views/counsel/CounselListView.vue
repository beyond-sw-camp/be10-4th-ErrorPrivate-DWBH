<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import dayjs from "dayjs";
import "@/css/style.css"

const counselHires = ref([]);
const counselTypes = ref([]);
const counselAgeRanges = ref([]);
const hopeAgeSeq = ref("");
const hopeGender = ref("");
const hopeTypeSeq = ref("");
const searchTitle = ref("");

const fetchCounselHires = async () => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/counselor-hire`, {
      params: {
        searchAgeSeq: hopeAgeSeq.value,
        searchGender: hopeGender.value,
        searchTypeSeq: hopeTypeSeq.value,
        searchTitle: searchTitle.value,
      },
    });
    console.log(response.data);
    counselHires.value = response.data.counselorList.content;
    counselTypes.value = response.data.counselorTypeList;
    counselAgeRanges.value = response.data.counselorAgeList;

  } catch (error) {
    console.error('게시판 목록을 가져오는 중 오류 발생', error)
  }
}

onMounted(() => {
  fetchCounselHires()
})
</script>

<template>
  <div class="container content">
    <div class="container mt-4">
      <!-- 제목 -->
      <h2 class="text-center mb-4">마음을 이어주는 다리</h2>

      <!-- 필터 및 검색 -->
      <div class="row mb-3 justify-content-end">
        <div class="col-md-2">
          <select class="form-select" v-model="hopeAgeSeq">
            <option value="" selected>희망 나이대</option>
            <template v-for="(counselAgeRange, index) in counselAgeRanges" :key="index">
              <option value="{{counselAgeRange.counselorAgeRangeSeq}}">{{counselAgeRange.counselorAgeRange}}</option>
            </template>
          </select>
        </div>
        <div class="col-md-2">
          <select class="form-select" v-model="hopeGender">
            <option value="" selected>희망 성별</option>
            <option value="남자">남자</option>
            <option value="여자">여자</option>
          </select>
        </div>
        <div class="col-md-2">
          <select class="form-select" v-model="hopeTypeSeq">
            <option value="" selected>희망 조언 유형</option>
            <template v-for="(counselorType, index) in counselTypes" :key="index">
              <option value="{{counselorType.counselorTypeSeq}}">{{counselorType.counselorType}}</option>
            </template>
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

      <!-- 테이블 -->
      <div class="table-responsive">
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
            <td>{{ counselHire.hireTitle }}</td>
            <td>{{ counselHire.userNickname }}</td>
            <td>{{ counselHire.hireGender }}</td>
            <td>
              <template v-for="(ageRange, index) in counselHire.ageRanges" :key="index">
                <template v-if="ageRange.counselorAgeRange === null">
                  무관
                </template>
                <template v-else>
                  {{ ageRange.counselorAgeRange}}대
                </template>
              </template>
            </td>
            <td>
              <template v-for="(type, index) in counselHire.types" :key="index">
                <template v-if="type.counselorType === null">
                  무관
                </template>
                <template v-else>
                  {{ type.counselorType }}
                </template>
              </template>
            </td>
            <td>{{ dayjs(counselHire.regDate).format("YYYY-MM-DD HH:mm:ss") }}</td>
          </tr>
          </tbody>
        </table>
      </div>

    </div>
  </div>
</template>

<style scoped>
</style>