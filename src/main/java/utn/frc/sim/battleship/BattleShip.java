package utn.frc.sim.battleship;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utn.frc.sim.battleship.game.Players;
import utn.frc.sim.battleship.strategies.RandomStrategy;

import java.util.Random;

public class BattleShip {

    private static final Logger logger = LogManager.getLogger(BattleShip.class);
    private Player player_1;
    private Player player_2;
    private Players turn;


    public BattleShip() {
        initGame();
    }

    private void initGame() {
        player_1 = new Player(new RandomStrategy());
        player_2 = new Player(new RandomStrategy());
        player_1.setEnemy(player_2);
        player_2.setEnemy(player_1);
        turn = getRandomTurn();
    }

    private Players getRandomTurn() {
        return Players.values()[new Random().nextInt(Players.values().length)];
    }

    public Board getBoardPlayer1() {
        return player_1.getBoard();
    }

    public Board getBoardPlayer2() {
        return player_2.getBoard();
    }

    public Players runGame() {
        return runGame(Boolean.FALSE);
    }

    public Players runGame(boolean withDelay) {
        while (gameRunning()) {
            handleTurnsAndGames();
            if (withDelay) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    logger.error("Error while sleeping thread");
                }
            }
        }
        return getWinner();

    }

    public boolean gameRunning() {
        return player1Alive() && player2Alive();
    }

    public void playOneTurn() {
        handleTurnsAndGames();
    }

    private void handleTurnsAndGames() {
        if (turn == Players.PLAYER_1) {
            player1Turn();
        } else {
            player2Turn();
        }
    }

    private boolean player1Alive() {
        return player_1.isAlive();
    }

    private boolean player2Alive() {
        return player_2.isAlive();
    }

    private void player1Turn() {
        player_1.play();
        turn = Players.PLAYER_2;
    }

    private void player2Turn() {
        player_2.play();
        turn = Players.PLAYER_1;
    }

    public Players getWinner() {
        if (player1Alive()) {
            return Players.PLAYER_1;
        } else {
            return Players.PLAYER_2;
        }
    }
}
