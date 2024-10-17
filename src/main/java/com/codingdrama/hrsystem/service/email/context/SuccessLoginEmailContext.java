package com.codingdrama.hrsystem.service.email.context;


import com.codingdrama.hrsystem.model.User;

import java.util.Date;

public class SuccessLoginEmailContext extends AbstractEmailContext {
    private String ip;
    private Date date;

    @Override
    public <T> void init(T context){
        User customer = (User) context;
        setTemplateLocation("emails/login-success");
        setSubject("BDACS Account Login Notification");
        setTo(customer.getEmail());
        setFrom("alerts@bdacs.co.kr");
        setTo(customer.getEmail());
    }

    public void setIp(String ip) {
        this.ip = ip;
        put("ip", ip);
    }

    public void setDate(Date date) {
        this.date = date;
        put("date", date);
    }
}
