/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeteriaapp.resources.controllers;

import cafeteriaapp.DatabaseConnection;
import cafeteriaapp.Session;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author DELL
 */
public class OrderHistoryController implements Initializable {

    @FXML
    private VBox orderListContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        orderListContainer.getChildren().clear();

        int userId = Session.getInstance().getCurrentUser().getUserId();

        String orderQuery = "SELECT order_id, total, order_placed FROM orders WHERE user_id = ? ORDER BY order_placed DESC";

        String itemsQuery = "SELECT oi.order_quantity, oi.order_price, mi.name " +
                            "FROM order_items oi " +
                            "JOIN menu_items mi ON oi.menu_item_id = mi.menu_item_id " +
                            "WHERE oi.order_id = ?";

        try (Connection connection = DatabaseConnection.getConnect();
             PreparedStatement orderStmt = connection.prepareStatement(orderQuery)) {

            orderStmt.setInt(1, userId);
            ResultSet orderRs = orderStmt.executeQuery();

            boolean hasOrders = false;

            while (orderRs.next()) {
                hasOrders = true;

                int orderId = orderRs.getInt("order_id");
                double total = orderRs.getDouble("total");
                String datePlaced = orderRs.getTimestamp("order_placed").toString().substring(0, 16);

                VBox orderCard = new VBox(8);
                orderCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 16;");

                HBox orderHeader = new HBox();
                orderHeader.setAlignment(Pos.CENTER_LEFT);

                Label orderIdLbl = new Label("Order #" + orderId);
                orderIdLbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label dateLbl = new Label(datePlaced);
                dateLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

                orderHeader.getChildren().addAll(orderIdLbl, spacer, dateLbl);

                Region divider = new Region();
                divider.setStyle("-fx-background-color: #e2e8f0;");
                divider.setPrefHeight(1);
                divider.setMaxWidth(Double.MAX_VALUE);

                orderCard.getChildren().addAll(orderHeader, divider);

                PreparedStatement itemsStmt = connection.prepareStatement(itemsQuery);
                itemsStmt.setInt(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();

                while (itemsRs.next()) {
                    String itemName = itemsRs.getString("name");
                    int itemQty = itemsRs.getInt("order_quantity");
                    double itemPrice = itemsRs.getDouble("order_price");
                    double itemSubtotal = itemQty * itemPrice;

                    HBox itemRow = new HBox();
                    itemRow.setAlignment(Pos.CENTER_LEFT);

                    Label itemNameLbl = new Label(itemName + " x" + itemQty);
                    itemNameLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #374151;");

                    Region itemSpacer = new Region();
                    HBox.setHgrow(itemSpacer, Priority.ALWAYS);

                    Label itemSubtotalLbl = new Label("₦" + String.format("%,.2f", itemSubtotal));
                    itemSubtotalLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #374151;");

                    itemRow.getChildren().addAll(itemNameLbl, itemSpacer, itemSubtotalLbl);
                    orderCard.getChildren().add(itemRow);
                }

                Region totalDivider = new Region();
                totalDivider.setStyle("-fx-background-color: #e2e8f0;");
                totalDivider.setPrefHeight(1);
                totalDivider.setMaxWidth(Double.MAX_VALUE);

                HBox totalRow = new HBox();
                totalRow.setAlignment(Pos.CENTER_LEFT);

                Label totalTextLbl = new Label("Total");
                totalTextLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");

                Region totalSpacer = new Region();
                HBox.setHgrow(totalSpacer, Priority.ALWAYS);

                Label totalAmountLbl = new Label("₦" + String.format("%,.2f", total));
                totalAmountLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a1a2e;");

                totalRow.getChildren().addAll(totalTextLbl, totalSpacer, totalAmountLbl);
                orderCard.getChildren().addAll(totalDivider, totalRow);

                orderListContainer.getChildren().add(orderCard);
            }

            if (!hasOrders) {
                Label empty = new Label("You haven't placed any orders yet.");
                empty.setStyle("-fx-text-fill: #999999; -fx-font-size: 14px; -fx-padding: 40 0 0 0;");
                empty.setMaxWidth(Double.MAX_VALUE);
                empty.setAlignment(Pos.CENTER);
                orderListContainer.getChildren().add(empty);
            }

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
            Logger.getLogger(OrderHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}