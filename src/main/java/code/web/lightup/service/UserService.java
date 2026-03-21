package code.web.lightup.service;

import code.web.lightup.dao.UserDAO;
import code.web.lightup.model.User;

import java.util.List;
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

    public boolean updatePasswordById(int customerId, String hashedPassword) {
        return userDAO.updatePasswordById(customerId, hashedPassword);
    }

    public boolean updateUserRole(int customerId, int newRoleId) {
        return userDAO.updateUserRole(customerId, newRoleId);
    }

    public boolean updateUserStatus(int customerId, String active) {
        return userDAO.updateUserStatus(customerId, active);
    }
    public Optional<User> getUserLoginInfo(String email){
        return userDAO.getUserLoginInfo(email);
    }

    public List<User> getAllCustomers() {
        return userDAO.getAllCustomers();
    }

    public void resetFailedLoginAttempts(String email) {
        userDAO.resetFailedAttempts(email);
    }

    public void recordFailedLoginAttempt(String email) {
        userDAO.recordFailedAttempt(email);
    }
    public boolean unlockUser(int userId) {
        return userDAO.unlockUser(userId);
    }
}