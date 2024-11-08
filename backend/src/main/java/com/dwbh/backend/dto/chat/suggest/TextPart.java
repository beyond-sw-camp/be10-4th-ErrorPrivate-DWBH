package com.dwbh.backend.dto.chat.suggest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 메시지 타입이 텍스트일 경우
@Getter
@Setter
@AllArgsConstructor
public class TextPart implements Part{
    private String text;

    /*
    * "parts":[
    *     {"text": "자기 반성과 소통 개선을 통해 인간관계 스트레스를 줄일 수 있습니다."}
    * ]
    * */
}
