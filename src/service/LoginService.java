package service;

import dao.UserDAO;
import model.User;

public class LoginService {

    UserDAO dao = new UserDAO();

    public User login(String email, String password) {

        return dao.loginUser(email, password);

    }

}