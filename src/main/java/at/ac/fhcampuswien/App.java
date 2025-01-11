package at.ac.fhcampuswien;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Scanner;

//public class App extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("hangman-View.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
//        stage.setTitle("Hangman 3000");
//        stage.setResizable(false);
//
//        stage.setScene(scene);
//        stage.show();
//    }

public class App{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Run Test Code for Console Version:


        System.out.println("Welcome to Hangman / Willkommen zu Hangman!");

        // Sprachwahl
        String language = "";
        while (!language.equals("en") && !language.equals("de")) {
            System.out.print("Choose your language / Wählen Sie Ihre Sprache (en/de): ");
            language = scanner.nextLine().trim().toLowerCase();
            if (!language.equals("en") && !language.equals("de")) {
                System.out.println("Invalid input. Please choose 'en' or 'de'.");
                System.out.println("Ungültige Eingabe. Bitte wählen Sie 'en' oder 'de'.");
            }
        }

        // Eingabe der Spieleranzahl mit Validierung
        int playerCount = 0;
        while (playerCount < 1 || playerCount > 4) {
            System.out.print(language.equals("de")
                    ? "Geben Sie die Anzahl der Spieler ein (1-4): "
                    : "Enter the number of players (1-4): ");
            try {
                playerCount = Integer.parseInt(scanner.nextLine().trim());
                if (playerCount < 1 || playerCount > 4) {
                    System.out.println(language.equals("de")
                            ? "Die Spieleranzahl muss zwischen 1 und 4 liegen."
                            : "The number of players must be between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println(language.equals("de")
                        ? "Bitte geben Sie eine gültige Zahl ein."
                        : "Please enter a valid number.");
            }
        }

        // Eingabe des Schwierigkeitsgrades mit Validierung
        String difficulty = "";
        while (!difficulty.equals("easy") && !difficulty.equals("medium") && !difficulty.equals("hard")) {
            System.out.print(language.equals("de")
                    ? "Wählen Sie einen Schwierigkeitsgrad (easy, medium, hard): "
                    : "Choose a difficulty level (easy, medium, hard): ");
            difficulty = scanner.nextLine().trim().toLowerCase();
            if (!difficulty.equals("easy") && !difficulty.equals("medium") && !difficulty.equals("hard")) {
                System.out.println(language.equals("de")
                        ? "Ungültiger Schwierigkeitsgrad. Bitte wählen Sie 'easy', 'medium' oder 'hard'."
                        : "Invalid difficulty level. Please choose 'easy', 'medium', or 'hard'.");
            }
        }

        // Starte das Spiel
        Hangman currentGame = new Hangman(playerCount, difficulty);
        currentGame.setLanguage(language); // Sprache setzen
        currentGame.startGame();
        currentGame.playRound();
        currentGame.calculateScoreAll();

        System.out.println(language.equals("de")
                ? "Vielen Dank fürs Spielen!"
                : "Thank you for playing!");
    }
}



