package model;

public class PurchaseOrder {

    private int orderId;
    private int productId;
    private int warehouseId;
    private int quantity;



    /** Used when creating a new order before DB insert — orderId not yet known */
    public PurchaseOrder(int productId, int warehouseId, int quantity) {
        this.orderId = 0;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }

    /** Used internally after DB insert with the generated orderId */
    public PurchaseOrder(int orderId, int productId, int warehouseId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;

    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public int getQuantity() {
        return quantity;
    }

}