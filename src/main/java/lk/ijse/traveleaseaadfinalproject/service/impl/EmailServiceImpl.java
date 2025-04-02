package lk.ijse.traveleaseaadfinalproject.service.impl;

import lk.ijse.traveleaseaadfinalproject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendGuideRegistrationEmail(String toEmail, String guideName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Guide Registration Successful");
        message.setText("Dear " + guideName + ",\n\nYour registration as a guide is successful!\n\nBest Regards,\nTravelEase Team");

        mailSender.send(message);
    }
}
