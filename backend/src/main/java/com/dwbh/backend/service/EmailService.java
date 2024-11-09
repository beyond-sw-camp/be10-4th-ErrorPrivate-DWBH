package com.dwbh.backend.service;

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

    private final JavaMailSender javaMailSender;
    private final Map<String, Pair> fakeInMemoryDB = new HashMap<>();

    // 이메일 인증 메일 전송
    public void sendEmail(SendEmailRequest sendEmailRequest) {

        String verifyKey = RandomStringGeneratorUtil.generateRandomString(10);
        Pair pair = new Pair(verifyKey, LocalDateTime.now().plusMinutes(10));
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

    // 이메일 인증 코드 검증
    public String verifyEmail(String email, String code) {
        Pair pair = fakeInMemoryDB.get(email);

        if (pair != null && pair.code.equals(code) && pair.expire.isAfter(LocalDateTime.now())) {
            fakeInMemoryDB.get(email).verify = true;
            return "Verify Success";
        } else { return "Verify Fail"; }
    }

    // FakeInMemoryDB 청소
    @Scheduled(fixedDelay = 60000)
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
    final LocalDateTime expire;
    boolean verify;

    public Pair(String code, LocalDateTime expire) {
        this.code = code;
        this.expire = expire;
        this.verify = false;
    }
}
