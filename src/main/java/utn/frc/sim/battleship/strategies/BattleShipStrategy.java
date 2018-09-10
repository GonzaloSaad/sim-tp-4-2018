package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.Shot;

public interface BattleShipStrategy {
    Board getBoardForPlay();
    Shot getNextShot();
}
