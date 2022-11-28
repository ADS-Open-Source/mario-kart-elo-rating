package pl.com.dolittle.mkelo.services;

public interface EmailService {

    void send(String receiver, String subject, String content);
}
