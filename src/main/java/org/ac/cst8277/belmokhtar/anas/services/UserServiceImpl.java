package org.ac.cst8277.belmokhtar.anas.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.ac.cst8277.belmokhtar.anas.beans.Mail;
import org.ac.cst8277.belmokhtar.anas.domain.Credentials;
import org.ac.cst8277.belmokhtar.anas.model.User;
import org.ac.cst8277.belmokhtar.anas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private String secret = "eyJhbGciOiJub25lIn0.eyJzdWIiOiIyIiwianRpIjoiNDUxMDZmZTEtOTcxYi00N2VmLWFkMGQtOWY0Yzc0NmZlZTRmIiwiaWF0IjoxNjQ4MzIxNzQ0LCJleHAiOjE2NDgzMjIwNDR9.";

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService verifyEmail;

    @Autowired
    private RestTemplate restTemplate;


    public UserServiceImpl() {

    }

    public void register(User u) throws SQLException {
        //generate UUID
        u.setToken(UUID.randomUUID());
        //save to db
        //TODO: Get storeCredentials() working again
        //int result = userRepository.storeCredentials(u);
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

    public int validateUser(String token) {
        return userRepository.validateUser(token);
    }

    public int changePassword(Credentials cs) {
        return userRepository.changePassword(cs.getPassword(),cs.getUsername());
    }

    public List<String> getAllRoles() {
        return userRepository.getAllRoles();
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
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

    public String login(Credentials cs){

        User user = userRepository.getUser(cs.getUsername(),cs.getPassword());
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim("UserId", user.getUserId())
                .build();
        JWSSigner signer = null;
        try {
            signer = new MACSigner(secret);
        } catch (KeyLengthException e) {
            e.printStackTrace();
            return null;
        }

        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(claims.toJSONObject()));

        try {
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        }

        String jwt = jwsObject.serialize();

        return jwt;
    }
}
