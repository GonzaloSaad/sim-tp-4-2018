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

    private ArrayList<PossibleShoots> hitted;
    //First hit es un punto "pivot" desde el que partimos para darle a un barco, se borra al obtener result destroyed.
    private PossibleShoots firstHit;
    //Last hit es un punto movil que se va actualizando hasta obtener el result destroyed
    private PossibleShoots lastHit;

    //Variables
    private Boolean targetIsHorizontal;
    private Boolean targetIsPositive;

    private Boolean isHuntingMode;

    private ArrayList<PossibleShoots> missed;


    public CIDSStrategy() {
        super();
        initialize();
    }

    private void initialize() {
        missed = new ArrayList<>();
        hitted = new ArrayList<>();
    }

    @Override
    public ShotResult getNextShot() {
        PossibleShoots ps = generateRandom(0, Board.BOARD_WIDTH, 0, Board.BOARD_HEIGHT);
        ShotResult result = null;
        if(isHuntingMode){

            if(lastHit == null){

            } else{
                if(getEnemyBoard().isValidX(firstHit.getX()+1) && !isShotMissed(new PossibleShoots(firstHit.getX()+1, firstHit.getY()))){
                    PossibleShoots pss = new PossibleShoots(firstHit.getX()+1, firstHit.getY());
                    result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                    if(!hasDestroyedShip(result)){
                        if(result == ShotResult.HIT){
                            lastHit = pss;
                            targetIsHorizontal = true;
                            targetIsPositive = true;
                        }
                    }
                }
                else if(getEnemyBoard().isValidX(firstHit.getX()) && getEnemyBoard().isValidY(firstHit.getY()+1)){

                }
            }
        }else{
            result = getEnemyBoard().handleShot(ps.getX(), ps.getY());
            if(result == ShotResult.HIT){
                isHuntingMode = true;
                firstHit = ps;
            }else{
                missed.add(ps);
            }
        }
        return result;
    }

    public Boolean hasDestroyedShip(ShotResult result){
        if(result == ShotResult.DESTROYED){
            firstHit = null;
            lastHit = null;
            targetIsHorizontal = null;
            targetIsPositive = null;
            isHuntingMode = false;
            return true;
        }
        return false;
    }

    public PossibleShoots generateRandom(int startX, int endX, int startY, int endY) {
        Random rand = new Random();
        int rangeX = endX - startX;
        int rangeY = endY - startY;

        int random_x = rand.nextInt(rangeX);
        int random_y = rand.nextInt(rangeY);
        PossibleShoots ps = new PossibleShoots(random_x, random_y);

        while (isShotMissed(ps)) {
            random_x = rand.nextInt(rangeX);
            random_y = rand.nextInt(rangeY);
            ps = new PossibleShoots(random_x, random_y);
        }
        return ps;
    }

    public Boolean isShotMissed(PossibleShoots ps){
        return missed.contains(ps);
    }

}