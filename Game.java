import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Game {
    Boolean isRunning = true;
    Socket player1;
    Socket player2;
    PrintWriter P1out;
    PrintWriter P2out;
    BufferedReader P1in;
    BufferedReader P2in;
    String secretPhrase;
    int gameLives = 2;
    int P1Losses = 0;
    int P2Losses = 0;
    public Game (Socket player1, Socket player2){
        this.player1 = player1;
        this.player2 = player2;
        try {
            P1out = new PrintWriter(player1.getOutputStream(), true);
            P2out = new PrintWriter(player2.getOutputStream(), true);
            P1in = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            P2in = new BufferedReader(new InputStreamReader(player2.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        int lives = 6;
        char[] guesses = new char[27];
        int nextGuessIndex = 1;
        guesses[0] = ' '; //initialize the guesses array with spaces so spaces automatically show up.
        P1out.println("Welcome to Hangman! You are Player 1!");
        P2out.println("Welcome to Hangman! You are Player 2!");
        while (P1Losses != gameLives && P2Losses != gameLives) {
            P1out.println("You get to make up the puzzle this round");
            P2out.println("Sit tight while your opponent chooses a phrase for you to guess.  Next round you will get to make up a phrase!");
            P1out.println("Lives left: " + (gameLives - P1Losses));
            P2out.println("Lives left: " + (gameLives - P2Losses));
            P1out.println("SETPHRASE");
            P1out.flush();
            P2out.flush();
            try {
                secretPhrase = P1in.readLine();
                System.out.println("Got the secret phrase: " + secretPhrase);
            } catch (IOException e) {
                e.printStackTrace();
            }
            P2out.println("Ok. time to start guessing.");
            P2out.println(display(guesses));
            P2out.flush();
            while ((!(checkForWin(guesses))) && lives != 0) {
                System.out.println((!(checkForWin(guesses))) && lives != 0);
                System.out.println(lives != 0);
                System.out.println((!(checkForWin(guesses))));
                System.out.println(lives);
                P2out.println("GUESS");
                P2out.flush();
                try {
                    char guess = P2in.readLine().charAt(0);
                    System.out.println("Your opponent guessed: " + guess);
                    if (!new String(guesses).contains(String.valueOf(guess))){
                        //if the guess hasn't already been made
                        guesses[nextGuessIndex] = guess;
                        nextGuessIndex++;
                    } else {
                        while ((new String(guesses).contains(String.valueOf(guess)))) {
                            P2out.println("GUESSAGAIN");
                            P2out.flush();
                            guess = P2in.readLine().charAt(0);
                        }
                        guesses[nextGuessIndex] = guess;
                        nextGuessIndex++;
                    }
                    if (secretPhrase.contains(String.valueOf(guess))) {
                        P2out.println("Good guess, " + guess + " was in the secret phrase.");
                        P2out.println(display(guesses));
                        P1out.println("Your opponent guessed the letter right: " + guess);
                        P1out.println(display(guesses));
                    } else {
                        P2out.println("Nope, " + guess + " is not in the secret phrase.");
                        P1out.println("Your opponent guessed wrong: " + guess);
                        lives--;
                        P1out.println(displayHangman(lives));
                        P2out.println(displayHangman(lives));
                        P1out.println("Incorrect guesses: " + String.valueOf(guesses));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (checkForWin(guesses)) {
                System.out.println(checkForWin(guesses));
                P1out.println("You lose this round.  Looks like your phrase was too easy.");
                P2out.println("Congratulations! You win this round");
                P1Losses++;
            } else {
                P1out.println("Congratulations! You win this round");
                P2out.println("You lose this round! The secret phrase was: " + secretPhrase);
                P2Losses++;
            }
            if (P1Losses == gameLives) {
                P1out.println("GAME OVER! YOU LOSE");
                P2out.println("CONGRATULATIONS! YOU WIN THE GAME!");
            } else if (P2Losses == gameLives) {
                P2out.println("GAME OVER! YOU LOSE");
                P1out.println("CONGRATULATIONS! YOU WIN THE GAME!");
            } else {
                switchPlayers(player1, player2);
                lives = 6;
                guesses = new char[27];
                guesses[0] = ' ';
            }
        }
        Thread.currentThread().interrupt();
    }

    public String display(char[] guesses){
//      secretPhraseHidden is the secret phrase written with only the guessed letters and underlines in the non-guessed spots.
        char[] secretPhraseHidden = secretPhrase.toCharArray();
        int i = 0;
        for(char c : secretPhraseHidden){
            //if the character isn't in the guesses list, convert it to an underline.  otherwise, leave it as is.
            if (!(new String(guesses).contains(String.valueOf(c)))){
                secretPhraseHidden[i] = '_';
            }
            i++;
        }
        return String.valueOf(secretPhraseHidden);
    }

    public Boolean checkForWin(char[] guesses){
        char[] secretPhraseHidden = secretPhrase.toCharArray();
        int i = 0;
        for(char c : secretPhraseHidden){
            //if the character isn't in the guesses list, the player hasn't won yet.  otherwise, they have.
            if (!(new String(guesses).contains(String.valueOf(c)))){
                return false;
            }
            i++;
        }
        return true;
    }

    public String displayHangman(int lives) {
        String[] hangmen = new String[7];
        hangmen[0] = "  +---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|\\  |\n" +
                " / \\  |\n" +
                "      |\n";
        hangmen[1] = "  +---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|\\  |\n" +
                " /    |\n" +
                "      |";
        hangmen[2] = "  +---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|\\  |\n" +
                "      |\n" +
                "      |";
        hangmen[3] = "  +---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                " /|   |\n" +
                "      |\n" +
                "      |";
        hangmen[4] = "  +---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                "  |   |\n" +
                "      |\n" +
                "      |";
        hangmen[5] = "  +---+\n" +
                "  |   |\n" +
                "  O   |\n" +
                "      |\n" +
                "      |\n" +
                "      |";
        hangmen[6] = "  +---+\n" +
                "  |   |\n" +
                "      |\n" +
                "      |\n" +
                "      |\n" +
                "      |";
        return hangmen[lives];
    }
    public void switchPlayers(Socket player1, Socket player2) {
        int temp = P1Losses;
        P1Losses = P2Losses;
        P2Losses = temp;
        try {
            P1out = new PrintWriter(player2.getOutputStream(), true);
            P2out = new PrintWriter(player1.getOutputStream(), true);
            P1in = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            P2in = new BufferedReader(new InputStreamReader(player1.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
