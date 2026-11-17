/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeteriaapp.resources.controllers;

import cafeteriaapp.Session;
import cafeteriaapp.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author DELL
 */
public class WalletController implements Initializable {

    @FXML
    private Label balanceLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = Session.getInstance().getCurrentUser();

        if (user != null) {
            balanceLabel.setText("₦" + String.format("%,.2f", user.getAccountBalance()));
        }
    }
}