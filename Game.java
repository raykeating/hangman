package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Game {
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
        int lives = 8;
        char[] guesses = new char[27];
        guesses[0] = ' '; //initialize the guesses array with spaces so spaces automatically show up.
        P1out.println("Welcome to Hangman! You are Player 1!");
        P2out.println("Welcome to Hangman! You are Player 2!");
        P1out.println("You get to make up the puzzle this round");
        P2out.println("Sit tight while your opponent chooses a phrase for you to guess.  Next round you will get to make up a phrase!");
        P1out.println("Lives left: " + (gameLives-P1Losses));
        P2out.println("Lives left: " + (gameLives-P2Losses));
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
}
