package com.company;

import com.company.Card;
import java.util.Collections;
import java.util.Stack;


/**
 * Created by Ethan on 9/27/2017.
 */
public class Deck {
    private static final int CARDS_PER_SUITE  = 13;
    private static final int CARDS_PER_DECK   = 52;
    private static final int NUMBER_OF_SUITES = 4;

    Stack<Card> cards;

    public void fillDeckAndShuffle(){
        cards = new Stack<>();
        for (int i = 0; i < NUMBER_OF_SUITES; i++){
            for (int j = 2; j < CARDS_PER_SUITE + 2; j++){
                cards.push(new Card(Card.Suite.values()[i], j));
            }
        }
        Collections.shuffle(cards);
    }

    public Card deal(){
        return cards.pop();
    }
}
