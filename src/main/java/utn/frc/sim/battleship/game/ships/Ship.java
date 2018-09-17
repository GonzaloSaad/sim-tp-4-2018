package utn.frc.sim.battleship.game.ships;

import javafx.scene.Parent;
import utn.frc.sim.battleship.game.ships.Orientation;
import utn.frc.sim.battleship.game.ships.ShipType;

public class Ship extends Parent {

    private final int x;
    private final int y;
    private final ShipType type;
    private final Orientation orientation;
    private int health;

    public Ship(int x, int y, ShipType type, Orientation orientation) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.health = type.getLength();
        this.orientation = orientation;
    }

    public void hit() {
        if (isAlive()) {
            health--;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ShipType getType() {
        return type;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isVertical(){
        return orientation == Orientation.NORTH || orientation==Orientation.SOUTH;
    }

    public boolean isHorizontal(){
        return orientation == Orientation.EAST || orientation==Orientation.WEST;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "x=" + x +
                ", y=" + y +
                ", type=" + type +
                ", orientation=" + orientation +
                ", health=" + health +
                '}';
    }
}
