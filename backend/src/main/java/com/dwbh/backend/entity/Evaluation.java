package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "evaluation_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "evaluation_mod_date"))
})
@Getter
@Table(name = "tb_evaluation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Evaluation extends BaseDateEntity {

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
