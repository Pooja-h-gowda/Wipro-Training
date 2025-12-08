package com.myfinbank.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDetail {
    private String recipient;
    private String subject;
    private String msgBody;
    private String attachment;
}
