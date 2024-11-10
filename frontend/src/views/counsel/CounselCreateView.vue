<script setup>
import { onMounted, ref } from "vue";
import $ from "jquery";
import "@/css/style.css";
import "summernote/dist/summernote-lite.css";
import "summernote/dist/summernote-lite.js";
import axios from "axios";
import router from "@/router";

const title = ref("");
const content = ref("");
const selectedAges = ref([]);
const hopeGender = ref("");
const hopeTypeSeq = ref("");

// 나이대와 상담 유형 옵션 데이터
const ageOptions = [
  { label: "10대", value: "10대" },
  { label: "20대", value: "20대" },
  { label: "30대", value: "30대" },
  { label: "40대", value: "40대" },
  { label: "50대", value: "50대" },
  { label: "60대", value: "60대" },
  { label: "70대", value: "70대" },
  { label: "80대", value: "80대" },
  { label: "90대", value: "90대" },
  { label: "100대", value: "100대" },

];

const counselTypeOptions = [
  { label: "T의 현실적 조언", value: "T의 현실적 조언" },
  { label: "F식 공감", value: "F식 공감" },
];

// 목록으로 돌아가기
const goToList = () => {
  router.push("/counsel");
};

// Summernote 에디터 초기화
onMounted(() => {
  $("#summernote").summernote({
    height: 300,
    placeholder: "내용을 입력하세요",
    dialogsInBody: true, // 대화 상자를 더 안정적으로 띄우도록 설정
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
        content.value = contents;
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
  title.value = "";
  content.value = "";
  selectedAges.value = [];
  hopeGender.value = "";
  hopeTypeSeq.value = "";
  $("#summernote").summernote("reset");
};

// 폼 제출 함수
const handleSubmit = () => {
  const formData = {
    title: title.value,
    content: content.value,
    selectedAges: selectedAges.value,
    hopeGender: hopeGender.value,
    hopeTypeSeq: hopeTypeSeq.value,
  };
  // 폼 데이터를 서버로 전송하는 코드 추가
  console.log("Form submitted", formData);
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
        <input type="text" id="title" v-model="title" placeholder="제목을 입력하세요" required class="form-control"
        />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <div id="summernote"></div>
      </div>

      <!-- 희망 상담사 나이대 -->
      <div class="form-group d-flex align-items-center">
        <label for="age" class="me-2">희망 상담사 나이대</label>
        <input type="text" readonly :value="selectedAges.map(a => a.label).join(', ')" placeholder="나이대를 선택해주세요." class="form-control me-2" style="width: 200px;"/>
        <div class="dropdown">
          <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
            선택
          </button>
          <ul class="dropdown-menu custom-dropdown">
            <li v-for="option in ageOptions" :key="option.value" class="dropdown-item custom-dropdown-item">
              <input type="checkbox" :id="option.value" :value="option" v-model="selectedAges" />
              <label :for="option.value">{{ option.label }}</label>
            </li>
          </ul>
        </div>
      </div>

      <div class="form-group d-flex align-items-center">
        <label for="gender" class="me-2">희망 상담사 성별</label>
        <select id="gender" v-model="hopeGender" class="form-select" style="width: 120px;">
          <option value="" selected>선택</option>
          <option value="male">남자</option>
          <option value="female">여자</option>
        </select>
      </div>

      <div class="form-group d-flex align-items-center">
        <label for="type" class="me-2">희망 조언 유형</label>
        <select id="type" v-model="hopeTypeSeq" class="form-select" style="width: 150px;">
          <option value="" selected>선택</option>
          <option v-for="option in counselTypeOptions" :key="option.value" :value="option.value">
            {{ option.label }}
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
.btn-submit {
  background-color: #333;
  color: white;
  border: none;
}
.btn-submit:hover {
  background-color: #555;
}
.form-group.d-flex.align-items-center {
  gap: 10px; /* 입력 필드와 버튼 사이 간격 설정 */
}

.custom-dropdown {
  max-height: 150px; /* 원하는 높이로 설정 */
  overflow-y: auto;
  width: 120px; /* 드롭다운 너비 조정 */
  padding: 5px; /* 간격을 좁게 설정 */
}

.custom-dropdown-item {
  display: flex;
  align-items: center;
  gap: 5px; /* 체크박스와 텍스트 사이 간격 설정 */
  font-size: 0.9rem; /* 폰트 크기 조절 */
}

.custom-dropdown input[type="checkbox"] {
  width: 15px;
  height: 15px;
  margin-right: 5px; /* 텍스트와 체크박스 사이 간격 */
}

.custom-dropdown label {
  margin: 0;
}


</style>
