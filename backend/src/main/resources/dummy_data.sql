-- tb_user 테이블 더미 데이터
INSERT INTO tb_user (user_email, user_password, user_nickname, user_gender, user_birthday, user_mbti, user_status,
                     user_temperature)
VALUES ('test1@example.com', '$2a$10$kbu8c9b44gvSbXDX3B.ePuzgmCFsm.UHnP6nl0eklFTOdeCUUrQqq', 'test1', 'male',
        '1990-01-01', 'INTJ', 'activate', 36.5),
       ('test2@example.com', '$2a$10$Uirmt2t9cE6oD85r5LYXwOIQSjfbjZdVZy3Hgk30ybKJnycHnD1MK', 'test2', 'female',
        '1992-02-02', 'ENFP', 'activate', 36.5),
       ('test3@example.com', '$2a$10$TPfSiyZ9uPFwzPGi94EYN.9RMZbUX0yrRzuqnnUmEL/S0mi/jYLkq', 'test3', 'male',
        '1999-09-09', 'ENTP', 'activate', 36.5),
       ('user01@example.com', '$2a$10$kbu8c9b44gvSbXDX3B.ePuzgmCFsm.UHnP6nl0eklFTOdeCUUrQqq', 'user1', 'male',
        '1990-01-01', 'INTJ', 'activate', 36.5),
       ('user02@example.com', '$2a$10$Uirmt2t9cE6oD85r5LYXwOIQSjfbjZdVZy3Hgk30ybKJnycHnD1MK', 'user2', 'female',
        '1992-02-02', 'ENFP', 'activate', 36.5),
       ('user03@example.com', '$2a$10$TPfSiyZ9uPFwzPGi94EYN.9RMZbUX0yrRzuqnnUmEL/S0mi/jYLkq', 'user3', 'male',
        '1999-09-09', 'ENTP', 'activate', 36.5),
       ('user04@example.com', '$2a$10$kbu8c9b44gvSbXDX3B.ePuzgmCFsm.UHnP6nl0eklFTOdeCUUrQqq', 'user4', 'female',
        '1985-03-01', 'ISFJ', 'deactivate', 36.0),
       ('user05@example.com', '$2a$10$Uirmt2t9cE6oD85r5LYXwOIQSjfbjZdVZy3Hgk30ybKJnycHnD1MK', 'user5', 'male',
        '1995-06-15', 'ISTJ', 'activate', 36.7),
       ('user06@example.com', '$2a$10$TPfSiyZ9uPFwzPGi94EYN.9RMZbUX0yrRzuqnnUmEL/S0mi/jYLkq', 'user6', 'female',
        '1990-08-22', 'INFJ', 'activate', 37.0),
       ('user07@example.com', '$2a$10$kbu8c9b44gvSbXDX3B.ePuzgmCFsm.UHnP6nl0eklFTOdeCUUrQqq', 'user7', 'male',
        '1993-11-11', 'INTP', 'activate', 36.8),
       ('user08@example.com', '$2a$10$Uirmt2t9cE6oD85r5LYXwOIQSjfbjZdVZy3Hgk30ybKJnycHnD1MK', 'user8', 'female',
        '1991-01-18', 'ENFJ', 'activate', 36.6),
       ('user09@example.com', '$2a$10$TPfSiyZ9uPFwzPGi94EYN.9RMZbUX0yrRzuqnnUmEL/S0mi/jYLkq', 'user9', 'male',
        '1989-05-25', 'ISFP', 'deactivate', 36.4),
       ('user10@example.com', '$2a$10$kbu8c9b44gvSbXDX3B.ePuzgmCFsm.UHnP6nl0eklFTOdeCUUrQqq', 'user10', 'female',
        '1996-07-03', 'ESFP', 'activate', 37.2);

-- tb_counselor_hire 테이블 더미 데이터
INSERT INTO tb_counselor_hire (user_seq, counselor_hire_title, counselor_hire_content, counselor_hire_counselor_gender)
VALUES (1, '현실적인 조언 해주실 분 구합니댜', '제가 어쩌고 잘못을 했는데 저는 잘못이라고 생각 안하는데 냉정하게 누구 잘못인지 알려주세요.', 'female'),
       (2, '너무 우울한 하루 ㅠㅠ', '오늘 너무 우울한데 그냥 무지성 공감 해주실 분 연락주세여 ㅠ', 'male'),
       (3, '의견이 필요합니다', '갈등 상황에 처했는데 진솔한 피드백을 부탁드립니다.', 'female'),
       (4, '상담 부탁드립니다', '어려운 상황인데 어떻게 해야 할지 모르겠어요.', 'male'),
       (5, '기분 전환이 필요해요', '다양한 의견을 듣고 싶습니다.', 'female'),
       (6, '결정을 못하겠어요', '이러저러한 상황인데 어떤 선택이 좋을까요?', 'male'),
       (7, '새로운 시작', '새로운 도전을 앞두고 조언 부탁드립니다.', 'female'),
       (8, '현실적 조언을 원해요', '당장 해결할 문제에 대해 솔직한 의견 주세요.', 'male'),
       (9, '공감을 구해요', '마음이 힘든데 공감해주실 분 있나요?', 'female'),
       (10, '인생 상담', '큰 결정을 앞두고 있는데 경험에서 우러난 조언 부탁드립니다.', 'male');

-- tb_counsel_offer 테이블 더미 데이터
INSERT INTO tb_counsel_offer (counselor_hire_seq, user_seq, counsel_offer_content, counsel_offer_private_yn)
VALUES (1, 2, '안녕하세요, 상담 요청드립니다.', 'N'),
       (2, 1, '지금 바로 상담 가능하신가요?', 'Y'),
       (3, 4, '안녕하세요, 상담 요청합니다.', 'N'),
       (4, 3, '상담 가능 시간 알려주세요.', 'Y'),
       (5, 6, '의견 듣고 싶습니다.', 'N'),
       (6, 5, '안녕하세요, 상담 요청합니다.', 'Y'),
       (7, 8, '도움이 필요해요.', 'N'),
       (8, 7, '바로 상담 가능하신가요?', 'Y'),
       (9, 10, '상담 요청 드려요.', 'N'),
       (10, 9, '상담 가능 시간 부탁드립니다.', 'Y');

-- tb_chat 테이블 더미 데이터
# INSERT INTO tb_chat (counsel_offer_seq, chat_send_seq, chat_receive_seq, chat_read_yn)
# VALUES (1, 1, 2, 'N'),
#        (2, 2, 1, 'Y'),
#        (3, 3, 4, 'N'),
#        (4, 4, 3, 'Y'),
#        (5, 5, 6, 'N'),
#        (6, 6, 5, 'Y'),
#        (7, 7, 8, 'N'),
#        (8, 8, 7, 'Y'),
#        (9, 9, 10, 'N'),
#        (10, 10, 9, 'Y');


-- tb_evaluation 테이블 더미 데이터
# INSERT INTO tb_evaluation (chat_seq, evaluation_satisfaction, evaluation_communication, evaluation_kindness,
#                            evaluation_score)
# VALUES (1, 5, 4, 5, 4.8),
#        (2, 3, 3, 4, 3.5),
#        (3, 4, 5, 5, 4.7),
#        (4, 2, 3, 3, 3.0),
#        (5, 5, 5, 5, 5.0),
#        (6, 4, 4, 4, 4.0),
#        (7, 3, 4, 5, 4.2),
#        (8, 2, 2, 3, 2.5),
#        (9, 4, 3, 4, 3.8),
#        (10, 5, 5, 5, 5.0);

-- tb_file 테이블 더미 데이터
INSERT INTO tb_file (file_name, file_type, file_path, file_content_type)
VALUES ('profile1.jpg', 'image', '/uploads/profile1.jpg', 'image/jpg'),
       ('document1.pdf', 'document', '/uploads/document1.pdf', 'application/pdf'),
       ('profile2.jpg', 'image', '/uploads/profile2.jpg', 'image/jpg'),
       ('profile3.jpg', 'image', '/uploads/profile3.jpg', 'image/jpg'),
       ('guide.pdf', 'document', '/uploads/guide.pdf', 'application/pdf'),
       ('avatar.png', 'image', '/uploads/avatar.png', 'image/png');


-- tb_counselor_hire_file 테이블 더미 데이터
INSERT INTO tb_counselor_hire_file (counselor_hire_seq, file_seq)
VALUES (1, 1),
       (2, 2);

-- tb_counsel_offer_file 테이블 더미 데이터
INSERT INTO tb_counsel_offer_file (file_seq, counsel_offer_seq)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);

-- tb_user_profile_file 테이블 더미 데이터
INSERT INTO tb_user_profile_file (file_seq, user_seq)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);

-- tb_notification 테이블 더미 데이터
# INSERT INTO tb_notification (chat_seq, user_seq, notification_check_yn)
# VALUES (1, 1, 'N'),
#        (2, 2, 'Y'),
#        (3, 3, 'N'),
#        (4, 4, 'Y'),
#        (5, 5, 'N'),
#        (6, 6, 'Y'),
#        (7, 7, 'N'),
#        (8, 8, 'Y'),
#        (9, 9, 'N'),
#        (10, 10, 'Y');

-- tb_counselor_age 테이블 더미 데이터
INSERT INTO tb_counselor_age (counselor_age_range)
VALUES ('10대'),
       ('20대'),
       ('30대'),
       ('40대'),
       ('50대'),
       ('60대'),
       ('70대'),
       ('80대'),
       ('90대'),
       ('100대');

-- tb_counselor_hire_age 테이블 더미 데이터
INSERT INTO tb_counselor_hire_age (counselor_hire_seq, counselor_age_range_seq)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10);

-- tb_counselor_type 테이블 더미 데이터
INSERT INTO tb_counselor_type (counselor_type)
VALUES ('T의 현실적 조언'),
       ('F식 공감');

-- tb_counselor_hire_type 테이블 더미 데이터
INSERT INTO tb_counselor_hire_type (counselor_hire_seq, counselor_type_seq)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 2),
       (5, 1),
       (6, 2),
       (7, 1),
       (8, 2),
       (9, 1),
       (10, 2);

