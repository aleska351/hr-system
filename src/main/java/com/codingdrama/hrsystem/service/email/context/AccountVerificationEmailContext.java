package com.codingdrama.hrsystem.service.email.context;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context){
        String email = (String) context;
        setTemplateLocation("emails/email-verification");
        setSubject("Complete your registration");
        setFrom("alonka.goncharenko1994@gmail.com");
        setTo(email);
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }
}
