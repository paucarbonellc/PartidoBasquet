package me.pau.point;

public class Point {

    private String id;
    private int amount;

    public Point(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }

    public String getId() {
        return this.id;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
