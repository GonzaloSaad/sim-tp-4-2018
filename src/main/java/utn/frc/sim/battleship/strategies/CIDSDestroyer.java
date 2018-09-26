package utn.frc.sim.battleship.strategies;

import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.exceptions.ConcurrentFailureException;
import utn.frc.sim.battleship.game.shots.Shot;
import utn.frc.sim.battleship.game.shots.ShotResult;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TreeSet;

public class CIDSDestroyer extends RandomStrategy {

    private static final int HIT_SHOT_SCALE = 40;
    private static final int X_HALF_BOARD = Board.BOARD_WIDTH / 2;
    private static final int Y_HALF_BOARD = Board.BOARD_HEIGHT / 2;
    private static final int REGION_1 = 0;
    private static final int REGION_2 = 1;
    private static final int REGION_3 = 2;
    private static final int REGION_4 = 3;
    private static final int[] NEIGHBORS_INDEX_ADJUSTMENT_HUNTING = new int[]{-1, 0, 1};


    private PossibleShot[][] possibleShots;
    private List<RegionOfShooting> regions;
    private List<Shot> hits;
    private State state;
    private Random random;
    private boolean randomFromRegions;

    public CIDSDestroyer() {
        super();
        initialize();
    }

    private void initialize() {
        random = new Random();
        randomFromRegions = true;
        startHits();
        startState();
        startRegionOfShooting();
        startPossibleShots();
    }

    private void startHits() {
        hits = new ArrayList<>();
    }

    private void startState() {
        state = State.RANDOM;
    }

    private void startRegionOfShooting() {
        regions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            regions.add(new RegionOfShooting());
        }
    }

    private void startPossibleShots() {
        possibleShots = new PossibleShot[Board.BOARD_WIDTH][Board.BOARD_HEIGHT];
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                PossibleShot shot = new PossibleShot(j, i, 1);
                possibleShots[j][i] = shot;
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    addShotToRegion(shot);
                }
            }
        }
    }

    private void addShotToRegion(PossibleShot shot) {
        getRegionOfShot(shot).addPossibleShot(shot);
    }

    private RegionOfShooting getRegionOfShot(PossibleShot shot) {
        int region = getRegionIndexOfShot(shot);
        return regions.get(region);
    }

    private int getRegionIndexOfShot(PossibleShot shot) {
        int x = shot.getX();
        int y = shot.getY();
        if (x < X_HALF_BOARD) {
            if (y < Y_HALF_BOARD) {
                return REGION_1;
            } else {
                return REGION_2;
            }
        } else {
            if (y < Y_HALF_BOARD) {
                return REGION_3;
            } else {
                return REGION_4;
            }
        }
    }

    @Override
    public ShotResult getNextShot() {
        PossibleShot shot = getBestPossibleShot();
        shot.use();
        ShotResult result = getEnemyBoard().handleShot(shot.getX(), shot.getY());
        addResultToRegion(result, shot);
        addShotIfHits(result, shot);
        handleStateChange(result);
        return result;
    }

    private void addResultToRegion(ShotResult result, PossibleShot shot) {
        getRegionOfShot(shot).addShot(result != ShotResult.MISS);
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

    private void increaseProbabilitiesForHits() {
        hits.stream()
                .map(this::getNeighbors)
                .flatMap(List::stream)
                .filter(PossibleShot::isAvailable)
                .forEach(possibleShot -> possibleShot.scaleWeight(HIT_SHOT_SCALE));

    }

    private PossibleShot getBestPossibleShot() {

        if (state == State.HUNTING) {
            return getBestPossibleHuntingShot();
        } else {
            return getBestPossibleRandomShot();
        }
    }

    private PossibleShot getBestPossibleRandomShot() {
        if (randomFromRegions) {
            try {
                return getPossibleShotFromRegions();
            } catch (IllegalStateException e) {
                randomFromRegions = false;
            }
        }

        return lookForUnusedPossibleShot();


    }

    private PossibleShot getPossibleShotFromRegions() {
        PossibleShot possibleShot;
        int count = 0;
        do {
            RegionOfShooting region;
            int regionCount = 0;
            do {
                region = regions.get(random.nextInt(regions.size()));
                regionCount++;
                if (regionCount > 150) {
                    throw new IllegalStateException();
                }
            } while (region.empty());
            possibleShot = region.getPossibleShot();
            count++;
            if (count > 150) {
                throw new IllegalStateException();
            }
        } while (possibleShot.isUsed());
        return possibleShot;
    }

    private PossibleShot lookForUnusedPossibleShot() {
        Shot shot = lookForUnusedShot();
        return possibleShots[shot.getX()][shot.getY()];
    }

    private PossibleShot getBestPossibleHuntingShot() {
        TreeSet<PossibleShot> orderedShots = new TreeSet<>();
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                PossibleShot shot = possibleShots[j][i];
                if (shot.isAvailable()) {
                    orderedShots.add(shot);
                }
            }
        }
        if (orderedShots.isEmpty()) {
            throw new ConcurrentFailureException();
        }
        return orderedShots.pollFirst();
    }

    private List<PossibleShot> getNeighbors(Shot shot) {
        List<PossibleShot> neighbors = new ArrayList<>();
        for (int i : NEIGHBORS_INDEX_ADJUSTMENT_HUNTING) {
            for (int j : NEIGHBORS_INDEX_ADJUSTMENT_HUNTING) {
                if (!(i == 0 && j == 0)) {
                    if (i == 0 || j == 0) {
                        int x = shot.getX() + i;
                        int y = shot.getY() + j;
                        if (isValid(x, y)) {
                            neighbors.add(possibleShots[x][y]);
                        }
                    }
                }
            }
        }
        return neighbors;

    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < Board.BOARD_WIDTH && y >= 0 && y < Board.BOARD_HEIGHT;
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

        private PossibleShot(int x, int y, int weight) {
            this.weight = weight;
            this.x = x;
            this.y = y;
            this.used = false;
        }

        private int getWeight() {
            return weight;
        }

        private void scaleWeight(int factor) {
            weight *= factor;
        }

        private boolean isUsed() {
            return used;
        }

        private boolean isAvailable() {
            return !used;
        }

        private void use() {
            used = true;
        }

        private int getX() {
            return x;
        }

        private int getY() {
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

    private class RegionOfShooting {
        private List<PossibleShot> shotsForExploring;
        private int hits;
        private int shots;

        public RegionOfShooting() {
            shotsForExploring = new ArrayList<>();
            hits = 0;
            shots = 0;
        }

        public void addPossibleShot(PossibleShot shot) {
            shotsForExploring.add(shot);
        }

        public PossibleShot getPossibleShot() {
            return shotsForExploring.get(random.nextInt(shotsForExploring.size()));
        }

        public double getRatioOfHits() {
            if (shots == 0) {
                return 0;
            }
            return (double) hits / shots;
        }

        public void addShot(boolean hit) {
            shots++;
            if (hit) {
                hits++;
            }
        }

        public boolean empty() {
            for (PossibleShot shot : shotsForExploring) {
                if (shot.isAvailable()) {
                    return false;
                }
            }
            return true;
        }
    }
}
