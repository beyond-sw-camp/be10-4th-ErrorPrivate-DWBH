package com.dwbh.backend.repository.counselor_hire;

import com.dwbh.backend.dto.counselor_hire.CounselorDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public class CounselorCustomRepositoryImpl implements CounselorCustomRepository {

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

}
