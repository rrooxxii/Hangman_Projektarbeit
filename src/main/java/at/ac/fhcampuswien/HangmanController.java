package at.ac.fhcampuswien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class HangmanController {

    //The Singleton Controller
    private static HangmanController gameController;
    //GameInstanz
    private HangmanGame currentHangmanGame;
    //ViewHandling
    private Stage currentStage;
    private Scene currentScene;
    private Parent currentRoot;
    //FXML Bindings
    @FXML
    private TextField playerNumberField;
    @FXML
    private Text PlayerNumberText;
    @FXML
    private TextField playerNameField;
    @FXML
    private Text playerNameText;
    @FXML
    private Label puzzleLabel; //Label, um das aktuelle Puzzle darzustellen
    @FXML
    private Label playerInfoLabel; //Label, um Spielerinformationen anzuzeigen
    @FXML
    private TextField letterInputField; //Eingabefeld für Buchstaben
    @FXML
    private Label feedbackLabel;  //Label, um Rückmeldungen anzuzeigen
    //Hilfscounter
    private int counter = 0;



    //Constructor
    protected HangmanController() {
        HangmanGame newHangman = new HangmanGame();
        this.currentHangmanGame = newHangman;
        currentHangmanGame.setLanguage("english");
        currentHangmanGame.setController(this);
        System.out.println(currentHangmanGame.toString());
        System.out.println("GameCreated");

    }


    public static HangmanController getInstanceSingletonController() {
        if (gameController == null) {
            gameController = new HangmanController();
        }
        return gameController;
    }

    //Methods


    public void switchScene(ActionEvent e, String fxmlFileString) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HangmanController.class.getResource(fxmlFileString));
        fxmlLoader.setController(gameController); // Reuse the same controller
        currentRoot = fxmlLoader.load();
        currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        currentScene = new Scene(currentRoot);
        currentStage.setScene(currentScene);
        currentStage.show();
    }

    @FXML
    public void initialize() {
        try {
            // Schwierigkeitsgrad ist ein Beispiel, später dynamisch vom Benutzer festlegen
            currentHangmanGame.setDifficulty("easy");
            currentHangmanGame.initPuzzleArray();
            updateGameDisplay();
        } catch (Exception e) {
            feedbackLabel.setText("Error initializing game: " + e.getMessage());
        }
    }

    private void updateGameDisplay() {
        //Update puzzle
        puzzleLabel.setText(String.valueOf(currentHangmanGame.getPuzzleArray()));
        //Uppdate player info
        HangmanPlayer currentPlayer = currentHangmanGame.getCurrentPlayer();
        playerInfoLabel.setText("Player: " + currentPlayer.getName() + ", Lives: " + currentPlayer.getLives());
    }

    @FXML
    public void guessLetter(ActionEvent e) {
        String input = letterInputField.getText().toLowerCase().trim();
        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            feedbackLabel.setText("Invalid input. Enter a single letter.");
            return;
        }

        char guessedLetter = input.charAt(0);
        boolean isCorrect = currentHangmanGame.guessLetter(guessedLetter);

        if (isCorrect) {
            feedbackLabel.setText("Correct Guess");
        } else {
            feedbackLabel.setText("Wrong guess!");
            currentHangmanGame.getCurrentPlayer().reduceLives();
        }

        updateGameDisplay();

        if (isGameOver()) {
            feedbackLabel.setText("Game Over! The word was: " + String.valueOf(currentHangmanGame.getWordArray()));
            return;
        }

        currentHangmanGame.switchToNextPlayer();
        updateGameDisplay();

        letterInputField.clear();

    }

    private boolean isGameOver() {
        for (HangmanPlayer player : currentHangmanGame.getPlayers()) {
            if (player.getLives() > 0 && !isPuzzleSolved()) {
                return false;
            }
        }
        return true;
    }

    private boolean isPuzzleSolved() {
        return String.valueOf(currentHangmanGame.getPuzzleArray()).equals(String.valueOf(currentHangmanGame.getWordArray()));
    }


    @FXML
    public void pressStart(ActionEvent e) throws IOException {
        this.currentHangmanGame = new HangmanGame();                    // Initialize GameObject
        System.out.println(this.currentHangmanGame.toString());
        this.switchScene(e, "hangman-Language.fxml");       // Go to 2nd Step
    }   // 1st Step

    @FXML
    public void setLanguage(ActionEvent e) throws IOException {
        Button button = (Button) e.getSource();                         // Set Language
        this.currentHangmanGame.setLanguage((String) button.getId());
        this.switchScene(e, "hangman-Difficulty.fxml");     // Go to 3rd Step
    }   // 2nd Step

    @FXML
    public void setDifficulty(ActionEvent e) throws IOException {
        Button button = (Button) e.getSource();                         // Set Language
        this.currentHangmanGame.setDifficulty((String) button.getId());
        this.switchScene(e, "hangman-Player.fxml");         // Go to 4th Step

    }   //3rd Step

    @FXML
    public void setPlayers(ActionEvent e) throws IOException {
        int playerNumber;
        try {
            playerNumber = Integer.parseInt(playerNumberField.getText().trim());
            if (playerNumber < 1 || playerNumber > 4) {
                feedbackLabel.setText("Please enter a number between 1 and 4.");
                return;
            }
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Invalid number. Please enter a number between 1 and 4.");
            return;
        }

        currentHangmanGame.setPlayerNumber(playerNumber);
        String[] playerNames = new String[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            playerNames[i] = "Player " + (i + 1); // Standardnamen oder Eingabe durch Benutzer
        }
        currentHangmanGame.initPlayerArray(playerNames);

        this.switchScene(e, "hangman-MainGame.fxml");
    }//4th Step


    @FXML
    public void setPlayerName(ActionEvent e) throws IOException {
        String name = playerNameField.getText();

        if (playerNameField.getText().length() == 0) {
            playerNameText.setText("Please enter a name");
            return;
        } else {
            currentHangmanGame.getPlayers()[counter] = new HangmanPlayer(name);
            System.out.println("Player " + (int) (counter + 1) + "initialized");
            playerNameField.clear();

            counter++;
        }
        if (counter == currentHangmanGame.getPlayerNumber()) {
            this.switchScene(e, "hangman-MainGame.fxml");         // Go to 5th Step (Game Scene)
        } else {
            playerNameText.setText("Player " + (int) (counter + 1) + ", choose a Name!");
        }

    }
}


