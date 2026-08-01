package dao;

import config.DBConnection;

import java.sql.*;


public class ReportDAO {

    // ==============================
    // 1. Current Stock Report
    // ==============================
    public void currentStockReport() {

        String sql =
                "SELECT p.product_name, w.warehouse_name, s.quantity " +
                        "FROM stock s " +
                        "JOIN product p ON s.product_id = p.product_id " +
                        "JOIN warehouse w ON s.warehouse_id = w.warehouse_id " +
                        "ORDER BY p.product_name";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) { System.out.println("  [!] Database connection failed."); return; }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n========== CURRENT STOCK REPORT ==========");
                System.out.printf("%-20s %-20s %-10s%n", "Product", "Warehouse", "Quantity");

                while (rs.next()) {
                    System.out.printf("%-20s %-20s %-10d%n",
                            rs.getString("product_name"),
                            rs.getString("warehouse_name"),
                            rs.getInt("quantity"));
                }
            }

        } catch (Exception e) {
            System.out.println("  [!] Could not generate current stock report: " + e.getMessage());
        }
    }

    // ==============================
    // 2. Low Stock Report
    // ==============================
    public void lowStockReport() {

        String sql =
                "SELECT p.product_name, s.quantity, p.reorder_level " +
                        "FROM stock s " +
                        "JOIN product p ON s.product_id = p.product_id " +
                        "WHERE s.quantity <= p.reorder_level";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) { System.out.println("  [!] Database connection failed."); return; }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n========== LOW STOCK REPORT ==========");
                System.out.printf("%-20s %-10s %-15s%n", "Product", "Stock", "Reorder Level");

                while (rs.next()) {
                    System.out.printf("%-20s %-10d %-15d%n",
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getInt("reorder_level"));
                }
            }

        } catch (Exception e) {
            System.out.println("  [!] Could not generate low stock report: " + e.getMessage());
        }
    }

    // ==============================
    // 3. Warehouse Stock Report
    // ==============================
    public void warehouseStockReport() {

        String sql =
                "SELECT w.warehouse_name, COUNT(s.stock_id) AS TotalProducts, " +
                        "SUM(s.quantity) AS TotalQuantity " +
                        "FROM warehouse w " +
                        "LEFT JOIN stock s ON w.warehouse_id = s.warehouse_id " +
                        "GROUP BY w.warehouse_name";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) { System.out.println("  [!] Database connection failed."); return; }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n========== WAREHOUSE REPORT ==========");
                System.out.printf("%-20s %-15s %-15s%n", "Warehouse", "Products", "Total Qty");

                while (rs.next()) {
                    System.out.printf("%-20s %-15d %-15d%n",
                            rs.getString("warehouse_name"),
                            rs.getInt("TotalProducts"),
                            rs.getInt("TotalQuantity"));
                }
            }

        } catch (Exception e) {
            System.out.println("  [!] Could not generate warehouse report: " + e.getMessage());
        }
    }

    // ==============================
    // 4. Category Report
    // ==============================
    public void categoryProductReport() {

        String sql =
                "SELECT c.category_name, COUNT(p.product_id) AS Products " +
                        "FROM category c " +
                        "LEFT JOIN product p ON c.category_id = p.category_id " +
                        "GROUP BY c.category_name";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) { System.out.println("  [!] Database connection failed."); return; }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n========== CATEGORY REPORT ==========");
                System.out.printf("%-20s %-15s%n", "Category", "Products");

                while (rs.next()) {
                    System.out.printf("%-20s %-15d%n",
                            rs.getString("category_name"),
                            rs.getInt("Products"));
                }
            }

        } catch (Exception e) {
            System.out.println("  [!] Could not generate category report: " + e.getMessage());
        }
    }

    // ==============================
    // 5. Supplier Report
    // ==============================
    public void supplierProductReport() {

        String sql =
                "SELECT s.supplier_name, COUNT(p.product_id) AS Products " +
                        "FROM supplier s " +
                        "LEFT JOIN product p ON s.supplier_id = p.supplier_id " +
                        "GROUP BY s.supplier_name";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) { System.out.println("  [!] Database connection failed."); return; }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n========== SUPPLIER REPORT ==========");
                System.out.printf("%-20s %-15s%n", "Supplier", "Products");

                while (rs.next()) {
                    System.out.printf("%-20s %-15d%n",
                            rs.getString("supplier_name"),
                            rs.getInt("Products"));
                }
            }

        } catch (Exception e) {
            System.out.println("  [!] Could not generate supplier report: " + e.getMessage());
        }
    }

    // ==============================
    // 6. Pending Purchase Orders
    // ==============================
    public void pendingOrdersReport() {

        String sql =
                "SELECT po.order_id, p.product_name, po.quantity, po.status " +
                        "FROM purchase_order po " +
                        "JOIN product p ON po.product_id = p.product_id " +
                        "WHERE po.status='PENDING'";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) { System.out.println("  [!] Database connection failed."); return; }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n========== PENDING PURCHASE ORDERS ==========");
                System.out.printf("%-10s %-20s %-10s %-15s%n", "Order ID", "Product", "Qty", "Status");

                while (rs.next()) {
                    System.out.printf("%-10d %-20s %-10d %-15s%n",
                            rs.getInt("order_id"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getString("status"));
                }
            }

        } catch (Exception e) {
            System.out.println("  [!] Could not generate pending orders report: " + e.getMessage());
        }
    }
}