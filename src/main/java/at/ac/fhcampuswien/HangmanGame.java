package at.ac.fhcampuswien;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HangmanGame {


    //Variables
        //Housekeeping
    HangmanController controller;
        //Game Variables
    private char[] puzzleArray; //current state of fields
    private char[] wordArray; //word to guess
    private HangmanPlayer[] players;
    private String language;
    private String difficulty;
    private int numberPlayer;

    //Constructor
    //Getter/Setter
    public void setController(HangmanController controller) {
        this.controller = controller;
    }
    public void setLanguage(String language) {
        if (language.equals("english") || language.equals("deutsch")) {
            this.language = language;
        } else {
            System.out.println("Language not recognised");
        }
    }

    //Methods
        //Initializers
    public void initPlayerArray () {
    } //Store Players
    public void initPuzzleArray () {
        puzzleArray = new char[wordArray.length];
        for (int i = 0; i < puzzleArray.length; i++) {
            puzzleArray[i] = '_';

        }
    } //Init with underlines
    private void printPuzzleArray() {
        for (int i = 0; i < puzzleArray.length; i++) {
            System.out.print(puzzleArray[i]+ " ");
        }
        System.out.println();
    } // Print Array



}