package com.dwbh.backend.repository.offer;

import com.dwbh.backend.entity.CounselOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferInfaRepositoy  extends JpaRepository<CounselOffer, Long>, OfferRepository {
}
