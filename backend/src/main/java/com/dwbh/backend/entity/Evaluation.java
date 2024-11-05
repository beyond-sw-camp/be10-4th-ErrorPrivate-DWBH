package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_evaluation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)  // 엔티티 생성, 삭제 시점 체크를 위해 필요한 리스너
public class Evaluation {

    @Id
    @Column(name = "evaluation_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationSeq;

    @ManyToOne
    @JoinColumn(name = "chat_seq", nullable = false)
    private Chat chat;

    @Column(name = "evaluation_satisfaction")
    private Integer evaluationSatisfaction;

    @Column(name = "evaluation_communication")
    private Integer evaluationCommunication;

    @Column(name = "evaluation_kindness")
    private Integer evaluationKindness;

    @Column(name = "evaluation_score")
    private Double evaluationScore;

    @CreatedDate
    @Column(name = "evaluation_reg_date", nullable = false)
    private LocalDateTime evaluationRegDate;

    @LastModifiedDate
    @Column(name = "evaluation_mod_date", insertable = false)
    private LocalDateTime evaluationModDate;

    public void updateEvaluation(Chat chat, Integer evaluationSatisfaction, Integer evaluationCommunication, Integer evaluationKindness, Double evaluationScore) {
        if (chat != null) this.chat = chat;
        if (evaluationSatisfaction != null) this.evaluationSatisfaction = evaluationSatisfaction;
        if (evaluationCommunication != null) this.evaluationCommunication = evaluationCommunication;
        if (evaluationKindness != null) this.evaluationKindness = evaluationKindness;
        if (evaluationScore != null) this.evaluationScore = evaluationScore;
    }

    public void builder(Chat chat, Double evaluationScore) {
        this.chat = chat;
        this.evaluationScore = evaluationScore;
    }
}
