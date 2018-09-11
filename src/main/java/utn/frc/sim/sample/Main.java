package utn.frc.sim.sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utn.frc.sim.battleship.BattleShip;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1024, 728));
        primaryStage.setOnCloseRequest(e -> {Platform.exit(); System.exit(0);});

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
      /* ExecutorService service =  Executors.newFixedThreadPool(4);
       service.submit(Main::run);*/

    }

    private static void run(){
        int p1 = 0;
        int p2 = 0;
        for (int i = 0; i < 1000; i++) {
            System.out.println("Game: " + (i+1));
            int result = new BattleShip().startGame();
            if (result ==1){
                p1++;
            }else {
                p2++;
            }
        }
        System.out.println(p1);
        System.out.println(p2);
    }
}
