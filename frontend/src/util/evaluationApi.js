import {ref} from 'vue';
import axios from 'axios';

// 평가 조회 Api 호출
// 수정 시 미리 데이터를 가져오기 위해 사용
export function readEvaluation() {
    const evaluationData = ref(null);

    const readEvaluationData = async (token, chatSeq) => {
        try {
            const response = await axios.get(`http://localhost:8089/chat/${chatSeq}/evaluation`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            evaluationData.value = response.data.result;
        } catch (error) {
            console.error('평가 가져오기 실패', error);
        }
    };
    return {evaluationData, readEvaluationData};
}

// 평가 작성 API 호출
export const createEvaluation = async (token, evaluationData) => {
    try {
        const response = await axios.post('http://localhost:8089/evaluation', evaluationData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        // await router.push(`/shoppinggroup/${groupNum}`);

        return response.data;
    } catch (error) {
        console.error('평가 작성 실패', error);
        throw error;
    }
};

// 평가 수정 API 호출
export const updateEvaluation = async (token, evaluationSeq, evaluationData) => {
    try {
        const response = await axios.put(`http://localhost:8089/evaluation/${evaluationSeq}`, evaluationData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        // await router.push(`/shoppinggroup/${groupNum}`);

        return response.data;

    } catch (error) {
        console.error('평가 수정 실패', error);
        throw error;
    }
};

// 평가 삭제 API 호출
export const deleteEvaluation = async (token, evaluationSeq) => {
    try {
        const response = await axios.delete(`http://localhost:8089/evaluation/${evaluationSeq}`,{
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        // await router.push(`/shoppinggroup/${groupNum}`);

        return response.data;

    } catch (error) {
        console.error('평가 삭제 실패', error);
        throw error;
    }
};