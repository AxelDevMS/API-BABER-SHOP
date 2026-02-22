package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.MailDataDto;

public interface EmailService {

    void sendEmailCredentialsAcces(String toEmail, String nombre, String username, String password);
}
