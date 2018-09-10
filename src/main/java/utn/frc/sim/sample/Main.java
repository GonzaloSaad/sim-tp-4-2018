package utn.frc.sim.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.Ship;
import utn.frc.sim.battleship.game.ships.Orientation;
import utn.frc.sim.battleship.game.ships.ShipType;
import utn.frc.sim.battleship.strategies.BasicStrategy;
import utn.frc.sim.battleship.strategies.BattleShipStrategy;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BattleShipStrategy shipStrategy = new BasicStrategy();
        //Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(shipStrategy.getBoardForPlay(), 1024, 728));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        /*Board board = new Board();

        Ship ship1 = new Ship(150,30, ShipType.CORBETA, Orientation.NORTH);
        Ship ship2 = new Ship(60,30, ShipType.CORBETA, Orientation.EAST);
        Ship ship3 = new Ship(10,30, ShipType.CORBETA, Orientation.NORTH);

        System.out.println(board.placeShip(ship1));
        System.out.println(board.placeShip(ship2));
        System.out.println(board.placeShip(ship3));*/


    }
}
