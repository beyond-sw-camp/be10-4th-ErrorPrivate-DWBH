package com.dwbh.backend.common.aggregate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum YnType {
    Y("Y"),
    N("N");

    private final String value;

}