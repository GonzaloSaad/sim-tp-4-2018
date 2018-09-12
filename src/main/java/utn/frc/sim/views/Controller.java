package utn.frc.sim.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utn.frc.sim.battleship.BattleShip;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @FXML
    private VBox battleshipPanel;
    private BattleShip battleShip;

    @FXML
    private void initialize(){
        battleShip = new BattleShip();
        battleshipPanel.getChildren().add(battleShip.getBoardPlayer1());
        battleshipPanel.getChildren().add(new Label());
        battleshipPanel.getChildren().add(battleShip.getBoardPlayer2());
    }

    @FXML
    void startClick(ActionEvent event) {
        start();
    }

    private void start(){
        executorService.submit(()->battleShip.startGame());
    }

}
