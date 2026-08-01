package model;

public class Warehouse {

    private int warehouseId;
    private String warehouseName;
    private String location;
    private int capacity;
    private String managerName;

    public Warehouse() {
    }

    public Warehouse(String warehouseName, String location, int capacity, String managerName) {
        this.warehouseName = warehouseName;
        this.location = location;
        this.capacity = capacity;
        this.managerName = managerName;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}