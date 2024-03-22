package com.codingdrama.hrsystem.service.email.context;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context){
        String email = (String) context;
        setTemplateLocation("emails/email-verification");
        setSubject("Complete your registration");
        setFrom("alerts@bdacs.co.kr");
        setTo(email);
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }
}
