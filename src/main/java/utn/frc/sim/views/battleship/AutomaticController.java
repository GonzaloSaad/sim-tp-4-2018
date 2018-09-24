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
        clearWinningLabel();
        runAllGamesService();
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
                setLabelOfP1Winning(player1.getWonMatches());
            } else {
                player2.addWonMatch();
                player2.addShotsToWin(battleShip.getPlayer2Shots());
                setLabelOfP2Winning(player2.getWonMatches());
            }

            player1.addAccuracy(battleShip.getPlayer1Accuracy(), i);
            player2.addAccuracy(battleShip.getPlayer2Accuracy(), i);

        }

        setResultsToUI(player1, player2);
        executorService.shutdownNow();
    }

    private void setLabelOfP2Winning(int player2) {
        Platform.runLater(() -> setLblP2(player2));
    }

    private void setLabelOfP1Winning(int player1) {
        Platform.runLater(() -> setLblP1(player1));
    }

    private void setResultsToUI(PlayerStatistics player1, PlayerStatistics player2) {
        Platform.runLater(() -> setP1AccLabel(player1.getAvgAccuracy() * 100));
        Platform.runLater(() -> setP2AccLabel(player2.getAvgAccuracy() * 100));
        Platform.runLater(() -> setWinnerLabel(player1.getWonMatches(), player2.getWonMatches()));
        Platform.runLater(this::enableRunButton);
    }

    private void setP1AccLabel(double accuracy) {
        lblPlayer1Acc.setText(DoubleUtils.getDoubleWithFourPlaces(accuracy));
    }

    private void setP2AccLabel(double accuracy) {
        lblPlayer2Acc.setText(DoubleUtils.getDoubleWithFourPlaces(accuracy));
    }

    private void setWinnerLabel(int p1wonMatches, int p2wonMatches) {
        if (p1wonMatches > p2wonMatches) {
            lblWinner.setText(PLAYER_1);
        } else if (p2wonMatches > p1wonMatches) {
            lblWinner.setText(PLAYER_2);
        } else {
            lblWinner.setText(TIE);
        }
    }

    private void setLblP1(int amount) {
        lblP1.setText(Integer.toString(amount));
    }

    private void setLblP2(int amount) {
        lblP2.setText(Integer.toString(amount));
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

    private void clearWinningLabel() {
        lblWinner.setText(Strings.EMPTY);
    }

    private void disableRunButton() {
        btnRun.setDisable(Boolean.TRUE);
    }

    private void enableRunButton() {
        btnRun.setDisable(Boolean.FALSE);
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
