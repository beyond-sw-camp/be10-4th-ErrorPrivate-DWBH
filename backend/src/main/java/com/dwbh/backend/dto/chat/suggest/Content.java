package com.dwbh.backend.dto.chat.suggest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Content {
    private List<Part> parts;
    private String role;
}
