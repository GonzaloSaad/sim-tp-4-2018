package utn.frc.sim.battleship;

import utn.frc.sim.battleship.game.ShotResult;
import utn.frc.sim.battleship.strategies.BattleShipStrategy;

public class Player {
    private BattleShipStrategy strategy;
    private Board board;
    private int hits;
    private int shots;

    public Player(BattleShipStrategy strategy) {
        this.board = strategy.getBoardForPlay();
        this.strategy = strategy;
        this.hits = 0;
        this.shots = 0;
    }

    public void play() {
        ShotResult result;
        do {
            shots++;
            result = strategy.getNextShot();
            if (result != ShotResult.MISS){
                hits++;
            }

        } while (result != ShotResult.MISS);
    }

    public void setEnemy(Player player){
        strategy.setEnemyBoard(player.getBoard());
    }

    public boolean isAlive() {
        return board.isAlive();
    }

    public int getHits() {
        return hits;
    }

    public Board getBoard() {
        return board;
    }

    public int getShots() {
        return shots;
    }
}
