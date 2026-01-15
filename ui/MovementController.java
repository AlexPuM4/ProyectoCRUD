/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

/**
 *
 * @author puchol
 */
public class MovementController {
@FXML
    private TextField idAccount;
@FXML
    private TableColumn date;
@FXML
    private TableColumn type;
@FXML
    private TableColumn amount;
@FXML
    private TableColumn balance;
@FXML
    private ComboBox comboBox;
@FXML
    private TextField amountFormulario;
@FXML
    private Button buttonCreateMovement;
@FXML
    private Button buttonUndoLastMovement;
@FXML    
    private Button buttonCancel;
    
    private static final Logger LOGGER = Logger.getLogger("ui/MovementController.ui");
    private Customer customer;
    
    public void setCustomer(Customer customer) {
            this.customer = customer;
    }
    
    public void init(Stage stage, Parent root) {
        LOGGER.info("Initializing Movement window");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styleSheet.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Movement");
        stage.setResizable(false);

        //applyExit.setDisable(true);
        // Asociar eventos a botones
        buttonCreateMovement.setOnAction(this::handleButtonCreateMovementOnAction);
        buttonUndoLastMovement.setOnAction(this::handleButtonUndoLastMovementOnAction);
        buttonCancel.setOnAction(this::handleButtonCancelOnAction);


        // Asociar validaciones a los campos

        stage.show();
    }
     //private void handleButtonCreateMovementOnAction(ObservableValue observable, String oldValue, String newValue) {
     //}
     private void handleButtonCreateMovementOnAction(ActionEvent event) {
     }
     private void handleButtonUndoLastMovementOnAction(ActionEvent event) {
     }
     private void handleButtonCancelOnAction(ActionEvent event) {
     }
     

}
