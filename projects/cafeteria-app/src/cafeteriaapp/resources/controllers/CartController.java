/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeteriaapp.resources.controllers;

import cafeteriaapp.Cart;
import cafeteriaapp.Cart.CartItem;
import cafeteriaapp.DatabaseConnection;
import cafeteriaapp.SceneManager;
import cafeteriaapp.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author DELL
 */
public class CartController implements Initializable {

    @FXML
    private VBox cartListContainer;
    @FXML
    private Label totalLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        renderCart();
    }

    private void renderCart() {
        cartListContainer.getChildren().clear();

        Cart cart = Cart.getInstance();

        if (cart.isEmpty()) {
            Label empty = new Label("Your cart is empty.");
            empty.setStyle("-fx-text-fill: #999999; -fx-font-size: 14px; -fx-padding: 40 0 0 0;");
            empty.setMaxWidth(Double.MAX_VALUE);
            empty.setAlignment(Pos.CENTER);
            cartListContainer.getChildren().add(empty);
            totalLabel.setText("₦0.00");
            return;
        }

        for (CartItem item : cart.getItems()) {
            cartListContainer.getChildren().add(buildCartRow(item));
        }

        updateTotal();
    }

    private HBox buildCartRow(CartItem item) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-padding: 14 20 14 20; -fx-border-color: transparent transparent #e2e8f0 transparent; -fx-border-width: 0 0 1 0;");

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nameLbl = new Label(item.getName());
        nameLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");

        Label priceLbl = new Label("₦" + String.format("%,.2f", item.getPrice()) + " each");
        priceLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

        info.getChildren().addAll(nameLbl, priceLbl);

        HBox qtyBox = new HBox(8);
        qtyBox.setAlignment(Pos.CENTER);

        Button minusBtn = new Button("-");
        minusBtn.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #1a1a2e; -fx-font-weight: bold; -fx-background-radius: 20; -fx-min-width: 28; -fx-min-height: 28; -fx-cursor: hand;");

        Label qtyLbl = new Label(String.valueOf(item.getQuantity()));
        qtyLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e; -fx-min-width: 24; -fx-alignment: CENTER;");

        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-min-width: 28; -fx-min-height: 28; -fx-cursor: hand;");

        minusBtn.setOnAction(e -> {
            Cart.getInstance().decrementQuantity(item.getMenuItemId());
            renderCart();
        });

        plusBtn.setOnAction(e -> {
            Cart.getInstance().incrementQuantity(item.getMenuItemId());
            renderCart();
        });

        qtyBox.getChildren().addAll(minusBtn, qtyLbl, plusBtn);

        Label subtotalLbl = new Label("₦" + String.format("%,.2f", item.getSubtotal()));
        subtotalLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e; -fx-min-width: 90; -fx-alignment: CENTER_RIGHT;");

        Button deleteBtn = new Button("✕");
        deleteBtn.setStyle("-fx-background-color: #fed7d7; -fx-text-fill: #e53e3e; -fx-font-weight: bold; -fx-background-radius: 20; -fx-min-width: 28; -fx-min-height: 28; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            Cart.getInstance().removeItem(item.getMenuItemId());
            renderCart();
        });

        row.getChildren().addAll(info, qtyBox, subtotalLbl, deleteBtn);
        return row;
    }

    private void updateTotal() {
        totalLabel.setText("₦" + String.format("%,.2f", Cart.getInstance().getTotal()));
    }

    @FXML
    private void handlePlaceOrder() {
        Cart cart = Cart.getInstance();

        if (cart.isEmpty()) {
            showError("Your cart is empty. Add items before placing an order.");
            return;
        }

        int userId = Session.getInstance().getCurrentUser().getUserId();
        double total = cart.getTotal();

        try (Connection connection = DatabaseConnection.getConnect()) {

            String orderQuery = "INSERT INTO orders(user_id, total, order_placed) VALUES (?, ?, ?) RETURNING order_id";
            PreparedStatement orderStmt = connection.prepareStatement(orderQuery);
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, total);
            orderStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ResultSet rs = orderStmt.executeQuery();

            if (!rs.next()) {
                showError("Failed to place order. Please try again.");
                return;
            }

            int orderId = rs.getInt("order_id");

            String itemQuery = "INSERT INTO order_items(order_id, menu_item_id, order_quantity, order_price) VALUES (?, ?, ?, ?)";
            String stockQuery = "UPDATE menu_items SET menu_quantity = menu_quantity - ? WHERE menu_item_id = ?";

            for (CartItem item : cart.getItems()) {
                PreparedStatement itemStmt = connection.prepareStatement(itemQuery);
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getMenuItemId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getPrice());
                itemStmt.executeUpdate();

                PreparedStatement stockStmt = connection.prepareStatement(stockQuery);
                stockStmt.setInt(1, item.getQuantity());
                stockStmt.setInt(2, item.getMenuItemId());
                stockStmt.executeUpdate();
            }

            showReceipt(orderId, cart, total);

            cart.clear();
           
            SceneManager.removeFromCache("resources/views/menu.fxml");

            SceneManager.switchTo("resources/views/menu.fxml", false);

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
            showError("Failed to place order: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showReceipt(int orderId, Cart cart, double total) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order #").append(orderId).append("\n");
        receipt.append("─────────────────────────\n");

        for (CartItem item : cart.getItems()) {
            receipt.append(item.getName())
                   .append(" x").append(item.getQuantity())
                   .append("  ₦").append(String.format("%,.2f", item.getSubtotal()))
                   .append("\n");
        }

        receipt.append("─────────────────────────\n");
        receipt.append("Total:  ₦").append(String.format("%,.2f", total));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed!");
        alert.setHeaderText("Your order has been placed successfully.");
        alert.setContentText(receipt.toString());
        alert.showAndWait();
    }

    @FXML
    private void handleBack(MouseEvent event) {
        try {
            SceneManager.switchTo("resources/views/menu.fxml", false);
        } catch (IOException ex) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}