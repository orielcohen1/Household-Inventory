import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;

public class Main extends Application {
    private static final String INVENTORY_FILE = "inventory.ser";
    private static final String SHOPPING_LIST_FILE = "shopping_list.ser";

    private Inventory inventory;
    private ShoppingList shoppingList;
    private BorderPane root;

    @Override
    public void init() {
        inventory = loadInventory();
        shoppingList = loadShoppingList();
        shoppingList.addItemExp(inventory);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ramchal House Inventory");
        root = new BorderPane();

        // --- בניית התפריט הצידי בקטגוריות ---
        VBox sideMenu = new VBox(10);
        sideMenu.setStyle("-fx-background-color: #1e293b; -fx-padding: 20; -fx-pref-width: 220;");

        // קטגוריה 1: המלאי שלי
        Label lblInventory = new Label("המלאי שלי");
        lblInventory.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 18;");
        VBox.setMargin(lblInventory, new Insets(0, 0, 5, 0)); // רווח קטן מתחת לכותרת

        Button btnViewInventory = createMenuButton("צפייה במלאי");
        Button btnAddInventory = createMenuButton("הוסף למלאי");
        Button btnRemoveInventory = createMenuButton("הסר מהמלאי");
        Button btnSearchInventory = createMenuButton("חפש במלאי");

        // קטגוריה 2: קניות
        Label lblShopping = new Label("קניות");
        lblShopping.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 18;");
        VBox.setMargin(lblShopping, new Insets(20, 0, 5, 0)); // רווח גדול יותר מעל הקטגוריה השנייה

        Button btnViewShoppingList = createMenuButton("רשימת קניות");
        Button btnAddShoppingItem = createMenuButton("הוסף לרשימת קניות");
        Button btnRemoveShoppingItem = createMenuButton("הסר מרשימת קניות");
        Button btnSearchShoppingItem = createMenuButton("חפש ברשימת קניות");

        // הוספת כל הרכיבים לתפריט הצידי
        sideMenu.getChildren().addAll(
                lblInventory, btnViewInventory, btnAddInventory, btnRemoveInventory, btnSearchInventory,
                lblShopping, btnViewShoppingList, btnAddShoppingItem, btnRemoveShoppingItem, btnSearchShoppingItem
        );
        root.setRight(sideMenu);

        // --- חיבור הכפתורים למסכים ---
        btnViewInventory.setOnAction(e -> showInventoryView());
        btnAddInventory.setOnAction(e -> showAddProductView());
        btnRemoveInventory.setOnAction(e -> showRemoveProductView());
        btnSearchInventory.setOnAction(e -> showSearchProductView());

        btnViewShoppingList.setOnAction(e -> showShoppingListView());
        btnAddShoppingItem.setOnAction(e -> showAddShoppingItemView());
        btnRemoveShoppingItem.setOnAction(e -> showRemoveShoppingItemView());
        btnSearchShoppingItem.setOnAction(e -> showSearchShoppingItemView());

        // מסך פתיחה ראשוני
        showWelcomeScreen();

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // פונקציית עזר ליצירת כפתורי תפריט בעיצוב אחיד
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-font-size: 14; -fx-padding: 8; -fx-alignment: CENTER_RIGHT;"); // יישור הטקסט לימין
        return btn;
    }

    // --- המסכים (Views) ---

    private void showWelcomeScreen() {
        Label welcomeLabel = new Label("ברוכים הבאים למערכת ניהול המלאי הביתית!\nבחר אפשרות מהתפריט הצידי כדי להתחיל.");
        welcomeLabel.setStyle("-fx-font-size: 20; -fx-text-alignment: center;");
        root.setCenter(welcomeLabel);
    }

    // 1. צפייה במלאי
    private void showInventoryView() {
        VBox view = new VBox(10);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("מלאי נוכחי בבית");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        for (Product p : inventory.getProducts()) {
            listView.getItems().add(p.getName() + " | כמות: " + p.getQuantity() + " | תוקף: " + p.getExpDate());
        }
        if (listView.getItems().isEmpty()) {
            listView.getItems().add("המלאי ריק כרגע.");
        }
        view.getChildren().addAll(title, listView);
        root.setCenter(view);
    }

    // 2. הוסף למלאי
    private void showAddProductView() {
        VBox view = new VBox(15);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("הוספת מוצר למלאי");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("שם המוצר");
        nameInput.setMaxWidth(300);

        TextField qtyInput = new TextField();
        qtyInput.setPromptText("כמות");
        qtyInput.setMaxWidth(300);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("תאריך תפוגה");
        datePicker.setMaxWidth(300);

        Button saveBtn = new Button("שמור במלאי");
        saveBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        Label feedbackLabel = new Label();

        saveBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText();
                int qty = Integer.parseInt(qtyInput.getText());
                LocalDate expDate = datePicker.getValue();

                if (name.isEmpty() || expDate == null) {
                    feedbackLabel.setText("נא למלא את כל השדות בצורה תקינה.");
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                inventory.addProduct(new Product(name, expDate, qty));
                nameInput.clear(); qtyInput.clear(); datePicker.setValue(null);
                feedbackLabel.setText("המוצר התווסף בהצלחה!");
                feedbackLabel.setStyle("-fx-text-fill: green;");
            } catch (NumberFormatException ex) {
                feedbackLabel.setText("שגיאה: יש להזין מספר תקין בכמות.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }
        });
        view.getChildren().addAll(title, nameInput, qtyInput, datePicker, saveBtn, feedbackLabel);
        root.setCenter(view);
    }

    // 3. הסר מהמלאי
    private void showRemoveProductView() {
        VBox view = new VBox(15);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("הסרת מוצר מהמלאי");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("הזן את שם המוצר להסרה");
        nameInput.setMaxWidth(300);

        Button removeBtn = new Button("הסר");
        removeBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        Label feedbackLabel = new Label();

        removeBtn.setOnAction(e -> {
            String name = nameInput.getText();
            if (!name.isEmpty()) {
                String result = inventory.removeAllByName(name);
                feedbackLabel.setText(result);
                if (result.contains("successfully")) {
                    feedbackLabel.setStyle("-fx-text-fill: green;");
                    nameInput.clear();
                } else {
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });
        view.getChildren().addAll(title, nameInput, removeBtn, feedbackLabel);
        root.setCenter(view);
    }

    // 4. חפש במלאי
    private void showSearchProductView() {
        VBox view = new VBox(15);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("חיפוש במלאי");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField searchInput = new TextField();
        searchInput.setPromptText("הזן שם מוצר לחיפוש");
        searchInput.setMaxWidth(300);

        Button searchBtn = new Button("חפש");
        searchBtn.setStyle("-fx-background-color: #38bdf8; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16;");

        searchBtn.setOnAction(e -> {
            String name = searchInput.getText();
            if (!name.isEmpty()) {
                String result = inventory.seek(name);
                resultLabel.setText(result);
            }
        });
        view.getChildren().addAll(title, searchInput, searchBtn, resultLabel);
        root.setCenter(view);
    }

    // 5. רשימת קניות (צפייה)
    private void showShoppingListView() {
        VBox view = new VBox(10);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("רשימת קניות");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        for (String item : shoppingList.getItems()) {
            listView.getItems().add(item);
        }
        if (listView.getItems().isEmpty()) {
            listView.getItems().add("רשימת הקניות ריקה.");
        }
        view.getChildren().addAll(title, listView);
        root.setCenter(view);
    }

    // 6. הוסף לרשימת קניות
    private void showAddShoppingItemView() {
        VBox view = new VBox(15);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("הוספת פריט לקניות");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("שם הפריט");
        nameInput.setMaxWidth(300);

        Button addBtn = new Button("הוסף לרשימה");
        addBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        Label feedbackLabel = new Label();

        addBtn.setOnAction(e -> {
            String name = nameInput.getText();
            if (!name.isEmpty()) {
                shoppingList.addItem(name);
                nameInput.clear();
                feedbackLabel.setText("הפריט התווסף לרשימת הקניות!");
                feedbackLabel.setStyle("-fx-text-fill: green;");
            }
        });
        view.getChildren().addAll(title, nameInput, addBtn, feedbackLabel);
        root.setCenter(view);
    }

    // 7. הסר מרשימת קניות
    private void showRemoveShoppingItemView() {
        VBox view = new VBox(15);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("הסרת פריט מקניות");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("שם הפריט להסרה");
        nameInput.setMaxWidth(300);

        Button removeBtn = new Button("הסר");
        removeBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        Label feedbackLabel = new Label();

        removeBtn.setOnAction(e -> {
            String name = nameInput.getText();
            if (!name.isEmpty()) {
                String result = shoppingList.deleteItem(name);
                feedbackLabel.setText(result);
                if (result.contains("successfully")) {
                    feedbackLabel.setStyle("-fx-text-fill: green;");
                    nameInput.clear();
                } else {
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });
        view.getChildren().addAll(title, nameInput, removeBtn, feedbackLabel);
        root.setCenter(view);
    }

    // 8. חפש ברשימת קניות
    private void showSearchShoppingItemView() {
        VBox view = new VBox(15);
        view.setStyle("-fx-padding: 30;");
        Label title = new Label("חיפוש ברשימת הקניות");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        TextField searchInput = new TextField();
        searchInput.setPromptText("הזן פריט לחיפוש");
        searchInput.setMaxWidth(300);

        Button searchBtn = new Button("חפש");
        searchBtn.setStyle("-fx-background-color: #38bdf8; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16;");

        searchBtn.setOnAction(e -> {
            String name = searchInput.getText().toLowerCase().trim();
            if (!name.isEmpty()) {
                if (shoppingList.getItems().contains(name)) {
                    resultLabel.setText("הפריט נמצא ברשימת הקניות!");
                    resultLabel.setStyle("-fx-text-fill: green;");
                } else {
                    resultLabel.setText("הפריט לא נמצא ברשימה.");
                    resultLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });
        view.getChildren().addAll(title, searchInput, searchBtn, resultLabel);
        root.setCenter(view);
    }

    @Override
    public void stop() {
        saveInventory(inventory);
        saveShoppingList(shoppingList);
        System.out.println("Data saved automatically.");
    }

    // --- פעולות השמירה והטעינה ---
    private static void saveInventory(Inventory inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventory);
        } catch (Exception e) {}
    }

    private static Inventory loadInventory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INVENTORY_FILE))) {
            return (Inventory) ois.readObject();
        } catch (Exception e) {
            return new Inventory();
        }
    }

    private static void saveShoppingList(ShoppingList list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SHOPPING_LIST_FILE))) {
            oos.writeObject(list);
        } catch (Exception e) {}
    }

    private static ShoppingList loadShoppingList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SHOPPING_LIST_FILE))) {
            return (ShoppingList) ois.readObject();
        } catch (Exception e) {
            return new ShoppingList();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}