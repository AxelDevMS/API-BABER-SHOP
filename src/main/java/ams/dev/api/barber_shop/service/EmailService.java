package ams.dev.api.barber_shop.service;

public interface EmailService {

    void sendEmailCredentialsAcces(String toEmail, String nombre, String username, String password);
}
