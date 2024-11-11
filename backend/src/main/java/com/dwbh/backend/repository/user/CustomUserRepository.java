package com.dwbh.backend.repository.user;

import com.dwbh.backend.dto.UserDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dwbh.backend.entity.QFile.file;
import static com.dwbh.backend.entity.QUser.user;
import static com.dwbh.backend.entity.QUserProfileFile.userProfileFile;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository {

    private final EntityManager em;

    // 회원 상세 조회
    public UserDetailResponse findUserDetailResponse(Long userSeq) {
        return new JPAQueryFactory(em)
                .select(Projections.constructor(UserDetailResponse.class,
                        file.filePath,
                        user.userBirthday,
                        user.userNickname,
                        user.userMbti,
                        user.userGender,
                        user.userTemperature))
                .from(user)
                .leftJoin(userProfileFile).on(user.userSeq.eq(userProfileFile.userSeq))
                .leftJoin(file).on(userProfileFile.fileSeq.eq(file.fileSeq))
                .where(user.userSeq.eq(userSeq))
                .fetchOne();
    }
}
