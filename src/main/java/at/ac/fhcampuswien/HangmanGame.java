package at.ac.fhcampuswien;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HangmanGame {
    public Scanner inputScanner = new Scanner(System.in);
    private char[] puzzleArray; //current state of fields
    private char[] wordArray; //word to guess
    private String language = "en"; // Standard Sprache ist Englisch
    private HangmanController hangmanController;
    // Liste der Spieler, die am Spieler teilnehmen
    private ArrayList<HangmanPlayer> playerList = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private String difficultySetting;

    private static final int MAX_PLAYERS = 4; //Maximale Anzahl an Spielern


//Constructor

    public HangmanGame() {

    }

    public HangmanGame(int playerCount, String difficulty) {
        // Überprüfen der Spieleranzahl
        if(playerCount < 1 || playerCount > MAX_PLAYERS){
            throw new IllegalArgumentException("Die Spieleranzahl muss zwischen 1 und " + MAX_PLAYERS + "liegen");
        }

        //Spieler einlesen
        for (int i = 0; i < playerCount; i++){
            String playerName;
            do{
                System.out.print("Spieler" + (i + 1) + ", bitte gib deinen Namen ein: ");
                playerName = inputScanner.nextLine().trim();
            }while(playerName.isEmpty());
            playerList.add(new HangmanPlayer(playerName));

        }

        // Überprüfen des SChwierigkeitsgrads
        if (!difficulty.equals("easy") && !difficulty.equals("medium") && !difficulty.equals("hard")) {
            throw new IllegalArgumentException("Ungueltige Schwierigkeit. Wählen Sie easy, medium oder hard.");
        }
        this.difficultySetting = difficulty;

        try {
            //Holt ein Wort basierend auf difficulty
            String word = fetchWordfromAPI(difficulty);
            this.wordArray = word.toCharArray();
            this.initPuzzleArray();
        } catch (Exception e) {
            throw new RuntimeException(language.equals("de") ? "Fehler beim Abrufen des Wortes von der API: " + e.getMessage() : "Error fetching word from API: " + e.getMessage());
        }
    }
//Getter_Setter


    public String getDifficultySetting() {
        return difficultySetting;
    }

    public void setDifficultySetting(String difficulty) {
        this.difficultySetting = difficulty;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setControllerHangman(HangmanController hangmanController) {
        this.hangmanController = hangmanController;
    }

    //Methoden
    @Override
    public String toString() {
        return "A Game has been Made";
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
                System.out.println(language.equals("de") ? "Das WOrte entspricht nicht der gewählten Schwierigkeit. Neuer Versuch..." : "Word does not fit difficulty. Retrying...");
                return fetchWordfromAPI(difficulty); // Wiederhole die Anfrage
            }
        } else {
            throw new RuntimeException(language.equals("de") ? "API-Anfrage fehlgeschlagen mit Statuscode: " + response.statusCode() : "API request failed with status code: " + response.statusCode());
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
                throw new IllegalArgumentException(language.equals("de") ? "Ungueltiger Schwierigkeitsgrad: " + difficulty : "Invalid difficulty: " + difficulty);
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
                throw new IllegalArgumentException(language.equals("de") ? "Ungueltiger Schwierigkeitsgrad. Waehle 'easy', 'medium' oder 'hard'." : "Invalid difficutly level. Choose 'easy', 'medium', or 'hard'.");
        }
    }
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
        // Wenn keine Sprache gewählt wird, wiederholt sich die Sprachabfrage.
        String langChoice = "";
        while (langChoice.isEmpty()) {
            System.out.println("Choose a language / Waehle eine Sprache aus (en/de):");
            langChoice = inputScanner.nextLine().trim().toLowerCase();
        }

        if (langChoice.equals("de")) {
            language = "de";
        } else {
            language = "en";
        }

        System.out.println(language.equals("de") ? "Willkommen zu Hangman!" : "Welcome to Hangman!");
        System.out.println(language.equals("de") ? "Gib die Anzahl der Spieler ein: " : "Enter the number of players: ");
        int playerCount = Integer.parseInt(inputScanner.nextLine());
        for (int i = 0; i < playerCount; i++) {
            System.out.printf(language.equals("de") ? "Spieler %d, bitte gib deinen Namen ein: " : "Player %d, please enter your name: ", i + 1);
            String playerName = inputScanner.nextLine().trim();
            playerList.add(new HangmanPlayer(playerName));
        }

        System.out.print(language.equals("de") ? "Hallo " : "Hi ");
        for (int i = 0; i < playerList.size(); i++) {
            System.out.print(playerList.get(i).getName());
            if (i < playerList.size() - 2) {
                System.out.print(", ");
            } else if (i == playerList.size() - 2) {
                System.out.print(" & ");
            }
        }
        System.out.println(language.equals("de") ? "! Willkommen zum Spiel Hangman Legends!" : "! Welcome to this game of Hangman Legends!");
        System.out.println(language.equals("de") ? "Das Wort, das ihr erraten muesst, hat " + puzzleArray.length + " Buchstaben." : "The Word u will be looking for is " + puzzleArray.length + " characters long. ");
        System.out.println(language.equals("de") ? "Viel Glueck!\n" : "Good Luck!\n" );

    }
    //Loop to Play Rounds (main game Logic)
    public void playRound() {
        boolean hasWon = false;

        while (!hasWon) {
            HangmanPlayer activePlayer = playerList.get(currentPlayerIndex);
            System.out.println(language.equals("de") ? "Du bist an der Reihe " + activePlayer.getName() + "!" : "It's your turn " + activePlayer.getName() + "!");
            System.out.println(language.equals("de") ? "Dies ist das aktuelle Spielfeld:" : "This is the current playing field:");
            this.printPuzzleArray();

            // Take input and validate if input was given
            String guessString = inputScanner.nextLine().trim();
            while (guessString.isEmpty()) {
                System.out.println(language.equals("de") ? "Ungueltige Eingabe. Bitte gib einen Buchstaben ein." : "Invalid input. Please enter a letter:");
                guessString = inputScanner.nextLine().trim();
            }
            char guess = guessString.charAt(0);

            // Check if the letter was already guessed
            while (this.guessedAlready(guess)) {
                System.out.println(language.equals("de") ? "Du hast diesen Buchstaben bereits geraten. Versuche es erneut!" : "You already guessed this letter, try again!");
                guessString = inputScanner.nextLine().trim();
                while (guessString.isEmpty()) {
                    System.out.println(language.equals("de") ? "Ungueltige Eingabe. Bitte gib einen Buchstaben ein." : "Invalid input. Please enter a letter:");
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
                    System.out.println(language.equals("de") ? "Glueckwunsch " +activePlayer.getName() + "! Du hast gewonnen!" : "Congrats " + activePlayer.getName() + "! You won!");
                }
            } else {
                if (activePlayer.reduceLives()) {
                    System.out.println(language.equals("de") ? "Falsch! Du hast ein Leben verloren! Du hast noch " + activePlayer.getLives() + " Leben uebrig." : "Wrong! You have lost a life! You have " + activePlayer.getLives() + " lifes remaining.");
                    this.currentPlayerIndex = (this.currentPlayerIndex + 1) % playerList.size();
                } else {
                    System.out.println(language.equals("de") ? "Schade " + activePlayer.getName() + "! Du hast verloren!" : "Sorry " + activePlayer.getName() + "! You lost!");
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
    public int calculateScore(HangmanPlayer player) { //generelle Punkte"logik"
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
        HangmanPlayer winner = null;
        int highestScore = Integer.MIN_VALUE;
        boolean tie = false;
        System.out.println(language.equals("de") ? "Punkte " : "Scores ");
        for (HangmanPlayer player : playerList) {
            int score = calculateScore(player);
            System.out.println(language.equals("de") ? "Spieler " + player.getName() + " hat " + score + " Punkte." : "Player " + player.getName() + " score is " + score);
            if (score > highestScore) {
                highestScore = score;
                winner = player;
                tie = false;
            }else if (score == highestScore) {
                tie = true;
            }
        }
        if (tie) {
            System.out.println(language.equals("de") ? "Untentschieden mit " + highestScore + " Punkten!" : "Draw with " + highestScore + " Points!");
            for (HangmanPlayer player : playerList) {
                if (calculateScore(player) == highestScore) {
                    System.out.println(player.getName());
                }
            }
        } else if (winner != null) {
            System.out.println(language.equals("de") ? "Der Gewinner ist " + winner.getName() + " mit " + highestScore + " Punkten!" : "The Winner is " + winner.getName() + " with " + highestScore + " Points!");
        } else {
            System.out.println(language.equals("de") ? "There is no winner!" : "Es gibt keinen Gewinner!");
        }
    }
}

