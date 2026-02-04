package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.net.URI;

public class MenuController {
    @FXML private Menu AboutMenu;
    @FXML private Menu HelpMenu;
    @FXML private Menu LogOutMenu;
    public void initialize() {
        AboutMenu.setOnShowing(event -> handleAbout());
        HelpMenu.setOnShowing(event -> handleHelp());
        LogOutMenu.setOnShowing(event -> handleLogOut());
    }
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Aplicación creada por Alex.");
        alert.showAndWait();
    }

    private void handleHelp() {
    try {
        java.net.URL url = getClass().getResource("/ui/help.html");
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    private void handleLogOut() {
        Stage stage = (Stage) AboutMenu.getParentPopup().getOwnerWindow();
        if (stage != null) {
            stage.close();
        }
    }
}