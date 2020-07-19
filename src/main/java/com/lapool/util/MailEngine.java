/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.util;

import com.lapool.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author akshat
 */
public class MailEngine {
    
    final static Logger log = LoggerFactory.getLogger(MailEngine.class);

    private MailSender mailSender;
    private SimpleMailMessage simpleMailMessage;

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail( String emailID, String subject, String name, String content) throws BaseException {
        final String methodName = "sendMail()";
        log.info("Entering "+methodName);
        try{
        SimpleMailMessage message = new SimpleMailMessage(simpleMailMessage);
        message.setTo(emailID);
        message.setSubject(subject);
        message.setText(String.format(
                simpleMailMessage.getText(), name, content));

        mailSender.send(message);
         }catch(MailException me){
             log.error("Mail Exception during sending email :"+me.getMessage());
             throw new BaseException("Error message:"+me.getMessage());
         }catch(Exception e){
             log.error("Exception sending email :"+e.getMessage());
             throw new BaseException("Error message:"+e.getMessage());
         }
    }

}
