package com.dwbh.backend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseDateEntity {
    @CreatedDate
    @Column(name = "reg_date") // 기본 이름 설정
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "mod_date") // 기본 이름 설정
    private LocalDateTime modDate;
}
