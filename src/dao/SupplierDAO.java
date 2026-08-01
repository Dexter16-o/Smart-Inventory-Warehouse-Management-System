package dao;

import config.DBConnection;
import model.Supplier;

import java.sql.*;

public class SupplierDAO {

    // Add Supplier
    public boolean addSupplier(Supplier supplier) {
        String sql = "INSERT INTO supplier(supplier_name,company_name,phone,email,address) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, supplier.getSupplierName());
                ps.setString(2, supplier.getCompanyName());
                ps.setString(3, supplier.getPhone());
                ps.setString(4, supplier.getEmail());
                ps.setString(5, supplier.getAddress());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to add supplier: " + e.getMessage());
            return false;
        }
    }

    // View All Suppliers
    public void viewSuppliers() {
        String sql = "SELECT * FROM supplier";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("\n==============================================");
                System.out.println("              SUPPLIER LIST");
                System.out.println("==============================================");
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("supplier_id") + " | " +
                            rs.getString("supplier_name") + " | " +
                            rs.getString("company_name") + " | " +
                            rs.getString("phone") + " | " +
                            rs.getString("email") + " | " +
                            rs.getString("address")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not load suppliers: " + e.getMessage());
        }
    }

    // Search Supplier by ID
    public void searchSupplier(int supplierId) {
        String sql = "SELECT * FROM supplier WHERE supplier_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, supplierId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nSupplier Found");
                        System.out.println("---------------------------");
                        System.out.println("Supplier ID   : " + rs.getInt("supplier_id"));
                        System.out.println("Supplier Name : " + rs.getString("supplier_name"));
                        System.out.println("Company Name  : " + rs.getString("company_name"));
                        System.out.println("Phone         : " + rs.getString("phone"));
                        System.out.println("Email         : " + rs.getString("email"));
                        System.out.println("Address       : " + rs.getString("address"));
                    } else {
                        System.out.println("  Supplier Not Found.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not search supplier: " + e.getMessage());
        }
    }

    // Update Supplier
    public boolean updateSupplier(int supplierId, String supplierName,
                                  String companyName, String phone,
                                  String email, String address) {
        String sql = "UPDATE supplier SET supplier_name=?, company_name=?, phone=?, email=?, address=? WHERE supplier_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, supplierName);
                ps.setString(2, companyName);
                ps.setString(3, phone);
                ps.setString(4, email);
                ps.setString(5, address);
                ps.setInt(6, supplierId);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to update supplier: " + e.getMessage());
            return false;
        }
    }

    // Delete Supplier
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM supplier WHERE supplier_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, supplierId);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to delete supplier: " + e.getMessage());
            return false;
        }
    }

    // Check if Supplier ID exists
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM supplier WHERE supplier_id=?";
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