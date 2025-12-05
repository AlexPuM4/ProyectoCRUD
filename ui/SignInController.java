package ui;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import logic.CustomerRESTClient;
import model.Customer;

/**
 * Controller class for user management windows
 *
 * @author Alex
 */
public class SignInController {

    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField pfPasswd;
    @FXML
    private Button btSignIn;
    @FXML
    private Button btExit;
    @FXML
    private Hyperlink hlRegister;
    @FXML
    private Label lbErrorEmail;
    @FXML
    private Label lbErrorSignIn;
    private static final Logger LOGGER = Logger.getLogger("proyectosignin1.ui");

    private Stage stage;

    public void initStage(Stage stage, Parent root) {
        LOGGER.info("Initializing windows");
        this.stage = stage;
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styleSheet.css").toExternalForm());
        stage.setScene(scene);
        stage.setScene(scene);
        //Establecer el titulo de la ventana al valor "Sign In".
        stage.setTitle("Sign In");
        //La ventana no debe ser redimensionable.
        stage.setResizable(false);
        //Deshabilitar temporalmente el boton Sign In hasta que ambos campos contengan texto.
        btSignIn.setDisable(true);
        //Asociar eventos a manejadores
        btSignIn.setOnAction(this::handleBtSignInOnAction);
        hlRegister.setOnAction(this::handleHlRegisterOnAction);
        tfUser.textProperty().addListener(this::handleTfUserTextChange);
        pfPasswd.textProperty().addListener(this::handlePfPasswdTextChange);
        //Controlar cierre de ventana
        btExit.setOnAction(this::handleBtExitOnAction);
        //Mostrar la ventana.
        stage.show();
        //Se enfoca en el campo Email
        tfUser.requestFocus();
        //Oculta los labels de error hasta que se hagan uso
        lbErrorSignIn.setVisible(false);
        lbErrorEmail.setVisible(false);
    }

    private void handleBtSignInOnAction(ActionEvent event) {
        try {
            String email = tfUser.getText().trim();
            String passwd = pfPasswd.getText().trim();
            CustomerRESTClient client = new CustomerRESTClient();
            Customer customer = client.findCustomerByEmailPassword_XML(Customer.class, email, passwd);
            client.close();
            LOGGER.info("User authenticated: " + customer.getEmail());
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePassword.fxml"));
            Parent root = loader.load();
            ChangePasswordController controller = loader.getController();
            controller.setCustomer(customer);
            controller.init(stage, root);

        } catch (NotAuthorizedException e) {
            lbErrorSignIn.setVisible(true);
        } catch (InternalServerErrorException e) {
            Alert alert = new Alert(AlertType.ERROR, "Error server down, try later: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     *
     * @param event
     */
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

    /**
     *
     * @param event
     */
    private void handleHlRegisterOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUP_View.fxml"));
            Parent root = loader.load();
            SignUpController controller = loader.getController();
            controller.initStage(this.stage, root);

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    private void handleTfUserTextChange(ObservableValue observable, String oldValue, String newValue) {
        try {

            String regex_email = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            String email = tfUser.getText().trim();
            // Validar formato de email
            if (!newValue.isEmpty() && !email.matches(regex_email)) {
                lbErrorEmail.setVisible(true);
            } else {
                lbErrorEmail.setVisible(false);
            }
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlePfPasswdTextChange(ObservableValue observable, Object oldValue, Object newValue) {
        try {
            if (tfUser.getText().isEmpty() && pfPasswd.getText().isEmpty()) {
                throw new Exception();
            } else {
                btSignIn.setDisable(false);
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

}
