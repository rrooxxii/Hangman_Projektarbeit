package at.ac.fhcampuswien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    //View-States
    private String currentLanguageString;
    private String setlanguageButtonString;
    private String startGameButtonString;
    private String setDifficultyString;
    private String setDifficultyButtonString;

    //FXML Bindings



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

    public void updateElements () {

    }

    @FXML
    public void goToSetLanguage(ActionEvent e) throws IOException {     // 1st Step
        this.currentHangmanGame = new HangmanGame();                    // Initialize GameObject
        System.out.println(this.currentHangmanGame.toString());
        this.switchScene(e, "hangman-Language.fxml");       // Go to 2nd Step
    }

    @FXML
    public void setLanguage(ActionEvent e) throws IOException {         // 2nd Step
        Button button = (Button) e.getSource();                         // Get Button Id

        try {                                                           // Set Language
            if (button.getId().equals("german")) {
            } else if (button.getId().equals("EnglishLanguage")) {
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        } finally {
            this.switchScene(e, "hangman-Difficulty.fxml"); // Go to 3rd Step

        }

    }

    @FXML
    public void setDifficulty(ActionEvent e) throws IOException {       //3rd Step
        Button button = (Button) e.getSource();                         // Get Button Id

        try {                                                           // Set Difficulty
            if (button.getId().equals("easy")) {

            } else if (button.getId().equals("medium")) {

            } else if (button.getId().equals("hard")) {

            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        } finally {
            this.switchScene(e, "hangman-Player.fxml");     // Go to 4th Step

        }

    }

    @FXML
    public void setPlayer(ActionEvent e) throws IOException {           //4thrd Step

        this.switchScene(e, "hangman-MainGame.fxml");
    }


    @FXML
    public void goToGame(ActionEvent e) throws IOException {
        this.switchScene(e, "hangman-MainGame.fxml");

    }

    /*
    @FXML
    public void setDifficulty(ActionEvent e) throws IOException {

        Button button = (Button) e.getSource();
        try {
            if (button.getId().equals("LevelEasy")) {
                this.setDifficultyString = "Easy";
            } else if (button.getId().equals("LevelMedium")) {
                this.setDifficultyString = "Medium";
            } else if (button.getId().equals("LevelHard")) {
                this.setDifficultyString = "Hard";
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        } finally {
            this.switchScene(e, "hangman-StartView.fxml");
            this.levelText.setText("Level: " + this.setDifficultyString);
        }
    }
    */
    @FXML
    public void goToStartGame(ActionEvent e) throws IOException {
        this.switchScene( e, "hangman-MainGame.fxml");
    }

}
