package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.ships.Orientation;
import utn.frc.sim.battleship.game.ships.Ship;
import utn.frc.sim.battleship.game.ships.ShipType;
import utn.frc.sim.battleship.game.shots.Shot;
import utn.frc.sim.battleship.game.shots.ShotResult;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CIDSDestroyer extends RandomStrategy {

    private static final int HIT_SHOT_SCALE = 40;
    private static final int[] NEIGHBORS_INDEX_ADJUSTMENT = new int[]{-1, 0, 1};
    private PossibleShot[][] possibleShots;
    private List<Shot> hits;
    private State state;

    public CIDSDestroyer() {
        super();
        initialize();
    }

    private void initialize() {
        startShots();
    }

    private void startShots() {
        hits = new ArrayList<>();
        state = State.RANDOM;
        possibleShots = new PossibleShot[Board.BOARD_WIDTH][Board.BOARD_HEIGHT];
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                PossibleShot shot = new PossibleShot(j, i, 1);
                possibleShots[j][i] = shot;
            }
        }
    }

    @Override
    public ShotResult getNextShot() {
        try {
            PossibleShot shot = getBestPossibleShot();
            shot.use();
            ShotResult result = getEnemyBoard().handleShot(shot.getX(), shot.getY());
            addShotIfHits(result, shot);
            handleStateChange(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }

    private void handleStateChange(ShotResult result) {
        if (state == State.RANDOM) {
            if (result != ShotResult.MISS) {
                state = State.HUNTING;
            }
        } else {
            if (result == ShotResult.DESTROYED) {
                state = State.RANDOM;
            }
        }
    }

    private void addShotIfHits(ShotResult result, PossibleShot possibleShot) {
        if (result == ShotResult.HIT) {
            hits.add(new Shot(possibleShot.getX(), possibleShot.getY()));
            increaseProbabilitiesForHits();
        }
    }

    private void updateProbabilities() {
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                PossibleShot shot = possibleShots[j][i];
                if (shot.isAvailable()) {
                    shot.setWeight(1);
                    for (ShipType type : ShipType.values()) {
                        for (Orientation orientation : Orientation.values()) {
                            Ship ship = new Ship(shot.getX(), shot.getY(), type, orientation);
                            updateProbabilitiesForShip(ship);
                        }
                    }
                } else {
                    shot.setWeight(0);
                }
            }
        }
        increaseProbabilitiesForHits();
    }

    private void increaseProbabilitiesForHits() {
        hits.stream()
                .map(this::getNeighbors)
                .flatMap(List::stream)
                .filter(PossibleShot::isAvailable)
                .forEach(possibleShot -> possibleShot.scaleWeight(HIT_SHOT_SCALE));

    }

    private List<PossibleShot> getNeighbors(Shot shot) {
        List<PossibleShot> neighbors = new ArrayList<>();
        for (int i : NEIGHBORS_INDEX_ADJUSTMENT) {
            for (int j : NEIGHBORS_INDEX_ADJUSTMENT) {
                if (!(i == 0 && j == 0)) {
                    int x = shot.getX() + i;
                    int y = shot.getY() + j;
                    if (isValid(x, y)) {
                        neighbors.add(possibleShots[x][y]);
                    }
                }
            }
        }
        return neighbors;

    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < Board.BOARD_WIDTH && y >= 0 && y < Board.BOARD_HEIGHT;
    }

    private void updateProbabilitiesForShip(Ship ship) {
        switch (ship.getOrientation()) {
            case EAST:
                eastUpdate(ship);
                break;
            case WEST:
                westUpdate(ship);
                break;
            case NORTH:
                northUpdate(ship);
                break;
            case SOUTH:
                southUpdate(ship);
                break;
        }
    }

    private void northUpdate(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = y; i < y + length; i++) {
            if (!isValid(x, i) || possibleShots[x][i].isUsed()) {
                return;
            }
        }

        for (int i = y; i < y + length; i++) {
            PossibleShot shot = possibleShots[x][i];
            if (shot.isAvailable()) {
                shot.increaseWeight();
            }
        }
    }

    private void southUpdate(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = y - length + 1; i <= y; i++) {
            if (!isValid(x, i) || possibleShots[x][i].isUsed()) {
                return;
            }
        }

        for (int i = y - length + 1; i <= y; i++) {
            PossibleShot shot = possibleShots[x][i];
            if (shot.isAvailable()) {
                shot.increaseWeight();
            }
        }
    }

    private void eastUpdate(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = x; i < x + length; i++) {
            if (!isValid(i, y) || possibleShots[i][y].isAvailable()) {
                return;
            }
        }

        for (int i = x; i < x + length; i++) {
            PossibleShot shot = possibleShots[i][y];
            if (shot.isAvailable()) {
                shot.increaseWeight();
            }
        }
    }

    private void westUpdate(Ship ship) {
        int y = ship.getY();
        int x = ship.getX();
        int length = ship.getType().getLength();

        for (int i = x - length + 1; i <= x; i++) {
            if (!isValid(i, y) || possibleShots[i][y].isAvailable()) {
                return;
            }
        }

        for (int i = x - length + 1; i <= x; i++) {
            PossibleShot shot = possibleShots[i][y];
            if (shot.isAvailable()) {
                shot.increaseWeight();
            }
        }
    }

    private PossibleShot getBestPossibleShot() {

        if (state == State.HUNTING){
            TreeSet<PossibleShot> orderedShots = new TreeSet<>();
            for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
                for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                    PossibleShot shot = possibleShots[j][i];
                    if (shot.isAvailable()) {
                        orderedShots.add(shot);
                    }
                }
            }
            return orderedShots.pollFirst();
        } else {
            return possibleShots[getRandomX()][getRandomY()];
        }
    }

    private enum State {
        RANDOM,
        HUNTING
    }

    private class PossibleShot implements Comparable<PossibleShot> {
        int weight;
        int x;
        int y;
        boolean used;

        public PossibleShot(int x, int y) {
            this(x, y, 0);
        }

        public PossibleShot(int x, int y, int weight) {
            this.weight = weight;
            this.x = x;
            this.y = y;
            this.used = false;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void scaleWeight(int factor) {
            weight *= factor;
        }

        public void increaseWeight() {
            weight++;
        }

        public boolean isUsed() {
            return used;
        }

        public boolean isAvailable() {
            return !used;
        }

        public void use() {
            used = true;
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
            if (o == null || getClass() != o.getClass()) return false;
            PossibleShot that = (PossibleShot) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public int compareTo(PossibleShot o) {
            return this.weight < o.weight ? o.weight - this.weight : -1;
        }
    }

    private String printMatrix() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                PossibleShot shot = possibleShots[j][i];
                stringBuilder.append(shot.getWeight());
                stringBuilder.append("\t");

            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


}
