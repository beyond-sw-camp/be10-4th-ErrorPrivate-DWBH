package com.dwbh.backend.repository.counsel_offer;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.dto.counsel_offer.OfferResponse;
import com.dwbh.backend.entity.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
public class OfferCustomRepositoryImpl implements OfferCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
//    public Page<CounselOffer> findOffersByHireSeq(Long hireSeq, Pageable pageable) {
    public Page<OfferResponse> findOffersWithFilter(Long hireSeq, Pageable pageable, String sortOrder, Long currentUserId) {

        // 정렬 조건
        OrderSpecifier<?> orderBy = sortOrder.equalsIgnoreCase("asc")
                ? QCounselOffer.counselOffer.regDate.asc()
                : QCounselOffer.counselOffer.regDate.desc();

        List<OfferResponse> results = queryFactory
                .select(Projections.fields(OfferResponse.class,
//                .select(Projections.constructor(OfferResponse.class,
                        counselOffer.offerSeq,
                        counselOffer.hire.hireSeq,
                        counselOffer.user.userSeq,
                        counselOffer.offerContent,
                        counselOffer.offerPrivateYn,
//                        counselOffer.offerFile.file.as("offerFilePath"),
                        file.filePath.as("offerFilePath"),
                        counselOffer.regDate,
                        counselOffer.modDate,
                        user.userNickname,
                        user.userGender,
                        user.userBirthday,
                        user.userStatus,
                        file.filePath.as("userProfilePath")))
                .from(counselOffer)
                .join(counselOffer.user, user)/*.fetchJoin()*/  // 연관된 User를 함께 불러옴
                .leftJoin(userProfileFile).on(user.userSeq.eq(userProfileFile.userSeq))
                .leftJoin(file).on(userProfileFile.fileSeq.eq(file.fileSeq))
                .where(counselOffer.delDate.isNull() // 필터조건
                        .and(counselOffer.hire.hireSeq.eq(hireSeq))
                        .and(filterPrivateComments(counselOffer, currentUserId))) // 비밀 댓글 필터링
                .orderBy(orderBy) // 정렬
                .offset(pageable.getOffset()) // 페이징 offset
                .limit(pageable.getPageSize()) // 페이징 limit
                .fetch();

        Long count = queryFactory
                .select(counselOffer.count())
                .from(counselOffer)
                .where(counselOffer.delDate.isNull()
                        .and(counselOffer.hire.hireSeq.eq(hireSeq))
                        .and(filterPrivateComments(counselOffer, currentUserId)))
                .fetchOne();
        long total = (count != null) ? count : 0L;

        return new PageImpl<>(results, pageable, total);

//        List<CounselOffer> results = queryFactory
//                .selectFrom(counselOffer)
//                .join(counselOffer.user, user).fetchJoin()  // 연관된 User를 함께 불러옴
//                .leftJoin(userProfileFile).on(user.userSeq.eq(userProfileFile.userSeq))
//                .leftJoin(file).on(userProfileFile.fileSeq.eq(file.fileSeq))
//                .where(counselOffer.delDate.isNull()
//                        .and(counselorHire.hireSeq.eq(hireSeq)))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = queryFactory
//                .select(counselOffer.count())
//                .from(counselOffer)
//                .where(counselOffer.delDate.isNull()
//                        .and(counselorHire.hireSeq.eq(hireSeq)))
//                .fetchOne();
//
//        return new PageImpl<>(results, pageable, total);

    }

    private BooleanExpression filterPrivateComments(QCounselOffer counselOffer, Long currentUserId) {
        return counselOffer.offerPrivateYn.eq(YnType.valueOf("Y"))
                .and(counselOffer.user.userSeq.eq(currentUserId)
                        .or(counselOffer.hire.user.userSeq.eq(currentUserId)))
                .or(counselOffer.offerPrivateYn.eq(YnType.N));
    }


}
