/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeteriaapp.resources.controllers;
import cafeteriaapp.SceneManager;
import cafeteriaapp.Session;
import cafeteriaapp.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
/**
 *
 * @author DELL
 */
public class ProfileController implements Initializable {
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label emailLabel;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = Session.getInstance().getCurrentUser();
        if (user != null) {
            fullNameLabel.setText(user.getFirstName() + " " + user.getLastName());
            emailLabel.setText(user.getEmail());
        }
    }

    @FXML
    private void handleViewWallet(MouseEvent event) {
        loadIntoCenterPane("../views/wallet-view.fxml");
    }

    @FXML
    private void handleViewOrderHistory(MouseEvent event) {
        loadIntoCenterPane("../views/order-history.fxml");
    }

    private void loadIntoCenterPane(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            Pane centerPane = (Pane) fullNameLabel.getScene().lookup("#centerPane");

            if (centerPane != null) {
                centerPane.getChildren().setAll(view);
                if (view instanceof javafx.scene.layout.Region region) {
                    region.prefWidthProperty().bind(centerPane.widthProperty());
                    region.prefHeightProperty().bind(centerPane.heightProperty());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleSignOut(javafx.event.ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to sign out?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Sign Out");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                Session.getInstance().setCurrentUser(null);
                SceneManager.removeFromCache("resources/views/dashboard.fxml");
                try {
                    SceneManager.switchTo("resources/views/login.fxml", false);
                } catch (IOException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}