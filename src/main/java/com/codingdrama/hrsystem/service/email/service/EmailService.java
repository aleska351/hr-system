package com.codingdrama.hrsystem.service.email.service;


import com.codingdrama.hrsystem.service.email.context.AbstractEmailContext;

/**
 * Interface for sending email messages.
 */
public interface EmailService {

    /**
     * Sends an email.
     *
     * @param email The email to send.
     */
    void sendMail(final AbstractEmailContext email);
    
}
