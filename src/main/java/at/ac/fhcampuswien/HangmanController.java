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
    //ViewHandling
    private Stage currentStage;
    private Scene currentScene;
    private Parent currentRoot;
    //GameInstanz
    private HangmanGame currentHangmanGame;
    //View-States
    private String currentLanguageString;
    private String setlanguageButtonString;
    private String startGameButtonString;
    private String setDifficultyString;
    private String setDifficultyButtonString;

    //FXML Bindings
    @FXML
    private Text currentLanguage = new Text("Test");
    @FXML
    private Button setLanguageButton = new Button();
    @FXML
    private Button startGameButton = new Button();
    @FXML
    private Button setDifficultyButton = new Button();
    @FXML
    private Text levelText = new Text("Level: ");


    //Constructor
    protected HangmanController() {

        System.out.println("Trying to create ControllerHangman");
        HangmanGame newHangman = new HangmanGame();
        this.currentHangmanGame = newHangman;
        currentHangmanGame.setLanguage("en");
        currentHangmanGame.setControllerHangman(this);
        System.out.println(currentHangmanGame.toString());
        System.out.println("GameCreated");

    }

    //Getter/Setter

    public HangmanController getInstanceSingletonController() {
        System.out.println("Trying to get ControllerHangman instance");
        if (gameController == null) {
            gameController = new HangmanController();
        }
        System.out.println("Trying to return ControllerHangman instance");
        return gameController;
    }

    //Methods

    public void updateLanguageField(String language) {
        this.currentLanguage.setText(language);
    }

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
    public void goToSetLanguage(ActionEvent e) throws IOException {
        this.switchScene(e, "hangman-ChangeLanguage.fxml");
    }

    @FXML
    public void goToSetDifficulty(ActionEvent e) throws IOException {
        this.switchScene(e, "hangman-Level.fxml");
    }

    @FXML
    public void goToGame(ActionEvent e) throws IOException {
        this.switchScene(e, "hangman-MainGame.fxml");

    }


    @FXML
    public void setLanguage(ActionEvent e) throws IOException {
        Button button = (Button) e.getSource();

        try {
            if (button.getId().equals("GermanLanguage")) {
                this.currentLanguageString = "Derzeitige Sprache: Deutsch";
                this.setlanguageButtonString = "Sprache Ändern";
                this.startGameButtonString = "Spiel starten";
                this.currentHangmanGame.setLanguage("de");


            } else if (button.getId().equals("EnglishLanguage")) {
                this.currentLanguageString = "Current Language: English";
                this.setlanguageButtonString = "Change Language";
                this.startGameButtonString = "Start Game";
                this.currentHangmanGame.setLanguage("en");

            }

        } catch (Exception exception) {
            System.out.println(exception.toString());
        } finally {
            this.switchScene(e, "hangman-StartView.fxml");
            this.currentLanguage.setText(this.currentLanguageString);
            this.setLanguageButton.setText(this.setlanguageButtonString);
            this.startGameButton.setText(this.startGameButtonString);
            this.setDifficultyButton.setText(this.setDifficultyString);
        }

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
