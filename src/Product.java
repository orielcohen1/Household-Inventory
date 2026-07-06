import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable {
    private String name;
    private LocalDate expDate;
    private int quantity;

    // בנאי (Constructor)
    public Product(String name, LocalDate expDate, int quantity) {
        this.name = name;
        this.expDate = expDate;
        this.quantity = quantity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // מתודה נוחה לעדכון כמות קיימת במקום לדרוס אותה
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return "Product name: " + name +
                ", quantity: " + quantity +
                ", expiration date: " + expDate;
    }
}