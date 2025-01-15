package at.ac.fhcampuswien;

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
    private int playerNumber;

    //Constructor
    //Getter/Setter
    public void setController(HangmanController controller) {
        this.controller = controller;
    }
    public void setLanguage(String language) {
        if (language.equals("english") || language.equals("german")) {
            this.language = language;
            System.out.println("Language set to " + language);
        } else {
            System.out.println("Language not recognised");
        }
    }
    public void setDifficulty(String difficulty) {
        if (difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard")) {
            this.difficulty = difficulty;
            System.out.println("Difficulty set to " + difficulty);
        } else {
            System.out.println("Difficulty not recognised");
        }

    }
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
    public int getPlayerNumber() {
        return playerNumber;
    }
    public HangmanPlayer[] getPlayers() {
        return players;
    }

    //Methods
        //Initializers
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
    public void initPlayerArray () {
        players = new HangmanPlayer[playerNumber];
        System.out.println("PlayerArray initialized to size " + playerNumber );

    }


}