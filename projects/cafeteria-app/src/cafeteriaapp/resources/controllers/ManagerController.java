package cafeteriaapp.resources.controllers;

import cafeteriaapp.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManagerController {

    @FXML private Label formTitle;
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextArea descriptionField;
    @FXML private Button submitBtn;
    @FXML private VBox menuListContainer;

    private int editingItemId = -1;

    @FXML
    public void initialize() {
        loadMenuItems();
    }

    private void loadMenuItems() {
        menuListContainer.getChildren().clear();

        String query = "SELECT menu_item_id, name, category, menu_price, menu_quantity FROM menu_items ORDER BY category, name";

        try (Connection connection = DatabaseConnection.getConnect();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("menu_item_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("menu_price");
                int quantity = rs.getInt("menu_quantity");

                menuListContainer.getChildren().add(buildItemRow(id, name, category, price, quantity));
            }

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
            Logger.getLogger(ManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private HBox buildItemRow(int id, String name, String category, double price, int quantity) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #ffffff; -fx-padding: 12px; -fx-background-radius: 8px; -fx-border-color: #edf2f7; -fx-border-width: 1px;");

        VBox info = new VBox(4);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2d3748;");
        
        Label metaLbl = new Label(category + "  •  ₦" + String.format("%.2f", price) + "  •  Stock: " + quantity);
        metaLbl.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");
        
        info.getChildren().addAll(nameLbl, metaLbl);

        HBox.setHgrow(info, Priority.ALWAYS);

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #edf2f7; -fx-text-fill: #4a5568; -fx-font-size: 12px; -fx-cursor: hand;");
        editBtn.setOnAction(e -> handleEdit(id, name, category, price, quantity));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #fed7d7; -fx-text-fill: #e53e3e; -fx-font-size: 12px; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> handleDelete(id, name));

        row.getChildren().addAll(info, editBtn, deleteBtn);
        return row;
    }

    private void handleEdit(int id, String name, String category, double price, int quantity) {
        editingItemId = id;
        formTitle.setText("Modify: " + name);
        submitBtn.setText("Save Changes");

        nameField.setText(name);
        categoryField.setText(category);
        priceField.setText(String.valueOf(price));
        quantityField.setText(String.valueOf(quantity));

        String descQuery = "SELECT description FROM menu_items WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnect();
             PreparedStatement stmt = connection.prepareStatement(descQuery)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    descriptionField.setText(rs.getString("description"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching description: " + ex.getMessage());
        }
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            showError("Please fill in all mandatory input fields marked with an asterisk (*).");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            if (price < 0 || quantity < 0) {
                showError("Price and Quantity numbers cannot sit below zero.");
                return;
            }

            if (editingItemId == -1) {
                insertMenuItem(name, category, price, quantity, description);
            } else {
                updateMenuItem(editingItemId, name, category, price, quantity, description);
            }

        } catch (NumberFormatException e) {
            showError("Invalid input format. Ensure Price uses decimal numbers and Quantity values are whole numbers.");
        }
    }

    private void insertMenuItem(String name, String category, double price, int quantity, String description) {
        String query = "INSERT INTO menu_items(name, category, menu_price, menu_quantity, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setString(5, description);
            
            stmt.executeUpdate();

            showSuccess("\"" + name + "\" has been added to the menu.");
            handleClear();
            loadMenuItems();

        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                showError("An entry named \"" + name + "\" already exists in the menu.");
            } else {
                showError("Database operation failed: " + ex.getMessage());
            }
        }
    }

    private void updateMenuItem(int id, String name, String category, double price, int quantity, String description) {
        String query = "UPDATE menu_items SET name = ?, category = ?, menu_price = ?, menu_quantity = ?, description = ? WHERE menu_item_id = ?";

        try (Connection connection = DatabaseConnection.getConnect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setString(5, description);
            stmt.setInt(6, id);

            stmt.executeUpdate();

            showSuccess("Details for \"" + name + "\" have been updated.");
            handleClear();
            loadMenuItems();

        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                showError("Cannot save changes. Another menu item already uses the name \"" + name + "\".");
            } else {
                showError("Modification update error: " + ex.getMessage());
            }
        }
    }

    private void handleDelete(int id, String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete \"" + name + "\" from the menu?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirm Deletion");
        alert.setTitle("Inventory Verification");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String query = "DELETE FROM menu_items WHERE menu_item_id = ?";
                try (Connection connection = DatabaseConnection.getConnect();
                     PreparedStatement stmt = connection.prepareStatement(query)) {
                    
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    
                    showSuccess("\"" + name + "\" has been removed from the menu.");
                    
                    loadMenuItems();
                    handleClear(); // Ensures we clear the form if the deleted item was currently loaded in Edit Mode
                    
                } catch (SQLException ex) {
                    showError("Purge restriction constraint broken: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        editingItemId = -1; 
        
        formTitle.setText("Add New Item");
        submitBtn.setText("Add Item");

        nameField.clear();
        categoryField.clear();
        priceField.clear();
        quantityField.clear();
        descriptionField.clear();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            cafeteriaapp.SceneManager.switchTo("resources/views/login.fxml", false);
        } catch (IOException e) {
            System.out.println("Failed to load login screen layout: " + e.getMessage());
            showError("An error occurred while trying to log out. Could not find login view.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Operational Error Window");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Process Verification System");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}