package com.algonquin.profile.services;

import com.algonquin.profile.beans.Mail;
import com.algonquin.profile.dao.UserDao;
import com.algonquin.profile.model.Credentials;
import com.algonquin.profile.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    HttpServletRequest r;
    @Autowired
    EmailService verifyEmail;

    public UserService() {

    }

    public void register(User u) throws SQLException {
        //generate UUID
        u.setToken(UUID.randomUUID());
        //save to db
        int result = userDao.storeCredentials(u);
        //send email
        String textBody = "Thank you for signing up with us! click on this link to verify your email: " + r.getRequestURL() + "/" + u.getToken();
        String subject = "Email Verification";

        Mail email = new Mail();

        email.setMailTo(u.getEmail());
        email.setMailFrom("anasbelmokhtar@gmail.com");
        email.setMailSubject(subject);
        email.setMailContent(textBody);

        verifyEmail.sendEmail(email);
        //System.out.println(result);
    }
    public User login(Credentials cs) throws SQLException {
        return userDao.login(cs);
    }

    public int validateUser(String token) {
        return userDao.validateUser(token);
    }
}
