package userapp.brian.duran.userappapi.service;

public interface EmailService {

    void sendWelcomeEmail(String to, String subject, String body);
}
