<script setup>
import { onMounted, ref } from "vue";
import $ from "jquery";
import "@/css/style.css";
import "summernote/dist/summernote-lite.css";
import "summernote/dist/summernote-lite.js";
import axios from "axios";
import router from "@/router";

// 폼 데이터 변수 선언
const hireTitle = ref("");
const hireContent = ref("");
const ageRangeId = ref([]); // 선택된 나이대
const hireGender = ref("");
const typeId = ref("");
const counselorAgeList = ref([]);
const counselorTypeList = ref([]);

const fetchCounselCrate = async () => {
  try {
    const response = await axios.get(`http://localhost:8089/api/v1/counselor-hire/create`,
      {
        headers: { Authorization: `Bearer ${localStorage.getItem('accessToken')}`}
      }
    );

    counselorAgeList.value = response.data.counselorAgeList;
    counselorTypeList.value = response.data.counselorTypeList;

    console.log(counselorTypeList.value)
  } catch (error) {
    console.error(error);
  }
}
// Summernote 에디터 초기화
onMounted(() => {
  fetchCounselCrate();

  $("#summernote").summernote({
    height: 300,
    placeholder: "내용을 입력하세요",
    dialogsInBody: true,
    toolbar: [
      ["style", ["style"]],
      ["font", ["bold", "italic", "underline", "clear"]],
      ["fontname", ["fontname"]],
      ["color", ["color"]],
      ["para", ["ul", "ol", "paragraph"]],
      ["table", ["table"]],
      ["insert", ["link", "picture", "video"]],
      ["view", ["fullscreen", "codeview", "help"]],
    ],
    callbacks: {
      onChange: function (contents) {
        hireContent.value = contents;
      },
      onImageUpload: function (files) {
        uploadImage(files[0]);
      },
    },
  });
});

// 이미지 업로드 함수
const uploadImage = async (file) => {
  const formData = new FormData();
  formData.append("file", file);
  try {
    const response = await axios.post("https://your-server.com/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    const imageUrl = response.data.url;
    $("#summernote").summernote("insertImage", imageUrl);
  } catch (error) {
    console.error("이미지 업로드 실패:", error);
  }
};

// 폼 초기화 함수
const resetForm = () => {
  hireTitle.value = "";
  hireContent.value = "";
  ageRangeId.value = [];
  hireGender.value = "";
  typeId.value = "";
  $("#summernote").summernote("reset");
};

// 폼 제출 핸들러
const handleSubmit = async () => {
  const formData = {
    hireTitle: hireTitle.value,
    hireContent: hireContent.value,
    ageRanges: ageRangeId.value.map(age => age.value), // 서버가 기대하는 데이터 구조로 맞춤
    hireGender: hireGender.value,
    types: typeId.value,
  };

  console.log(formData);

  try {
    const response = await axios.post(
        "http://localhost:8089/api/v1/counselor-hire",
        formData,
        { headers: { Authorization: `Bearer ${localStorage.getItem('accessToken')}` } }
    );
    console.log("Form submitted successfully:", response.data);
    alert("글이 성공적으로 저장되었습니다.");
    goToList();
  } catch (error) {
    console.error("Form submission failed:", error);
    alert("글 등록에 실패했습니다.");
  }
};

// 목록으로 돌아가기
const goToList = () => {
  router.push("/counsel");
};
</script>

<template>
  <div class="container content">
    <div class="responsive-container mt-4">
      <div class="title-container d-flex justify-content-between align-items-center">
        <h2 class="title-text">마음을 이어주는 다리 / 글 작성</h2>
        <button class="btn btn-dark" @click="goToList">목록으로</button>
      </div>

      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" v-model="hireTitle" placeholder="제목을 입력하세요" required class="form-control"/>
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <div id="summernote"></div>
      </div>

      <!-- 희망 상담사 나이대 -->
      <div class="form-group d-flex align-items-center">
        <label for="age" class="me-2">희망 상담사 나이대</label>
        <input type="text" readonly :value="ageRangeId.map(a => a.label).join(', ')" placeholder="나이대를 선택해주세요." class="form-control me-2" style="width: 200px;"/>
        <div class="dropdown">
          <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
            선택
          </button>
          <ul class="dropdown-menu custom-dropdown">
            <li v-for="(counselAge, index) in counselorAgeList" :key="index" class="dropdown-item custom-dropdown-item">
              <input type="checkbox" :id="counselAge.counselorAgeRangeSeq" :value="counselAge.counselorAgeRangeSeq" v-model="ageRangeId" />
              <label :for="counselAge.counselorAgeRangeSeq">{{ counselAge.counselorAgeRange }}</label>
            </li>
          </ul>
        </div>
      </div>

      <div class="form-group d-flex align-items-center">
        <label for="gender" class="me-2">희망 상담사 성별</label>
        <select id="gender" v-model="hireGender" class="form-select" style="width: 120px;">
          <option value="" selected>선택</option>
          <option value="male">남자</option>
          <option value="female">여자</option>
        </select>
      </div>

      <div class="form-group d-flex align-items-center">
        <label for="type" class="me-2">희망 조언 유형</label>
        <select id="type" v-model="typeId" class="form-select" style="width: 150px;">
          <option value="" selected>선택</option>
          <option v-for="(counselorType, index) in counselorTypeList" :key="counselorType.counselorTypeSeq" :value="counselorType.counselorTypeSeq">
            {{ counselorType.counselorType }}
          </option>
        </select>
      </div>

      <div class="button-group d-flex justify-content-end mt-4">
        <button type="reset" class="btn-reset btn btn-light me-2" @click="resetForm">초기화</button>
        <button type="button" class="btn-submit btn btn-success" @click="handleSubmit">확인</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
h2 {
  font-weight: bold;
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #333;
}

input,
select {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-reset {
  background-color: #cccccc;
  color: #333;
}

.btn-submit {
  background-color: #4CAF50;
  color: white;
}

.btn-submit:hover {
  background-color: #555;
}

.form-group.d-flex.align-items-center {
  gap: 10px;
}

.custom-dropdown {
  max-height: 150px;
  overflow-y: auto;
  width: 120px;
  padding: 5px;
}

.custom-dropdown-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.9rem;
}

.custom-dropdown input[type="checkbox"] {
  width: 15px;
  height: 15px;
  margin-right: 5px;
}

.custom-dropdown label {
  margin: 0;
}
</style>
