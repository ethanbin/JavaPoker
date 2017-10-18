package com.company;

import com.company.Card;
import java.util.Collections;
import java.util.Stack;


/**
 * Created by Ethan on 9/27/2017.
 */
public class Deck extends Card{

	Deck(Suite suite, int value) {
		super(suite, value);
		// TODO Auto-generated constructor stub
	}

    private static final int CARDS_PER_SUITE  = 13;
    private static final int NUMBER_OF_SUITES = 4;

    Stack<Card> cards;
    Stack<Card> usedCards;

    public void init(){		//this is essentially the initializer since we only need the deck to be created once
        cards = new Stack<>();
        for (int i = 0; i < NUMBER_OF_SUITES; i++){
            for (int j = 2; j < CARDS_PER_SUITE + 2; j++){
                cards.push(new Card(Card.Suite.values()[i], j));
            }
        }
    }
    
    public void shuffle() {
    	Card ret;	//the card being returned
    	
    	while(!usedCards.isEmpty()) {
    		ret = usedCards.pop();
    		cards.push(ret);
    	}
    	
        Collections.shuffle(cards);
    }

    public Card dealOneCard(){	// we would have to call this twice when dealing
    	Card deal = cards.pop();	// what is dealt should have its own stack so we don't have to fillDeck every round
    	usedCards.push(deal);
    	return deal;    	
    }
}
