package com.dwbh.backend.dto.chat.suggest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InlineData {
    private String mimeType; // 데이터 타입
    private String data;
    /*
    * "inline_data": {
    *       "mime_type": "image/jpeg",
    *       "data": "'$(base64 -w0 image.jpg)'"
    * }
    * */
}
