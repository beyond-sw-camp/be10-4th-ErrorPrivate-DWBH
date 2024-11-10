import { defineStore } from 'pinia';
import { ref, onMounted } from 'vue';

// 어디서든 사용할 수 있는 useAuthStore
export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref('eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJra203aGpoQG5hdmVyLmNvbSIsInNlcSI6MywiYXV0aCI6W10sImV4cCI6MTczMTI1NTEyMX0.cfYfhWLqUWAD1_gYGP5v-K1p7k4oOx4Kb8y57eSUDd9iN6W4TMyzmM6maO3VDUJUl_StDTQzzt71Nsq6asilzA');
    const userRole = ref(null);
    const userEmail = ref(null);
    const userSeq = ref(null);

    // 페이지가 로드될 때 localStorage 에서 토큰을 읽어와 상태를 초기화한다.
    onMounted(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            accessToken.value = token;
            // JWT 토큰의 페이로드 추출
            const payload = JSON.parse(atob(token.split('.')[1]));
            userRole.value = payload.auth;
            userSeq.value = payload.seq;
        }
    });

    // 회원가입 시 작성한 이메일을 저장하는 메소드
    function registerEmail(email) {
        userEmail.value = email;
    }

    // 회원가입시 작성한 이메일을 삭제하는 메소드
    function clearEmail() {
        userEmail.value = null;
    }

    function login(token) {
        try {

        } catch (error) {
            console.error('토큰 뽑는 중 오류: ', error);
        }
        accessToken.value = token;
        // 새로고침 시 로그인이 풀리는 것을 막기 위해 토큰을 localStorage 에 저장
        localStorage.setItem('accessToken', token);
        // JWT 토큰의 페이로드 추출
        const payload = JSON.parse(atob(token.split('.')[1]));
        userRole.value = payload.auth;
        userSeq.value = payload.seq;  // seq 정보 추출해서 저장
    }

    function logout() {
        accessToken.value = null;
        userRole.value = null;
        userSeq.value = null;
        localStorage.removeItem('accessToken');
    }

    function isAuthorized(requiredRole) {
        if (!userRole.value) return false;
        // userRole 이 배열로 반환되므로 includes 로 확인
        return userRole.value.includes(requiredRole);
    }

    return { accessToken, userRole, userSeq, login, logout, isAuthorized, userEmail, registerEmail, clearEmail };
});