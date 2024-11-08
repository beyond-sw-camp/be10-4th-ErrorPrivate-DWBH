<script setup>
import {defineEmits, defineProps} from "vue";

// props 정의
const props = defineProps({
  modelValue: String,
  placeholder: String,
  type: {
    type: String,
    default: 'text',
  },
  disabled: Boolean,  // input 비활성화 여부
  errorMessage: String,
});

// emit 정의 (부모 컴포넌트에게 이벤트 전달)
const emit = defineEmits([
  'update:modelValue',
  'input',
  'focus',
  'blur'
]);

// 입력 값 처리
const handleInput = (event) => {
  emit('update:modelValue', event.target.value); // v-model과 연동
  emit('input', event); // 부모에게 input 이벤트 전달
}


</script>

<template>
  <div class="input-box">
    <input
        :type="type"
        :placeholder="placeholder"
        :value="modelValue"
        @input="handleInput"
        @focus="$emit('focus')"
        @blur="$emit('blur')"
        :disabled="disabled"
        class="input-field"
    />
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<style scoped>
.input-box {
  display: flex;
  flex-direction: column;
}

.input-field {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

.input-field:focus {
  outline: none;
  border-color: #53d9c1;
}

.error {
  color: red;
  font-size: 12px;
  margin-top: 4px;
}
</style>