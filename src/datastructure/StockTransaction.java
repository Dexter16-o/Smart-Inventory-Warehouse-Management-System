package datastructure;

public class StockTransaction {

    private int stockId;
    private int quantity;
    private String type;

    public StockTransaction(int stockId, int quantity, String type) {
        this.stockId = stockId;
        this.quantity = quantity;
        this.type = type;
    }

    public int getStockId() {
        return stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }
}