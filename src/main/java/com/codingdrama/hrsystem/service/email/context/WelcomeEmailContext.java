package com.codingdrama.hrsystem.service.email.context;


import com.codingdrama.hrsystem.model.User;

public class WelcomeEmailContext extends AbstractEmailContext {



    @Override
    public <T> void init(T context){
        User customer = (User) context;
        setTemplateLocation("emails/welcome-email");
        setSubject("Welcome to HR platform!");
        setFrom("alerts@bdacs.co.kr");
        setTo(customer.getEmail());
    }
}