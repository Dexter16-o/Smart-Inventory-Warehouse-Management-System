package dao;

import config.DBConnection;
import model.User;

import java.sql.*;

public class UserDAO {

    public boolean registerUser(User user) {

        String sql = "INSERT INTO users(full_name,email,password,phone,role) VALUES(?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("  [!] Database connection failed.");
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, user.getFullName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getPhone());
                ps.setString(5, user.getRole());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("  [!] Registration error: " + e.getMessage());
            return false;
        }
    }

    public User loginUser(String email, String password) {

        String sql = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("  [!] Database connection failed.");
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setPhone(rs.getString("phone"));
                        user.setRole(rs.getString("role"));
                        return user;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("  [!] Login error: " + e.getMessage());
        }

        return null;
    }

}