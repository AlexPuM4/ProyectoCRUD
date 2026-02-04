package ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.util.logging.Logger;

public class MenuController {
    @FXML private MenuBar menuBar;
    @FXML private Menu AboutMenu;
    @FXML private Menu HelpMenu;
    @FXML private Menu LogOutMenu;
    private static final Logger LOGGER = Logger.getLogger("proyectosignin1.ui");

    public void initialize() {
        // Usar setOnShowing es lo que permite que detecte el clic directo
        LOGGER.info("Controlador de menu cargado");
        AboutMenu.setOnShowing(event -> handleAbout());
        HelpMenu.setOnShowing(event -> handleHelp());
        LogOutMenu.setOnShowing(event -> handleLogOut());
    }

    private void handleAbout() {
    LOGGER.info("Acción About disparada");
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("About");
    alert.setHeaderText(null);
    alert.setContentText("Aplicación realizada por Alex.");
    alert.showAndWait();
}

private void handleHelp() {
    LOGGER.info("Acción Help disparada");
    try {
       WebView webView = new WebView();
       webView.getEngine().load(getClass().getResource("/home/alex/ProyectoCRUD/ui/help.html").toExternalForm());
       Stage AccountHelp = new Stage();
       AccountHelp.setTitle("Help");
       AccountHelp.setScene(new Scene(new StackPane(webView),800,600));
       AccountHelp.show();
    } catch (Exception e) {
        LOGGER.info("Error al abrir la ayuda: " + e.getMessage());
    }
}

private void handleLogOut() {
    LOGGER.info("Acción LogOut disparada");
    // Obtenemos la ventana a través de la barra de menú
    if (menuBar != null && menuBar.getScene() != null) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }
}
}