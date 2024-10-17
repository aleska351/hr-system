package com.codingdrama.hrsystem.service.email.context;


import com.codingdrama.hrsystem.model.User;

public class PasswordResetEmailVerificationContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context){
        User user = (User) context;
        setTemplateLocation("emails/password-reset-email-verification");
        setSubject("BDACS Account Email Verification");
        setFrom("alerts@bdacs.co.kr");
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }
}
