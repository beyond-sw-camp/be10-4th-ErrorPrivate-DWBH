package com.dwbh.backend.service;

import com.dwbh.backend.common.util.JwtUtil;
import com.dwbh.backend.common.util.RandomStringGeneratorUtil;
import com.dwbh.backend.dto.user.SendEmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender;
    private final Map<String, Pair> fakeInMemoryDB = new HashMap<>();

    // 인증 시간 ( 분 / 미리초 )
    private static final int VERIFY_LIMIT = 10;
    private static final long VERIFY_LIMIT_MS = 600000L;

    // 이메일 인증 메일 전송
    public void sendEmail(SendEmailRequest sendEmailRequest) {

        String verifyKey = RandomStringGeneratorUtil.generateRandomString(VERIFY_LIMIT);
        Pair pair = new Pair(verifyKey, LocalDateTime.now().plusMinutes(VERIFY_LIMIT));
        String body = "DWBH 이메일 인증 번호입니다.<br>" + verifyKey;
        fakeInMemoryDB.put(sendEmailRequest.getEmail(), pair);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(sendEmailRequest.getEmail());
            helper.setSubject("DWBH 이메일 인증");
            helper.setText(body, true);
            javaMailSender.send(message);
            fakeInMemoryDB.put(sendEmailRequest.getEmail(), pair);
            System.out.println("fakeInMemoryDB = " + fakeInMemoryDB);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 이메일 검증 여부 반환
    public boolean verifyEmail(String email) {
        return fakeInMemoryDB.get(email).expire.isAfter(LocalDateTime.now()) &&
                fakeInMemoryDB.get(email).verify;
    }

    // 이메일 인증 코드 검증
    public String verifyEmailCode(String email, String code) {
        Pair pair = fakeInMemoryDB.get(email);

        if (pair != null && pair.code.equals(code) && pair.expire.isAfter(LocalDateTime.now())) {
            fakeInMemoryDB.get(email).expire = LocalDateTime.now().plusMinutes(VERIFY_LIMIT);
            fakeInMemoryDB.get(email).verify = true;

            Map<String, String> claim = new HashMap<>();
            claim.put("email", email);
            return jwtUtil.createCustomToken("DWBH Email verify", claim, VERIFY_LIMIT_MS);
        } else { return null; }
    }

    // FakeInMemoryDB 청소
    @Scheduled(fixedDelay = VERIFY_LIMIT_MS)
    public void cleanFakeInMemoryDB() {
        log.info("이메일 인증 정보 청소하려 왔어요~");

        for (Map.Entry<String, Pair> entry : fakeInMemoryDB.entrySet()) {
            if (entry.getValue().expire.isBefore(LocalDateTime.now())) {
                fakeInMemoryDB.remove(entry.getKey());
                log.info("청소 당한 키: " + entry.getKey());
            }
        }
    }
}

class Pair {
    final String code;
    LocalDateTime expire;
    boolean verify;

    public Pair(String code, LocalDateTime expire) {
        this.code = code;
        this.expire = expire;
        this.verify = false;
    }
}
