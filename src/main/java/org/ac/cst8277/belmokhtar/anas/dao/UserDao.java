package org.ac.cst8277.belmokhtar.anas.dao;

import org.ac.cst8277.belmokhtar.anas.model.Credentials;
import org.ac.cst8277.belmokhtar.anas.model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository("mysql")
public class UserDao {

    private String secret = "eyJhbGciOiJub25lIn0.eyJzdWIiOiIyIiwianRpIjoiNDUxMDZmZTEtOTcxYi00N2VmLWFkMGQtOWY0Yzc0NmZlZTRmIiwiaWF0IjoxNjQ4MzIxNzQ0LCJleHAiOjE2NDgzMjIwNDR9.";
    private JdbcTemplate jdbcTemplate;

    public int validateUser(String token) {
        String sql = "UPDATE TwitterLikeApp.Users SET UserStatus = 'active' WHERE TOKEN = ?";
        int rowsAffected = jdbcTemplate.update(sql,new Object[]{token});
        return rowsAffected;
    }

    public int changePassword(Credentials cs) {
        String sql = "UPDATE TwitterLikeApp.Users SET UserPassword = ? WHERE Username = ?";
        int rowsAffected = jdbcTemplate.update(sql,new Object[]{cs.getPassword(), cs.getUsername()});
        return rowsAffected;
    }

    public List<String> getAllRoles() {
        String sql = "SELECT RoleName FROM TwitterLikeApp.Roles";
        List<String> users = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString(1));
        return users;
    }

    private static class UserRowMapper implements RowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getString("UserId"));
            user.setEmail(rs.getString("Email"));
            user.setUsername(rs.getString("Username"));
            user.setLastName(rs.getString("LastName"));
            user.setFirstName(rs.getString("FirstName"));
            user.setPhone(rs.getString("Phone"));
            return user;
        }
    }

    @Autowired
    public UserDao(JdbcTemplate template){
        this.jdbcTemplate = template;
    }
    public List<User> getAllUsers() {
        String sql = "SELECT UserId, Username, Email, FirstName, LastName, Phone FROM TwitterLikeApp.Users";
        List<User> users = jdbcTemplate.query(sql,new UserRowMapper());
        return users;

    }
    public int storeCredentials(User u) throws SQLException, SQLException {
            String insert = "INSERT INTO TwitterLikeApp.Users (Username, UserPassword, LastName, FirstName, Email, Phone, Token, UserStatus) VALUES (?,?,?,?,?,?,?,?)";
            int rowsAffected = jdbcTemplate.update(insert, new Object[]{u.getUsername(),u.getPassword(),u.getLastName(),u.getFirstName(),u.getEmail(),u.getPhone(),u.getToken().toString(),"pending"});
            return rowsAffected;
    }
    public String login(Credentials cs) throws SQLException {

        String sql = "SELECT UserId, Username, Email, FirstName, LastName, Phone FROM TwitterLikeApp.Users WHERE Username = ? AND UserPassword = ? AND UserStatus = 'active'";
        User user = jdbcTemplate.queryForObject(sql,new UserRowMapper(), cs.getUsername(), cs.getPassword());

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
