package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.ships.Ship;
import utn.frc.sim.battleship.game.shots.ShotResult;
import utn.frc.sim.battleship.game.ships.Orientation;
import utn.frc.sim.battleship.game.ships.ShipType;

public class RandomStrategy extends BaseStrategy {


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

    private void insertShip(ShipType type, Board board) {
        Ship ship;
        do {
            Orientation orientation = getRandomOrientation();
            int x = getRandomX();
            int y = getRandomY();
            ship = new Ship(x, y, type, orientation);
        } while (!board.placeShip(ship));
    }

    @Override
    public ShotResult getNextShot() {
        int x;
        int y;
        do {
            x = getRandomX();
            y = getRandomY();
        } while (getEnemyBoard().wasShot(x, y));
        return getEnemyBoard().handleShot(getRandomX(), getRandomY());
    }
}
