package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.Ship;
import utn.frc.sim.battleship.game.ShotResult;
import utn.frc.sim.battleship.game.ships.Orientation;
import utn.frc.sim.battleship.game.ships.ShipType;

import java.util.Random;

public class BasicStrategy implements BattleShipStrategy {

    private static final Random random = new Random();
    private Board enemyBoard;

    @Override
    public Board getBoardForPlay() {
        Board board = new Board();

        for (ShipType type : ShipType.values()) {
            for (int i = 0; i < type.getAmount(); i++) {
                insertShip(type, board);
            }
        }
        return board;
    }

    @Override
    public ShotResult getNextShot() {

        int x;
        int y;
        do {
            x = getRandomX();
            y = getRandomY();
        } while (enemyBoard.wasShot(x, y));


        return enemyBoard.handleShot(getRandomX(), getRandomY());
    }

    @Override
    public void setEnemyBoard(Board enemyBoard) {
        this.enemyBoard = enemyBoard;
    }

    private void insertShip(ShipType type, Board board) {
        Ship ship;
        do {
            Orientation orientation = getRandomOrientation();
            int x = getRandomX();
            int y = getRandomY();
            ship = new Ship(x, y, type, orientation);
        } while (!board.placeShip(ship));

        System.out.println(ship);
    }


    private Orientation getRandomOrientation() {
        return Orientation.values()[new Random().nextInt(Orientation.values().length)];
    }

    private int getRandomX() {
        return random.nextInt(Board.BOARD_WIDTH);
    }

    private int getRandomY() {
        return random.nextInt(Board.BOARD_HEIGHT);
    }


}
