package utn.frc.sim.battleship.game.ships;

public enum ShipType {
    PORTAAVION(6),
    FRAGATA(5),
    SUBMARINO(4),
    CORBETA(3),
    DESTRUCTORES(2);

    private int length;

    ShipType(int length){
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
