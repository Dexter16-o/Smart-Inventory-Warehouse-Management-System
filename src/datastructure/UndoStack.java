package datastructure;

public class UndoStack {


    private static final int MAX_SIZE = 100;

    private StockTransaction[] stack;
    private int top;

    public UndoStack() {
        stack = new StockTransaction[MAX_SIZE];
        top = -1;
    }

    // Push Operation
    public boolean push(StockTransaction transaction) {

        if (isFull()) {
            System.out.println("Stack Overflow! Cannot store more transactions.");
            return false;
        }

        stack[++top] = transaction;
        return true;
    }

    // Pop Operation
    public StockTransaction pop() {

        if (isEmpty()) {
            System.out.println("Stack Underflow! No transaction available.");
            return null;
        }

        return stack[top--];
    }

    // Peek Operation
    public StockTransaction peek() {
        if (isEmpty()) {
            return null;
        }

        return stack[top];
    }

    // Check Empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Check Full
    public boolean isFull() {
        return top == MAX_SIZE - 1;
    }

    // Current Size
    public int size() {
        return top + 1;
    }

    // Display Stack (for testing)
    public void display() {

        if (isEmpty()) {
            System.out.println("Stack is Empty.");
            return;
        }

        System.out.println("\n======= UNDO STACK =======");

        for (int i = top; i >= 0; i--) {
            StockTransaction t = stack[i];
            System.out.println(
                    "Stock ID : " + t.getStockId()
                            + " | Type : " + t.getType()
                            + " | Qty : " + t.getQuantity()
            );
        }
    }

    // Clear Stack
    public void clear() {
        top = -1;
    }

}