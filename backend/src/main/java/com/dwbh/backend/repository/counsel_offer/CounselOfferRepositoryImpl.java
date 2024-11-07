package com.dwbh.backend.repository.counsel_offer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dwbh.backend.entity.QCounselOffer.counselOffer;
import static com.dwbh.backend.entity.QCounselorHire.counselorHire;

@Repository
@RequiredArgsConstructor
public class CounselOfferRepositoryImpl implements CounselOfferCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public String findCounselorContentByCounselOfferSeq(Long counselOfferSeq) {
        return queryFactory.select(counselorHire.hireContent)
                .from(counselOffer)
                .leftJoin(counselorHire)
                .on(counselorHire.eq(counselOffer.hire))
                .where(counselOffer.offerSeq.eq(counselOfferSeq))
                .fetchOne();
    }
}
