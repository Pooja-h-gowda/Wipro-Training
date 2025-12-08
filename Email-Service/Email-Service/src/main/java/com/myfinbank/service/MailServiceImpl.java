package com.myfinbank.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.myfinbank.bean.MailDetail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    // Injects JavaMailSender to send emails using SMTP configuration
    @Autowired
    private JavaMailSender mailSender;

    // Injects sender email ID from application properties
    @Value("${spring.mail.username}")
    private String sender;

    // Sends a simple email without any attachment
    @Override
    public String sendMail(MailDetail mailDetail) {
        try {
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setFrom(sender);
            emailMessage.setTo(mailDetail.getRecipient());
            emailMessage.setSubject(mailDetail.getSubject());
            emailMessage.setText(mailDetail.getMsgBody());
            mailSender.send(emailMessage);
            return "Email has been sent successfully...";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending email!!!";
        }
    }

    // Sends an email along with a file attachment
    @Override
    public String sendMailWithAttachment(MailDetail mailDetail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailDetail.getRecipient());
            mimeMessageHelper.setText(mailDetail.getMsgBody());
            mimeMessageHelper.setSubject(mailDetail.getSubject());

            FileSystemResource file = new FileSystemResource(new File(mailDetail.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            mailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending mail!!!";
        }
    }
}
