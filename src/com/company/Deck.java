package com.company;

import com.company.Card;
import java.util.Collections;
import java.util.Stack;


/**
 * Created by Ethan on 9/27/2017.
 */
public class Deck {

    Deck(){
        init();
    }

    private static final int CARDS_PER_SUITE = 13;
    private static final int NUMBER_OF_SUITES = 4;

    private Stack<Card> cards;
    private Stack<Card> usedCards;

    private void init() {        //this is essentially the initializer since we only need the deck to be created once
        cards = new Stack<>();
        for (int i = 0; i < NUMBER_OF_SUITES; i++) {
            for (int j = 2; j < CARDS_PER_SUITE + 2; j++) {
                cards.push(new Card(Card.Suite.values()[i], j));
            }
        }
    }

    public void shuffle() {
        // restoring used cards back to the deck
        while (!usedCards.isEmpty())
            cards.push(usedCards.pop());

        Collections.shuffle(cards);
    }

    public Card dealOneCard() {    // we would have to call this twice when dealing
        Card deal = cards.pop();    // what is dealt should have its own stack so we don't have to fillDeck every round
        usedCards.push(deal);
        return deal;
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.init();
        System.out.printf("ok");
    }
}
