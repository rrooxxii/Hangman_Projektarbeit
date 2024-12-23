package at.ac.fhcampuswien;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("cadastre-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        stage.setTitle("My Simple \"Kataster\"");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //Run Test Code for Console Version:
        Hangman currentGame = new Hangman("ThisistheWay", "Vi", "Jinx");
        currentGame.startGame();
        currentGame.playRound();

        launch();


    }
}
