/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeteriaapp.resources.controllers;

import cafeteriaapp.Cart;
import cafeteriaapp.DatabaseConnection;
import cafeteriaapp.SceneManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Priority;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author DELL
 */
public class MenuController implements Initializable {

    @FXML
    private VBox menuListContainer;

    private final List<MenuItem> allItems = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadMenuFromDatabase();
        renderItems(allItems);
    }

    private void loadMenuFromDatabase() {
        String query = "SELECT menu_item_id, name, category, menu_quantity, menu_price, image FROM menu_items ORDER BY category, name";

        try (Connection connection = DatabaseConnection.getConnect();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int menuItemId = rs.getInt("menu_item_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                int quantity = rs.getInt("menu_quantity");
                double price = rs.getDouble("menu_price");
                String image = rs.getString("image");

                String imagePath = "../images/" + image;

                allItems.add(new MenuItem(menuItemId, name, category, quantity, price, imagePath));
            }

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void renderItems(List<MenuItem> items) {
        menuListContainer.getChildren().clear();

        if (items.isEmpty()) {
            Label empty = new Label("No items match your search.");
            empty.getStyleClass().add("empty-label");
            empty.setMaxWidth(Double.MAX_VALUE);
            empty.setAlignment(Pos.CENTER);
            menuListContainer.getChildren().add(empty);
            return;
        }

        String currentCategory = null;

        for (MenuItem item : items) {

            if (!item.getCategory().equals(currentCategory)) {
                currentCategory = item.getCategory();

                Label categoryHeading = new Label(currentCategory);
                categoryHeading.getStyleClass().add("category-heading");
                categoryHeading.setMaxWidth(Double.MAX_VALUE);
                categoryHeading.setAlignment(Pos.CENTER_LEFT);
                menuListContainer.getChildren().add(categoryHeading);
            }

            menuListContainer.getChildren().add(buildCard(item));
        }
    }

    private VBox buildCard(MenuItem item) {
        VBox wrapper = new VBox();
        wrapper.getStyleClass().add("menu-item-wrapper");

        HBox card = new HBox(14);
        card.getStyleClass().add("menu-item-card");
        card.setAlignment(Pos.CENTER_LEFT);

        StackPane imageBox = new StackPane();
        imageBox.getStyleClass().add("item-image-container");
        imageBox.setMinSize(72, 72);
        imageBox.setMaxSize(72, 72);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(72);
        imageView.setFitHeight(72);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("menu-item-image");

        try {
            URL imageUrl = getClass().getResource(item.getImagePath());
            if (imageUrl != null) {
                imageView.setImage(new Image(imageUrl.toExternalForm(), true));
            }
        } catch (Exception ignored) {
        }
        imageBox.getChildren().add(imageView);

        VBox info = new VBox(4);
        info.getStyleClass().add("item-info");
        HBox.setHgrow(info, Priority.ALWAYS);

        Label name = new Label(item.getName());
        name.getStyleClass().add("item-name");
        name.setWrapText(true);
        name.setMaxWidth(160);

        Label qty = new Label("Quantity: " + item.getQuantity());
        qty.getStyleClass().add("item-quantity");

        info.getChildren().addAll(name, qty);

        VBox rightCol = new VBox(6);
        rightCol.setAlignment(Pos.CENTER_RIGHT);

        Label price = new Label("₦" + String.format("%,.2f", item.getPrice()));
        price.getStyleClass().add("item-price");

        Button addBtn = new Button("Add  +");
        addBtn.getStyleClass().add("add-btn");

        addBtn.setOnAction(e -> handleAddToCart(item));

        rightCol.getChildren().addAll(price, addBtn);

        card.getChildren().addAll(imageBox, info, rightCol);
        wrapper.getChildren().add(card);
        return wrapper;
    }

    private void handleAddToCart(MenuItem item) {
        if (item.getQuantity() <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Sorry, \"" + item.getName() + "\" is out of stock.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Cart.getInstance().addItem(item.getMenuItemId(), item.getName(), item.getPrice());
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "\"" + item.getName() + "\" has been added to your cart.", ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void handleOpenCart(MouseEvent event) {
        try {
            SceneManager.removeFromCache("resources/views/cart.fxml");
            SceneManager.switchTo("resources/views/cart.fxml", false);
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleBackToHome(MouseEvent event) {
        try {
            cafeteriaapp.SceneManager.switchTo("resources/views/dashboard.fxml", false);
        } catch (java.io.IOException e) {
            System.out.println("Navigation error structural exception: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Application could not render home view layout framework.");
            alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public static class MenuItem {

        private final int menuItemId;
        private final String name;
        private final String category;
        private final int quantity;
        private final double price;
        private final String imagePath;

        public MenuItem(int menuItemId, String name, String category, int quantity, double price, String imagePath) {
            this.menuItemId = menuItemId;
            this.name = name;
            this.category = category;
            this.quantity = quantity;
            this.price = price;
            this.imagePath = imagePath;
        }

        public int getMenuItemId() {
            return menuItemId;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}