package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 10/17/2017.
 */
public class Table {
    enum Stage {
        // each number value represents number of cards the table should have
        PRE_FLOP(0), FLOP(3), TURN(4), RIVER(5);

        private int numberOfCardsToDeal;

        // needed for giving each stage a number value
        Stage(int numberOfCardsToDeal) {
            this.numberOfCardsToDeal = numberOfCardsToDeal;
        }

        // Returns number of cards that should be on the table this round
        public int getNumberOfCardsToDeal() {
            return numberOfCardsToDeal;
        }

        // Save an array of every Stage value so getNextStage won't need to keep calling values().
        private static final Stage[] stageValues = values();

        public static final int size = stageValues.length;

        @Override
        public String toString() {
            return "Stage " + this.name() + " needs " + getNumberOfCardsToDeal() + " cards on the table.\n";
        }
    }

    // turn represents which entity's turn it is to act, with 0 being the table's
    private int turn = 0;

    // pot holds the current round's bets
    private int pot = 0;

    private boolean roundInProgress = false;

    private final int maxUsers = 6;

    // Cards dealt onto table for all to use;
    private List<Card> communityCards = new ArrayList<>();

    private Deck deck = new Deck();

    private List<Player> players = new ArrayList<>();

    // TODO: uncomment when the User class is made
//    public void nextTurn(){
//        do {
//            this.turn = (this.turn + 1) % (userList.size() + 1);
//        } while (userList.get(i).didUserFold());
//    }
    // to use for testing until above function can be used
    public void nextTurn() {
        turn += (turn + 1) % 2;
    }

    int mainTurnID = 1; //to be used for debugging

    public int getTurn() {
        return turn;
    }

    // TODO: add method to add users and start them in their own thread.
//    public boolean addUser(){
//        if (users.size > maxUsers)
//            return false;
//    }

    private void startNewRound() {
        pot = 0;
        communityCards = new ArrayList<>();
        roundInProgress = true;
        deck.shuffle();
        playRound();
    }

    // first table acts, then players play
    private void playRound() {
        // TODO:    add condition to loop checking that playing users > 1, maybe with static function in User class.
        // This loop will go through each user's turn and dealing cards.
        for (Stage stage : Stage.stageValues) {
            switch (stage) {
                case PRE_FLOP:
                    //dealToTable(stage.getNumberOfCardsToDeal());
                    nextTurn();
                    break;
                default:
                    dealToTable(stage.getNumberOfCardsToDeal());
                    nextTurn();
                    break;
            }
            // 0 represents table's turn.
            // Maybe make static int in Player class called tableTurnID = 0 and use that instead?
            while (getTurn() != 0) ;
        }
        // TODO:    call a method here to figure out who won.
        // calculateWinner();
        roundInProgress = false;
    }

    private void dealToTable(int numberOfCardsToDeal) {
        while (communityCards.size() < numberOfCardsToDeal) {
            communityCards.add(deck.dealCard());
        }
    }

    // currently used for testing
    public static void main(String[] args) {

        for (Stage stage : Stage.values()) {
            System.out.print(stage.toString());
        }
    }
}
