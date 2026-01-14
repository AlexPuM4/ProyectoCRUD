/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;

/**
 *
 * @author puchol
 */
public class MovementController {
@FXML
    private TableColumn date;
@FXML
    private TableColumn type;
@FXML
    private TableColumn amount;
@FXML
    private TableColumn balance;
@FXML
    private DatePicker filterDateFrom;
@FXML
    private DatePicker filterDateUntil;
@FXML
    private SplitMenuButton selectAccount;
@FXML    
    private Button buttonCancel;
}
