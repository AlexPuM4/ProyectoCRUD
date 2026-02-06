package ProyectoCRUD.ui;

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
import javafx.scene.control.Label;


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
    Stage stageAbout = new Stage();
    stageAbout.setTitle("About");
    Label label = new Label("Aplicacion creada por Alex");
    StackPane root = new StackPane();
    root.getChildren().add(label); 
    Scene scene = new Scene(root, 400, 100);
    stageAbout.setScene(scene);
    stageAbout.show();
}
    private void handleHelp() {
    LOGGER.info("Acción Help disparada");
    try {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        java.net.URL url = getClass().getResource("/ui/help.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
            Stage stageHelp = new Stage();
            stageHelp.setTitle("Ayuda del Sistema");
            StackPane root = new StackPane(webView);
            Scene scene = new Scene(root, 800, 600);
            stageHelp.setScene(scene);
            stageHelp.show();
        } else {
            LOGGER.severe("ERROR: No se encontró el archivo help.html en la ruta /ui/");
        }
    } catch (Exception e) {
        LOGGER.severe("Error al abrir la ayuda con WebView: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void handleLogOut() {
    LOGGER.info("Acción LogOut disparada");
    if (menuBar != null && menuBar.getScene() != null) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }
}
}