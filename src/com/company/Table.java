package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 10/17/2017.
 */
public class Table {
    enum Stage {
        // each number value represents number of cards the table should have
        PREFLOP(0), FLOP(3), TURN(4), RIVER(5);

        private int numberOfCardsToDeal;

        // needed for giving each stage a number value
        Stage(int numberOfCardsToDeal) { this.numberOfCardsToDeal = numberOfCardsToDeal; }

        // Returns number of cards that should be on the table this round
        public int getNumberOfCardsNeeded(){ return numberOfCardsToDeal; }

        // Save an array of every Stage value so getNextStage won't need to keep calling values().
        private static final Stage [] stageValues = values();
        public Stage getNextStage(){
            return stageValues[(this.ordinal()+1) % 4];
        };
    }

    private Stage stage = Stage.PREFLOP;

    // turn represents which entity's turn it is to act, with 0 being the table's
    private int turn = 0;

    // pot holds the current round's bets
    private int pot = 0;

    // Cards dealt onto table for all to use;
    private List<Card> communityCards = new ArrayList<>();

    private Deck deck = new Deck();

    void dealToTable(){
        while (communityCards.size() < stage.getNumberOfCardsNeeded()){
            communityCards.add(deck.deal());
        }
    }

    // currently used for testing
    public static void main(String [ ] args){
        Table table = new Table();
        for (int i = 0; i < 4; i++) {
            System.out.println(table.stage.toString());
            table.stage = table.stage.getNextStage();
        }
    }
}
