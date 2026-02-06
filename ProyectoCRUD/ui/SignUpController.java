/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProyectoCRUD.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import ProyectoCRUD.model.Customer;
import ProyectoCRUD.logic.CustomerRESTClient;

/**
 * Controlador para la vista de registro de usuarios (Sign Up). Maneja la lógica
 * de la interfaz, la validación de entradas y los eventos de los componentes
 * FXML.
 *
 * @author juan
 */
public class SignUpController {

    /**
     * FXML fields
     */
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfStreet;
    @FXML
    private TextField tfMiddleName;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfState;
    @FXML
    private TextField tfZip;

    //FXML passwordFields
    @FXML
    private PasswordField pfPass;
    @FXML
    private PasswordField pfConfirmPass;

    //FXML Hyperlink
    @FXML
    private Hyperlink hyperLink;

    //FXML Buttons
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnBack;

    //FXML Labels
    @FXML
    private Label emailMessage;
    @FXML
    private Label nameMessage;
    @FXML
    private Label lastNameMessage;
    @FXML
    private Label midNameMessage;
    @FXML
    private Label streetMessage;
    @FXML
    private Label stateMessage;
    @FXML
    private Label zipMessage;
    @FXML
    private Label phoneMessage;
    @FXML
    private Label cityMessage;
    @FXML
    private Label passwordMessage;
    @FXML
    private Label confirmPassMessage;
    private Stage stage;

    private static final Logger LOGGER = Logger.getLogger("proyectosignin.ui");

    private static final String REGEX_NAME = "^[A-Za-z ]+$";
    private static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String REGEX_PASSWORD = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[\\W_])(?=.{8,}).*$";

    /**
     * Inicializa la ventana (Stage) principal de la aplicación. Configura la
     * escena, aplica la hoja de estilos, establece el título y asocia todos los
     * manejadores de eventos (listeners) a los controles de la UI.
     *
     * @param stage
     * @param root
     */
    public void initStage(Stage stage, Parent root) {
        try {

            LOGGER.info("Initializing window");
            this.stage = stage;
            Scene scene = new Scene(root);
            //Asigno la hoja de estilos
            scene.getStylesheets().add(getClass().getResource("styleSheet.css").toExternalForm());
            stage.setScene(scene);
            //Deshabilitar el botón de Sign Up hasta que todos los campos estén rellenos 
            btnSignUp.setDisable(true);

            //Establecer el título de la ventana a “Sign up”
            stage.setTitle("Sign Up");

            //La ventana no debe ser redimensionable
            stage.setResizable(false);

            // Limpiar todos los campos de texto. 
            fieldsList().clear();

            //Ajustar el foco al primer texto, email
            tfEmail.requestFocus();

            //Asociacion de manejadores a properties
            tfEmail.focusedProperty()
                    .addListener(this::handlerEmailfocusChanged);
            tfFirstName.focusedProperty()
                    .addListener(this::handlerNameFocusChanged);
            tfMiddleName.focusedProperty()
                    .addListener(this::handlerMidNameFocusChanged);
            tfLastName.focusedProperty()
                    .addListener(this::handlerLastNameFocusChanged);
            tfPhone.focusedProperty()
                    .addListener(this::handlerPhoneFocusChanged);
            tfZip.focusedProperty()
                    .addListener(this::handlerZipFocusChanged);
            tfCity.focusedProperty()
                    .addListener(this::handlerCityFocusChanged);
            tfState.focusedProperty()
                    .addListener(this::handlerStateFocusChanged);

            tfStreet.focusedProperty()
                    .addListener(this::handlerStreetFocusChanged);

            pfPass.focusedProperty()
                    .addListener(this::handlerPasswordFocusChanged);
            pfConfirmPass.textProperty().addListener(this::handlerConfirmPassChanged);

            //Asociar manjeadores a eventos
            btnBack.setOnAction(this::handleExitOnAction);
            btnSignUp.setOnAction(this::handleOnSignUpAction);
            hyperLink.setOnAction(this::handleOnClickLink);

            //Mostrar la ventana 
            stage.show();
            //Centrar la ventana en la pantalla
            //stage.centerOnScreen();

        } catch (Exception e) {
            String erroMsg = "Error opening window:\n" + e.getMessage();

            LOGGER.log(Level.SEVERE, erroMsg);
        }

    }

    /**
     * Maneja el evento de clic en el hipervínculo "Sign In".
     *
     * @param event
     */
    private void handleOnClickLink(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit, all changes will delete");
            Optional<ButtonType> btnType = alert.showAndWait();
            if (btnType.isPresent() && btnType.get() == ButtonType.OK) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInFX.fxml"));
                Parent root = loader.load();
                SignInController controller = loader.getController();
                controller.initStage(this.stage, root);
                LOGGER.info("volviendo a la pagina de Sign In");
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * Valida el campo Street cuando pierde el foco (on-blur). Comprueba que el
     * campo no esté vacío. Llama a checkFields() para actualizar el estado del
     * botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerStreetFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {
        try {
            if (!newValue) {
                String streer = tfStreet.getText().trim();
                if (streer.isEmpty()) {
                    throw new Exception("This field must be informed");
                }
                showCheckLabel(streetMessage, tfStreet);
            }
        } catch (Exception e) {
            showErrorLabel(e.getMessage(), streetMessage, tfStreet);
        } finally {
            checkFields();
        }
    }

    /**
     * Valida el campo "Confirmar Contraseña" *mientras se escribe*
     * (on-text-change). Comprueba que no esté vacío y que coincida con el campo
     * de contraseña. Llama a checkFields() para actualizar el estado del botón
     * de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerConfirmPassChanged(ObservableValue observable, Object oldValue, Object newValue) {

        try {
            String confirmPass = pfConfirmPass.getText();
            String thisPass = pfPass.getText();

            // Validación si no esta  vacio
            if (confirmPass.isEmpty()) {
                throw new Exception("The field must be informed");
            }

            // Valida si las contraseñas son la mismas
            if (!thisPass.equals(confirmPass)) {
                throw new Exception("The password doesn't match");
            }

            // Si es válido mostrar label de confirmacion
            showCheckLabel(confirmPassMessage, pfConfirmPass);

        } catch (Exception e) {
            // Si falla mostra label de error
            showErrorLabel(e.getMessage(), confirmPassMessage, pfConfirmPass);
        } finally {

            //Siempre checkear los campos para habilitar el boton o no
            checkFields();
        }

    }

    /**
     * Valida el campo Password cuando pierde el foco (on-blur). Comprueba que
     * no esté vacío, que cumpla la longitud y el formato REGEX. Llama a
     * checkFields() para actualizar el estado del botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerPasswordFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            if (!newValue) {
                String password = pfPass.getText();

                //Validar contraseña
                if (password.isEmpty()) {
                    throw new Exception("The field must be informed");
                }
                if (!password.matches(REGEX_PASSWORD)) {
                    LOGGER.info("Contraseña no valida");
                    throw new Exception("Password does not meet requirements");
                }
                if (password.length() < 8) {
                    throw new IllegalArgumentException("The password is too short");
                }
                showCheckLabel(passwordMessage, pfPass);
            }
        } catch (Exception e) {
            LOGGER.info("Error in password");
            showErrorLabel(e.getMessage(), passwordMessage, pfPass);
        } finally {
            checkFields();
        }
    }

    /**
     * Valida el campo City cuando pierde el foco (on-blur). Comprueba que el
     * campo no esté vacío. Llama a checkFields() para actualizar el estado del
     * botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerCityFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            if (!newValue) {
                String city = tfCity.getText().trim().toUpperCase();
                if (city.isEmpty()) {
                    throw new IllegalStateException("The field must be informed");
                }
                showCheckLabel(cityMessage, tfCity);
            }
        } catch (IllegalStateException e) {
            showErrorLabel(e.getMessage(), cityMessage, tfCity);
        } finally {
            checkFields();
        }

    }

    /**
     * Valida el campo State cuando pierde el foco (on-blur). Comprueba que el
     * campo no esté vacío. Llama a checkFields() para actualizar el estado del
     * botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerStateFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {
        try {
            String state = tfState.getText().trim().toUpperCase();
            if (!newValue) {
                if (state.isEmpty()) {
                    throw new Exception("The field must be informed");
                }
                showCheckLabel(stateMessage, tfState);
            }

        } catch (Exception e) {
            showErrorLabel(e.getMessage(), stateMessage, tfState);
        } finally {
            checkFields();
        }
    }

    /**
     * Valida el campo Zip Code cuando pierde el foco (on-blur). Comprueba que
     * no esté vacío y que tenga la longitud de 6 dígitos. Llama a checkFields()
     * para actualizar el estado del botón de registro.
     *
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerZipFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            if (!newValue) {
                String zip = tfZip
                        .getText()
                        .trim();
                if (zip.isEmpty()) {
                    throw new Exception("This field must be informed");
                }
                if (zip.length() != 6) {
                    throw new IllegalArgumentException("Zip length must be 6 digits");
                }
                showCheckLabel(zipMessage, tfZip);
            }
        } catch (Exception num) {
            showErrorLabel(num.getMessage(), zipMessage, tfZip);
        } finally {
            checkFields();
        }

    }

    /**
     * Valida el campo Phone cuando pierde el foco (on-blur). Comprueba que no
     * esté vacío y que tenga la longitud de 9 dígitos. Llama a checkFields()
     * para actualizar el estado del botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerPhoneFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            if (!newValue) {
                String phone = tfPhone
                        .getText()
                        .trim();

                if (phone.isEmpty()) {
                    throw new Exception("This field must be informed");
                }
                if (phone.length() != 9) {
                    throw new Exception("Phone length must be 9 digits");
                }

                showCheckLabel(phoneMessage, tfPhone);
            }

        } catch (Exception num) {
            showErrorLabel(num.getMessage(), phoneMessage, tfPhone);
        } finally {
            checkFields();
        }

    }

    /**
     * Valida el campo Middle Name cuando pierde el foco (on-blur). Comprueba
     * que no esté vacío, que sea una sola letra y que cumpla el REGEX. Llama a
     * checkFields() para actualizar el estado del botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerMidNameFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            String midName = tfMiddleName.getText().toUpperCase().trim();
            if (!newValue) {
                if (midName.isEmpty()) {
                    throw new Exception("This field must be informed");
                }
                if (midName.length() != 1) {
                    throw new Exception("The mid name must be 1 letter");
                }
                if (!midName.matches(REGEX_NAME)) {
                    throw new Exception("Format mid name invalid");
                }
                showCheckLabel(midNameMessage, tfMiddleName);
            }
        } catch (Exception e) {
            showErrorLabel(e.getMessage(), midNameMessage, tfMiddleName);
        } finally {
            checkFields();
        }

    }

    /**
     * Valida el campo Last Name cuando pierde el foco (on-blur). Comprueba que
     * no esté vacío y que cumpla el formato REGEX (solo letras). Llama a
     * checkFields() para actualizar el estado del botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerLastNameFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            if (!newValue) {
                String lastName = tfLastName.getText().toUpperCase().trim();
                if (lastName.isEmpty()) {
                    throw new Exception("This field must be informed");
                }
                if (!lastName.matches(REGEX_NAME)) {
                    throw new Exception("Format last name invalid");
                }
                showCheckLabel(lastNameMessage, tfLastName);
            }
        } catch (Exception e) {
            showErrorLabel(e.getMessage(), lastNameMessage, tfLastName);
        } finally {
            checkFields();
        }

    }

    /**
     * Valida el campo First Name cuando pierde el foco (on-blur). Comprueba que
     * no esté vacío y que cumpla el formato REGEX (solo letras). Llama a
     * checkFields() para actualizar el estado del botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerNameFocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            if (!newValue) {
                String fName = tfFirstName.getText().toUpperCase().trim();
                if (fName.isEmpty()) {
                    throw new Exception("This field must be informed");
                }
                if (!fName.matches(REGEX_NAME)) {
                    throw new Exception("Format name invalid");
                }
                showCheckLabel(nameMessage, tfFirstName);
            }
        } catch (Exception name) {
            showErrorLabel(name.getMessage(), nameMessage, tfFirstName);
        } finally {
            checkFields();
        }
    }

    /**
     * Valida el campo Email cuando pierde el foco (on-blur). Comprueba que no
     * esté vacío, que cumpla la longitud y el formato REGEX. Llama a
     * checkFields() para actualizar el estado del botón de registro.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handlerEmailfocusChanged(ObservableValue observable, Boolean oldValue, Boolean newValue) {

        try {
            if (!newValue) {
                String emailText = tfEmail.getText().trim();
                //Validar que el campo no este vacio 
                if (emailText.isEmpty()) {
                    throw new Exception("This field must be informed");
                }

                //Validar que cumple el límite de caracteres posibles para el campo 
                if (emailText.length() > 255) {
                    throw new Exception("The email exceeds the maximum length");
                }
                //Validar que tenga el formato correcto 
                if (!emailText.matches(REGEX_EMAIL)) {
                    throw new Exception("Invalid email format");
                }
                //Si el campo es válido, mostrar etiqueta asociada al campo con mensaje de validación correcta  
                showCheckLabel(emailMessage, tfEmail);
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            showErrorLabel(e.getMessage(), emailMessage, tfEmail);

        } finally {
            checkFields();
        }

    }

    /**
     * Maneja el evento de clic del botón "Sign Up" (Registrarse). Este método
     * solo se ejecuta si el botón está habilitado (formulario válido). Muestra
     * una alerta de confirmación antes de proceder con el registro.
     *
     * @param event
     */
    private void handleOnSignUpAction(ActionEvent event) {
        CustomerRESTClient client = null;
        try {

            //Crear objeto customer
            Customer customer = new Customer(
                    tfFirstName.getText(),
                    tfLastName.getText(),
                    tfMiddleName.getText(),
                    tfStreet.getText(),
                    tfCity.getText(),
                    tfState.getText(),
                    tfZip.getText(),
                    tfPhone.getText(),
                    tfEmail.getText(),
                    pfConfirmPass.getText()
            );

            //Insertar customer en BD
            client = new CustomerRESTClient();
            client.create_XML(customer);
            //Indicar al usuario que se ha registrado correctamente

            LOGGER.info("USER registered");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("User register");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInFX.fxml"));
            Parent root = loader.load();
            SignInController controller = loader.getController();
            controller.initStage(this.stage, root);

        } catch (InternalServerErrorException e) {
            LOGGER.info("Error server" + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Server error, try later");
            alert.showAndWait();
        } catch (ForbiddenException forbiddenException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Email already exist");
            alert.showAndWait();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unknown error");
            alert.showAndWait();

        } finally {
            client.close();
        }
    }

    /**
     * Método de utilidad para mostrar un mensaje de error de validación. Pone
     * el texto de la etiqueta y el borde del campo en color rojo.
     *
     * @param event Metodo manejador del boton de back
     */
    private void handleExitOnAction(ActionEvent event) {

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");

            Optional<ButtonType> btnType = alert.showAndWait();

            if (btnType.isPresent() && btnType.get() == ButtonType.OK) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInFX.fxml"));
                Parent root = loader.load();
                SignInController controller = loader.getController();
                controller.initStage(this.stage, root);
                LOGGER.info("Volviendo a la pagina de inicio");
            }

        } catch (Exception e) {
            LOGGER.warning("Se ha producido un error" + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage() + "\nTry again");

        }
    }

    /**
     * Método de utilidad para mostrar un mensaje de error de validación. Pone
     * el texto de la etiqueta y el borde del campo en color rojo.
     *
     * @param erroMsg
     * @param label
     * @param textField Metodo para mostrar label de campo invalido o con error
     */
    protected void showErrorLabel(String erroMsg, Label label, TextField textField) {

        label.setVisible(true);
        label.setText(erroMsg);
        label.setStyle("-fx-text-fill: red;");
        textField.setStyle("-fx-border-color: red");

    }

    /**
     * Método de utilidad para mostrar un mensaje de éxito de validación.
     * Muestra un "check" (✔) y pone el texto y el borde del campo en verde.
     *
     * @param label
     * @param textField Metodo para mostrar label de campo valido
     */
    protected void showCheckLabel(Label label, TextField textField) {
        label.setVisible(true);
        label.setText("✔");
        label.setStyle("-fx-text-fill: green;");
        textField.setStyle("-fx-border-color: green");
    }

    /**
     *
     * @return Este metodo devuelve una lista de todos los TextFields para
     * comodidad en su acceso
     */
    private List<TextField> fieldsList() {
        //Listado de todos textFields 
        List<TextField> fields = new ArrayList<>();

        fields.add(tfZip);
        fields.add(tfEmail);
        fields.add(tfCity);
        fields.add(tfFirstName);
        fields.add(tfLastName);
        fields.add(tfMiddleName);
        fields.add(tfPhone);
        fields.add(tfState);
        fields.add(tfStreet);
        fields.add(pfConfirmPass);
        fields.add(pfPass);

        return fields;

    }

    /**
     * Método central de control del botón de registro. Itera sobre todos los
     * campos del formulario (de fieldsList()). Deshabilita el botón si
     * CUALQUIER campo está vacío o tiene un borde rojo (inválido). Habilita el
     * botón solo si TODOS los campos están llenos y son válidos.
     */
    private void checkFields() {

        for (TextField tf : fieldsList()) {
            if (tf.getText().trim().isEmpty()) {
                btnSignUp.setDisable(true);
                return;
            }

            if (tf.getStyle() != null && tf.getStyle().contains("-fx-border-color: red")) {
                btnSignUp.setDisable(true);
                return;
            }
        }
        btnSignUp.setDisable(false);

    }

}
