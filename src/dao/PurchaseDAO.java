package dao;

import config.DBConnection;
import datastructure.PurchaseQueue;
import model.PurchaseOrder;

import java.sql.*;

public class PurchaseDAO {

    private PurchaseQueue queue = new PurchaseQueue();

    // Add Purchase Order
    public boolean addPurchaseOrder(PurchaseOrder order) {

        String sql = "INSERT INTO purchase_order(product_id, warehouse_id, quantity) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("  [!] Database connection failed.");
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, order.getProductId());
                ps.setInt(2, order.getWarehouseId());
                ps.setInt(3, order.getQuantity());

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int orderId = rs.getInt(1);
                            queue.enqueue(new PurchaseOrder(
                                    orderId,
                                    order.getProductId(),
                                    order.getWarehouseId(),
                                    order.getQuantity()
                            ));
                        }
                    }
                }

                return rows > 0;
            }

        } catch (Exception e) {
            System.out.println("  [!] Failed to add purchase order: " + e.getMessage());
            return false;
        }
    }

    // View Purchase Orders
    public void viewPurchaseOrders() {

        String sql =
                "SELECT po.order_id, p.product_name, w.warehouse_name, po.quantity, po.status " +
                        "FROM purchase_order po " +
                        "JOIN product p ON po.product_id = p.product_id " +
                        "JOIN warehouse w ON po.warehouse_id = w.warehouse_id";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("  [!] Database connection failed.");
                return;
            }

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                System.out.println("\n============= PURCHASE ORDERS =============");

                while (rs.next()) {
                    System.out.println(
                            rs.getInt("order_id") + " | " +
                                    rs.getString("product_name") + " | " +
                                    rs.getString("warehouse_name") + " | Qty : " +
                                    rs.getInt("quantity") + " | " +
                                    rs.getString("status")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("  [!] Could not load purchase orders: " + e.getMessage());
        }
    }

    // Process Next Order (FIFO) — fully transactional
    public boolean processNextOrder() {

        PurchaseOrder order = queue.dequeue();

        if (order == null) {
            System.out.println("No Purchase Orders in Queue.");
            return false;
        }

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("  [!] Database connection failed.");
                return false;
            }

            con.setAutoCommit(false);

            try {

                // Step 1: Mark purchase order as COMPLETED
                try (PreparedStatement ps1 = con.prepareStatement(
                        "UPDATE purchase_order SET status='COMPLETED' WHERE order_id=?")) {
                    ps1.setInt(1, order.getOrderId());
                    ps1.executeUpdate();
                }

                // Step 2: Check if a stock entry already exists for this product + warehouse
                try (PreparedStatement check = con.prepareStatement(
                        "SELECT stock_id FROM stock WHERE product_id=? AND warehouse_id=?")) {

                    check.setInt(1, order.getProductId());
                    check.setInt(2, order.getWarehouseId());

                    try (ResultSet rs = check.executeQuery()) {

                        if (rs.next()) {
                            // Existing stock — just add quantity
                            try (PreparedStatement ps2 = con.prepareStatement(
                                    "UPDATE stock SET quantity = quantity + ? WHERE stock_id=?")) {
                                ps2.setInt(1, order.getQuantity());
                                ps2.setInt(2, rs.getInt("stock_id"));
                                ps2.executeUpdate();
                            }
                        } else {
                            // No stock entry yet — create one
                            try (PreparedStatement ps3 = con.prepareStatement(
                                    "INSERT INTO stock(product_id, warehouse_id, quantity) VALUES(?,?,?)")) {
                                ps3.setInt(1, order.getProductId());
                                ps3.setInt(2, order.getWarehouseId());
                                ps3.setInt(3, order.getQuantity());
                                ps3.executeUpdate();
                            }
                        }
                    }
                }

                con.commit();
                return true;

            } catch (Exception inner) {
                con.rollback();
                System.out.println("  [!] Error processing purchase order (rolled back): " + inner.getMessage());
                return false;
            }

        } catch (Exception e) {
            System.out.println("  [!] Error processing purchase order: " + e.getMessage());
            return false;
        }
    }
}