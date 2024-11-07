package com.dwbh.backend.repository.counsel_offer;

import com.dwbh.backend.entity.CounselOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselOfferRepository extends JpaRepository<CounselOffer, Long>, CounselOfferCustomRepository {
}
