package model;

public class Product {

    private int productId;
    private String productName;
    private int categoryId;
    private int supplierId;
    private double unitPrice;
    private int reorderLevel;

    public Product() {
    }

    public Product(String productName, int categoryId, int supplierId, double unitPrice, int reorderLevel) {
        this.productName = productName;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.unitPrice = unitPrice;
        this.reorderLevel = reorderLevel;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
}