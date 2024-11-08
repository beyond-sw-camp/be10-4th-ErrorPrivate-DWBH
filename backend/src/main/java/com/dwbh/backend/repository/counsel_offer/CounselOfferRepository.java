package com.dwbh.backend.repository.counsel_offer;

import com.dwbh.backend.entity.CounselOffer;
import com.dwbh.backend.entity.CounselorHire;
import com.dwbh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CounselOfferRepository extends JpaRepository<CounselOffer, Long>, OfferCustomRepository  {

    @Modifying
    @Query("UPDATE CounselOffer o SET o.delDate = :delDate WHERE o.offerSeq = :offerSeq")
    void softDeleteById(@Param("offerSeq") Long offerSeq, @Param("delDate") LocalDateTime delDate);

    boolean existsByUserAndHireAndDelDateIsNull(User user, CounselorHire hire);

}
