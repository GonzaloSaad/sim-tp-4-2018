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
import utn.frc.sim.util.DoubleUtils;

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

    @FXML
    private Label lblP1Shots;

    @FXML
    private Label lblP1Hits;

    @FXML
    private Label lblP2Shots;

    @FXML
    private Label lblP2Hits;

    @FXML
    private Label lblP1Accuracy;

    @FXML
    private Label lblP2Accuracy;

    @FXML
    private Label lblTurn;

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
        setTurnLabel();
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
        Platform.runLater(this::disableShotsButtons);
        battleShip.runGame(true);
        Platform.runLater(this::setWinnerState);
        Platform.runLater(this::setStatisticsToUI);
        executorService.shutdown();
    }

    private void handleOneTurnShot() {
        if (battleShip.gameRunning()) {
            battleShip.playOneTurn();
            setStatisticsToUI();
        } else {
            setWinnerState();
        }
    }

    private void setStatisticsToUI() {
        setShotsLabels();
        setHitsLabels();
        setAccuracyLabels();
    }

    private void setShotsLabels() {
        lblP1Shots.setText(Integer.toString(battleShip.getPlayer1Shots()));
        lblP2Shots.setText(Integer.toString(battleShip.getPlayer2Shots()));
    }

    private void setHitsLabels() {
        lblP1Hits.setText(Integer.toString(battleShip.getPlayer1Hits()));
        lblP2Hits.setText(Integer.toString(battleShip.getPlayer2Hits()));
    }

    private void setAccuracyLabels() {
        lblP1Accuracy.setText(DoubleUtils.getDoubleWithFourPlaces(battleShip.getPlayer1Accuracy()));
        lblP2Accuracy.setText(DoubleUtils.getDoubleWithFourPlaces(battleShip.getPlayer2Accuracy()));
    }

    private void setWinnerState() {
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

    private void setTurnLabel() {
        if (battleShip.getTurn() == Players.PLAYER_1){
            lblTurn.setText(PLAYER_1);
        } else {
            lblTurn.setText(PLAYER_2);
        }
    }
}

