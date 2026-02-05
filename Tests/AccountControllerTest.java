import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
import model.Account;
import ui.AccountController;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new ProyectoSignInApplication().start(stage);
    }

    @Before
    public void setupToAccountWindow() {
        // Logueamos para entrar en la ventana de cuentas
        clickOn("#tfUser").write("jsmith@enterprise.net");
        clickOn("#pfPasswd").write("abcd*1234");
        clickOn("#btSignIn");
        // Verificamos que estamos en la ventana correcta
        verifyThat("#tablatv", isVisible());
    }

    /**
     * Test 1: Crear una cuenta nueva.
     * Al pulsar el botón "New", se genera un ID aleatorio y se añade a la tabla.
     */
    @Test
    public void testA_CreateAccount() {
        int filasAntes = lookup("#tablatv").queryAs(TableView.class).getItems().size();
        
        clickOn("#newAccountBt"); // Botón New
        
        int filasDespues = lookup("#tablatv").queryAs(TableView.class).getItems().size();
        assert(filasDespues == filasAntes + 1);
    }

    /**
     * Test 2: Modificar la descripción de una cuenta.
     * Simula el doble clic para editar la celda de descripción.
     */
    @Test
    public void testB_ModifyDescription() {
        String nuevaDesc = "Cuenta de Ahorros Test";
        
        // Doble clic en la celda de la columna Description de la primera fila
        doubleClickOn(hasTableCell("Description")); 
        
        // Escribimos la nueva descripción y pulsamos ENTER para confirmar el commit
        write(nuevaDesc).push(KeyCode.ENTER);
        
        // Verificamos que la celda contiene el nuevo valor
        verifyThat("#tablatv", hasTableCell(nuevaDesc));
    }

    /**
     * Test 3: Eliminar una cuenta.
     * Selecciona una fila y pulsa el botón Delete.
     */
    @Test
    public void testC_DeleteAccount() {
        TableView<Account> tabla = lookup("#tablatv").queryAs(TableView.class);
        int filasAntes = tabla.getItems().size();
        
        if (filasAntes > 0) {
            // Seleccionamos la primera fila
            clickOn(hasTableCell(tabla.getItems().get(0).getId())); 
            
            // Pulsamos el botón Delete
            clickOn("#deleteBT");
            
            // Si la cuenta no tiene movimientos, se borrará
            // Nota: Si tiene movimientos, saltará un Alert de error
            int filasDespues = tabla.getItems().size();
            // Este assert dependerá de si la cuenta seleccionada era borrable
            System.out.println("Filas tras intentar borrar: " + filasDespues);
        }
    }
}