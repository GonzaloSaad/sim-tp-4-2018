package utn.frc.sim.views.battleship;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utn.frc.sim.battleship.BattleShip;
import utn.frc.sim.battleship.game.Players;
import utn.frc.sim.util.DoubleUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutomaticController {

    private static final String PLAYER_1 = "Player 1";
    private static final String PLAYER_2 = "Player 2";
    private static final String TIE = "Empate";
    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private static final Logger logger = LogManager.getLogger(AutomaticController.class);

    private ExecutorService executorService;

    @FXML
    private Label lblP1;

    @FXML
    private Label lblP2;

    @FXML
    private Label lblWinner;

    @FXML
    private TextField txtGames;

    @FXML
    private Button btnRun;

    @FXML
    private Label lblPlayer1Acc;

    @FXML
    private Label lblPlayer2Acc;

    @FXML
    void btnRunClick(ActionEvent event) {
        disableRunButton();
        runAllGamesService();
    }

    private void runAllGamesService() {
        executorService = Executors.newFixedThreadPool(THREADS);
        executorService.submit(this::runAllGames);
    }

    private void runAllGames() {
        int amountOfGames = getAmountOfGames();

        int p1Won = 0;
        int p2Won = 0;

        double p2Acc = 0;
        double p1Acc = 0;

        for (int i = 1; i <= amountOfGames; i++) {
            BattleShip battleShip = new BattleShip();
            Players winner = battleShip.runGame();

            if (winner == Players.PLAYER_1) {
                p1Won++;
                setLabelOfP1Winning(p1Won);
            } else {
                p2Won++;
                setLabelOfP2Winning(p2Won);
            }

            if (i == 1) {
                p2Acc = battleShip.getPlayer2Accuracy();
                p1Acc = battleShip.getPlayer1Accuracy();
            } else {
                p1Acc = ((i - 1) * p1Acc + battleShip.getPlayer1Accuracy()) / i;
                p2Acc = ((i - 1) * p2Acc + battleShip.getPlayer2Accuracy()) / i;
            }
        }

        setResultsToUI(p1Acc, p2Acc, p1Won, p2Won);
        executorService.shutdownNow();
    }

    private void setLabelOfP2Winning(int player2) {
        Platform.runLater(() -> setLblP2(player2));
    }

    private void setLabelOfP1Winning(int player1) {
        Platform.runLater(() -> setLblP1(player1));
    }

    private void setResultsToUI(double p1Accuracy, double p2Accuracy, int p1, int p2) {
        Platform.runLater(() -> setP1AccLabel(p1Accuracy * 100));
        Platform.runLater(() -> setP2AccLabel(p2Accuracy * 100));
        Platform.runLater(() -> setWinnerLabel(p1, p2));
        Platform.runLater(this::enableRunButton);
    }

    private void setP1AccLabel(double accuracy) {
        lblPlayer1Acc.setText(DoubleUtils.getDoubleWithFourPlaces(accuracy));
    }

    private void setP2AccLabel(double accuracy) {
        lblPlayer2Acc.setText(DoubleUtils.getDoubleWithFourPlaces(accuracy));
    }

    private void setWinnerLabel(int p1, int p2) {
        if (p1 > p2) {
            lblWinner.setText(PLAYER_1);
        } else if (p2 > p1) {
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
        return Integer.valueOf(txtGames.getText());
    }

    private void disableRunButton() {
        btnRun.setDisable(Boolean.TRUE);
    }

    private void enableRunButton() {
        btnRun.setDisable(Boolean.FALSE);
    }

}
