package utn.frc.sim.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utn.frc.sim.battleship.BattleShip;
import utn.frc.sim.battleship.Board;
import utn.frc.sim.battleship.game.Ship;
import utn.frc.sim.battleship.game.ships.Orientation;
import utn.frc.sim.battleship.game.ships.ShipType;
import utn.frc.sim.battleship.strategies.BasicStrategy;
import utn.frc.sim.battleship.strategies.BattleShipStrategy;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1024, 728));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
