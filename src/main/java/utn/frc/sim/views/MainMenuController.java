package utn.frc.sim.views;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/***
 * Clase que se encarga de implementar la logica de la vista principal de menu.
 * La pantalla principal, tiene un panel en el cual ocurren todas las escenas.
 */
public class MainMenuController {

    @FXML
    private Pane mainPane;
    /**
     * Panel donde ocurren las escenas.
     **/

    private static Logger logger = LogManager.getLogger(MainMenuController.class);

    /**
     * Metodo que manera el evento de click sobre
     * la opcion de la lista de numeros aleratorios
     */
    @FXML
    void openSemiAutomaticDialog(ActionEvent event) {
        setSemiAutomaticDialog();
    }

    /**
     * Metodo que maneja el evento de click sobre
     * la opcion de la prueba de chi-cuadrado.
     */
    @FXML
    void openAutomaticDialog(ActionEvent event) {
        setAutomaticDialog();
    }

    /**
     * Metodo que maneja el evento de click sobre
     * la opcion de volver a pantalla de info.
     *
     */
    @FXML
    void openEssayInfoDialog(ActionEvent event) {
        setEssayInfoDialog();
    }

    /**
     * Accion de setear la vista de la lista de
     * generacion de numeros aleatorios al
     * panel principal
     **/
    private void setSemiAutomaticDialog() {
        try {
            mainPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("/views/battleship/semi-automatic.fxml")));
        } catch (IOException e) {
            logger.error("Problem opening SemiAutomaticDialog.", e);
        }
    }

    /**
     * Accion de setear la vista de la el test
     * de chi cuadrado al panel principal.
     */
    private void setAutomaticDialog() {
        try {
            mainPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("/views/battleship/automatic.fxml")));
        } catch (IOException e) {
            logger.error("Problem opening AutomaticDialog.", e);
        }
    }

    private void setEssayInfoDialog() {
        try {
            mainPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("/views/menu/essay-info.fxml")));
        } catch (IOException e) {
            logger.error("Problem opening EssayInfoDialog.", e);
        }
    }

}
