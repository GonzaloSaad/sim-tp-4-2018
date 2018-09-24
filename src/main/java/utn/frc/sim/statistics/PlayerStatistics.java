package utn.frc.sim.statistics;

public class PlayerStatistics {
    int wonMatches;
    int avgShotToWin;
    double avgAccuracy;


    public PlayerStatistics() {
        wonMatches = 0;
        avgShotToWin = 0;
        avgAccuracy = 0;
    }

    public int getWonMatches() {
        return wonMatches;
    }

    public int getAvgShotToWin() {
        return avgShotToWin;
    }

    public double getAvgAccuracy() {
        return avgAccuracy;
    }

    public void addAccuracy(double accuracy, int i){
        if (i == 1) {
            avgAccuracy = accuracy;
        } else {
            avgAccuracy = ((i - 1) * avgAccuracy + accuracy) / i;
        }
    }

    public void addWonMatch(){
        wonMatches++;
    }

    public void addShotsToWin(int shotsToWin){
        if (wonMatches == 1) {
            avgShotToWin = shotsToWin;
        } else {
            avgShotToWin = ((wonMatches - 1) * avgShotToWin + shotsToWin) / wonMatches;
        }
    }
}
