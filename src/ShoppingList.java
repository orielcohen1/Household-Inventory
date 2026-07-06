import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ShoppingList implements Serializable {
    // HashSet מבטיח שלא יהיו כפילויות ושהרשימה תהיה דינמית
    private Set<String> items;

    public ShoppingList() {
        this.items = new HashSet<>();
    }

    // הוספת פריט לרשימה
    public void addItem(String name) {
        // מתודת add של Set כבר דואגת שלא להוסיף אם הערך קיים
        items.add(name.toLowerCase().trim());
    }

    // מחיקת פריט מהרשימה
    public String deleteItem(String name) {
        if (items.remove(name.toLowerCase().trim())) {
            return "Item successfully deleted!";
        }
        return "Item not in list";
    }

    // מעבר על המלאי והוספה אוטומטית של מוצרים שפג תוקפם
    public void addItemExp(Inventory inventory) {
        LocalDate today = LocalDate.now();

        // עוברים על כל המוצרים שיש כרגע במלאי
        for (Product p : inventory.getProducts()) {
            // אם תאריך התפוגה הוא היום או לפני היום (כבר פג תוקף)
            if (!p.getExpDate().isAfter(today)) {
                addItem(p.getName());
            }
        }
    }

    // פונקציה שתעזור לנו להציג את הרשימה בממשק הגרפי
    public Set<String> getItems() {
        return items;
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "The list is empty";
        }

        StringBuilder res = new StringBuilder();
        for (String item : items) {
            res.append(item).append("\n");
        }
        return res.toString();
    }
}