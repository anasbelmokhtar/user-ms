package org.ac.cst8277.belmokhtar.anas.services;

import org.ac.cst8277.belmokhtar.anas.beans.Mail;
import org.ac.cst8277.belmokhtar.anas.dao.UserDao;
import org.ac.cst8277.belmokhtar.anas.model.Credentials;
import org.ac.cst8277.belmokhtar.anas.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    EmailService verifyEmail;

    @Autowired
    private RestTemplate restTemplate;

    public UserService() {

    }

    public void register(User u) throws SQLException {
        //generate UUID
        u.setToken(UUID.randomUUID());
        //save to db
        int result = userDao.storeCredentials(u);
        //send email
        String textBody = "Thank you for signing up with us! click on this link to verify your email: " + "/" + u.getToken();
        String subject = "Email Verification";

        Mail email = new Mail();

        email.setMailTo(u.getEmail());
        email.setMailFrom("anasbelmokhtar@gmail.com");
        email.setMailSubject(subject);
        email.setMailContent(textBody);

        verifyEmail.sendEmail(email);
        //System.out.println(result);
    }
    public String login(Credentials cs) throws SQLException {
        return userDao.login(cs);
    }

    public int validateUser(String token) {
        return userDao.validateUser(token);
    }

    public int changePassword(Credentials cs) {
        return userDao.changePassword(cs);
    }

    public List<String> getAllRoles() {
        return userDao.getAllRoles();
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public String loginOauth() {
        String url = "https://github.com/login/oauth/authorize";
        HttpEntity<?> httpEntity = null;
        Class<String> clazz = null;
        HttpEntity<String> response = restTemplate.exchange("https://github.com/login/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}",
                HttpMethod.GET,
                null,
                clazz,
                "b747106f9edf73905c76",
                "http://localhost:8080/twitter-like-app/login/oauth"
        );
        return url;
    }
}
