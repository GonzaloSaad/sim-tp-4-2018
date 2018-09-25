package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.shots.ShotResult;
import utn.frc.sim.battleship.strategies.models.PossibleShoots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CIDSStrategy extends RandomStrategy {

    private static final int QUARTER_BOARD_Y = Board.BOARD_HEIGHT / 4;
    private static final int QUARTER_BOARD_X = Board.BOARD_WIDTH / 4;

    private ArrayList<PossibleShoots> fired;
    private ArrayList<PossibleShoots> missed;

    public CIDSStrategy() {
        super();
        initialize();
    }

    private void initialize() {
        missed = new ArrayList<>();
    }



    @Override
    public ShotResult getNextShot() {
       PossibleShoots ps = generateRandom(0, 63, 0, 31);
        ShotResult result = null;
      /*   if(!missed.contains(ps)){
             result = getEnemyBoard().handleShot(ps.getX(), ps.getY());
        }
            PossibleShoots pss = generateRandom(0, 63, 0, 31);
            ShotResult result = getEnemyBoard().handleShot(ps.getX(), ps.getY());
            if (result.equals(ShotResult.MISS)) {
                missed.add(ps);
            } else if (result.equals(ShotResult.HIT)) {
                hitted.add(ps);
            }*/
        return result;
    }

    public PossibleShoots generateRandom(int startX, int endX, int startY, int endY) {
        Random rand = new Random();
        int rangeX = endX - startX + 1;
        int rangeY = endY - startY + 1;

        int random_x = rand.nextInt(rangeX);
        int random_y = rand.nextInt(rangeY);

        PossibleShoots ps = new PossibleShoots(random_x, random_y, ShotResult.MISS);
        return ps;
    }

}