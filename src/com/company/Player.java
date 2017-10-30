package com.company;

import java.util.*;

/**
 * Created by Kailie on 10/17/2017.
 */

public class Player {

    private String username;
    private int balance;
    private boolean smallBlind;
    private boolean bigBlind;
    private Stack<Card> playerCards;

    Player(String username, int balance) {
        this.username = username;
        this.balance = balance;
        this.playerCards = new Stack<>();
    }

    public void displayPlayerInfo() {
        System.out.println(this);
        System.out.println(username+"'s cards:");
        for (Card card : playerCards) {
            System.out.print(card);
        }
        System.out.println("\n");
    }

//	public boolean isDealer() {
//
//	}

    public boolean isSmallBlind() {
        //we need a counter to know which player its up to to determine this, bigBlind and Dealer too
        //we also need a player ID to be able to do ^^^^^^
        return smallBlind;
    }

    public boolean isBigBlind() {
        return bigBlind;
    }

    public int getBalance() {
        return balance;
    }

    public boolean canPlay() {
        return getBalance() > 0;
    }

    public void givePlayerCard(Card card) {
        this.playerCards.push(card);
    }

    @Override
    public String toString() {
        String playerInfo;

        playerInfo = "username = " + username + "\n";
        playerInfo += username + "'s Balance is $" + getBalance() + "\n";
        if(canPlay()){
            playerInfo += username + " is still able to play.";
        } else {
            playerInfo += username + " is no longer able to play.";
        }
        return playerInfo;
    }

    public static void main(String[] args) {
        Deck deck = new Deck();

        Stack<Player> players = new Stack<>();

        players.push(new Player("Kay", 2000));
        players.push(new Player("Rob", -1000));
        players.push(new Player("Vanessa", 0));
        players.push(new Player("David", 251));

        System.out.println("Dealing Cards To Players...\n");

        for (Player player : players) {
            player.givePlayerCard(deck.dealCard());
        }
        for (Player player : players) {
            player.givePlayerCard(deck.dealCard());
        }

        System.out.println("Players Information...\n");

        for (Player player : players) {
            player.displayPlayerInfo();
        }
        
    }
}
