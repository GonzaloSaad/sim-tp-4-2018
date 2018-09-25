package utn.frc.sim.views.battleship;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import utn.frc.sim.battleship.BattleShip;
import utn.frc.sim.battleship.game.Players;
import utn.frc.sim.util.DoubleUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SemiAutomaticController {

    private static final Logger logger = LogManager.getLogger(SemiAutomaticController.class);

    private static final String PLAYER_1 = "Player 1";
    private static final String PLAYER_2 = "Player 2";
    private static final String SEPARATOR = "    ";
    private ExecutorService executorService;
    private static final int SPINNER_INTEGER_MIN_VALUE = 5;
    private static final int SPINNER_INTEGER_MAX_VALUE = 50;
    private boolean running = false;

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

    @FXML
    private Spinner<Integer> spnDelay;


    private BattleShip battleShip;

    @FXML
    public void initialize() {
        initializeSpinner();
        startBoards();
    }

    private void initializeSpinner() {
        spnDelay.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));


    }

    private void startBoards() {
        battleShip = new BattleShip();
        pnlBoards.getChildren().add(battleShip.getBoardPlayer1());
        pnlBoards.getChildren().add(new Label(SEPARATOR));
        pnlBoards.getChildren().add(battleShip.getBoardPlayer2());
        setTurnLabel();
        setStatisticsToUI();
    }

    @FXML
    void btnRestartClick(ActionEvent event) {
        stopExecutionService();
        restartGame();
    }

    private void stopExecutionService() {
        if (executorService != null) {
            executorService.shutdownNow();
            running = false;
            try {
                if (executorService != null){
                    executorService.awaitTermination(6, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                logger.error("Error stopping execution service.");
            }
        }
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
        running = true;
        Platform.runLater(this::disableShotsButtons);
        int delay = getDelayFromSpinner();
        while (running && battleShip.gameRunning()) {

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted.");
                break;
            }
            battleShip.playOneTurn();
            Platform.runLater(this::setStatisticsToUI);
        }

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
        if (battleShip.getTurn() == Players.PLAYER_1) {
            lblTurn.setText(PLAYER_1);
        } else {
            lblTurn.setText(PLAYER_2);
        }
    }

    /**
     * Genera una fabrica de valores enteros para darle un limite al spinner.
     */
    private SpinnerValueFactory<Integer> getIntegerValueFactory(int min, int max) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    }

    public int getDelayFromSpinner() {
        return spnDelay.getValue();
    }
}

