package utn.frc.sim.battleship.game;

public class Shot {
    private final int x;
    private final int y;

    public Shot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
