package ProyectoCRUD.Test;








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
import ProyectoCRUD.model.AccountType;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
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
public void test03_Update() {
    Node celdaDesc = lookup("#tablatv .table-cell").nth(1).query();
    doubleClickOn(celdaDesc);
    write("Test:Des:type:CrdLine").push(KeyCode.ENTER);
    Node celdaTipo = lookup("#tablatv .table-cell").nth(6).query();
    doubleClickOn(celdaTipo);
    type(KeyCode.DOWN).type(KeyCode.ENTER);
    sleep(500); 
    Node celdaCreditLine = lookup("#tablatv .table-cell").nth(3).query();
    doubleClickOn(celdaCreditLine);
    write("1000").push(KeyCode.ENTER);
    sleep(1000); 
    verifyThat("#tablatv", hasTableCell("Test:Des:type:CrdLine"));
    verifyThat("#tablatv", hasTableCell("1000.0")); 
    verifyThat("#tablatv", hasTableCell("CREDIT"));
}
    @Test
public void test01_CreateStandartAccount() {
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
    write("TestStandartAcc").push(KeyCode.ENTER);
    int idxCeldaBalance = (ultimoRegistroIdx * numColumnas) + 4;
    Node celdaBalance = lookup("#tablatv .table-cell").nth(idxCeldaBalance).query();
    doubleClickOn(celdaBalance);
    write("100").push(KeyCode.ENTER);
    assertNotEquals("La fila debería haberse creado", filasAntes, filasDespues);
    verifyThat("#tablatv", hasTableCell("TestStandartAcc"));
    verifyThat("#tablatv", hasTableCell("100.0"));
    boolean existeCuenta = tabla.getItems().stream()
            .anyMatch(account -> 
                "TestStandartAcc".equals(account.getDescription()) && 
                account.getBeginBalance() != null && 
                account.getBeginBalance() == 100.0
            );            
    assertTrue("La nueva Account con los datos introducidos debe estar entre los items de la tabla", existeCuenta);
    //TODO Añadir un aserto que compruebe que la nueva Account(Hecho)
    //TODO con los datos introducidos está entre los items de la tabla.(Hecho)
}
@Test
public void test02_CreateCreditAccount() {
    TableView<Account> tabla = lookup("#tablatv").queryAs(TableView.class);
    int filasAntes = tabla.getItems().size();
    clickOn("#newAccountBt");
    sleep(1000); 
    int filasDespues = tabla.getItems().size();
    int numColumnas = 7;
    int ultimoIndice = filasDespues - 1;
    assertNotEquals("La fila debería haberse creado", filasAntes, filasDespues);
    interact(() -> tabla.scrollTo(ultimoIndice));
    int idxCeldaDesc = (ultimoIndice * numColumnas) + 1;
    Node celdaDesc = lookup("#tablatv .table-cell").nth(idxCeldaDesc).query();
    doubleClickOn(celdaDesc);
    write("TestCreditAcc").push(KeyCode.ENTER);
    int idxCeldaBalance = (ultimoIndice * numColumnas) + 4;
    Node celdaBalance = lookup("#tablatv .table-cell").nth(idxCeldaBalance).query();
    doubleClickOn(celdaBalance);
    write("50").push(KeyCode.ENTER);
    int idxCeldaTipo = (ultimoIndice * numColumnas) + 6;
    Node celdaTipo = lookup("#tablatv .table-cell").nth(idxCeldaTipo).query();
    doubleClickOn(celdaTipo);
    type(KeyCode.DOWN).type(KeyCode.ENTER);
    verifyThat("#tablatv", hasTableCell("TestCreditAcc"));
    verifyThat("#tablatv", hasTableCell("50.0"));
    verifyThat("#tablatv", hasTableCell("CREDIT"));
    boolean existe = tabla.getItems().stream()
            .anyMatch(a -> "TestCreditAcc".equals(a.getDescription()) 
                        && a.getBeginBalance() != null && a.getBeginBalance() == 50.0 
                        && AccountType.CREDIT.equals(a.getType()));
    
    assertTrue("La cuenta con los datos introducidos debe existir en la tabla", existe);
}
/**
 * @fixme Test insuficiente:Completar y verificar que el objeto Account eliminado ya NO está entre los items de la tabla.(Hecho)

 */
@Test
public void test04_Delete() {
    TableView<Account> tabla = lookup("#tablatv").queryAs(TableView.class);
    Node celdaDesc = lookup("#tablatv .table-cell").nth(1).query();
    clickOn(celdaDesc);
    clickOn("#deleteBT");
    boolean existe = tabla.getItems().stream()
            .anyMatch(a -> celdaDesc.equals(a.getDescription()));
    assertFalse("La cuenta debería haber sido eliminada de la lista de ítems", existe);
}
}


