package com.myfinbank.service;

import com.myfinbank.bean.MailDetail;

public interface MailService {
    String sendMail(MailDetail mailDetail);
    String sendMailWithAttachment(MailDetail mailDetail);
}
