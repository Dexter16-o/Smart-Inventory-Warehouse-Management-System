package datastructure;

import model.PurchaseOrder;

public class PurchaseQueue {

    private static final int MAX = 100;
    private PurchaseOrder[] queue;
    private int front;
    private int rear;

    public PurchaseQueue(){
        queue = new PurchaseOrder[MAX];
        front = 0;
        rear  = MAX - 1; // Circular queue: rear starts at last index
    }

    public boolean isEmpty(){
        return (rear + 1) % MAX == front;
    }

    public boolean isFull(){
        return (rear + 2) % MAX == front;
    }

    public boolean enqueue(PurchaseOrder order){
        if(isFull()){
            System.out.println("Queue Overflow");
            return false;
        }
        rear = (rear + 1) % MAX;
        queue[rear] = order;
        return true;
    }

    public PurchaseOrder dequeue(){
        if(isEmpty()){
            return null;
        }
        PurchaseOrder order = queue[front];
        queue[front] = null; // Help GC
        front = (front + 1) % MAX;
        return order;
    }

    public PurchaseOrder peek(){
        if(isEmpty()){
            return null;
        }
        return queue[front];
    }

    public int size(){
        return (rear - front + 1 + MAX) % MAX;
    }
}