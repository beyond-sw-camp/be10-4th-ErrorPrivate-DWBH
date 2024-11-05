package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counsel_offer_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CounselOfferFile {

    @Id
    @Column(name = "counsel_offer_file_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OfferFileSeq;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_seq", nullable = false)
    private File file;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsel_offer_seq", nullable = false)
    private CounselOffer counselOffer;

    public CounselOfferFile(File file, CounselOffer offer) {
        this.file = file;
        this.counselOffer = offer;
    }

}
