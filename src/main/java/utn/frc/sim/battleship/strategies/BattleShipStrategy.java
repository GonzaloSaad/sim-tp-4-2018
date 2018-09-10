package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.Shot;
import utn.frc.sim.battleship.game.ShotResult;

public interface BattleShipStrategy {
    Board getBoardForPlay();
    ShotResult getNextShot();
    void setEnemyBoard(Board enemyBoard);
}
