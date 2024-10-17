package com.codingdrama.hrsystem.service.email.context;


import com.codingdrama.hrsystem.model.User;

public class PasswordResetEmailVerificationContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context){
        User user = (User) context;
        setTemplateLocation("emails/password-reset-email-verification");
        setSubject("Account Email Verification");
        setFrom("alonka.goncharenko1994@gmail.com");
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }
}
