package at.ac.fhcampuswien;
import java.net.URI;
import java.util.Scanner;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class Hangman {
private PlayerHangman player1;
private PlayerHangman player2;
public Scanner inputScanner = new Scanner(System.in);
private char[] puzzleArray; //current state of fields
private char[] wordArray; //word to guess


PlayerHangman[] playerList = new PlayerHangman[2];
public int playerCounter = 0; //currentplayer

//Methode

//Constructor
    //Wir geben Wort ein es gibt fix 2 Spieler, Spiel startet
    public Hangman(String player1Name, String player2Name, String difficulty) {
        playerList[0] = player1 = new PlayerHangman(player1Name);
        playerList[1] = player2 = new PlayerHangman(player2Name);

        try {
            //Holt ein Wort basierend auf difficulty
            String word = fetchWordfromAPI(difficulty);
            this.wordArray = word.toCharArray();
            this.initPuzzleArray();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching word from API: " + e.getMessage());
        }
    }

    //Methode um Wort von der API abzurufen
    private String fetchWordfromAPI(String difficulty) throws Exception{
        HttpClient client = HttpClient.newHttpClient();     //HTTP Client für API-Anfrage
        String apiUrl = "https://random-word-api.herokuapp.com/word?number=1"; // Basis-URL ohne Längenfilter
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //Anfrage senden


        if (response.statusCode() == 200) {
            String responseBody = response.body().substring(2, response.body().length() - 2); // Entferne JSON-Array-Formatierung

            // Überprüfe ob WOrt zu difficulty passt
            if (isWordValidForDifficulty(responseBody, difficulty)) {
                return responseBody;
            } else {
                System.out.println("Word does not fit difficulty. Retrying...");
                return fetchWordfromAPI(difficulty); // Wiederhole die Anfrage
            }
        } else {
            throw new RuntimeException("API request failed with status code: " + response.statusCode());
        }
    }

    // Methode ob das Wort zur Schwierigkeit passt
    private boolean isWordValidForDifficulty(String word, String difficulty) {
        int length = word.length(); //Wortlänge bestimmen
        //Wortlänge bestimmen anhand von difficulty
        switch (difficulty.toLowerCase()) {
            case "easy":
                return length >= 1 && length <= 10;
            case "medium":
                return length >= 10 && length <= 15;
            case "hard":
                return length >= 15;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }


    // Hilfsmethode zum Erstellen der API-URL basierend auf dem Schwierigkeitsgrad
    private String buildApiUrl(String difficulty) {
        String baseUrl = "https://random-word-api.herokuapp.com/word?lang=de&number=1";
        switch (difficulty.toLowerCase()) {
            case "easy":
                return baseUrl + "&minLength=1&maxLength=10"; //Wörter zwischen 1 und 10 char
            case "medium":
                return baseUrl + "&minLength=10&maxLength=15"; //Wörter mit 10-15 char
            case "hard":
                return baseUrl + "&minLength=15"; // Beispiel: Wörter mit mindestens 15 char
            default:
                throw new IllegalArgumentException("Ungueltiger Schwierigkeitsgrad. Waehle 'easy', 'medium' oder 'hard'.");
        }
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
    /*Methode um Punkte zu berechnen bzw Highscore
       Highscore Berechnung für Hangman
        7 - 21
        6, 5 - 18
        4, 3 - 15
        2, 1 - 12
        0 - 9

        Gewonnen mit 7 leben - 21pts
        Verloren - 9pts
        nur eine Grundbasis kann geändert werden mit einem schwierigkeitsmultiplikator
     */
    public int calculateScore(PlayerHangman player) { //generelle Punkte"logik"
        if (!checkIfWon()) {
            return 9;
        }
        if (player.getLives() == 7) {
            return 21;
        } else if (player.getLives() >= 5) {
            return 18;
        } else if (player.getLives() >= 3) {
            return 15;
        } else if (player.getLives() >= 1) {
            return 12;
        } else {
            return 9;
        }
    }
    public void calculateScoreAll(){ //Logik um die Punkte für alle Spieler zu berechnen
        PlayerHangman winner = null;
        int highestScore = Integer.MIN_VALUE;
        boolean tie = false;
        System.out.println("Scores ");
        for (PlayerHangman player : playerList) {
            int score = calculateScore(player);
            System.out.println("Player " + player.getName() + " score is " + score);
            if (score > highestScore) {
                highestScore = score;
                winner = player;
                tie = false;
            }else if (score == highestScore) {
                tie = true;
            }
        }
        if (tie) {
            System.out.println("Untentschieden mit " + highestScore + " Punkten!");
            for (PlayerHangman player : playerList) {
                if (calculateScore(player) == highestScore) {
                    System.out.println(player.getName());
                }
            }
        } else if (winner != null) {
            System.out.println("Der Gewinner ist " + winner.getName() + " mit " + highestScore + " Punkten!");
        } else {
            System.out.println("Es gibt keinen Gewinner!");
        }
    }
}

