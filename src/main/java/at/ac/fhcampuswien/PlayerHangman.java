
package at.ac.fhcampuswien;



public class PlayerHangman {

    private static int MAX_LIVES = 11;
    int wrongGuessCount = 0;
    public String name = null;

//Getter_Setter

    public int getWrongGuessCount() {
        return wrongGuessCount;
    }

    public void setWrongGuessCount(int wrongGuessCount) {
        this.wrongGuessCount = wrongGuessCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Methoden

    public boolean reduceLives (){

        if (wrongGuessCount <=1){
            wrongGuessCount = wrongGuessCount - 1;
            return true;
        }
        else {
            return false;
        }

    }

}
