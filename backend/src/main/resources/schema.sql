DROP TABLE IF EXISTS `tb_file`;

CREATE TABLE `tb_file` (
                           `file_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '파일 번호',
                           `file_name`	varchar(100)	NOT NULL	COMMENT '파일 이름',
                           `file_type`	varchar(10)	NOT NULL	COMMENT '파일 타입(image/video)',
                           `file_path`	varchar(2000)	NOT NULL	COMMENT '파일 주소',
                           `file_content_type`	varchar(50)	NOT NULL	COMMENT '(image/jpg)',
                           `file_reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '파일 등록일',
                           `file_mod_date`	timestamp	NULL	COMMENT '파일 수정일',
                           `file_del_date`	timestamp	NULL	COMMENT '파일 삭제일',
                           CONSTRAINT `PK_file` PRIMARY KEY (`file_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='파일';

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
                           `user_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '회원 번호',
                           `user_email`	varchar(100)	NOT NULL	COMMENT '회원 이메일',
                           `user_password`	varchar(100)	NOT NULL	COMMENT '회원 비밀번호',
                           `user_nickname`	varchar(50)	NOT NULL	COMMENT '회원 닉네임',
                           `user_gender`	varchar(10)	NULL	COMMENT '회원 성별  (male, female)',
                           `user_birthday`	timestamp	NULL	COMMENT '회원 생일',
                           `user_mbti`	varchar(10)	NULL	COMMENT '회원 MBTI',
                           `user_status`	varchar(10)	NOT NULL	DEFAULT 'activate'	COMMENT '회원 상태',
                           `user_temperature`	DECIMAL(5,2)	NOT NULL	DEFAULT 36.5	COMMENT '회원 온도',
                           `user_reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '회원 가입 날짜',
                           `user_del_date`	timestamp	NULL	COMMENT '회원 탈퇴 날짜',
                           CONSTRAINT `PK_user` PRIMARY KEY (`user_seq`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원';

DROP TABLE IF EXISTS `tb_user_profile_file`;

CREATE TABLE `tb_user_profile_file` (
                                        `user_profile_file_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '회원 프로필 사진 번호',
                                        `user_seq`	bigint	NOT NULL	COMMENT '회원 번호',
                                        `file_seq`	bigint	NOT NULL	COMMENT '파일 번호',
                                        CONSTRAINT `PK_user_profile_file` PRIMARY KEY (`user_profile_file_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 프로필 사진';

DROP TABLE IF EXISTS `tb_counselor_age_range`;

CREATE TABLE `tb_counselor_age_range` (
                                          `counselor_age_range_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '상담사 나이대 번호',
                                          `counselor_age_range`	varchar(10)	NOT NULL	COMMENT '상담사 나이대',
                                          `counselor_age_min_age`	int	NOT NULL	COMMENT '상담사 나이대 최솟값',
                                          `counselor_age_max_age`	int	NOT NULL	COMMENT '상담사 나이대 최댓값',
                                          CONSTRAINT `PK_counselor_age_range` PRIMARY KEY (`counselor_age_range_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='파일';

DROP TABLE IF EXISTS `tb_counselor_hire`;

CREATE TABLE `tb_counselor_hire` (
                                     `counselor_hire_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '상담사 구인 번호',
                                     `user_seq`	bigint	NOT NULL	COMMENT '상담사 구인 작성자 회원 번호',
                                     `counselor_hire_title`	varchar(100)	NOT NULL	COMMENT '상담사 구인 제목',
                                     `counselor_hire_content`	varchar(4500)	NOT NULL	COMMENT '상담사 구인 내용',
                                     `counselor_hire_counselor_gender`	varchar(10)	NULL	COMMENT '상담사 구인 작성자가 원하는 상담사 성별 (male, female)',
                                     `counselor_age_range_seq`	bigint	NOT NULL	COMMENT '상담사 구인 작성자가 원하는 상담사 나이대 번호',
                                     `counselor_hire_counselor_type`	varchar(10)	NULL	COMMENT '상담사 구인 작성자가 원하는 조언 유형 (idealism, realism)',
                                     `counselor_hire_reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '상담사 구인 작성일',
                                     `counselor_hire_mod_date`	timestamp	NULL	COMMENT '상담사 구인 수정일',
                                     `counselor_hire_del_date`	timestamp	NULL	COMMENT '상담사 구인 삭제일',
                                     CONSTRAINT `PK_counsel_hire` PRIMARY KEY (`counselor_hire_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상담사 구인 (게시판)';

DROP TABLE IF EXISTS `tb_counsel_offer`;

CREATE TABLE `tb_counsel_offer` (
                                    `counsel_offer_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '상담 제안 번호',
                                    `counselor_hire_seq`	bigint	NOT NULL	COMMENT '상담사 구인 번호',
                                    `user_seq`	bigint	NOT NULL	COMMENT '상담 제안 댓글 작성자',
                                    `counsel_offer_content`	varchar(1500)	NOT NULL	COMMENT '상담 제안 내용',
                                    `counsel_offer_private_yn`	enum('Y', 'N')	NOT NULL	DEFAULT 'N'	COMMENT '상담 제안 비밀댓글 여부',
                                    `counsel_offer_reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '상담 제안 작성일',
                                    `counsel_offer_mod_date`	timestamp	NULL	COMMENT '상담 제안 수정일',
                                    `counsel_offer_del_date`	timestamp	NULL	COMMENT '상담 제안 삭제일',
                                    CONSTRAINT `PK_counsel_offer` PRIMARY KEY (`counsel_offer_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상담 제안 (댓글)';

DROP TABLE IF EXISTS `tb_counselor_hire_file`;

CREATE TABLE `tb_counselor_hire_file` (
                                          `counselor_hire_file_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '상담사 구인 파일 번호',
                                          `counselor_hire_seq`	bigint	NOT NULL	COMMENT '상담사 구인 번호',
                                          `file_seq`	bigint	NOT NULL	COMMENT '파일 번호',
                                          CONSTRAINT `PK_counselor_hire_file` PRIMARY KEY (`counselor_hire_file_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='상담사 구인 파일';

DROP TABLE IF EXISTS `tb_counsel_offer_file`;

CREATE TABLE `tb_counsel_offer_file` (
                                         `counsel_offer_file_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '상담 제안 파일 번호',
                                         `counsel_offer_seq`	bigint	NOT NULL	COMMENT '상담 제안 번호',
                                         `file_seq`	bigint	NOT NULL	COMMENT '파일 번호',
                                         CONSTRAINT `PK_counsel_offer_file` PRIMARY KEY (`counsel_offer_file_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='상담 제안 파일';

DROP TABLE IF EXISTS `tb_chat`;

CREATE TABLE `tb_chat` (
                           `chat_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '채팅방 번호',
                           `counsel_offer_seq`	bigint	NOT NULL	COMMENT '상담 제안 번호',
                           `chat_send_seq`	bigint	NOT NULL	COMMENT '댓글 작성자 번호',
                           `chat_receive_seq`	bigint	NOT NULL	COMMENT '게시판 작성자 번호',
                           `chat_reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '채팅방 생성일',
                           `chat_del_date`	timestamp	NULL	COMMENT '채팅방 종료일',
                           CONSTRAINT `PK_chat`  PRIMARY KEY (`chat_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='채팅방';

DROP TABLE IF EXISTS `tb_evaluation`;

CREATE TABLE `tb_evaluation` (
                                 `evaluation_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '평가 번호',
                                 `chat_seq`	bigint	NOT NULL		COMMENT '채팅방 번호',
                                 `evaluation_content`	varchar(1000)	NULL	COMMENT '평가 내용',
                                 `evaluation_score`	DECIMAL(5,2)	NOT NULL	COMMENT '평가 점수',
                                 `evaluation_reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '평가 시작 날짜',
                                 CONSTRAINT `PK_evaluation`  PRIMARY KEY (`evaluation_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='평가';

DROP TABLE IF EXISTS `tb_notify`;

CREATE TABLE `tb_notify` (
                             `notify_seq`	bigint	NOT NULL	AUTO_INCREMENT	COMMENT '알림 번호',
                             `chat_seq`	bigint	NOT NULL	COMMENT '채팅방 번호',
                             `notify_check_yn`	enum('Y', 'N')	NOT NULL	DEFAULT 'N'	COMMENT '알림 확인 여부',
                             CONSTRAINT `PK_notify` PRIMARY KEY (`notify_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='알림';

ALTER TABLE `tb_chat`
    ADD CONSTRAINT `FK_tb_counsel_offer_TO_tb_chat_1`
        FOREIGN KEY (`counsel_offer_seq`)
            REFERENCES `tb_counsel_offer` (`counsel_offer_seq`);

ALTER TABLE `tb_chat`
    ADD CONSTRAINT `FK_tb_user_TO_tb_chat_1`
        FOREIGN KEY (`chat_send_seq`)
            REFERENCES `tb_user` (`user_seq`);

ALTER TABLE `tb_chat`
    ADD CONSTRAINT `FK_tb_user_TO_tb_chat_2`
        FOREIGN KEY (`chat_receive_seq`)
            REFERENCES `tb_user` (`user_seq`);

ALTER TABLE `tb_counsel_offer`
    ADD CONSTRAINT `FK_tb_counselor_hire_TO_tb_counsel_offer_1`
        FOREIGN KEY (`counselor_hire_seq`)
            REFERENCES `tb_counselor_hire` (`counselor_hire_seq`);

ALTER TABLE `tb_counsel_offer`
    ADD CONSTRAINT `FK_tb_user_TO_tb_counsel_offer_1`
        FOREIGN KEY (`user_seq`)
            REFERENCES `tb_user` (`user_seq`);

ALTER TABLE `tb_counselor_hire`
    ADD CONSTRAINT `FK_tb_user_TO_tb_counselor_hire_1`
        FOREIGN KEY (`user_seq`)
            REFERENCES `tb_user` (`user_seq`);

ALTER TABLE `tb_counselor_hire`
    ADD CONSTRAINT `FK_tb_counselor_age_range_TO_tb_counselor_hire_1`
        FOREIGN KEY (`counselor_age_range_seq`)
            REFERENCES `tb_counselor_age_range` (`counselor_age_range_seq`);

ALTER TABLE `tb_evaluation`
    ADD CONSTRAINT `FK_tb_chat_TO_tb_evaluation_1`
        FOREIGN KEY (`chat_seq`)
            REFERENCES `tb_chat` (`chat_seq`);

ALTER TABLE `tb_counselor_hire_file`
    ADD CONSTRAINT `FK_tb_counselor_hire_TO_tb_counselor_hire_file_1`
        FOREIGN KEY (`counselor_hire_seq`)
            REFERENCES `tb_counselor_hire` (`counselor_hire_seq`);

ALTER TABLE `tb_counselor_hire_file`
    ADD CONSTRAINT `FK_tb_file_TO_tb_counselor_hire_file_1`
        FOREIGN KEY (`file_seq`)
            REFERENCES `tb_file` (`file_seq`);

ALTER TABLE `tb_counsel_offer_file`
    ADD CONSTRAINT `FK_tb_counsel_offer_TO_tb_counsel_offer_file_1`
        FOREIGN KEY (`counsel_offer_seq`)
            REFERENCES `tb_counsel_offer` (`counsel_offer_seq`);

ALTER TABLE `tb_counsel_offer_file`
    ADD CONSTRAINT `FK_tb_file_TO_tb_counsel_offer_file_1`
        FOREIGN KEY (`file_seq`)
            REFERENCES `tb_file` (`file_seq`);

ALTER TABLE `tb_user_profile_file`
    ADD CONSTRAINT `FK_tb_user_TO_tb_user_profile_file_1`
        FOREIGN KEY (`user_seq`)
            REFERENCES `tb_user` (`user_seq`);

ALTER TABLE `tb_user_profile_file`
    ADD CONSTRAINT `FK_tb_file_TO_tb_user_profile_file_1`
        FOREIGN KEY (`file_seq`)
            REFERENCES `tb_file` (`file_seq`);

ALTER TABLE `tb_notify`
    ADD CONSTRAINT `FK_tb_chat_TO_tb_notify_1`
        FOREIGN KEY (`chat_seq`)
            REFERENCES `tb_chat` (`chat_seq`);
