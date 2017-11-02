package com.company;

import java.util.ArrayList;
import java.util.Stack;
import java.util.List;

/**
 * Created by Ethan on 10/17/2017.
 */
public class Table {
    enum Stage {
        // each number value represents number of cards that should be dealt now
        PRE_FLOP(0), FLOP(3), TURN(1), RIVER(1), SHOWDOWN(0);

        private int numberOfCardsToDeal;

        // needed for giving each stage a number value
        Stage(int numberOfCardsToDeal) {
            this.numberOfCardsToDeal = numberOfCardsToDeal;
        }

        // Returns number of cards that should be on the table this round
        public int getNumberOfCardsToDeal() {
            return numberOfCardsToDeal;
        }
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
    private int firstToBetIndex;
    private int activePlayers;
    private int amountToCall;

    public Table() {
        this.init();
    }

    private void init() {
        pot = 0;
        deck = new Deck();
        communityCards = new Stack<>();
        players = new ArrayList<>(TABLE_SEATS);
        tableIsFull = false;
        dealerIndex = smallBlindIndex = bigBlindIndex = -1;
        activePlayers = 0;
        amountToCall = 0;
    }

    public int getActivePlayers() {
        return activePlayers;
    }

    public void playerJoinsGame(Player player) {
        if (!tableIsFull) {
            players.add(player);
            activePlayers++;
            if (players.size() == TABLE_SEATS) {
                tableIsFull = true;
            }

            return;
        }

        // Only prints out if table is full.
        System.out.println("Table is Full " + player.getUserName() + " cannot join game.\n");
    }

    public void playMatch() {
        for (Stage stage : Stage.values()) {
            callStageMethod(stage);
        }
    }

    private void callStageMethod(Stage stage) {
        switch (stage) {
            case PRE_FLOP:
                stagePreFlop(stage);
                break;
            case FLOP:
                dealingStage(stage);
                break;
            case TURN:
                dealingStage(stage);
                break;
            case RIVER:
                dealingStage(stage);
                break;
            case SHOWDOWN:
                stageShowDown();
                break;
            default:
                //throw
        }
    }

    private void stagePreFlop(Stage stage) {
        System.out.println("Stage: " + stage.toString());
        setDealerAndBlinds();
        dealPlayersHoleCards();
        requestSmallBlind();
        requestBigBlind();
        goThroughRoundOfBetting();
        System.out.println(this);
    }

    private void dealingStage(Stage stage) {
        System.out.println("Stage: " + stage.toString());
        dealToTable(stage.getNumberOfCardsToDeal());
        System.out.println(this);
    }

    private void stageShowDown() {
        System.out.println("Stage: Showdown:");
        System.out.println(this);
    }

    //TODO: make this safe against miscalculation when current dealer is taken out of player list
    private void setDealerAndBlinds() {
        dealerIndex = (dealerIndex + 1) % players.size();
        smallBlindIndex = getNextValidatedPlayerIndex(dealerIndex);
        bigBlindIndex = getNextValidatedPlayerIndex(smallBlindIndex);
        firstToBetIndex = getNextValidatedPlayerIndex(bigBlindIndex);
    }

    private void dealPlayersHoleCards() {
        int timesToDeal = 2;

        System.out.println("Dealing Cards To Players...\n");

        while (timesToDeal != 0) {
            for (Player player : players) {
                player.givePlayerCard(deck.dealCard());
            }
            timesToDeal--;
        }
    }

    private void requestSmallBlind() {
        pot += players.get(smallBlindIndex).bet(TABLE_MINIMUM_BET / 2);
    }

    private void requestBigBlind() {
        pot += players.get(bigBlindIndex).bet(TABLE_MINIMUM_BET);
        amountToCall = TABLE_MINIMUM_BET;
    }

    // TODO: Still needs further testing for edge cases. ie: firstToBetIndex folds.
    private void goThroughRoundOfBetting() {
        boolean continueBetting = true;
        int currentTurnIndex = firstToBetIndex;
        int betValue;

        while (continueBetting) {
            Player currentPlayer = players.get(currentTurnIndex);

            if(currentPlayer.getIsFolded()){
                currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);
                continue;
            }

            betValue = currentPlayer.getPlayerBetValue(amountToCall);

            switch (currentPlayer.getAction()) {
                case "CH":
                    pot += betValue;
                    break;
                case "C":
                    pot += betValue;
                    break;
                case "R":
                    amountToCall += betValue;
                    pot += betValue;
                    break;
                default:
                    amountToCall += betValue;
                    break;
            }

            currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);

            if(currentTurnIndex == firstToBetIndex && players.get(firstToBetIndex).getCurrentBet() == amountToCall){
                continueBetting = false;
            }
        }
    }

    private int getNextValidatedPlayerIndex(int index){
        int newIndex = (index + 1) % players.size();
        return index;
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


       /* while (table.getActivePlayers() != 1){
            table.playMatch();
        }*/

        table.playMatch();
    }
}
