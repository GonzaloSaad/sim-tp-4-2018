package utn.frc.sim.views.battleship;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utn.frc.sim.battleship.BattleShip;
import utn.frc.sim.battleship.game.Players;

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
    void btnRunClick(ActionEvent event) {
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
        double player1Accuracy = 0;
        double player2Accuracy = 0;

        for (int i = 1; i <= amountOfGames; i++) {
            logger.info("Game: {}.", i);
            BattleShip battleShip = new BattleShip();
            Players winner = battleShip.runGame();

            if (winner == Players.PLAYER_1) {
                p1Won++;
                final int player1 = p1Won;
                Platform.runLater(() -> setLblP1(player1));
            } else {
                p2Won++;
                final int player2 = p2Won;
                Platform.runLater(() -> setLblP2(player2));
            }

            if (i==1){
                player1Accuracy = battleShip.getPlayer1Accuracy();
                player2Accuracy = battleShip.getPlayer2Accuracy();
            } else {
                player1Accuracy = ((double)1/i) * ((i -1) * player1Accuracy + battleShip.getPlayer1Accuracy());
                player2Accuracy = ((double)1/i) * ((i -1) * player2Accuracy + battleShip.getPlayer2Accuracy());
            }
        }

        logger.info("Player 1 accuracy: {}.", player1Accuracy*100);
        logger.info("Player 2 accuracy: {}.", player2Accuracy*100);


        final int p1 = p1Won;
        final int p2 = p2Won;
        Platform.runLater(() -> setWinnerLabel(p1, p2));
        executorService.shutdownNow();
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

}
