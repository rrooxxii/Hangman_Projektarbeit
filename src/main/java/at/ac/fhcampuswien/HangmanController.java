package at.ac.fhcampuswien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    //Getter/Setter

    public HangmanController getInstanceSingletonController() {
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
        currentStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        currentScene = new Scene(currentRoot);
        currentStage.setScene(currentScene);
        currentStage.show();
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
    public void setPlayer(ActionEvent e) throws IOException {
        String input = playerNumberField.getText();
        try {
            int playerNumber = Integer.parseInt(input);
            if (playerNumber >= 1 && playerNumber <= 4) {
                currentHangmanGame.setPlayerNumber(playerNumber);       //Initialize PlayerArray if possible
                currentHangmanGame.initPlayerArray();
                this.switchScene(e, "hangman-Name.fxml");         // Go to 5th Step
            } else {
                playerNumberField.clear();
                PlayerNumberText.setText("The number is not between 1 and 4.");
                PlayerNumberText.setLayoutX(180);
                System.out.println("The number is not between 1 and 4.");
            }
        } catch (NumberFormatException ex) {
            playerNumberField.clear();
            PlayerNumberText.setText("Please enter a number between 1 and 4");
            PlayerNumberText.setLayoutX(166);
            System.out.println("Please enter a number between 1 and 4.");
        }
    }   //4th Step


    @FXML
    public void setPlayerName(ActionEvent e) throws IOException {
        String name = playerNameField.getText();

        if (playerNameField.getText().length() == 0) {
            playerNameText.setText("Please enter a name");
            return;
        } else {
            currentHangmanGame.getPlayers()[counter] = new HangmanPlayer(name);
            System.out.println("Player "+ (int) (counter+1) + "initialized");
            playerNameField.clear();

            counter ++;
        }
        if (counter ==currentHangmanGame.getPlayerNumber()) {
            this.switchScene(e, "hangman-MainGame.fxml");         // Go to 5th Step
        } else {
            playerNameText.setText("Player "+ (int) (counter+1) +", choose a Name!");
        }

    }   //5th Step

}
