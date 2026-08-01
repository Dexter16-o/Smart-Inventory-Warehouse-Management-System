package dao;

import config.DBConnection;
import datastructure.StockTransaction;
import datastructure.UndoStack;
import model.Stock;

import java.sql.*;

public class StockDAO {

    private UndoStack undoStack = new UndoStack();

    // Add New Stock Entry
    public boolean addStock(Stock stock) {
        String sql = "INSERT INTO stock(product_id, warehouse_id, quantity) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, stock.getProductId());
                ps.setInt(2, stock.getWarehouseId());
                ps.setInt(3, stock.getQuantity());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to add stock: " + e.getMessage());
            return false;
        }
    }

    // View All Stock
    public void viewStock() {
        String sql =
                "SELECT s.stock_id, p.product_name, w.warehouse_name, s.quantity " +
                "FROM stock s " +
                "JOIN product p ON s.product_id = p.product_id " +
                "JOIN warehouse w ON s.warehouse_id = w.warehouse_id";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return; }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("\n================ STOCK LIST ================");
                System.out.printf("%-10s %-20s %-20s %-10s%n", "Stock ID", "Product", "Warehouse", "Quantity");
                while (rs.next()) {
                    System.out.printf("%-10d %-20s %-20s %-10d%n",
                            rs.getInt("stock_id"),
                            rs.getString("product_name"),
                            rs.getString("warehouse_name"),
                            rs.getInt("quantity"));
                }
            }
        } catch (Exception e) {
            System.out.println("  [!] Could not load stock: " + e.getMessage());
        }
    }

    // Stock In — add quantity
    public boolean stockIn(int stockId, int qty) {
        String sql = "UPDATE stock SET quantity = quantity + ? WHERE stock_id = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, qty);
                ps.setInt(2, stockId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    undoStack.push(new StockTransaction(stockId, qty, "IN"));
                }
                return rows > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Stock In error: " + e.getMessage());
            return false;
        }
    }

    // Stock Out — remove quantity (only if sufficient)
    public boolean stockOut(int stockId, int qty) {
        String sql = "UPDATE stock SET quantity = quantity - ? WHERE stock_id = ? AND quantity >= ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, qty);
                ps.setInt(2, stockId);
                ps.setInt(3, qty);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    undoStack.push(new StockTransaction(stockId, qty, "OUT"));
                }
                return rows > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Stock Out error: " + e.getMessage());
            return false;
        }
    }

    // Delete Stock Entry
    public boolean deleteStock(int stockId) {
        String sql = "DELETE FROM stock WHERE stock_id = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, stockId);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Failed to delete stock: " + e.getMessage());
            return false;
        }
    }

    // Undo Last Stock Transaction
    public boolean undoLastTransaction() {
        StockTransaction t = undoStack.pop();
        if (t == null) return false;

        // Reverse: IN becomes subtract, OUT becomes add
        String sql = t.getType().equals("IN")
                ? "UPDATE stock SET quantity = quantity - ? WHERE stock_id = ?"
                : "UPDATE stock SET quantity = quantity + ? WHERE stock_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { System.out.println("  [!] Database connection failed."); return false; }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, t.getQuantity());
                ps.setInt(2, t.getStockId());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("  [!] Undo error: " + e.getMessage());
            return false;
        }
    }

    // Check if Stock ID exists
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM stock WHERE stock_id=?";
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