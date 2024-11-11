package com.dwbh.backend.repository.counsel_offer;

import com.dwbh.backend.dto.counsel_offer.OfferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CounselOfferCustomRepository {
    String findCounselorContentByCounselOfferSeq(Long counselOfferSeq);

    Page<OfferResponse> findOffersByUserSeq(Long userSeq, Pageable pageable);
}
