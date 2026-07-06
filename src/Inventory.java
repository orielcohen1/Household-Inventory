import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Inventory implements Serializable {
    // המפתח יהיה שילוב של שם ותאריך תפוגה, למשל: "milk#2026-06-16"
    private Map<String, Product> productsMap;

    public Inventory() {
        this.productsMap = new HashMap<>();
    }

    // פונקציית עזר פרטית לייצור המפתח המשולב באופן אחיד
    private String generateKey(String name, LocalDate expDate) {
        return name.toLowerCase().trim() + "#" + expDate.toString();
    }

    public void addProduct(Product newProduct) {
        // יצירת המפתח המשולב עבור המוצר החדש
        String key = generateKey(newProduct.getName(), newProduct.getExpDate());

        if (productsMap.containsKey(key)) {
            // אם כבר קיים במלאי בדיוק אותו מוצר עם אותו תאריך תפוגה - נחבר כמויות
            productsMap.get(key).addQuantity(newProduct.getQuantity());
        } else {
            // אם מדובר בתאריך תפוגה חדש, זה יישמר כפריט נפרד במפה
            productsMap.put(key, newProduct);
        }
    }

    // אפשרות 1: מחיקת מוצר ספציפי לפי שם ותאריך תפוגה מדויק
    public String removeProduct(String name, LocalDate expDate) {
        String key = generateKey(name, expDate);
        if (productsMap.remove(key) != null) {
            return "Item with expiration date " + expDate + " successfully deleted!";
        }
        return "Item with this expiration date not found in stock";
    }

    // אפשרות 2: מחיקה גורפת של כל המוצרים בעלי אותו שם (ללא קשר לתאריך)
    public String removeAllByName(String name) {
        String searchName = name.toLowerCase().trim();
        // removeIf עובר על הערכים ומוחק את כל מי שעונה על התנאי
        boolean removed = productsMap.values().removeIf(p -> p.getName().toLowerCase().equals(searchName));

        if (removed) {
            return "All items named '" + name + "' were successfully deleted!";
        }
        return "Item not in stock";
    }

    // אפשרות 1: חיפוש כל המופעים של מוצר מסוים (מציג את כל תאריכי התפוגה השונים שלו)
    public String seek(String search) {
        StringBuilder data = new StringBuilder();
        String searchName = search.toLowerCase().trim();
        boolean found = false;

        for (Product p : productsMap.values()) {
            if (p.getName().toLowerCase().equals(searchName)) {
                found = true;
                data.append("Item found! There are ")
                        .append(p.getQuantity()).append(" ")
                        .append(p.getName())
                        .append(" | Expiration date: ").append(p.getExpDate()).append("\n");
            }
        }

        if (found) {
            return data.toString();
        }
        return "Item '" + search + "' not found!";
    }

    // החזרת אוסף כל המוצרים (בשביל תצוגה בממשק הגרפי בהמשך)
    public Collection<Product> getProducts() {
        return productsMap.values();
    }

    @Override
    public String toString() {
        if (productsMap.isEmpty()) {
            return "No products available.";
        }

        StringBuilder result = new StringBuilder();
        for (Product p : productsMap.values()) {
            result.append(p.toString()).append("\n");
        }
        return result.toString();
    }
}