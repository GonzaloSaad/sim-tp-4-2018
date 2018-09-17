package utn.frc.sim.battleship;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utn.frc.sim.battleship.game.ships.Ship;
import utn.frc.sim.battleship.game.shots.ShotResult;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Board extends Parent {

    public static final int BOARD_HEIGHT = 32;
    public static final int BOARD_WIDTH = 64;
    private static final int CELL_SIZE = 8;

    private List<Ship> ships;
    private int shots;
    private int hits;
    private int health;
    private VBox rows = new VBox();

    public Board() {
        initBoard();
    }

    private void initBoard() {
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            HBox row = new HBox();
            for (int x = 0; x < BOARD_WIDTH; x++) {
                row.getChildren().add(new Cell(x, y));
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
        ships = new ArrayList<>();
        hits = 0;
        shots = 0;
        health = 0;
    }

    public boolean placeShip(Ship ship) {
        if (canPlaceShip(ship)) {
            placeShipInAllCells(ship);
            health += ship.getType().getLength();
            ships.add(ship);
            return true;
        }

        return false;
    }

    private void placeShipInAllCells(Ship ship) {
        switch (ship.getOrientation()) {
            case EAST:
                eastPlace(ship);
                break;
            case WEST:
                westPlace(ship);
                break;
            case NORTH:
                northPlace(ship);
                break;
            case SOUTH:
                southPlace(ship);
                break;
        }
    }

    public boolean canPlaceShip(Ship ship) {

        if (isValidPoint(ship.getX(), ship.getY())) {
            switch (ship.getOrientation()) {
                case EAST:
                    return canBeEastPlaced(ship);
                case WEST:
                    return canBeWestPlaced(ship);
                case NORTH:
                    return canBeNorthPlaced(ship);
                case SOUTH:
                    return canBeSouthPlaced(ship);
            }
        }
        return Boolean.FALSE;
    }

    public ShotResult handleShot(int x, int y) {
        if (isValidPoint(x, y)) {
            Cell cell = getCell(x, y);
            return cell.shoot();
        }
        throw new RuntimeException();
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean wasShot(int x, int y) {
        if (isValidPoint(x, y)) {
            Cell cell = getCell(x, y);
            return cell.wasShot();
        }
        return Boolean.FALSE;
    }

    private Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    private boolean isValidPoint(int x, int y) {
        return isValidX(x) && isValidY(y);
    }

    private boolean isValidX(double x) {
        return x >= 0 && x < BOARD_WIDTH;
    }

    private boolean isValidY(double y) {
        return y >= 0 && y < BOARD_HEIGHT;
    }

    private void northPlace(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = y; i < y + length; i++) {
            if (isValidY(i)) {
                Cell cell = getCell(x, i);
                cell.setShip(ship);
            }
        }
    }

    private void southPlace(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = y - length + 1; i <= y; i++) {
            if (isValidY(i)) {
                Cell cell = getCell(x, i);
                cell.setShip(ship);
            }
        }
    }

    private void eastPlace(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = x; i < x + length; i++) {
            if (isValidX(i)) {
                Cell cell = getCell(i, y);
                cell.setShip(ship);
            }
        }
    }

    private void westPlace(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = x - length + 1; i <= x; i++) {
            if (isValidX(i)) {
                Cell cell = getCell(i, y);
                cell.setShip(ship);
            }
        }
    }

    private boolean canBeNorthPlaced(Ship ship) {

        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = y; i < y + length; i++) {
            if (isValidY(i)) {
                Cell cell = getCell(x, i);
                if (cell.hasShip()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean canBeSouthPlaced(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = y - length + 1; i <= y; i++) {
            if (isValidY(i)) {
                Cell cell = getCell(x, i);
                if (cell.hasShip()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean canBeEastPlaced(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = x; i < x + length; i++) {
            if (isValidX(i)) {
                Cell cell = getCell(i, y);
                if (cell.hasShip()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean canBeWestPlaced(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = x - length + 1; i <= x; i++) {
            if (isValidX(i)) {
                Cell cell = getCell(i, y);
                if (cell.hasShip()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private class Cell extends Rectangle {
        private int x, y;
        private Ship ship;
        private boolean wasShot = false;


        private Cell(int x, int y) {
            super(CELL_SIZE, CELL_SIZE);
            initCell(x, y);
        }

        private void initCell(int x, int y) {
            this.x = x;
            this.y = y;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        private void setShip(Ship ship) {
            this.ship = ship;
            setFill(Color.GREEN);
        }

        private boolean hasShip() {
            return ship != null;
        }

        private boolean wasShot() {
            return wasShot;
        }

        private ShotResult shoot() {
            ShotResult result = ShotResult.MISS;
            if (!wasShot) {
                shots++;
                wasShot = true;
                if (ship != null) {
                    if (ship.isAlive()) {
                        ship.hit();
                        hits++;
                        health--;
                        setFill(Color.RED);
                        if (ship.isAlive()) {
                            result = ShotResult.HIT;
                        } else {
                            result = ShotResult.DESTROYED;
                        }
                    }
                } else {
                    setFill(Color.BLACK);
                }
            }
            return result;
        }
    }


}
