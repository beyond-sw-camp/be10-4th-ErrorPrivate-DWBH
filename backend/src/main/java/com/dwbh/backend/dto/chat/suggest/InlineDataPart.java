package com.dwbh.backend.dto.chat.suggest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 메시지가 텍스트가 아닌 경우(이미지, 영상, ...)
@Getter
@Setter
@AllArgsConstructor
public class InlineDataPart implements Part {
    public InlineData inlineData;
    /*
    * "parts": [
    *      {
    *          "text": "What is this picture?"
    *      },
    *      {
    *          "inline_data": {
    *              "mime_type": "image/jpeg",
    *              "data": "'$(base64 -w0 image.jpg)'"
    *          }
    *      }
    *  ]
    * */
}
