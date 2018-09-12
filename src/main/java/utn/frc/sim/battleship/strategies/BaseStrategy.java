package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.ships.Orientation;

import java.util.Random;

public abstract class BaseStrategy implements BattleShipStrategy {

    private Random random;
    private Board enemyBoard;

    protected BaseStrategy(){
        this.random = new Random();
    }

    @Override
    public void setEnemyBoard(Board enemyBoard) {
        this.enemyBoard = enemyBoard;
    }

    protected Board getEnemyBoard(){
        return enemyBoard;
    }

    protected Random getRandom(){
        return random;
    }

    protected Orientation getRandomOrientation() {
        return Orientation.values()[new Random().nextInt(Orientation.values().length)];
    }

    protected int getRandomX() {
        return random.nextInt(Board.BOARD_WIDTH);
    }

    protected int getRandomY() {
        return random.nextInt(Board.BOARD_HEIGHT);
    }
}
