package org.ac.cst8277.belmokhtar.anas.repository;

import org.ac.cst8277.belmokhtar.anas.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mysql")
public interface UserRepository {

    @Modifying
    @Query("UPDATE User u SET u.userStatus =: active WHERE u.token =: token")
    int validateUser(@Param("token")String token);

    @Modifying
    @Query("UPDATE User u SET u.password =: password WHERE u.username =: username")
    int changePassword(@Param("password")String password, @Param("username")String username);

    @Query("SELECT r.roleName as string FROM Roles r")
    List<String> getAllRoles();

    @Query("SELECT u.UserId, u.username, u.email, u.firstName, u.lastName, u.phone FROM User u")
    List<User> getAllUsers();

    /*@Modifying
    @Query("INSERT INTO User (Username, UserPassword, LastName, FirstName, Email, Phone, Token, UserStatus) VALUES (:username,:password,:lastName,:firstName,:email,phone,:token,:userStatus)")
    @Transactional
    int storeCredentials(@Param("password")String password, @Param("username")String username);

    public int storeCredentials(User u) throws SQLException, SQLException {
        String insert = "INSERT INTO TwitterLikeApp.Users (Username, UserPassword, LastName, FirstName, Email, Phone, Token, UserStatus) VALUES (?,?,?,?,?,?,?,?)";
        int rowsAffected = jdbcTemplate.update(insert, new Object[]{u.getUsername(), u.getPassword(), u.getLastName(), u.getFirstName(), u.getEmail(), u.getPhone(), u.getToken().toString(), "pending"});
        return rowsAffected;
    }*/

    @Query("SELECT u.UserId, u.username, u.email, u.firstName, u.lastName, u.phone FROM User u WHERE u.username =: username AND u.password =: password AND u.userStatus = 'active'")
    User getUser(@Param("username")String username, @Param("password") String password);

}
