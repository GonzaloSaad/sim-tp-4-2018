package utn.frc.sim.views.battleship;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.util.Strings;
import utn.frc.sim.battleship.BattleShip;
import utn.frc.sim.battleship.game.Players;
import utn.frc.sim.statistics.PlayerStatistics;
import utn.frc.sim.util.DoubleUtils;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutomaticController {

    private static final String PLAYER_1 = "Player 1";
    private static final String PLAYER_2 = "Player 2";
    private static final String TIE = "Empate";
    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private static final int SPINNER_INTEGER_MIN_VALUE = 1;
    private static final int SPINNER_INTEGER_MAX_VALUE = 2000;
    private static final int SPINNER_NO_INCREMENT_STEP = 0;
    private static final String NO_RESULT = "-";

    private ExecutorService executorService;

    @FXML
    private Label lblP1;

    @FXML
    private Label lblP2;

    @FXML
    private Label lblWinner;

    @FXML
    private Spinner<Integer> spnGames;

    @FXML
    private Button btnRun;

    @FXML
    private Label lblPlayer1Acc;

    @FXML
    private Label lblPlayer2Acc;

    @FXML
    private Label lblPlayer1ShotsToWin;

    @FXML
    private Label lblPlayer2ShotsToWin;

    @FXML
    public void initialize() {
        initializeSpinner();
    }

    private void initializeSpinner() {
        spnGames.setValueFactory(getIntegerValueFactory(SPINNER_INTEGER_MIN_VALUE, SPINNER_INTEGER_MAX_VALUE));
        spnGames.focusedProperty().addListener(getListenerForChangeValue(spnGames));
        setTextFieldListenerToSpinner(spnGames);
    }

    @FXML
    void btnRunClick(ActionEvent event) {
        disableRunButton();
        clearResultUI();

        runAllGamesService();
    }

    private void clearResultUI() {
        clearWinningLabel();
        clearAccuracyLabels();
        clearShotsToWinLabels();
    }

    private void runAllGamesService() {
        executorService = Executors.newFixedThreadPool(THREADS);
        executorService.submit(this::runAllGames);
    }

    private void runAllGames() {
        int amountOfGames = getAmountOfGames();

        PlayerStatistics player1 = new PlayerStatistics();
        PlayerStatistics player2 = new PlayerStatistics();


        for (int i = 1; i <= amountOfGames; i++) {
            BattleShip battleShip = new BattleShip();
            Players winner = battleShip.runGame();

            if (winner == Players.PLAYER_1) {
                player1.addWonMatch();
                player1.addShotsToWin(battleShip.getPlayer1Shots());
                setWonGamesLabelOfP1(player1.getWonMatches());
            } else {
                player2.addWonMatch();
                player2.addShotsToWin(battleShip.getPlayer2Shots());
                setWonGamesLabelOfP2(player2.getWonMatches());
            }

            player1.addAccuracy(battleShip.getPlayer1Accuracy(), i);
            player2.addAccuracy(battleShip.getPlayer2Accuracy(), i);

        }

        setResultsToUI(player1, player2);
        executorService.shutdownNow();
    }

    private void setResultsToUI(PlayerStatistics player1, PlayerStatistics player2) {
        Platform.runLater(() -> setAvgAccuracyLabels(player1, player2));
        Platform.runLater(() -> setShotsToWinLabels(player1, player2));
        Platform.runLater(() -> setWinnerLabels(player1, player2));
        Platform.runLater(this::enableRunButton);
    }

    private void setAvgAccuracyLabels(PlayerStatistics player1, PlayerStatistics player2) {
        setP1AccuracyLabel(player1.getAvgAccuracy() * 100);
        setP2AccuracyLabel(player2.getAvgAccuracy() * 100);
    }

    private void setP1AccuracyLabel(double accuracy) {
        lblPlayer1Acc.setText(DoubleUtils.getDoubleWithFourPlaces(accuracy));
    }

    private void setP2AccuracyLabel(double accuracy) {
        lblPlayer2Acc.setText(DoubleUtils.getDoubleWithFourPlaces(accuracy));
    }

    private void setShotsToWinLabels(PlayerStatistics player1, PlayerStatistics player2) {
        setP1ShotsToWinLabel(player1.getAvgShotToWin());
        setP2ShotsToWinLabel(player2.getAvgShotToWin());
    }

    private void setP1ShotsToWinLabel(int shotsToWin) {
        lblPlayer1ShotsToWin.setText(shotsToWin == 0 ? NO_RESULT : Integer.toString(shotsToWin));
    }

    private void setP2ShotsToWinLabel(int shotsToWin) {
        lblPlayer2ShotsToWin.setText(shotsToWin == 0 ? NO_RESULT : Integer.toString(shotsToWin));
    }

    private void setWinnerLabels(PlayerStatistics player1, PlayerStatistics player2) {
        int p1wonMatches = player1.getWonMatches();
        int p2wonMatches = player2.getWonMatches();
        if (p1wonMatches > p2wonMatches) {
            lblWinner.setText(PLAYER_1);
        } else if (p2wonMatches > p1wonMatches) {
            lblWinner.setText(PLAYER_2);
        } else {
            lblWinner.setText(TIE);
        }
    }

    private void setWonGamesLabelOfP1(int player1WonMatches) {
        Platform.runLater(() -> setWonGamesLblP1(player1WonMatches));
    }

    private void setWonGamesLabelOfP2(int player2WonMatches) {
        Platform.runLater(() -> setWonGamesLblP2(player2WonMatches));
    }

    private void setWonGamesLblP1(int amountOfWonMatches) {
        lblP1.setText(Integer.toString(amountOfWonMatches));
    }

    private void setWonGamesLblP2(int amountOfWonMatches) {
        lblP2.setText(Integer.toString(amountOfWonMatches));
    }

    private void clearWinningLabel() {
        lblWinner.setText(Strings.EMPTY);
    }

    private void clearShotsToWinLabels() {
        lblPlayer1ShotsToWin.setText(Strings.EMPTY);
        lblPlayer2ShotsToWin.setText(Strings.EMPTY);
    }

    private void clearAccuracyLabels() {
        lblPlayer1Acc.setText(Strings.EMPTY);
        lblPlayer2Acc.setText(Strings.EMPTY);
    }

    private void disableRunButton() {
        btnRun.setDisable(Boolean.TRUE);
    }

    private void enableRunButton() {
        btnRun.setDisable(Boolean.FALSE);
    }

    private int getAmountOfGames() {
        Optional<Integer> games = Optional.ofNullable(spnGames.getValue());
        if (games.isPresent()) {
            return games.get();
        } else {
            spnGames.getValueFactory().setValue(SPINNER_INTEGER_MIN_VALUE);
            return SPINNER_INTEGER_MIN_VALUE;
        }
    }

    /**
     * Genera una fabrica de valores enteros para darle un limite al spinner.
     */
    private SpinnerValueFactory<Integer> getIntegerValueFactory(int min, int max) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    }

    /**
     * Creacion del listener de perdida de focus para el bug de JavaFx.
     */
    private <T> ChangeListener<? super Boolean> getListenerForChangeValue(Spinner<T> spinner) {
        return (observable, oldValue, newValue) -> {
            if (!newValue) {
                Optional<Integer> games = Optional.ofNullable(spnGames.getValue());
                if (games.isPresent()) {
                    spinner.increment(SPINNER_NO_INCREMENT_STEP);
                } else {
                    spnGames.getValueFactory().setValue(SPINNER_INTEGER_MIN_VALUE);
                }

            }
        };
    }

    /**
     * Metodo que inserta un listener de texto de Texfield
     * a un spinner.
     */
    private void setTextFieldListenerToSpinner(Spinner spinner) {
        TextField textField = spinner.getEditor();
        textField.textProperty().addListener(getListenerForText(textField));
    }

    /**
     * Metodo que genera un Listener para el cambio de
     * texto de un TextField.
     */
    private ChangeListener<String> getListenerForText(TextField textField) {
        return (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        };
    }

}
