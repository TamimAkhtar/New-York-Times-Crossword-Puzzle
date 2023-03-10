package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("PLUTO - New York Times Mini Crossword Puzzle Solver");
        primaryStage.setScene(new Scene(root, 1800, 1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
