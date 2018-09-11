package utn.frc.sim.battleship;

import utn.frc.sim.battleship.strategies.RandomStrategy;

public class BattleShip {

    private Player player_1;
    private Player player_2;

    int result;


    public BattleShip() {
        initGame();
    }

    private void initGame() {
        player_1 = new Player(new RandomStrategy());
        player_2 = new Player(new RandomStrategy());
        player_1.setEnemy(player_2);
        player_2.setEnemy(player_1);
    }

    public Board getBoardPlayer1() {
        return player_1.getBoard();
    }

    public Board getBoardPlayer2() {
        return player_2.getBoard();
    }

    public int startGame() {
        while (gameRunning()) {
            player_1.play();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(player_2.isAlive()){
                player_2.play();
            }
        }
        if (player2Alive()) {
            System.out.println("Player 2 won.");
            return 2;
        } else{
            System.out.println("Player 1 won.");
            return 1;
        }
        /*System.out.println("player1 hits: "+ player_1.getHits());
        System.out.println("player1 shots:" + player_1.getShots());
        System.out.println("player2 hits: "+ player_2.getHits());
        System.out.println("player2 shots: "+ player_2.getShots());*/
    }

    private boolean gameRunning() {
        return player1Alive() && player2Alive();
    }

    private boolean player1Alive() {
        return player_1.isAlive();
    }

    private boolean player2Alive(){
        return player_2.isAlive();
    }
}
