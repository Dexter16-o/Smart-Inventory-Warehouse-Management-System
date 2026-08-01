package utils;

/**
 * InputValidator — Central class for all input validation rules.
 * Used by Main.java to validate every user-entered field before
 * passing data to DAO/database layer.
 */
public class InputValidator {

    // -------------------------------------------------------
    // Person Name : letters and spaces only, 2-60 chars
    // -------------------------------------------------------
    public static boolean isValidPersonName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.trim().matches("^[A-Za-z][A-Za-z .'-]{1,59}$");
    }

    // -------------------------------------------------------
    // General Name (product, category, warehouse, company)
    // At least one alphabet and may contain letters, numbers, spaces or hyphens
    // -------------------------------------------------------
    public static boolean isValidGeneralName(String name) {

        if (name == null)
            return false;

        String trimmed = name.trim();

        // Minimum length
        if (trimmed.length() < 2)
            return false;

        // Must contain at least one alphabet
        if (!trimmed.matches(".*[A-Za-z].*"))
            return false;

        // Allow only letters, numbers, spaces and hyphens
        return trimmed.matches("[A-Za-z0-9\\- ]+");
    }

    // -------------------------------------------------------
    // Phone : exactly 10 digits
    // -------------------------------------------------------
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.trim().matches("^[0-9]{10}$");
    }

    // -------------------------------------------------------
    // Email : standard email format
    // -------------------------------------------------------
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.trim().matches(
                "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
        );
    }

    // -------------------------------------------------------
    // Password : minimum 6 characters
    // -------------------------------------------------------
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 6;
    }

    // -------------------------------------------------------
    // Role : must be ADMIN, MANAGER, or STAFF
    // -------------------------------------------------------
    public static boolean isValidRole(String role) {
        if (role == null) return false;
        String r = role.trim().toUpperCase();
        return r.equals("ADMIN") || r.equals("MANAGER") || r.equals("STAFF");
    }

    // -------------------------------------------------------
    // Positive Integer  (> 0) — for IDs, quantities, capacity
    // -------------------------------------------------------
    public static boolean isPositiveInteger(String input) {
        try {
            return Integer.parseInt(input.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // -------------------------------------------------------
    // Non-negative Integer (>= 0) — for reorder level
    // -------------------------------------------------------
    public static boolean isNonNegativeInteger(String input) {
        try {
            return Integer.parseInt(input.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // -------------------------------------------------------
    // Positive Double  (> 0) — for unit price
    // -------------------------------------------------------
    public static boolean isPositiveDouble(String input) {
        try {
            return Double.parseDouble(input.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // -------------------------------------------------------
    // Not blank — for address, location, description, etc.
    // -------------------------------------------------------
    public static boolean isNotBlank(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
