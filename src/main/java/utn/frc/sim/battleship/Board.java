package utn.frc.sim.battleship;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utn.frc.sim.battleship.game.Ship;
import utn.frc.sim.battleship.game.ShotResult;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Board extends Parent {

    public static final int BOARD_HEIGHT = 32;
    public static final int BOARD_WIDTH = 64;

    private List<Ship> ships;
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
    }

    public boolean placeShip(Ship ship) {
        if (canPlaceShip(ship)) {
            int x = ship.getX();
            int y = ship.getY();
            Cell cellForShip = getCell(x, y);
            cellForShip.setShip(ship);
            ships.add(ship);
            return true;
        }

        return false;
    }

    //TODO go to all of the shipssss!!!!!
    private void placeShipInAllCells(Ship ship){
        int x = ship.getX();
        int y = ship.getY();
        Cell cellForShip = getCell(x, y);
        cellForShip.setShip(ship);
    }

    private boolean canPlaceShip(Ship ship) {

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

        for (int i = x + length + 1; i <= x; i++) {
            if (isValidX(i)) {
                Cell cell = getCell(i, y);
                if (cell.hasShip()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    private boolean isValidPoint(double x, double y) {
        return isValidX(x) && isValidY(y);
    }

    private boolean isValidX(double x) {
        return x >= 0 && x < BOARD_WIDTH;
    }

    private boolean isValidY(double y) {
        return y >= 0 && y < BOARD_HEIGHT;
    }

    public class Cell extends Rectangle {
        private int x, y;
        private Ship ship;
        private boolean wasShot = false;


        private Cell(int x, int y) {
            super(10, 10);
            initCell(x, y);
        }

        private void initCell(int x, int y) {
            this.x = x;
            this.y = y;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
            setOnMouseClicked(event -> shoot());
        }

        public void setShip(Ship ship) {
            this.ship = ship;
            setFill(Color.WHITE);
            setStroke(Color.GREEN);
        }

        public boolean hasShip() {
            return ship != null;
        }

        public ShotResult shoot() {
            ShotResult result = ShotResult.MISS;
            if (!wasShot) {
                wasShot = true;
                if (ship != null) {
                    ship.hit();
                    setFill(Color.RED);
                    result = ShotResult.HIT;
                    System.out.println("hitted");
                } else {
                    setFill(Color.BLACK);
                }
            }
            return result;
        }


    }
}
