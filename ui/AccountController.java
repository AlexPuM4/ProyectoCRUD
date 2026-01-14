/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import logic.CustomerRESTClient;
import model.Customer;
import model.Account;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.ws.rs.core.GenericType;
import logic.AccountRESTClient;
import model.AccountType;



/**
 *
 * @author lossi
 */
public class AccountController {
    @FXML
    private Button ContinueBt;
    @FXML
    private Button ExitBt;
    @FXML
    private TableView<Account> tablatv;
    @FXML
    private TableColumn<Account , Long> idClmn;
    @FXML
    private TableColumn<Account , AccountType> AccountTypeClmn;
    @FXML
    private TableColumn<Account , String> DescriptionClmn;
    @FXML
    private TableColumn<Account , Double> BalanceClmn;
    @FXML
    private TableColumn<Account , Double> CreditLineClmn;
    @FXML
    private TableColumn<Account , Double> BeginBalanceClmn;
    @FXML
    private TableColumn<Account , Date> DateClmn;
    private static final Logger LOGGER = Logger.getLogger("proyectosignin1.ui");
    private Stage stage;
    private Customer customer;
    public void initStage (){
    LOGGER.info("Initializing account window");
    try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountFX.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
          
    idClmn.setCellValueFactory(new PropertyValueFactory<>("id"));
    AccountTypeClmn.setCellValueFactory(new PropertyValueFactory<>("type"));
    DescriptionClmn.setCellValueFactory(new PropertyValueFactory<>("description"));
    BalanceClmn.setCellValueFactory(new PropertyValueFactory<>("balance"));
    CreditLineClmn.setCellValueFactory(new PropertyValueFactory<>("creditLine"));
    BeginBalanceClmn.setCellValueFactory(new PropertyValueFactory<>("beginBalance"));
    DateClmn.setCellValueFactory(new PropertyValueFactory<>("beginBalanceTimestamp"));
    this.stage = new Stage();  
    stage.setTitle("My Accounts");
    stage.setScene(scene);
    stage.setResizable(false);
    ContinueBt.setDisable(true);
    ExitBt.setOnAction(this::handleBtExitOnAction);
    stage.show();
    } catch (IOException e) {
    e.printStackTrace();
    }
    }
    public void setCustomer(Customer customer) {
            this.customer = customer;
    }
    private void handleBtExitOnAction(ActionEvent event) {
        try {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Estas seguro que quieres salir?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage.close();
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }
}