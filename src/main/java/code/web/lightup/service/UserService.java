package code.web.lightup.service;

import code.web.lightup.dao.UserDAO;
import code.web.lightup.model.User;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean register(User user) {
        return userDAO.register(user);
    }

    public Optional<User> login(String email, String password) {
        return userDAO.login(email, password);
    }

    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }

    public Optional<User> getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public boolean updatePassword(String email, String newPassword) {
        return userDAO.updatePassword(email, newPassword);
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean updateAvatar(int userId, String avatarUrl) {
        return userDAO.updateAvatar(userId, avatarUrl);
    }

    public boolean checkEmailExists(String email) {
        return userDAO.checkEmailExists(email);
    }

    public boolean registerGoogleUser(User user) {
        return userDAO.registerGoogleUser(user);
    }

    public int getTotalCustomerCount() {
        return userDAO.getTotalCustomerCount();
    }





}