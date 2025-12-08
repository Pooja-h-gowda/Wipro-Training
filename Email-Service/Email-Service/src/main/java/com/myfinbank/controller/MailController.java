package com.myfinbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.myfinbank.bean.MailDetail;
import com.myfinbank.service.MailService;

@RestController
@RequestMapping("/api")
public class MailController {

    // Injects MailService to handle email sending operations
    @Autowired
    private MailService mailService;

    // Sends a basic email without attachment
    @PostMapping("/send-mail")
    public String sendMail(@RequestBody MailDetail mailDetail) {
        return mailService.sendMail(mailDetail);
    }

    // Sends an email with attachment
    @PostMapping("/send-mail-attachment")
    public String sendMailWithAttachment(@RequestBody MailDetail mailDetail) {
        return mailService.sendMailWithAttachment(mailDetail);
    }

    // Sends a zero balance alert email triggered by customer service
    @PostMapping("/zero-balance-alert")
    public String sendZeroBalanceAlert(@RequestBody MailDetail mailDetail) {
        return mailService.sendMail(mailDetail);
    }
}
