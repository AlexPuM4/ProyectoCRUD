package Test;








import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import javafx.stage.Stage;
import ProyectoCRUD.model.Account;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.FixMethodOrder;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
import javafx.stage.Stage;
import ProyectoCRUD.ProyectoSignInApplication;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 * @fixme Test insuficientes: crear un test que compruebe la creación con éxito de una cuenta de Crédito.Verificar que la Account creada está entre los items de la tabla. 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest extends ApplicationTest{
    //Inicia la ventana Principal de Sign In
    @Override
    public void start(Stage stage) throws Exception {
        new ProyectoSignInApplication().start(stage);
    }
    //Mete las credenciales de una cuenta
    @Before
    public void Login(){
        clickOn("#tfUser");
        write("jsmith@enterprise.net");
        clickOn("#pfPasswd");
        write("abcd*1234");
        clickOn("#btSignIn");
    }
    @Test
    //Modifica la celda de decripcion , cambia el tipo de cuenta , y añade creditLine de 1000
public void test02_Update() {
    Node celdaDesc = lookup("#tablatv .table-cell").nth(1).query();
    doubleClickOn(celdaDesc);
    write("Test:Des:type:CrdLine").push(KeyCode.ENTER);
    Node celdaTipo = lookup("#tablatv .table-cell").nth(6).query();
    doubleClickOn(celdaTipo);
    type(KeyCode.DOWN).type(KeyCode.ENTER);
    Node celdaCreditLine = lookup("#tablatv .table-cell").nth(3).query();
    doubleClickOn(celdaCreditLine);
    write("1000").push(KeyCode.ENTER);
    verifyThat("#tablatv", hasTableCell("Test:Des:type:CrdLine"));
    verifyThat("#tablatv", hasTableCell("1000"));
    verifyThat("#tablatv", hasTableCell("CREDIT"));
}
//Crea una cuenta nueva con la descripcion TestCreateAcc y añade un BeginBalance de 100
    @Test
public void test01_Create() {
    TableView<Account> tabla = lookup("#tablatv").queryAs(TableView.class);
    int filasAntes = tabla.getItems().size();
    clickOn("#newAccountBt");
    int filasDespues = tabla.getItems().size();
    int numColumnas = 7;
    int ultimoRegistroIdx = filasDespues - 1;
    interact(() -> tabla.scrollTo(ultimoRegistroIdx));
    int idxCeldaDesc = (ultimoRegistroIdx * numColumnas) + 1;
    Node celdaDesc = lookup("#tablatv .table-cell").nth(idxCeldaDesc).query();
    doubleClickOn(celdaDesc);
    write("TestCreateAcc").push(KeyCode.ENTER);
    int idxCeldaBalance = (ultimoRegistroIdx * numColumnas) + 4;
    Node celdaBalance = lookup("#tablatv .table-cell").nth(idxCeldaBalance).query();
    doubleClickOn(celdaBalance);
    write("100").push(KeyCode.ENTER);
    assertNotEquals("La fila debería haberse creado", filasAntes, filasDespues);
    verifyThat("#tablatv", hasTableCell("TestCreateAcc"));
    verifyThat("#tablatv", hasTableCell("100.0"));
    //TODO Añadir un aserto que compruebe que la nueva Account 
    //TODO con los datos introducidos está entre los items de la tabla.

}
/**
 * @fixme Test insuficiente:Completar y verificar que el objeto Account eliminado ya NO está entre los items de la tabla. 

 */
@Test
public void test03_Delete() {
    Node celdaDesc = lookup("#tablatv .table-cell").nth(1).query();
    clickOn(celdaDesc);
    clickOn("#deleteBT");
}
}


