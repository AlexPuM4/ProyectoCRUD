/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
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
    private Button newAccountBt;
    @FXML
    private Button deleteBT;
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
    private AccountRESTClient client = new AccountRESTClient();
    public void setCustomer(Customer customer) {
            this.customer = customer;
    }
    public void initStage(Parent root){
    LOGGER.info("Initializing account window");
    try {
    Scene scene = new Scene(root);   
    idClmn.setCellValueFactory(new PropertyValueFactory<>("id"));
    //Establecer factoria de celda y factoria de valor de velda de tipo de cuenta
    AccountTypeClmn.setCellValueFactory(new PropertyValueFactory<>("type"));
    AccountTypeClmn.setCellFactory(ComboBoxTableCell.forTableColumn(AccountType.values()));
    AccountTypeClmn.setOnEditCommit(this::handleTypeChng);
    //Establecer factoria de celda y factoria de valor de celda de descripcion
    DescriptionClmn.setCellValueFactory(new PropertyValueFactory<>("description"));
    DescriptionClmn.setCellFactory(TextFieldTableCell.<Account>forTableColumn());
    DescriptionClmn.setOnEditCommit(this::handleDescrChng);
    //
    BalanceClmn.setCellValueFactory(new PropertyValueFactory<>("balance"));
    //Establecer la factoria de celda y factoria de valor de celda de linea de credita
    CreditLineClmn.setCellValueFactory(new PropertyValueFactory<>("creditLine"));
    CreditLineClmn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    CreditLineClmn.setOnEditCommit(this::handleCreditLineChng);
    //Establecer factoria de celda y factoria de valor de celda de begin Balance
    BeginBalanceClmn.setCellValueFactory(new PropertyValueFactory<>("beginBalance"));
    BeginBalanceClmn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    BeginBalanceClmn.setOnEditCommit(this::handleBgnBlcChng);
    //
    DateClmn.setCellValueFactory(new PropertyValueFactory<>("beginBalanceTimestamp"));
    tablatv.setItems(FXCollections.observableArrayList(client.findAccountsByCustomerId_XML(new GenericType<List<Account>>(){},customer.getId().toString())));
    this.stage = new Stage();  
    stage.setTitle("My Accounts");
    stage.setScene(scene);
    stage.setResizable(false);
    tablatv.setEditable(true);
    ContinueBt.setDisable(true);
    ExitBt.setOnAction(this::handleBtExitOnAction);
    tablatv.getSelectionModel().selectedItemProperty().addListener(this::handleTableSelectionChanged);
    newAccountBt.setOnAction(this::handleBtnwAccountOnAction);
    deleteBT.setOnAction(this::handleBtndlAccountOnAction);
    ContinueBt.setOnAction(this::handleBtContinueOnAction);
    deleteBT.setDisable(true);
    stage.show();
    } catch (Exception e) {
    Alert alert = new Alert(AlertType.ERROR,e.getMessage());
    alert.showAndWait();
    }
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
    private void handleTableSelectionChanged(ObservableValue observable, Account oldValue, Account newValue){
        if (newValue != null) {
        ContinueBt.setDisable(false);
        deleteBT.setDisable(false);
        } else {
        ContinueBt.setDisable(true);
        deleteBT.setDisable(true);
        }
    }
    private void handleBtContinueOnAction(ActionEvent event){
        try{
        Account slAccount = tablatv.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Movement.fxml"));
        Parent root = loader.load();
        MovementController controller = loader.getController();
        controller.setAccount(slAccount);
        Stage movementStage = new Stage();
        controller.init(movementStage,root);
    }catch(Exception e){
        Alert alert = new Alert(AlertType.ERROR,e.getMessage());
        alert.showAndWait();
    }
    }
    private void handleBtnwAccountOnAction(ActionEvent event) {
        try {
        Account nwAccount = new Account();
        long idRandom = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        nwAccount.setId(idRandom);
        nwAccount.setBeginBalanceTimestamp(new Date());
        nwAccount.setType(AccountType.STANDARD);
        nwAccount.setCreditLine(0.0);
        nwAccount.setDescription("");
        Set<Customer> customerr = new HashSet<>();
        customerr.add(customer);
        nwAccount.setCustomers(customerr);
        tablatv.getItems().add(nwAccount);
        tablatv.getSelectionModel().select(nwAccount);
        tablatv.refresh();
        client.createAccount_XML(nwAccount);
        LOGGER.info("Successfully created account");
        } catch(InternalServerErrorException e){
        Alert alert = new Alert(AlertType.ERROR,e.getMessage());
        alert.showAndWait();
        } catch(Exception e){
        Alert alert = new Alert(AlertType.ERROR,e.getMessage());
        alert.showAndWait();
        }
    }
    private void handleBtndlAccountOnAction(ActionEvent event){
        try {
        Account slAccount = tablatv.getSelectionModel().getSelectedItem();
        if(slAccount.getMovements() == null){
        client.removeAccount(slAccount.getId().toString());
        tablatv.getItems().remove(slAccount);
        }else{
        Alert alert = new Alert(AlertType.ERROR,"No se puede borrar la cuenta , contiene movimientos");
        alert.showAndWait();
        }
        } catch(ClientErrorException e){
        Alert alert = new Alert(AlertType.ERROR,e.getMessage());
        alert.showAndWait();
        }
    }
    private void handleCreditLineChng(CellEditEvent<Account , Double> event){
        Account editAccount = event.getRowValue();
        if(editAccount.getType() == AccountType.CREDIT){
        editAccount.setCreditLine(event.getNewValue());
        client.updateAccount_XML(editAccount);
        LOGGER.info("Account Updated");
        }else {
        event.getTableView().refresh();
        Alert alert = new Alert(AlertType.ERROR,"Solo se puede editar la linea de credito si su tipo de cuenta es de credito");
        alert.showAndWait();
        }
    }
    private void handleBgnBlcChng(CellEditEvent<Account , Double> event){
        Account editAccount = event.getRowValue();
        Double nwBeginBalance = event.getNewValue();
        if(editAccount.getBeginBalance() != null){
            event.getTableView().refresh();
            Alert alert = new Alert(AlertType.ERROR,"Balance inicial no es editable,una vez que ha sido ya creada la cuenta");
            alert.showAndWait();
            LOGGER.info("Account Updated");
            return;
        }
        editAccount.setBalance(nwBeginBalance);
        editAccount.setBeginBalance(nwBeginBalance);
        client.updateAccount_XML(editAccount);
    }
    private void handleTypeChng(CellEditEvent<Account , AccountType> event){
        Account editAccount = event.getRowValue();
        editAccount.setType(event.getNewValue());
        tablatv.refresh();
        client.updateAccount_XML(editAccount);
        LOGGER.info("Account Updated");
    }
    private void handleDescrChng(CellEditEvent<Account , String> event){
        Account editAccount = event.getRowValue();
        editAccount.setDescription(event.getNewValue());
        client.updateAccount_XML(editAccount);
        tablatv.refresh();
        LOGGER.info("Account Updated");
    }
    
}