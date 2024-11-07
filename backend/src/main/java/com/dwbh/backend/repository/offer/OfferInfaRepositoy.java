package com.dwbh.backend.repository.offer;

import com.dwbh.backend.entity.CounselOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OfferInfaRepositoy  extends JpaRepository<CounselOffer, Long>, OfferRepository {

    @Modifying
    @Query("UPDATE CounselOffer o SET o.delDate = :delDate WHERE o.offerSeq = :offerSeq")
    void softDeleteById(@Param("offerSeq") Long offerSeq, @Param("delDate") LocalDateTime delDate);
}
