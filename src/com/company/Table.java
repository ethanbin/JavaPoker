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

        @Override
        public String toString() {
            return "Stage " + this.name() + " needs " + getNumberOfCardsToDeal() + " cards dealt to table.\n";
        }
    }

    private Stage stage;

    // turn represents which entity's turn it is to act, with 0 being the table's
    private int turn = 0;

    // pot holds the current round's bets
    private int pot = 0;

    // Cards dealt onto table for all to use;
    private List<Card> communityCards = new ArrayList<>();

    private Deck deck = new Deck();

    public Table(){
        stage = Stage.PRE_FLOP;
    }

    void dealToTable(){
        while (communityCards.size() < stage.getNumberOfCardsToDeal()){
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
