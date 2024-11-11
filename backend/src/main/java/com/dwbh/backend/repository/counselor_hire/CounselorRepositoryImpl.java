package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import com.dwbh.backend.dto.counselor_hire.*;
import com.dwbh.backend.entity.QCounselorType;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.dwbh.backend.entity.QCounselorAge.counselorAge;
import static com.dwbh.backend.entity.QCounselorHire.counselorHire;
import static com.dwbh.backend.entity.QCounselorHireAge.counselorHireAge;
import static com.dwbh.backend.entity.QCounselorHireFile.counselorHireFile;
import static com.dwbh.backend.entity.QCounselorHireType.counselorHireType;
import static com.dwbh.backend.entity.QCounselorType.counselorType1;
import static com.dwbh.backend.entity.QFile.file;
import static com.dwbh.backend.entity.QUser.user;
import static com.querydsl.core.types.Projections.list;

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

        List<String> counselorTypes = queryFactory
                .select(counselorType1.counselorType)
                .from(counselorHireType)
                .join(counselorType1).on(counselorHireType.counselorType.counselorTypeSeq.eq(counselorType1.counselorTypeSeq))
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
                        counselorHire.regDate,
                        counselorHire.modDate,
                        file.filePath.as("hireFilePath")
                        ))
                .from(counselorHire)
                .join(counselorHire.user, user)
                .leftJoin(counselorHireFile).on(counselorHire.hireSeq.eq(counselorHireFile.counselorHire.hireSeq))
                .leftJoin(file).on(counselorHireFile.file.fileSeq.eq(file.fileSeq))
                .where(counselorHire.hireSeq.eq(hireSeq))
                .fetchOne();

        // DTO에 나이대 설정
        if (result != null) {
            result.setCounselorAgeRanges(counselorAgeRanges);
            result.setCounselorTypes(counselorTypes);
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

        QCounselorType counselorType = new QCounselorType("counselorType");
        QueryResults<CounselorDTO> results = queryFactory.select(
                Projections.constructor(
                        CounselorDTO.class,
                        counselorHire.hireSeq,
                        counselorHire.hireTitle,
                        counselorHire.hireGender,
                        user.userNickname,
                        counselorHire.regDate,
                        counselorAge.counselorAgeRangeSeq,
                        counselorAge.counselorAgeRange,
                        counselorType.counselorTypeSeq,
                        counselorType.counselorType
                ))
                .from(counselorHire)
                .join(counselorHire.user, user)
                .leftJoin(counselorHireAge).on(counselorHireAge.counselorHire.eq(counselorHire))
                .leftJoin(counselorHireType).on(counselorHireType.counselorHire.eq(counselorHire))
                .leftJoin(counselorAge).on(counselorHireAge.counselorAge.eq(counselorAge))
                .leftJoin(counselorType).on(counselorHireType.counselorType.eq(counselorType))
                .where(ageSeqEq(request.getSearchAgeSeq()),
                        genderEq(request.getSearchGender()),
                        typeSeqEq(request.getSearchTypeSeq()),
                        titleLike(request.getSearchTitle()),
                        counselorHire.delDate.isNull())
                .orderBy(counselorHire.regDate.desc())
                .fetchResults();

        List<CounselorDTO> counselorList = results.getResults();

        // 그룹화 처리
        Map<Long, CounselorDTO> groupedMap = new HashMap<>();

        for (CounselorDTO dto : counselorList) {
            CounselorDTO groupedDto = groupedMap.getOrDefault(dto.getHireSeq(), new CounselorDTO(
                    dto.getHireSeq(),
                    dto.getHireTitle(),
                    dto.getHireGender(),
                    dto.getUserNickname(),
                    dto.getRegDate(),
                    new ArrayList<>(), // ageRanges 리스트 초기화
                    new ArrayList<>()  // types 리스트 초기화
            ));

            // AgeRanges 추가
            if (dto.getCounselorAgeRangeSeq() != null) {
                groupedDto.getAgeRanges().add(new CounselorAgeDTO(dto.getCounselorAgeRangeSeq(), dto.getCounselorAgeRange()));
            }

            // Types 추가
            if (dto.getCounselorTypeSeq() != null) {
                groupedDto.getTypes().add(new CounselorTypeDTO(dto.getCounselorTypeSeq(), dto.getCounselorType()));
            }

            groupedMap.put(dto.getHireSeq(), groupedDto);
        }

        for (Map.Entry<Long, CounselorDTO> entry : groupedMap.entrySet()) {
            entry.getValue().setAgeRanges(entry.getValue().getAgeRanges().stream().distinct().sorted(Comparator.comparing(CounselorAgeDTO::getCounselorAgeRangeSeq)).toList());
            entry.getValue().setTypes(entry.getValue().getTypes().stream().distinct().toList());
        }

        // 그룹화된 리스트로 변환
        List<CounselorDTO> groupedList = new ArrayList<>(groupedMap.values()).stream().sorted(Comparator.comparing(CounselorDTO::getRegDate).reversed()).toList();

        return new PageImpl<>(groupedList, pageable, groupedList.size());
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
