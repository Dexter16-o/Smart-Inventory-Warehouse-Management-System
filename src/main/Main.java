package main;

import dao.*;
import model.*;
import service.LoginService;
import utils.InputValidator;

import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    // ─────────────────────────────────────────────────────────────
    //  DAO instances shared for ID existence checks across menus
    // ─────────────────────────────────────────────────────────────
    static CategoryDAO  categoryDAO  = new CategoryDAO();
    static SupplierDAO  supplierDAO  = new SupplierDAO();
    static WarehouseDAO warehouseDAO = new WarehouseDAO();
    static ProductDAO   productDAO   = new ProductDAO();
    static StockDAO     stockDAO     = new StockDAO();
    static PurchaseDAO  purchaseDAO  = new PurchaseDAO();

    // =================================================================
    //  VALIDATED INPUT HELPERS  — loop until user gives valid input
    // =================================================================

    /** Person name : letters, spaces, dots, apostrophes — 2-60 chars */
    static String readPersonName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (InputValidator.isValidPersonName(input)) return input;
            System.out.println("  [!] Name must contain letters only (2-60 chars). No digits or symbols.");
        }
    }

    /** General name : min 2 chars (products, categories, warehouses, companies) */
    static String readGeneralName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (InputValidator.isValidGeneralName(input)) return input;
            System.out.println("    [!] Invalid name. It must contain at least one alphabet and may contain letters, numbers, spaces or hyphens.");
        }
    }

    /** Not blank : address, location, description etc. */
    static String readNotBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (InputValidator.isNotBlank(input)) return input;
            System.out.println("  [!] This field cannot be empty.");
        }
    }

    /** Phone : exactly 10 digits */
    static String readPhone(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (InputValidator.isValidPhone(input)) return input;
            System.out.println("  [!] Phone must be exactly 10 digits. Example: 9876543210");
        }
    }

    /** Email : standard format */
    static String readEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (InputValidator.isValidEmail(input)) return input;
            System.out.println("  [!] Invalid email. Use format: user@example.com");
        }
    }

    /** Registration password : min 6 chars */
    static String readPassword(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (InputValidator.isValidPassword(input)) return input;
            System.out.println("  [!] Password must be at least 6 characters.");
        }
    }

    /** Login password : just not empty */
    static String readLoginPassword() {
        while (true) {
            System.out.print("Password : ");
            String input = sc.nextLine().trim();
            if (InputValidator.isNotBlank(input)) return input;
            System.out.println("  [!] Password cannot be empty.");
        }
    }

    /** Role : ADMIN / MANAGER / STAFF */
    static String readRole() {
        while (true) {
            System.out.print("Role (ADMIN / MANAGER / STAFF) : ");
            String input = sc.nextLine().trim().toUpperCase();
            if (InputValidator.isValidRole(input)) return input;
            System.out.println("  [!] Invalid role. Choose: ADMIN, MANAGER, or STAFF");
        }
    }

    /** Menu choice — no crash on "abc" */
    static int readMenuChoice() {
        while (true) {
            try {
                String input = sc.nextLine().trim();
                if (!input.isEmpty()) return Integer.parseInt(input);
                System.out.print("  [!] Enter a number : ");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input.");
                System.out.print("Enter a choice : ");
            }
        }
    }

    /** Positive int (> 0) : IDs, quantities, capacity */
    static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v > 0) return v;
                System.out.println("  [!] Must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Enter a whole positive number (e.g. 5).");
            }
        }
    }

    /** Non-negative int (>= 0) : reorder level */
    static int readNonNegativeInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= 0) return v;
                System.out.println("  [!] Value cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Enter a whole number (e.g. 10).");
            }
        }
    }

    /** Positive double (> 0) : unit price */
    static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v > 0) return v;
                System.out.println("  [!] Price must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Enter a valid number (e.g. 299.99).");
            }
        }
    }

    // =================================================================
    //  MAIN
    // =================================================================

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("  SMART INVENTORY MANAGEMENT SYSTEM");
            System.out.println("======================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter Choice : ");

            switch (readMenuChoice()) {
                case 1: register(); break;
                case 2: login();    break;
                case 3:
                    System.out.println("Thank You. Goodbye!");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("  [!] Invalid Choice. Enter 1, 2, or 3.");
            }
        }
    }

    // =================================================================
    //  REGISTER
    // =================================================================

    static void register() {

        System.out.println("\n--- REGISTER NEW USER ---");

        UserDAO dao = new UserDAO();

        String name     = readPersonName("Full Name : ");
        String email    = readEmail("Email : ");
        String password = readPassword("Password (min 6 chars) : ");
        String phone    = readPhone("Phone (10 digits) : ");
        String role     = readRole();

        User user = new User(name, email, password, phone, role);

        if (dao.registerUser(user)) {
            System.out.println("  [OK] Registration Successful.");
        } else {
            System.out.println("  [FAIL] Registration Failed. Email may already be in use.");
        }
    }

    // =================================================================
    //  LOGIN
    // =================================================================

    static void login() {

        System.out.println("\n--- LOGIN ---");

        LoginService service = new LoginService();

        String email    = readEmail("Email : ");
        String password = readLoginPassword();

        User user = service.login(email, password);

        if (user == null) {
            System.out.println("  [!] Invalid Email or Password.");
            return;
        }

        System.out.println("\n  Welcome, " + user.getFullName() + "! (Role: " + user.getRole() + ")");

        switch (user.getRole()) {
            case "ADMIN":   adminMenu();   break;
            case "MANAGER": managerMenu(); break;
            case "STAFF":   staffMenu();   break;
            default:
                System.out.println("  [!] Unknown role. Access denied.");
        }
    }

    // =================================================================
    //  ADMIN MENU
    // =================================================================

    static void adminMenu() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("         ADMIN DASHBOARD");
            System.out.println("====================================");
            System.out.println("1. Warehouse Management");
            System.out.println("2. Category Management");
            System.out.println("3. Supplier Management");
            System.out.println("4. Product Management");
            System.out.println("5. Stock Management");
            System.out.println("6. Purchase Order");
            System.out.println("7. Reports");
            System.out.println("8. Logout");
            System.out.println("====================================");
            System.out.print("Enter Choice : ");

            switch (readMenuChoice()) {
                case 1: warehouseMenu(); break;
                case 2: categoryMenu();  break;
                case 3: supplierMenu();  break;
                case 4: productMenu();   break;
                case 5: stockMenu();     break;
                case 6: purchaseMenu();  break;
                case 7: reportMenu();    break;
                case 8:
                    System.out.println("Logging Out...");
                    return;
                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-8.");
            }
        }
    }

    // =================================================================
    //  WAREHOUSE MENU
    // =================================================================

    static void warehouseMenu() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("       WAREHOUSE MANAGEMENT");
            System.out.println("====================================");
            System.out.println("1. Add Warehouse");
            System.out.println("2. View Warehouses");
            System.out.println("3. Search Warehouse");
            System.out.println("4. Update Warehouse");
            System.out.println("5. Delete Warehouse");
            System.out.println("6. Back");
            System.out.println("====================================");
            System.out.print("Enter Choice : ");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    // No FK dependency — ask all fields
                    String wName    = readGeneralName("Warehouse Name : ");
                    String location = readNotBlank("Location : ");
                    int capacity    = readPositiveInt("Capacity (units) : ");
                    String manager  = readPersonName("Manager Name : ");

                    if (warehouseDAO.addWarehouse(new Warehouse(wName, location, capacity, manager))) {
                        System.out.println("  [OK] Warehouse Added Successfully.");
                    } else {
                        System.out.println("  [FAIL] Failed to Add Warehouse.");
                    }
                    break;

                case 2:
                    warehouseDAO.viewWarehouses();
                    break;

                case 3:
                    int sId = readPositiveInt("Warehouse ID : ");
                    warehouseDAO.searchWarehouse(sId);
                    break;

                case 4:
                    // Validate ID FIRST → then ask new details
                    int uId = readPositiveInt("Warehouse ID to Update : ");
                    if (!warehouseDAO.exists(uId)) {
                        System.out.println("  [FAIL] Warehouse ID " + uId + " does not exist.");
                        break;
                    }
                    String newWName   = readGeneralName("New Warehouse Name : ");
                    String newLoc     = readNotBlank("New Location : ");
                    int newCap        = readPositiveInt("New Capacity (units) : ");
                    String newManager = readPersonName("New Manager Name : ");

                    if (warehouseDAO.updateWarehouse(uId, newWName, newLoc, newCap, newManager)) {
                        System.out.println("  [OK] Warehouse Updated Successfully.");
                    } else {
                        System.out.println("  [FAIL] Warehouse Update Failed.");
                    }
                    break;

                case 5:
                    // Validate ID FIRST → then confirm delete
                    int dId = readPositiveInt("Warehouse ID to Delete : ");
                    if (!warehouseDAO.exists(dId)) {
                        System.out.println("  [FAIL] Warehouse ID " + dId + " does not exist.");
                        break;
                    }
                    System.out.print("  Confirm delete Warehouse ID " + dId + "? (yes/no) : ");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        if (warehouseDAO.deleteWarehouse(dId)) {
                            System.out.println("  [OK] Warehouse Deleted Successfully.");
                        } else {
                            System.out.println("  [FAIL] Delete Failed. May have linked stock records.");
                        }
                    } else {
                        System.out.println("  Delete cancelled.");
                    }
                    break;

                case 6:
                    return;

                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-6.");
            }
        }
    }

    // =================================================================
    //  CATEGORY MENU
    // =================================================================

    static void categoryMenu() {

        while (true) {

            System.out.println("\n========== CATEGORY MENU ==========");
            System.out.println("1. Add Category");
            System.out.println("2. View Categories");
            System.out.println("3. Search Category");
            System.out.println("4. Update Category");
            System.out.println("5. Delete Category");
            System.out.println("6. Back");
            System.out.print("Enter Choice : ");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    String cName = readGeneralName("Category Name : ");
                    String cDesc = readNotBlank("Description : ");
                    if (categoryDAO.addCategory(new Category(cName, cDesc))) {
                        System.out.println("  [OK] Category Added Successfully.");
                    } else {
                        System.out.println("  [FAIL] Failed. Category name may already exist.");
                    }
                    break;

                case 2:
                    categoryDAO.viewCategories();
                    break;

                case 3:
                    int csId = readPositiveInt("Category ID : ");
                    categoryDAO.searchCategory(csId);
                    break;

                case 4:
                    // Validate ID FIRST
                    int cuId = readPositiveInt("Category ID to Update : ");
                    if (!categoryDAO.exists(cuId)) {
                        System.out.println("  [FAIL] Category ID " + cuId + " does not exist.");
                        break;
                    }
                    String newCName = readGeneralName("New Category Name : ");
                    String newCDesc = readNotBlank("New Description : ");
                    if (categoryDAO.updateCategory(cuId, newCName, newCDesc)) {
                        System.out.println("  [OK] Category Updated Successfully.");
                    } else {
                        System.out.println("  [FAIL] Update Failed.");
                    }
                    break;

                case 5:
                    // Validate ID FIRST
                    int cdId = readPositiveInt("Category ID to Delete : ");
                    if (!categoryDAO.exists(cdId)) {
                        System.out.println("  [FAIL] Category ID " + cdId + " does not exist.");
                        break;
                    }
                    System.out.print("  Confirm delete Category ID " + cdId + "? (yes/no) : ");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        if (categoryDAO.deleteCategory(cdId)) {
                            System.out.println("  [OK] Category Deleted Successfully.");
                        } else {
                            System.out.println("  [FAIL] Delete Failed. May have linked products.");
                        }
                    } else {
                        System.out.println("  Delete cancelled.");
                    }
                    break;

                case 6:
                    return;

                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-6.");
            }
        }
    }

    // =================================================================
    //  SUPPLIER MENU
    // =================================================================

    static void supplierMenu() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("        SUPPLIER MANAGEMENT");
            System.out.println("====================================");
            System.out.println("1. Add Supplier");
            System.out.println("2. View Suppliers");
            System.out.println("3. Search Supplier");
            System.out.println("4. Update Supplier");
            System.out.println("5. Delete Supplier");
            System.out.println("6. Back");
            System.out.println("====================================");
            System.out.print("Enter Choice : ");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    String sName    = readPersonName("Supplier Name : ");
                    String sCompany = readGeneralName("Company Name : ");
                    String sPhone   = readPhone("Phone (10 digits) : ");
                    String sEmail   = readEmail("Email : ");
                    String sAddress = readNotBlank("Address : ");
                    if (supplierDAO.addSupplier(new Supplier(sName, sCompany, sPhone, sEmail, sAddress))) {
                        System.out.println("  [OK] Supplier Added Successfully.");
                    } else {
                        System.out.println("  [FAIL] Failed to Add Supplier.");
                    }
                    break;

                case 2:
                    supplierDAO.viewSuppliers();
                    break;

                case 3:
                    int ssId = readPositiveInt("Supplier ID : ");
                    supplierDAO.searchSupplier(ssId);
                    break;

                case 4:
                    // Validate ID FIRST
                    int suId = readPositiveInt("Supplier ID to Update : ");
                    if (!supplierDAO.exists(suId)) {
                        System.out.println("  [FAIL] Supplier ID " + suId + " does not exist.");
                        break;
                    }
                    String newSName  = readPersonName("New Supplier Name : ");
                    String newSCo    = readGeneralName("New Company Name : ");
                    String newSPhone = readPhone("New Phone (10 digits) : ");
                    String newSEmail = readEmail("New Email : ");
                    String newSAddr  = readNotBlank("New Address : ");
                    if (supplierDAO.updateSupplier(suId, newSName, newSCo, newSPhone, newSEmail, newSAddr)) {
                        System.out.println("  [OK] Supplier Updated Successfully.");
                    } else {
                        System.out.println("  [FAIL] Supplier Update Failed.");
                    }
                    break;

                case 5:
                    // Validate ID FIRST
                    int sdId = readPositiveInt("Supplier ID to Delete : ");
                    if (!supplierDAO.exists(sdId)) {
                        System.out.println("  [FAIL] Supplier ID " + sdId + " does not exist.");
                        break;
                    }
                    System.out.print("  Confirm delete Supplier ID " + sdId + "? (yes/no) : ");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        if (supplierDAO.deleteSupplier(sdId)) {
                            System.out.println("  [OK] Supplier Deleted Successfully.");
                        } else {
                            System.out.println("  [FAIL] Delete Failed. May have linked products.");
                        }
                    } else {
                        System.out.println("  Delete cancelled.");
                    }
                    break;

                case 6:
                    return;

                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-6.");
            }
        }
    }

    // =================================================================
    //  PRODUCT MENU
    // =================================================================

    static void productMenu() {

        while (true) {

            System.out.println("\n========== PRODUCT MENU ==========");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Back");
            System.out.print("Enter Choice : ");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    String pName    = readGeneralName("Product Name : ");

                    // Validate Category ID immediately
                    int pCatId = readPositiveInt("Category ID : ");
                    if (!categoryDAO.exists(pCatId)) {
                        System.out.println("  [FAIL] Category ID " + pCatId + " does not exist. View categories first.");
                        break;
                    }

                    // Validate Supplier ID immediately
                    int pSupId = readPositiveInt("Supplier ID : ");
                    if (!supplierDAO.exists(pSupId)) {
                        System.out.println("  [FAIL] Supplier ID " + pSupId + " does not exist. View suppliers first.");
                        break;
                    }

                    double pPrice   = readPositiveDouble("Unit Price (e.g. 299.99) : ");
                    int pReorder    = readNonNegativeInt("Reorder Level (min qty before alert) : ");

                    if (productDAO.addProduct(new Product(pName, pCatId, pSupId, pPrice, pReorder))) {
                        System.out.println("  [OK] Product Added Successfully.");
                    } else {
                        System.out.println("  [FAIL] Failed to Add Product.");
                    }
                    break;

                case 2:
                    productDAO.viewProducts();
                    break;

                case 3:
                    int psId = readPositiveInt("Product ID : ");
                    productDAO.searchProduct(psId);
                    break;

                case 4:
                    // Validate Product ID FIRST
                    int puId = readPositiveInt("Product ID to Update : ");
                    if (!productDAO.exists(puId)) {
                        System.out.println("  [FAIL] Product ID " + puId + " does not exist.");
                        break;
                    }
                    String newPName   = readGeneralName("New Product Name : ");
                    double newPPrice  = readPositiveDouble("New Unit Price : ");
                    int newPReorder   = readNonNegativeInt("New Reorder Level : ");
                    if (productDAO.updateProduct(puId, newPName, newPPrice, newPReorder)) {
                        System.out.println("  [OK] Product Updated Successfully.");
                    } else {
                        System.out.println("  [FAIL] Product Update Failed.");
                    }
                    break;

                case 5:
                    // Validate Product ID FIRST
                    int pdId = readPositiveInt("Product ID to Delete : ");
                    if (!productDAO.exists(pdId)) {
                        System.out.println("  [FAIL] Product ID " + pdId + " does not exist.");
                        break;
                    }
                    System.out.print("  Confirm delete Product ID " + pdId + "? (yes/no) : ");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        if (productDAO.deleteProduct(pdId)) {
                            System.out.println("  [OK] Product Deleted Successfully.");
                        } else {
                            System.out.println("  [FAIL] Delete Failed. May have linked stock records.");
                        }
                    } else {
                        System.out.println("  Delete cancelled.");
                    }
                    break;

                case 6:
                    return;

                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-6.");
            }
        }
    }

    // =================================================================
    //  STOCK MENU
    // =================================================================

    static void stockMenu() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("           STOCK MENU");
            System.out.println("====================================");
            System.out.println("1. Add Stock Entry");
            System.out.println("2. View All Stock");
            System.out.println("3. Stock In  (Add Quantity)");
            System.out.println("4. Stock Out (Remove Quantity)");
            System.out.println("5. Undo Last Transaction");
            System.out.println("6. Delete Stock Entry");
            System.out.println("7. Back");
            System.out.println("====================================");
            System.out.print("Enter Choice : ");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    // Validate Product ID FIRST
                    int stProdId = readPositiveInt("Product ID : ");
                    if (!productDAO.exists(stProdId)) {
                        System.out.println("  [FAIL] Product ID " + stProdId + " does not exist. Add the product first.");
                        break;
                    }

                    // Validate Warehouse ID SECOND
                    int stWhId = readPositiveInt("Warehouse ID : ");
                    if (!warehouseDAO.exists(stWhId)) {
                        System.out.println("  [FAIL] Warehouse ID " + stWhId + " does not exist. Add the warehouse first.");
                        break;
                    }

                    // Only then ask quantity
                    int stQty = readPositiveInt("Initial Quantity : ");
                    if (stockDAO.addStock(new Stock(stProdId, stWhId, stQty))) {
                        System.out.println("  [OK] Stock Entry Added Successfully.");
                    } else {
                        System.out.println("  [FAIL] Failed to Add Stock Entry.");
                    }
                    break;

                case 2:
                    stockDAO.viewStock();
                    break;

                case 3:
                    // Validate Stock ID FIRST
                    int inId = readPositiveInt("Stock ID : ");
                    if (!stockDAO.exists(inId)) {
                        System.out.println("  [FAIL] Stock ID " + inId + " does not exist. View stock list first.");
                        break;
                    }
                    // Only then ask quantity
                    int inQty = readPositiveInt("Quantity to Add : ");
                    if (stockDAO.stockIn(inId, inQty)) {
                        System.out.println("  [OK] Stock IN successful. +" + inQty + " units added.");
                    } else {
                        System.out.println("  [FAIL] Stock In Failed.");
                    }
                    break;

                case 4:
                    // Validate Stock ID FIRST
                    int outId = readPositiveInt("Stock ID : ");
                    if (!stockDAO.exists(outId)) {
                        System.out.println("  [FAIL] Stock ID " + outId + " does not exist. View stock list first.");
                        break;
                    }
                    // Only then ask quantity
                    int outQty = readPositiveInt("Quantity to Remove : ");
                    if (stockDAO.stockOut(outId, outQty)) {
                        System.out.println("  [OK] Stock OUT successful. -" + outQty + " units removed.");
                    } else {
                        System.out.println("  [FAIL] Insufficient stock quantity.");
                    }
                    break;

                case 5:
                    if (stockDAO.undoLastTransaction()) {
                        System.out.println("  [OK] Last transaction undone successfully.");
                    } else {
                        System.out.println("  [!] No transactions available to undo.");
                    }
                    break;

                case 6:
                    // Validate Stock ID FIRST
                    int delId = readPositiveInt("Stock ID to Delete : ");
                    if (!stockDAO.exists(delId)) {
                        System.out.println("  [FAIL] Stock ID " + delId + " does not exist.");
                        break;
                    }
                    System.out.print("  Confirm delete Stock ID " + delId + "? (yes/no) : ");
                    if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
                        if (stockDAO.deleteStock(delId)) {
                            System.out.println("  [OK] Stock Entry Deleted Successfully.");
                        } else {
                            System.out.println("  [FAIL] Delete Failed.");
                        }
                    } else {
                        System.out.println("  Delete cancelled.");
                    }
                    break;

                case 7:
                    return;

                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-7.");
            }
        }
    }

    // =================================================================
    //  PURCHASE MENU
    // =================================================================

    static void purchaseMenu() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("          PURCHASE MENU");
            System.out.println("====================================");
            System.out.println("1. Add Purchase Order");
            System.out.println("2. View All Purchase Orders");
            System.out.println("3. Process Next Order (FIFO)");
            System.out.println("4. Back");
            System.out.println("====================================");
            System.out.print("Enter Choice : ");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    // Validate Product ID FIRST
                    int poProdId = readPositiveInt("Product ID : ");
                    if (!productDAO.exists(poProdId)) {
                        System.out.println("  [FAIL] Product ID " + poProdId + " does not exist. Add the product first.");
                        break;
                    }

                    // Validate Warehouse ID SECOND
                    int poWhId = readPositiveInt("Warehouse ID : ");
                    if (!warehouseDAO.exists(poWhId)) {
                        System.out.println("  [FAIL] Warehouse ID " + poWhId + " does not exist. Add the warehouse first.");
                        break;
                    }

                    // Only then ask quantity
                    int poQty = readPositiveInt("Quantity : ");
                    if (purchaseDAO.addPurchaseOrder(new PurchaseOrder(poProdId, poWhId, poQty))) {
                        System.out.println("  [OK] Purchase Order Added and Queued Successfully.");
                    } else {
                        System.out.println("  [FAIL] Failed to Add Purchase Order.");
                    }
                    break;

                case 2:
                    purchaseDAO.viewPurchaseOrders();
                    break;

                case 3:
                    if (purchaseDAO.processNextOrder()) {
                        System.out.println("  [OK] Purchase Order Processed Successfully.");
                    } else {
                        System.out.println("  [!] No Pending Purchase Orders in Queue.");
                    }
                    break;

                case 4:
                    return;

                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-4.");
            }
        }
    }

    // =================================================================
    //  REPORT MENU
    // =================================================================

    static void reportMenu() {

        ReportDAO reportDAO = new ReportDAO();

        while (true) {

            System.out.println("\n========== REPORTS ==========");
            System.out.println("1. Current Stock Report");
            System.out.println("2. Low Stock Alert");
            System.out.println("3. Warehouse Stock Summary");
            System.out.println("4. Category Product Report");
            System.out.println("5. Supplier Product Report");
            System.out.println("6. Pending Purchase Orders");
            System.out.println("7. Back");
            System.out.print("Choice : ");

            switch (readMenuChoice()) {
                case 1: reportDAO.currentStockReport();   break;
                case 2: reportDAO.lowStockReport();        break;
                case 3: reportDAO.warehouseStockReport();  break;
                case 4: reportDAO.categoryProductReport(); break;
                case 5: reportDAO.supplierProductReport(); break;
                case 6: reportDAO.pendingOrdersReport();   break;
                case 7: return;
                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-7.");
            }
        }
    }

    // =================================================================
    //  MANAGER MENU
    // =================================================================

    static void managerMenu() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("        MANAGER DASHBOARD");
            System.out.println("====================================");
            System.out.println("1. Product Management");
            System.out.println("2. Stock Management");
            System.out.println("3. Purchase Orders");
            System.out.println("4. View Reports");
            System.out.println("5. Logout");
            System.out.println("====================================");
            System.out.print("Enter Choice : ");

            switch (readMenuChoice()) {
                case 1: productMenu();  break;
                case 2: stockMenu();    break;
                case 3: purchaseMenu(); break;
                case 4: reportMenu();   break;
                case 5:
                    System.out.println("Logging Out...");
                    return;
                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-5.");
            }
        }
    }

    // =================================================================
    //  STAFF MENU
    // =================================================================

    static void staffMenu() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("         STAFF DASHBOARD");
            System.out.println("====================================");
            System.out.println("1. View Stock");
            System.out.println("2. Stock In  (Add Quantity)");
            System.out.println("3. Stock Out (Remove Quantity)");
            System.out.println("4. Logout");
            System.out.println("====================================");
            System.out.print("Enter Choice : ");

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    stockDAO.viewStock();
                    break;

                case 2:
                    // Validate Stock ID FIRST
                    int sfInId = readPositiveInt("Enter Stock ID : ");
                    if (!stockDAO.exists(sfInId)) {
                        System.out.println("  [FAIL] Stock ID " + sfInId + " does not exist. View stock list first.");
                        break;
                    }
                    int sfInQty = readPositiveInt("Enter Quantity to Add : ");
                    if (stockDAO.stockIn(sfInId, sfInQty)) {
                        System.out.println("  [OK] Stock Added Successfully. +" + sfInQty + " units.");
                    } else {
                        System.out.println("  [FAIL] Operation Failed.");
                    }
                    break;

                case 3:
                    // Validate Stock ID FIRST
                    int sfOutId = readPositiveInt("Enter Stock ID : ");
                    if (!stockDAO.exists(sfOutId)) {
                        System.out.println("  [FAIL] Stock ID " + sfOutId + " does not exist. View stock list first.");
                        break;
                    }
                    int sfOutQty = readPositiveInt("Enter Quantity to Remove : ");
                    if (stockDAO.stockOut(sfOutId, sfOutQty)) {
                        System.out.println("  [OK] Stock Removed Successfully. -" + sfOutQty + " units.");
                    } else {
                        System.out.println("  [FAIL] Insufficient Stock.");
                    }
                    break;

                case 4:
                    System.out.println("Logging Out...");
                    return;

                default:
                    System.out.println("  [!] Invalid Choice. Enter 1-4.");
            }
        }
    }
}