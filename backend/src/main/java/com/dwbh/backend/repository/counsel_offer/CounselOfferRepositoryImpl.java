package com.dwbh.backend.repository.counsel_offer;

import com.dwbh.backend.dto.counsel_offer.OfferResponse;
import com.dwbh.backend.entity.QCounselOffer;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dwbh.backend.entity.QCounselOffer.counselOffer;
import static com.dwbh.backend.entity.QCounselorHire.counselorHire;
import static com.dwbh.backend.entity.QFile.file;
import static com.dwbh.backend.entity.QUser.user;
import static com.dwbh.backend.entity.QUserProfileFile.userProfileFile;

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

    @Override
    public Page<OfferResponse> findOffersByUserSeq(Long userSeq, Pageable pageable) {

        QueryResults<OfferResponse> results = queryFactory
                .select(Projections.fields(OfferResponse.class,
                        counselOffer.offerSeq,
                        counselOffer.hire.hireSeq,
                        counselOffer.user.userSeq,
                        counselOffer.offerContent,
                        counselOffer.offerPrivateYn,
                        file.filePath.as("offerFilePath"),
                        counselOffer.regDate,
                        counselOffer.modDate,
                        user.userNickname,
                        user.userGender,
                        user.userBirthday,
                        user.userStatus,
                        file.filePath.as("userProfilePath")))
                .from(counselOffer)
                .join(counselOffer.user, user)
                .leftJoin(userProfileFile).on(user.userSeq.eq(userProfileFile.userSeq))
                .leftJoin(file).on(userProfileFile.fileSeq.eq(file.fileSeq))
                .where(counselOffer.delDate.isNull() // 필터조건
                        .and(user.userSeq.eq(userSeq)))
                .orderBy(counselOffer.regDate.desc())
                .offset(pageable.getOffset()) // 페이징 offset
                .limit(pageable.getPageSize()) // 페이징 limit
                .fetchResults();

        Long count =  queryFactory
                .select(counselOffer.count())
                .from(counselOffer)
                .join(counselOffer.user, user)
                .where(counselOffer.delDate.isNull() // 필터조건
                        .and(user.userSeq.eq(userSeq)))
                .fetchOne();
        long total = (count != null) ? count : 0L;

        return new PageImpl<>(results.getResults(), pageable, total);
    }
}