-- tb_user 테이블 더미 데이터
INSERT INTO tb_user (user_email, user_password, user_nickname, user_gender, user_birthday, user_mbti, user_status, user_temperature)
VALUES
    ('test1@example.com', 'password1', 'user1', 'male', '1990-01-01', 'INTJ', 'activate', 36.5),
    ('test2@example.com', 'password2', 'user2', 'female', '1992-02-02', 'ENFP', 'activate', 36.8),
    ('test3@example.com', 'password3', 'user3', 'male', '1999-09-09', 'ENTP', 'activate', 36.50);

-- tb_counselor_hire 테이블 더미 데이터
INSERT INTO tb_counselor_hire (user_seq, counselor_hire_title, counselor_hire_content, counselor_hire_counselor_gender)
VALUES
    (1, '현실적인 조언 해주실 분 구합니댜', '제가 어쩌고 잘못을 했는데 저는 잘못이라고 생각 안하는데 냉정하게 누구 잘못인지 알려주세요.', 'female'),
    (2, '너무 우울한 하루 ㅠㅠ', '오늘 너무 우울한데 그냥 무지성 공감 해주실 분 연락주세여 ㅠ', 'male');

-- tb_counsel_offer 테이블 더미 데이터
INSERT INTO tb_counsel_offer (counselor_hire_seq, user_seq, counsel_offer_content, counsel_offer_private_yn)
VALUES
    (1, 2, '안녕하세요, 상담 요청드립니다.', 'N'),
    (2, 1, '지금 바로 상담 가능하신가요?', 'Y');

-- tb_chat 테이블 더미 데이터
INSERT INTO tb_chat (counsel_offer_seq, chat_send_seq, chat_receive_seq, chat_read_yn)
VALUES
    (1, 1, 2, 'N'),
    (2, 2, 1, 'Y');

-- tb_evaluation 테이블 더미 데이터
INSERT INTO tb_evaluation (chat_seq, evaluation_satisfaction, evaluation_communication, evaluation_kindness, evaluation_score)
VALUES
    (1, 5, 4, 5, 4.8),
    (2, 3, 3, 4, 3.5);

-- tb_file 테이블 더미 데이터
INSERT INTO tb_file (file_name, file_type, file_path, file_content_type)
VALUES
    ('profile1.jpg', 'image', '/uploads/profile1.jpg', 'image/jpg'),
    ('document1.pdf', 'document', '/uploads/document1.pdf', 'application/pdf');

-- tb_counselor_hire_file 테이블 더미 데이터
INSERT INTO tb_counselor_hire_file (counselor_hire_seq, file_seq)
VALUES
    (1, 1),
    (2, 2);

-- tb_counsel_offer_file 테이블 더미 데이터
INSERT INTO tb_counsel_offer_file (file_seq, counsel_offer_seq)
VALUES
    (1, 1),
    (2, 2);

-- tb_user_profile_file 테이블 더미 데이터
INSERT INTO tb_user_profile_file (file_seq, user_seq)
VALUES
    (1, 1),
    (2, 2);

-- tb_notification 테이블 더미 데이터
INSERT INTO tb_notification (chat_seq, user_seq, notification_check_yn)
VALUES
    (1, 1, 'N'),
    (2, 2, 'Y');

-- tb_counselor_age 테이블 더미 데이터
INSERT INTO tb_counselor_age (counselor_age_range)
VALUES
    ('10대'),
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
VALUES
    (1, 1),
    (2, 2);

-- tb_counselor_type 테이블 더미 데이터
INSERT INTO tb_counselor_type (counselor_type)
VALUES
    ('T의 현실적 조언'),
    ('F식 공감');

-- tb_counselor_hire_type 테이블 더미 데이터
INSERT INTO tb_counselor_hire_type (counselor_hire_seq, counselor_type_seq)
VALUES
    (1, 1),
    (2, 2);

