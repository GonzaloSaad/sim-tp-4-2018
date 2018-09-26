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

    public PossibleShoots(int x, int y){
        this(x, y, ShotResult.NONE);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PossibleShoots)) return false;
        PossibleShoots that = (PossibleShoots) o;
        return x == that.x &&
                y == that.y &&
                state == that.state;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y, state);
    }
}

