package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.shots.ShotResult;

public interface BattleShipStrategy {
    Board getBoardForPlay();
    ShotResult getNextShot();
    void setEnemyBoard(Board enemyBoard);
}
