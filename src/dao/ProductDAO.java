package dao;

import config.DBConnection;
import model.Product;

import java.sql.*;

public class ProductDAO {

    // Add Product
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO product(product_name,category_id,supplier_id,unit_price,reorder_level) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, product.getProductName());
                ps.setInt(2, product.getCategoryId());
                ps.setInt(3, product.getSupplierId());
                ps.setDouble(4, product.getUnitPrice());
                ps.setInt(5, product.getReorderLevel());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to add product: " + e.getMessage());
            return false;
        }
    }

    // View All Products
    public void viewProducts() {
        String sql =
                "SELECT p.product_id, p.product_name, c.category_name, s.supplier_name, p.unit_price, p.reorder_level " +
                "FROM product p " +
                "JOIN category c ON p.category_id = c.category_id " +
                "JOIN supplier s ON p.supplier_id = s.supplier_id";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("\n============ PRODUCT LIST ============");
                System.out.printf("%-5s %-20s %-15s %-15s %-10s %-10s%n",
                        "ID", "Name", "Category", "Supplier", "Price", "Reorder");
                while (rs.next()) {
                    System.out.printf("%-5d %-20s %-15s %-15s %-10.2f %-10d%n",
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("category_name"),
                            rs.getString("supplier_name"),
                            rs.getDouble("unit_price"),
                            rs.getInt("reorder_level"));
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not load products: " + e.getMessage());
        }
    }

    // Search Product by ID
    public void searchProduct(int productId) {
        String sql =
                "SELECT p.product_id, p.product_name, c.category_name, s.supplier_name, p.unit_price, p.reorder_level " +
                "FROM product p " +
                "JOIN category c ON p.category_id = c.category_id " +
                "JOIN supplier s ON p.supplier_id = s.supplier_id " +
                "WHERE p.product_id = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nProduct Found");
                        System.out.println("------------------------");
                        System.out.println("ID            : " + rs.getInt("product_id"));
                        System.out.println("Name          : " + rs.getString("product_name"));
                        System.out.println("Category      : " + rs.getString("category_name"));
                        System.out.println("Supplier      : " + rs.getString("supplier_name"));
                        System.out.println("Unit Price    : " + rs.getDouble("unit_price"));
                        System.out.println("Reorder Level : " + rs.getInt("reorder_level"));
                    } else {
                        System.out.println("  Product Not Found.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not search product: " + e.getMessage());
        }
    }

    // Update Product
    public boolean updateProduct(int productId, String productName, double unitPrice, int reorderLevel) {
        String sql = "UPDATE product SET product_name=?, unit_price=?, reorder_level=? WHERE product_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, productName);
                ps.setDouble(2, unitPrice);
                ps.setInt(3, reorderLevel);
                ps.setInt(4, productId);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to update product: " + e.getMessage());
            return false;
        }
    }

    // Delete Product
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, productId);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to delete product: " + e.getMessage());
            return false;
        }
    }

    // Check if Product ID exists
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM product WHERE product_id=?";
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