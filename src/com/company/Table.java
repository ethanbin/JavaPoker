package com.company;

import java.util.ArrayList;
import java.util.Stack;
import java.util.List;

/**
 * Created by Ethan on 10/17/2017.
 */
public class Table {
    enum Stage {
        PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN
    }

    private final int TABLE_MINIMUM_BET = 10;
    private final int TABLE_SEATS = 6;

    private int pot;
    private Deck deck;
    private Stack<Card> communityCards;
    private List<Player> players;
    private boolean tableIsFull;
    private int dealerIndex;
    private int smallBlindIndex;
    private int bigBlindIndex;
    private int activePlayers;

    public Table(){
        this.init();
    }

    private void init(){
        pot = 0;
        deck = new Deck();
        communityCards = new Stack<>();
        players = new ArrayList<>(TABLE_SEATS);
        tableIsFull = false;
        dealerIndex = smallBlindIndex = bigBlindIndex = -1;
        activePlayers = 0;
    }

    public int getActivePlayers() {
        return activePlayers;
    }

    public void playerJoinsGame(Player player){
        if (!tableIsFull){
            players.add(player);
            activePlayers++;
            if (players.size() == TABLE_SEATS){
                tableIsFull = true;
            }

            return;
        }

        // Only prints out if table is full.
        System.out.println("Table is Full " + player.getUserName() + " cannot join game.\n");
    }

    public void playMatch(){
        for (Stage stage : Stage.values()) {
            callStageMethod(stage);
        }
    }

    private void callStageMethod(Stage stage){
        switch (stage){
            case PRE_FLOP:
                stagePreFlop();
                break;
            case FLOP:
                stageFlop();
                break;
            case TURN:
                stageTurn();
                break;
            case RIVER:
                stageRiver();
                break;
            case SHOWDOWN:
                stageShowDown();
                break;
        }
    }

    private void stagePreFlop(){
        System.out.println("Stage: Pre Flop:");
        setDealerAndBlinds();
        dealPlayersHoleCards();
        System.out.println(this);
    }

    private void stageFlop(){
        System.out.println("Stage: Flop:");
        dealToTable(3);
        System.out.println(this);
    }

    private void stageTurn(){
        System.out.println("Stage: Turn:");
        dealToTable(1);
        System.out.println(this);
    }

    private void stageRiver(){
        System.out.println("Stage: River:");
        dealToTable(1);
        System.out.println(this);
    }

    private void stageShowDown(){
        System.out.println("Stage: Showdown:");
        System.out.println(this);
    }

    private void setDealerAndBlinds(){
        int indexLimit = players.size()-1;

        dealerIndex++;
        if(dealerIndex > indexLimit){
            dealerIndex = 0;
        }

        smallBlindIndex = dealerIndex++;
        if(smallBlindIndex > indexLimit){
            smallBlindIndex = 0;
        }

        bigBlindIndex = smallBlindIndex++;
        if(bigBlindIndex > indexLimit){
            bigBlindIndex = 0;
        }
    }

    private void dealPlayersHoleCards(){
        int timesToDeal = 2;

        System.out.println("Dealing Cards To Players...\n");

        while (timesToDeal != 0){
            for (Player player : players) {
                player.givePlayerCard(deck.dealCard());
            }
            timesToDeal--;
        }
    }

    private void dealToTable(int numberOfCardsToDeal) {
        while (numberOfCardsToDeal != 0) {
            communityCards.add(deck.dealCard());
            numberOfCardsToDeal--;
        }
    }

    @Override
    public String toString() {
        StringBuilder tableInformation = new StringBuilder("Table Information...\n\n");

        tableInformation.append("Current pot = ").append(pot).append("\n");

        tableInformation.append("Community Cards\n\n");
        for (Card card : communityCards) {
            tableInformation.append(card.toString());
        }
        tableInformation.append("\n");

        tableInformation.append("Player Info\n\n");
        for (Player player : players) {
            tableInformation.append(player.toString());
            tableInformation.append("\n");
        }
        return tableInformation.toString();
    }

    /**
     * Entry point for our game.
     *
     * @param args
     */
    public static void main(String[] args) {

        Table table = new Table();

        table.playerJoinsGame(new Player("Kay"));
        table.playerJoinsGame(new Player("Rob"));
        table.playerJoinsGame(new Player("Vanessa"));
        table.playerJoinsGame(new Player("David"));


        /*while (table.getActivePlayers() != 1){
            table.playMatch();
        }*/

        table.playMatch();
    }
}
