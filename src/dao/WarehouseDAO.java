package dao;

import config.DBConnection;
import model.Warehouse;

import java.sql.*;

public class WarehouseDAO {

    // Add Warehouse
    public boolean addWarehouse(Warehouse warehouse) {
        String sql = "INSERT INTO warehouse(warehouse_name,location,capacity,manager_name) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, warehouse.getWarehouseName());
                ps.setString(2, warehouse.getLocation());
                ps.setInt(3, warehouse.getCapacity());
                ps.setString(4, warehouse.getManagerName());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to add warehouse: " + e.getMessage());
            return false;
        }
    }

    // View All Warehouses
    public void viewWarehouses() {
        String sql = "SELECT * FROM warehouse";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("\n===============================");
                System.out.println("       WAREHOUSE LIST");
                System.out.println("===============================");
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("warehouse_id") + " | " +
                            rs.getString("warehouse_name") + " | " +
                            rs.getString("location") + " | Capacity: " +
                            rs.getInt("capacity") + " | Manager: " +
                            rs.getString("manager_name")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not load warehouses: " + e.getMessage());
        }
    }

    // Update Warehouse
    public boolean updateWarehouse(int id, String name, String location,
                                   int capacity, String manager) {
        String sql = "UPDATE warehouse SET warehouse_name=?, location=?, capacity=?, manager_name=? WHERE warehouse_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, location);
                ps.setInt(3, capacity);
                ps.setString(4, manager);
                ps.setInt(5, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to update warehouse: " + e.getMessage());
            return false;
        }
    }

    // Delete Warehouse
    public boolean deleteWarehouse(int id) {
        String sql = "DELETE FROM warehouse WHERE warehouse_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to delete warehouse: " + e.getMessage());
            return false;
        }
    }

    // Search Warehouse by ID
    public void searchWarehouse(int id) {
        String sql = "SELECT * FROM warehouse WHERE warehouse_id=?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nWarehouse Found");
                        System.out.println("----------------------------");
                        System.out.println("ID       : " + rs.getInt("warehouse_id"));
                        System.out.println("Name     : " + rs.getString("warehouse_name"));
                        System.out.println("Location : " + rs.getString("location"));
                        System.out.println("Capacity : " + rs.getInt("capacity"));
                        System.out.println("Manager  : " + rs.getString("manager_name"));
                    } else {
                        System.out.println("  Warehouse Not Found.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not search warehouse: " + e.getMessage());
        }
    }

    // Check if Warehouse ID exists
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM warehouse WHERE warehouse_id=?";
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