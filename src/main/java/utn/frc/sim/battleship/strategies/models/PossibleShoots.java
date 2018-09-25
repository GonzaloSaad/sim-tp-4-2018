package utn.frc.sim.battleship.strategies.models;

import utn.frc.sim.battleship.game.shots.ShotResult;

import java.util.Objects;

public class PossibleShoots {
    int x;
    int y;
    ShotResult state;

    public PossibleShoots(int x, int y, ShotResult state) {
        this.x = x;
        this.y = y;
        this.state = ShotResult.MISS;
    }

    private ShotResult state() {
        return state;
    }

    public void setState(ShotResult state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}

