package signin;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ui.MovementController;
import ui.SignInController;

/**
 *
 * @author alex
 */
public class ProyectoSignInApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("ui/Movement.fxml"));
        Parent root = (Parent)loader.load();
        
        MovementController controller = loader.getController();
        
        controller.init(stage,root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
