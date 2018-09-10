package utn.frc.sim.battleship.game.ships;

public enum ShipType {
    PORTAAVION(6,2),
    FRAGATA(5,2),
    SUBMARINO(4,2),
    CORBETA(3,2),
    DESTRUCTORES(2,2);

    private int length;
    private int amount;

    ShipType(int length, int amount){
        this.length = length;
        this.amount = amount;
    }

    public int getLength() {
        return length;
    }

    public int getAmount() {
        return amount;
    }
}
