<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import dayjs from "dayjs";
import "@/css/style.css"

  const counselHires = ref([]);

  const fetchCounselHires = async () => {
    try {
      const response = await axios.get('http://localhost:8089/api/v1/counselor-hire');
      counselHires.value = response.data;

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
      <div class="row mb-3">
        <div class="col-md-3">
          <select class="form-select">
            <option selected>희망 성별</option>
            <option value="남자">남자</option>
            <option value="여자">여자</option>
          </select>
        </div>
        <div class="col-md-3">
          <select class="form-select">
            <option selected>희망 조언 유형</option>
            <option value="직설적인 타입">직설적인 타입</option>
            <option value="감성적인 타입">감성적인 타입</option>
            <option value="현실적인 타입">현실적인 타입</option>
          </select>
        </div>
        <div class="col-md-6">
          <input
              type="text"
              class="form-control"
              placeholder="제목을 입력하세요"
          />
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