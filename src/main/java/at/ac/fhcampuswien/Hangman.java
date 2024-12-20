package at.ac.fhcampuswien;
import java.util.Scanner;

class Hangman {
private String word;
public PlayerHangman player1;
public PlayerHangman player2;
public Scanner inputScanner = new Scanner(System.in);



//Constructor
    //Wir geben Wort ein
    public Hangman(String word, PlayerHangman playerHangman1, PlayerHangman playerHangman2){

        this.word = word;

    }

    //Wir holen uns das Wort aus einem File
    public Hangman(){
        //Implement Random Word from List

    }


//Getter_Setter



//Methoden


    public void startGame(){
        System.out.println("Welcome to Hangman!");
        System.out.println("Player 1 Please enter your name: ");
        player1.setName(inputScanner.nextLine());
        System.out.println("Player 2 Please enter your name: ");
        player2.setName(inputScanner.nextLine());



    }

    public void playRound (){



    }

}

