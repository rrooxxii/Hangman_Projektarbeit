package at.ac.fhcampuswien;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HangmanGame {


    //Variables
        //Housekeeping
    private HangmanController controller;
        //Game Variables
    private char[] puzzleArray; //current state of fields
    private char[] wordArray; //word to guess
    private HangmanPlayer[] players;
    private String language;
    private String difficulty;
    private int playerNumber;
    private int currentPlayerIndex = 0;


    public String fetchWordFromAPI(String difficulty) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String apiUrl = buildApiUrl(difficulty);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            String responseBody = response.body().substring(2, response.body().length() - 2); // Entferne JSON-Array-Formatierung
            if (isWordValidForDifficulty(responseBody, difficulty)) {
                return responseBody;
            } else {
                return fetchWordFromAPI(difficulty); // Wiederhole die Anfrage, falls Wort nicht passt
            }
        } else {
            throw new RuntimeException("API request failed with status code: " + response.statusCode());
        }
    }

    private boolean isWordValidForDifficulty(String word, String difficulty) {
        int length = word.length();
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

    private String buildApiUrl(String difficulty) {
        String baseUrl = "https://random-word-api.herokuapp.com/word?number=1";
        switch (difficulty.toLowerCase()) {
            case "easy":
                return baseUrl + "&minLength=1&maxLength=10";
            case "medium":
                return baseUrl + "&minLength=10&maxLength=15";
            case "hard":
                return baseUrl + "&minLength=15";
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }

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

        this.difficulty = difficulty;
        try {
            String word = fetchWordFromAPI(difficulty);
            setWord(word);
            System.out.println("Word fetched from API: " + word);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching word from API: " + e.getMessage());
        }



//        if (difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard")) {
//            this.difficulty = difficulty;
//            System.out.println("Difficulty set to " + difficulty);
//        } else {
//            System.out.println("Difficulty not recognised");
//        }

    }

    private String translate(String en, String de) {
        return language.equals("german") ? de : en;
    }

    public void switchToNextPlayer() {
        if (players == null || players.length == 0) {
            throw new IllegalStateException(translate(
                    "Players are not initialized. Call initPlayerArray first.",
                    "Spieler sind nicht initialisiert. Rufen Sie zuerst initPlayerArray auf."
            ));
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        System.out.println(translate(
                "Switched to next player: ",
                "Wechsel zum nächsten Spieler: ") + players[currentPlayerIndex].getName());
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

    public char[] getPuzzleArray(){
        return puzzleArray;
    }

    public char[] getWordArray(){
        return wordArray;
    }

    //Methods
        //Initializers
    public void initPuzzleArray () {
        if(wordArray == null){
            throw new IllegalStateException("wordArray is not initialized. Set a word before initializing the puzzle.");
        }

        puzzleArray = new char[wordArray.length];
        for (int i = 0; i < puzzleArray.length; i++) {
            puzzleArray[i] = '_';
        }
        System.out.println("Puzzle array initialized with underscores.");

    } //Init with underlines
    private void printPuzzleArray() {
        for (int i = 0; i < puzzleArray.length; i++) {
            System.out.print(puzzleArray[i]+ " ");
        }
        System.out.println();
    }
    public void initPlayerArray(String[] playerNames) {
        if (playerNames.length != playerNumber) {
            throw new IllegalArgumentException("Number of player names must match playerNumber.");
        }
        players = new HangmanPlayer[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            players[i] = new HangmanPlayer(playerNames[i]);
            System.out.println("Player " + (i + 1) + ": " + players[i].getName() + " initialized.");
        }
    }


    public boolean guessLetter(char letter){
        boolean correct = false;

        //Überprüfen, ob Buchstabe im Wort ist
        for (int i = 0; i < wordArray.length; i++){
            if(wordArray[i] == letter){
                puzzleArray[i] = letter; //Aktualisierung von Puzzle
                correct = true;
            }
        }

        return correct; //true wenn der Buchstabe korrekt war
    }

    public int calculateScore(HangmanPlayer player) {
        if (!checkIfWon()) {
            return 9; // Minimum Score bei Spielverlust
        }
        if (player.getLives() == 7) {
            return 21; // Maximaler Score
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

    public String calculateScoreAll() {
        StringBuilder result = new StringBuilder("Scores:\n");
        HangmanPlayer winner = null;
        int highestScore = Integer.MIN_VALUE;
        boolean tie = false;

        for (HangmanPlayer player : players) {
            int score = calculateScore(player);
            result.append(player.getName()).append(": ").append(score).append("\n");

            if (score > highestScore) {
                highestScore = score;
                winner = player;
                tie = false;
            } else if (score == highestScore) {
                tie = true;
            }
        }

        if (tie) {
            result.append("It's a tie with score: ").append(highestScore).append("\n");
        } else if (winner != null) {
            result.append("Winner is ").append(winner.getName()).append(" with score: ").append(highestScore).append("\n");
        }
        System.out.println(result.toString());
        return result.toString();
    }



    public boolean isGameOver() {
        // Prüfe, ob ein Spieler noch Leben hat und ob das Puzzle vollständig gelöst ist
        for (HangmanPlayer player : players) {
            if (player.getLives() > 0 && !checkIfWon()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfWon() {
        for (char c : puzzleArray) {
            if (c == '_') {
                return false; // Noch nicht gewonnen
            }
        }
        return true; // Alle Buchstaben erraten
    }

    public void setWord(String word) {
        this.wordArray = word.toCharArray(); // Wandelt das Wort in ein char-Array um
        System.out.println("Word set to: " + word);
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public HangmanPlayer getCurrentPlayer(){
        return players[currentPlayerIndex];
    }

}