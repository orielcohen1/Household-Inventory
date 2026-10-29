# Household Inventory

A desktop JavaFX application for managing a household's product inventory and shopping list. The UI is in Hebrew (RTL layout).

## Features

- **Inventory management**
  - View all products currently in stock (name, quantity, expiration date)
  - Add a product with a quantity and expiration date (quantities merge automatically if the same product/expiration date already exists)
  - Remove all stock of a product by name
  - Search for a product and see all its stock entries across different expiration dates
- **Shopping list**
  - View the current shopping list
  - Add an item
  - Remove an item
  - Search for an item
  - Expired inventory items are automatically added to the shopping list on startup
- **Persistence**: inventory and shopping list are saved to disk (Java serialization) and reloaded automatically on the next launch.

## Project structure

```
src/
  Main.java          # JavaFX application entry point and UI
  Inventory.java      # Inventory model (map of products keyed by name + expiration date)
  Product.java        # Product model (name, quantity, expiration date)
  ShoppingList.java    # Shopping list model (backed by a Set)
```

## Requirements

- JDK 17+ (or a version compatible with your JavaFX SDK)
- JavaFX SDK (not bundled with the JDK since Java 11)

## Running

### From IntelliJ IDEA

1. Open the project.
2. Configure a JavaFX library/SDK and add the required VM options, e.g.:
   ```
   --module-path "<path-to-javafx-sdk>/lib" --add-modules javafx.controls,javafx.graphics
   ```
3. Run `Main.java`.

### From the command line

```
javac --module-path "<path-to-javafx-sdk>/lib" --add-modules javafx.controls -d out src/*.java
java --module-path "<path-to-javafx-sdk>/lib" --add-modules javafx.controls,javafx.graphics -cp out Main
```

## Data files

On first run, `inventory.ser` and `shopping_list.ser` are created in the working directory and updated automatically whenever the application closes. Delete these files to reset the data.
