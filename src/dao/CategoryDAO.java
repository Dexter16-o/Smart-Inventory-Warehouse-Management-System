package dao;

import config.DBConnection;
import model.Category;

import java.sql.*;

public class CategoryDAO {

    // Add Category
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO category(category_name,description) VALUES(?,?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, category.getCategoryName());
                ps.setString(2, category.getDescription());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to add category: " + e.getMessage());
            return false;
        }
    }

    // View All Categories
    public void viewCategories() {
        String sql = "SELECT * FROM category";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("\n=========== CATEGORY LIST ===========");
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("category_id") + " | "
                            + rs.getString("category_name") + " | "
                            + rs.getString("description")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not load categories: " + e.getMessage());
        }
    }

    // Update Category
    public boolean updateCategory(int id, String name, String description) {
        String sql = "UPDATE category SET category_name=?, description=? WHERE category_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setInt(3, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to update category: " + e.getMessage());
            return false;
        }
    }

    // Delete Category
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM category WHERE category_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to delete category: " + e.getMessage());
            return false;
        }
    }

    // Search Category by ID
    public void searchCategory(int id) {
        String sql = "SELECT * FROM category WHERE category_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nCategory Found");
                        System.out.println("---------------------");
                        System.out.println("ID          : " + rs.getInt("category_id"));
                        System.out.println("Name        : " + rs.getString("category_name"));
                        System.out.println("Description : " + rs.getString("description"));
                    } else {
                        System.out.println("  Category Not Found.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not search category: " + e.getMessage());
        }
    }

    // Check if Category ID exists
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM category WHERE category_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}