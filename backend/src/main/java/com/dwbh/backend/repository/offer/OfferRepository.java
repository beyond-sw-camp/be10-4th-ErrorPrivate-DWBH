package com.dwbh.backend.repository.offer;

import com.dwbh.backend.entity.CounselOffer;

import java.util.Optional;

public interface OfferRepository {
    CounselOffer save(CounselOffer offer);

    Optional<CounselOffer> findById(Long offerSeq);

    void deleteById(Long offerSeq);
}
