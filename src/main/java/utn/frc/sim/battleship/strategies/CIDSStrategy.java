package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.shots.ShotResult;
import utn.frc.sim.battleship.strategies.models.PossibleShoots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CIDSStrategy extends RandomStrategy {

    private ArrayList<PossibleShoots> hitted;
    //First hit es un punto "pivot" desde el que partimos para darle a un barco, se borra al obtener result destroyed.
    private PossibleShoots firstHit;
    //Last hit es un punto movil que se va actualizando hasta obtener el result destroyed
    private PossibleShoots lastHit;

    //Variables
    private Boolean targetIsHorizontal;
    private Boolean targetIsPositive;

    private Boolean isHuntingMode;


    public CIDSStrategy() {
        super();
        initialize();
    }

    private void initialize() {
        hitted = new ArrayList<>();
        isHuntingMode = false;
    }

    @Override
    public ShotResult getNextShot() {
        PossibleShoots ps = generateRandom(0, Board.BOARD_WIDTH, 0, Board.BOARD_HEIGHT, getEnemyBoard());
        ShotResult result = null;
        //Si estamos en modo caza
        if (isHuntingMode) {
            //Si ya pegamos una vez
            if (lastHit != null) {
                //Si estimamos que el target es horizontal y derecho
                if (targetIsHorizontal && targetIsPositive) {
                    //Si la coordenada es valida y no es un tiro realizado previamente
                    if (getEnemyBoard().isValidX(lastHit.getX() + 1) && !getEnemyBoard().wasShot(lastHit.getX() + 1, lastHit.getY())) {
                        PossibleShoots pss = new PossibleShoots(lastHit.getX() + 1, lastHit.getY());
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        //Si no destruimos el target
                        if (!hasDestroyedShip(result)) {
                            //Si pegamos, seguimos en la misma direccion
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                                targetIsHorizontal = true;
                                targetIsPositive = true;
                            }
                            //Si erramos, cambiamos de direccion
                            else {
                                lastHit = null;
                                targetIsPositive = false;
                            }
                        }
                    }
                    //Para el tiro N despues del segundo, si las coordenadas no son validas cambiamos de direccion
                    else {
                        PossibleShoots pss = new PossibleShoots(firstHit.getX() - 1, firstHit.getY());
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        if (!hasDestroyedShip(result)) {
                            targetIsPositive = false;
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                            } else {
                                lastHit = null;
                            }
                        }
                    }
                }
                //Si estimamos que el target es horizontal e izquierdo
                else if (targetIsHorizontal && !targetIsPositive) {
                    if (getEnemyBoard().isValidX(lastHit.getX() - 1) && !getEnemyBoard().wasShot(lastHit.getX() - 1, lastHit.getY())) {
                        PossibleShoots pss = new PossibleShoots(lastHit.getX() - 1, lastHit.getY());
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        if (!hasDestroyedShip(result)) {
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                                targetIsHorizontal = true;
                                targetIsPositive = false;
                            } else {
                                lastHit = null;
                                targetIsPositive = true;
                            }
                        }

                    } else {
                        PossibleShoots pss = new PossibleShoots(firstHit.getX() + 1, firstHit.getY());
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        if (!hasDestroyedShip(result)) {
                            targetIsPositive = true;
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                            } else {
                                lastHit = null;
                            }
                        }

                    }
                }
                //Si estimamos que el target es vertical y arriba
                else if (!targetIsHorizontal && targetIsPositive) {
                    if (getEnemyBoard().isValidY(lastHit.getY() + 1) && !getEnemyBoard().wasShot(lastHit.getX(), lastHit.getY() + 1)) {
                        PossibleShoots pss = new PossibleShoots(lastHit.getX(), lastHit.getY() + 1);
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        if (!hasDestroyedShip(result)) {
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                                targetIsHorizontal = false;
                                targetIsPositive = true;
                            } else {
                                lastHit = null;
                                targetIsPositive = false;
                            }
                        }

                    } else {
                        PossibleShoots pss = new PossibleShoots(firstHit.getX(), firstHit.getY() - 1);
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        if (!hasDestroyedShip(result)) {
                            targetIsPositive = false;
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                            } else {
                                lastHit = null;
                            }
                        }

                    }
                }
                //Si estimamos que el target es vertical y abajo
                else {
                    if (getEnemyBoard().isValidY(lastHit.getY() - 1) && !getEnemyBoard().wasShot(lastHit.getX(), lastHit.getY() - 1)) {
                        PossibleShoots pss = new PossibleShoots(lastHit.getX(), lastHit.getY() - 1);
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        if (!hasDestroyedShip(result)) {
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                                targetIsHorizontal = false;
                                targetIsPositive = false;
                            } else {
                                lastHit = null;
                                targetIsPositive = true;
                            }
                        }

                    } else {
                        PossibleShoots pss = new PossibleShoots(firstHit.getX(), firstHit.getY() + 1);
                        result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                        if (!hasDestroyedShip(result)) {
                            targetIsPositive = true;
                            if (result == ShotResult.HIT) {
                                lastHit = pss;
                            } else {
                                lastHit = null;
                            }
                        }

                    }
                }
            }
            //Si no pegamos ninguna todavia
            else {
                //Si la coordenada es valida y no disparamos ahi anteriormente
                if (getEnemyBoard().isValidX(firstHit.getX() + 1) && !getEnemyBoard().wasShot(firstHit.getX() + 1, firstHit.getY())) {
                    PossibleShoots pss = new PossibleShoots(firstHit.getX() + 1, firstHit.getY());
                    result = getEnemyBoard().handleShot(pss.getX(), pss.getY());
                    if (!hasDestroyedShip(result)) {
                        if (result == ShotResult.HIT) {
                            lastHit = pss;
                            targetIsHorizontal = true;
                            targetIsPositive = true;
                        }
                    }

                } else if (getEnemyBoard().isValidY(firstHit.getY() + 1) && !getEnemyBoard().wasShot(firstHit.getX(), firstHit.getY() + 1)) {
                    PossibleShoots pssY = new PossibleShoots(firstHit.getX(), firstHit.getY() + 1);
                    result = getEnemyBoard().handleShot(pssY.getX(), pssY.getY());
                    if (!hasDestroyedShip(result)) {
                        if (result == ShotResult.HIT) {
                            lastHit = pssY;
                            targetIsHorizontal = false;
                            targetIsPositive = true;
                        }
                    }
                } else if (getEnemyBoard().isValidX(firstHit.getX() - 1) && !getEnemyBoard().wasShot(firstHit.getX() - 1, firstHit.getY())) {
                    PossibleShoots pssX = new PossibleShoots(firstHit.getX() - 1, firstHit.getY());
                    result = getEnemyBoard().handleShot(pssX.getX(), pssX.getY());
                    if (!hasDestroyedShip(result)) {
                        if (result == ShotResult.HIT) {
                            lastHit = pssX;
                            targetIsHorizontal = true;
                            targetIsPositive = false;
                        }
                    }
                } else if (getEnemyBoard().isValidY(firstHit.getY() - 1) && !getEnemyBoard().wasShot(firstHit.getX(), firstHit.getY() - 1)) {
                    PossibleShoots pssYN = new PossibleShoots(firstHit.getX(), firstHit.getY() - 1);
                    result = getEnemyBoard().handleShot(pssYN.getX(), pssYN.getY());
                    if (!hasDestroyedShip(result)) {
                        if (result == ShotResult.HIT) {
                            lastHit = pssYN;
                            targetIsHorizontal = false;
                            targetIsPositive = false;
                        }
                    }
                } else {
                    result = getEnemyBoard().handleShot(ps.getX(), ps.getY());
                    if(!hasDestroyedShip(result)){
                        if (result == ShotResult.HIT) {
                            isHuntingMode = true;
                            firstHit = ps;
                        }
                    }
                }
            }
        } else {
            result = getEnemyBoard().handleShot(ps.getX(), ps.getY());
            if(!hasDestroyedShip(result)){
                if (result == ShotResult.HIT) {
                    isHuntingMode = true;
                    firstHit = ps;
                }
            }

        }
        return result;
    }

    public Boolean hasDestroyedShip(ShotResult result) {
        if (result == ShotResult.DESTROYED) {
            firstHit = null;
            lastHit = null;
            targetIsHorizontal = null;
            targetIsPositive = null;
            isHuntingMode = false;
            return true;
        }
        return false;
    }

    public PossibleShoots generateRandom(int startX, int endX, int startY, int endY, Board board) {
        Random rand = new Random();
        int rangeX = endX - startX;
        int rangeY = endY - startY;

        int random_x;
        int random_y;
        PossibleShoots ps;

        do{
            random_x = rand.nextInt(rangeX);
            random_y = rand.nextInt(rangeY);
            ps = new PossibleShoots(random_x, random_y);
        }
        while (getEnemyBoard().wasShot(ps.getX(), ps.getY()) || !isParityCell(ps.getX(), ps.getY()) || !board.isValidX(ps.getX()) || !board.isValidX(ps.getY()));

        return ps;
    }

    public Boolean isParityCell(int x, int y){
        return (x % 2 == 0 && y % 2 == 0) ||(x % 2 != 0 && y % 2 != 0);
    }

}