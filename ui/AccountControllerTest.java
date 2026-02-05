
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import javafx.stage.Stage;
import org.junit.Before;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class AccountControllerTest extends ApplicationTest{
    @Override
    public void start(Stage stage) throws Exception {
        new ProyectoSignInApplication().start(stage);
    }
    @Before
    public void testLogin(){
        clickOn("#tfUser");
        write("jsmith@enterprise.net");
        clickOn("#pfPasswd");
        write("abcd*1234");
        clickOn("#btSignIn");
        clickOn("#ExitBt");
    }
    @Test
    public void testExit(){
        clickOn("#ExitBt");
        clickOn("Aceptar");
    }
    @Test
    public void testUpdate(){
    
    }
    @Test
    public void testCreate(){
    
    }
    @Test
    public void testDelete(){
    
    }
}
