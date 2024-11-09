package com.dwbh.backend.service;

import com.dwbh.backend.common.util.RandomStringGeneratorUtil;
import com.dwbh.backend.dto.user.SendEmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Map<String, Pair> fakeInMemoryDB = new HashMap<>();

    public void sendEmail(SendEmailRequest sendEmailRequest) {

        String verifyKey = RandomStringGeneratorUtil.generateRandomString(10);
        Pair pair = new Pair(verifyKey, LocalDateTime.now().plusMinutes(10));
        String body = "DWBH 이메일 인증 번호입니다.<br>" + verifyKey;
        System.out.println("body = " + body);
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

    public String verifyEmail(String email, String code) {
        Pair pair = fakeInMemoryDB.get(email);

        if (pair != null && pair.code.equals(code) && pair.expire.isAfter(LocalDateTime.now())) {
            fakeInMemoryDB.get(email).verify = true;
            return "Verify Success";
        } else { return "Verify Fail"; }
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