package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.dto.counselor_hire.*;
import com.dwbh.backend.entity.QCounselorType;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.dwbh.backend.entity.QCounselorAge.counselorAge;
import static com.dwbh.backend.entity.QCounselorHire.counselorHire;
import static com.dwbh.backend.entity.QCounselorHireAge.counselorHireAge;
import static com.dwbh.backend.entity.QCounselorHireFile.counselorHireFile;
import static com.dwbh.backend.entity.QCounselorHireType.counselorHireType;
import static com.dwbh.backend.entity.QCounselorType.counselorType1;
import static com.dwbh.backend.entity.QFile.file;
import static com.dwbh.backend.entity.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CounselorRepositoryImpl implements CounselorCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CounselorDetailResponse findCounselorHireDetail(Long hireSeq, Long currentUserSeq) {
        // 로그로 hireSeq 출력
        log.info("------------------------------------------ findCounselorHireDetail with hireSeq: {}", hireSeq);

        List<String> counselorAgeRanges = queryFactory
                .select(counselorAge.counselorAgeRange)
                .from(counselorHireAge)
                .join(counselorAge).on(counselorHireAge.counselorAge.counselorAgeRangeSeq.eq(counselorAge.counselorAgeRangeSeq))
                .where(counselorHire.hireSeq.eq(hireSeq))
                .fetch();

        CounselorDetailResponse result = queryFactory
                .select(Projections.fields(CounselorDetailResponse.class,
                        counselorHire.hireSeq,
                        counselorHire.user.userSeq,
                        counselorHire.user.userNickname,
                        counselorHire.hireTitle,
                        counselorHire.hireContent,
                        counselorHire.hireGender,
                        counselorType1.counselorType.as("counselorType"),
                        counselorHire.regDate,
                        counselorHire.modDate,
                        file.filePath.as("hireFilePath")
                        ))
                .from(counselorHire)
                .join(counselorHire.user, user)
                .join(counselorHireType).on(counselorHire.hireSeq.eq(counselorHireType.counselorHire.hireSeq))
                .join(counselorType1).on(counselorHireType.counselorType.counselorTypeSeq.eq(counselorType1.counselorTypeSeq))
                .leftJoin(counselorHireFile).on(counselorHire.hireSeq.eq(counselorHireFile.hireSeq))
                .leftJoin(file).on(counselorHireFile.fileSeq.eq(file.fileSeq))
                .where(counselorHire.hireSeq.eq(hireSeq))
                .fetchOne();

        // DTO에 나이대 설정
        if (result != null) {
            result.setCounselorAgeRanges(counselorAgeRanges);
        }

        return result;
    }

    @Override
    public List<CounselorTypeDTO> findCounselorTypeByCounselorHireSeq(Long counselorHireSeq) {
        QCounselorType counselorType = new QCounselorType("counselorType");

        return queryFactory.select(Projections.constructor(CounselorTypeDTO.class
                        ,counselorType.counselorTypeSeq
                        ,counselorType.counselorType))
                .from(counselorHire)
                .leftJoin(counselorHire.hireTypes, counselorHireType)
                .leftJoin(counselorHireType.counselorType, counselorType)
                .where(counselorHire.hireSeq.eq(counselorHireSeq))
                .fetch();
    }

    @Override
    public List<CounselorAgeDTO> findCounselorAgeByCounselorHireSeq(Long counselorHireSeq) {
        return queryFactory.select(Projections.constructor(CounselorAgeDTO.class
                        ,counselorAge.counselorAgeRangeSeq
                        ,counselorAge.counselorAgeRange))
                .from(counselorHire)
                .leftJoin(counselorHire.hireAges, counselorHireAge)
                .leftJoin(counselorHireAge.counselorAge, counselorAge)
                .where(counselorHire.hireSeq.eq(counselorHireSeq))
                .fetch();
    }

    @Override
    public Page<CounselorDTO> findAllJoinUser(ReadCounselorListRequest request, Pageable pageable) {
        QueryResults<CounselorDTO> results = queryFactory.select(Projections.constructor(CounselorDTO.class,
                counselorHire.hireSeq,
                counselorHire.hireTitle,
                counselorHire.hireGender,
                user.userNickname,
                counselorHire.regDate))
                .from(counselorHire)
                .join(counselorHire.user, user)
                .leftJoin(counselorHireAge).on(counselorHireAge.counselorHire.eq(counselorHire))
                .leftJoin(counselorHireType).on(counselorHireType.counselorHire.eq(counselorHire))
                .where(ageSeqEq(request.getSearchAgeSeq()),
                        genderEq(request.getSearchGender()),
                        typeSeqEq(request.getSearchTypeSeq()),
                        titleLike(request.getSearchTitle()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<CounselorDTO> counselorList = results.getResults();
        long totalCount = results.getTotal();

        return new PageImpl<>(counselorList, pageable, totalCount);
    }

    private BooleanExpression ageSeqEq(Long searchAgeSeq) {
        return Optional.ofNullable(searchAgeSeq).orElse(0L) == 0 ? null : counselorHireAge.counselorHireAgeSeq.eq(searchAgeSeq);
    }

    private BooleanExpression genderEq(Gender searchGender) {
        return searchGender == null ? null : counselorHire.hireGender.eq(searchGender);
    }

    private BooleanExpression typeSeqEq(Long searchTypeSeq) {
        return Optional.ofNullable(searchTypeSeq).orElse(0L) == 0 ? null : counselorHireType.counselorHireTypeSeq.eq(searchTypeSeq);
    }

    private BooleanExpression titleLike(String searchTitle) {
        log.info("searchTitle : {}", searchTitle);
        return StringUtils.isBlank(searchTitle) ? null : counselorHire.hireTitle.like("%"+searchTitle+"%");
    }
}
