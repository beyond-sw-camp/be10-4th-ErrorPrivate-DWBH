package com.dwbh.backend.repository.counsel_offer;

import com.dwbh.backend.dto.counsel_offer.OfferResponse;
import com.dwbh.backend.entity.CounselOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfferCustomRepository {

    Page<OfferResponse> findOffersWithFilter(Long hireSeq, Pageable pageable, String sortOrder, Long currentUserId);

}
