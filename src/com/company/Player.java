package com.company;

import java.util.*;

/**
 * Created by Kailie on 10/17/2017.
 */

public class Player {

    private final int DEFAULT_PLAYER_BALANCE = 2000;

    private String username;
    private int balance;
    private Stack<Card> holeCards;

    Player(String username) {
        this.username = username;
        this.balance = DEFAULT_PLAYER_BALANCE;
        this.init();
    }

    Player(String username, int balance) {
        this.username = username;
        this.balance = balance;
        this.init();
    }

    private void init(){
        this.holeCards = new Stack<>();
    }

    protected void displayPlayerInfo() {
        System.out.println(this);
    }

    protected int getBalance() {
        return balance;
    }

    protected boolean canPlay() {
        return getBalance() > 0;
    }

    protected void givePlayerCard(Card card) {
        this.holeCards.push(card);
    }

    protected String getUserName(){
        return username;
    }

    public void check() {
    }

    public void call() {
    }

    public void raise() {
    }

    public void jam() {
    }

    public void fold() {
    }

    @Override
    public String toString() {
        StringBuilder playerInfo = new StringBuilder("username = " + username + "\n");

        playerInfo.append(username).append("'s Balance is $").append(getBalance()).append("\n");
        if(canPlay()){
            playerInfo.append(username).append(" is still able to play.");
        } else {
            playerInfo.append(username).append(" is no longer able to play.");
        }

        playerInfo.append("\n").append(username).append("'s cards:");

        for (Card card : holeCards) {
            playerInfo.append(card.toString());
        }

        playerInfo.append("\n");

        return playerInfo.toString();
    }

    public static void main(String[] args) {
        Deck deck = new Deck();

        Stack<Player> players = new Stack<>();

        players.push(new Player("Kay"));
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
            System.out.println(player);
        }
        
    }
}
