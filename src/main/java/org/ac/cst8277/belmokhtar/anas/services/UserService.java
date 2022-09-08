package org.ac.cst8277.belmokhtar.anas.services;

import org.ac.cst8277.belmokhtar.anas.model.Credentials;
import org.ac.cst8277.belmokhtar.anas.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    void register(User u) throws SQLException;

    String login(Credentials cs) throws SQLException;

    int validateUser(String token);

    int changePassword(Credentials cs);

    List<String> getAllRoles();

    List<User> getAllUsers();
}
