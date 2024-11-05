package com.dwbh.backend.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum YnType {
    Y("Y"),
    N("N");

    private final String value;

}