package at.ac.fhcampuswien;
import java.util.Scanner;

class Hangman {
private PlayerHangman player1;
private PlayerHangman player2;
public Scanner inputScanner = new Scanner(System.in);
private char[] puzzleArray;
private char[] wordArray;

PlayerHangman[] playerList = new PlayerHangman[2];
public int playerCounter = 0;



//Constructor
    //Wir geben Wort ein es gibt fix 2 Spieler
    public Hangman(String word, String player1Name, String player2Name) {
        playerList[0] = player1 = new PlayerHangman(player1Name);
        playerList[1] = player2 = new PlayerHangman(player2Name);
        this.wordArray = word.toCharArray();
        this.initPuzzleArray();

    }

    //Wir holen uns das Wort aus einem File
    public Hangman(){
        //Implement Random Word from List

    }


//Getter_Setter



//Methoden

    //Initialize Puzzle with just Underlines
    private boolean checkIfWon () {
        for (int i = 0; i < puzzleArray.length; i++) {
            if (puzzleArray[i] == '_') {
                return false;
            }
        }
        return true;
    }

    //Print the currentState of the Puzzle
    private void printPuzzleArray() {
        for (int i = 0; i < puzzleArray.length; i++) {
            System.out.print(puzzleArray[i]+ " ");
        }
        System.out.println();
    }
    //Initialize Puzzle with just Underlines
    private void initPuzzleArray () {
        puzzleArray = new char[wordArray.length];
        for (int i = 0; i < puzzleArray.length; i++) {
            puzzleArray[i] = '_';

        }
    }

    //Check where the letters are in the array
    private void updatePuzzleArray(char guess) {
        for (int i = 0; i < puzzleArray.length; i++) {
            if( wordArray[i] == guess) {
                puzzleArray[i] = guess;
            }
        }

    }

    //Check if the guessed Letter is part of Word
    private boolean isPartOfWord(char letter) {
        for (int i = 0; i < wordArray.length; i++) {
            if( wordArray[i] == letter) {
                return true;
            }
        }
        return false;
    }

    //Check if the guessed Letter was guessed already
    private boolean guessedAlready(char letter) {
        for (int i = 0; i < puzzleArray.length; i++) {
            if (puzzleArray[i] == letter) {
                return true;
            }
        }
        return false;
    }

    //Start Game
    public void startGame(){

        System.out.println("Welcome to Hangman!");
        System.out.println("Player 1 Please enter your name: ");
        player1.setName(inputScanner.nextLine());
        System.out.println("Player 2 Please enter your name: ");
        player2.setName(inputScanner.nextLine());
        System.out.println("Hi "
                            + player1.getName()
                            +" & "
                            + player2.getName()
                            + "! Welcome to this game of Hangman Legends! ");
        System.out.println("The Word u will be looking for is " + puzzleArray.length + " characters long. ");
        System.out.println("Good Luck!\n" );

    }

    //Loop to Play Rounds (main game Logic)
    public void playRound() {
        boolean hasWon = false;

        while (!hasWon) {
            PlayerHangman activePlayer = playerList[playerCounter];
            System.out.println("It's your turn " + activePlayer.getName() + "!");
            System.out.println("This is the current playing field:");
            this.printPuzzleArray();

            // Take input and validate if input was given
            String guessString = inputScanner.nextLine().trim();
            while (guessString.isEmpty()) {
                System.out.println("Invalid input. Please enter a letter:");
                guessString = inputScanner.nextLine().trim();
            }
            char guess = guessString.charAt(0);

            // Check if the letter was already guessed
            while (this.guessedAlready(guess)) {
                System.out.println("You already guessed this letter, try again!");
                guessString = inputScanner.nextLine().trim();
                while (guessString.isEmpty()) {
                    System.out.println("Invalid input. Please enter a letter:");
                    guessString = inputScanner.nextLine().trim();
                }
                guess = guessString.charAt(0);
            }

            // Check if the guess is part of the word
            if (this.isPartOfWord(guess)) {
                this.updatePuzzleArray(guess);
                this.printPuzzleArray();
                if (this.checkIfWon()) {
                    hasWon = true;
                    System.out.println("Congrats " + activePlayer.getName() + "! You won!");
                }
            } else {
                if (activePlayer.reduceLives()) {
                    System.out.println("Wrong! You have lost a life! You have "
                            + activePlayer.getLives() + " remaining.");
                    this.playerCounter = (this.playerCounter + 1) % 2; // Switch to the next player
                } else {
                    System.out.println("Sorry " + activePlayer.getName() + "! You lost!");
                    break; // Exit the game loop
                }
            }
        }
    }

}

