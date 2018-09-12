package utn.frc.sim.views.battleship;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.util.Strings;
import utn.frc.sim.battleship.BattleShip;
import utn.frc.sim.battleship.game.Players;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SemiAutomaticController {

    private static final String PLAYER_1 = "Player 1";
    private static final String PLAYER_2 = "Player 2";
    private static final String SEPARATOR = "    ";
    private ExecutorService executorService;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnShot;

    @FXML
    private Label lblWinner;

    @FXML
    private HBox pnlBoards;

    private BattleShip battleShip;

    @FXML
    public void initialize() {
        startBoards();
    }

    private void startBoards() {
        battleShip = new BattleShip();
        pnlBoards.getChildren().add(battleShip.getBoardPlayer1());
        pnlBoards.getChildren().add(new Label(SEPARATOR));
        pnlBoards.getChildren().add(battleShip.getBoardPlayer2());
    }


    @FXML
    void btnRestartClick(ActionEvent event) {
        restartGame();
    }

    @FXML
    void btnShotClick(ActionEvent event) {
        handleOneTurnShot();
    }

    @FXML
    void btnStartClick(ActionEvent event) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::runGameToEnd);
    }

    private void restartGame() {
        clearBoardPanel();
        clearWinnerLabel();
        startBoards();
        enableShotsButtons();
    }

    private void runGameToEnd() {
        battleShip.runGame();
        Platform.runLater(this::setWinnerState);
        executorService.shutdown();
    }

    private void handleOneTurnShot() {
        if (battleShip.gameRunning()) {
            battleShip.playOneTurn();
        } else {
            setWinnerState();
        }
    }

    private void setWinnerState() {
        disableShotsButtons();
        setWinnerLabel();
    }

    private void setWinnerLabel() {
        if (battleShip.getWinner() == Players.PLAYER_1) {
            setPlayer1WinnerLabel();
        } else {
            setPlayer2WinnerLabel();
        }
    }

    private void clearBoardPanel() {
        pnlBoards.getChildren().clear();
    }

    private void clearWinnerLabel() {
        lblWinner.setText(Strings.EMPTY);
    }

    private void setPlayer1WinnerLabel() {
        lblWinner.setText(PLAYER_1);
    }

    private void setPlayer2WinnerLabel() {
        lblWinner.setText(PLAYER_2);
    }

    private void disableShotsButtons() {
        btnShot.setDisable(Boolean.TRUE);
        btnStart.setDisable(Boolean.TRUE);
    }

    private void enableShotsButtons() {
        btnShot.setDisable(Boolean.FALSE);
        btnStart.setDisable(Boolean.FALSE);
    }
}

