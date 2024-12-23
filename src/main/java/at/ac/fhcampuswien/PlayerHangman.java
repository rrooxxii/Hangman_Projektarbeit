
package at.ac.fhcampuswien;


import java.util.Scanner;

public class PlayerHangman {

    private static final int MAX_LIVES = 7;
    private int lives = MAX_LIVES;
    private  String name = null;

//Constructor

    public PlayerHangman(String name) {
        this.setName(name);
    }


//Getter_Setter


    public static int getMaxLives() {
        return MAX_LIVES;
    }

    public int getLives() {
        return lives;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//Methoden

    public boolean reduceLives() {
        if (this.lives > 0) {
            this.lives--; // Decrement lives if above 0
            System.out.println("Your lives remaining: " + this.lives);
            return true; // Player still has lives
        }
        return false; // Player has no lives left
    }

}
