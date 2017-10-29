package TexasHoldem;

import java.util.*;

/**
 * Created by Kailie on 10/28/2017.
 */

public class Hand {
	enum HandType {
		HIGHCARD("High Card", 0), ONEPAIR("One Pair", 1), TWOPAIRS("Two Pairs", 2), 
		THREEOFAKIND("Three of a Kind",	3), STRAIGHT("Straight", 4), FLUSH("Flush", 5), 
		FULLHOUSE("Full House", 6), FOUROFAKIND("Four of a Kind", 7), 
		STRAIGHTFLUSH("Straight Flush", 8), ROYALFLUSH("Royal Flush", 9);

		private String handType;
		private int valueOfHand;

		HandType(String handType, int valueOfHand) {
			this.handType = handType;
			this.valueOfHand = valueOfHand;
		}

		public String getHandType() {
			return handType;
		}

		public int getValueOfHand() {
			return valueOfHand;
		}
	}
	
	/*
	 * HandType memeber variables
	 */
	private String handType;
	private int valueOfHand;
	
	public Hand(HandType hand) {
		this.handType = hand.getHandType();
		this.valueOfHand = hand.getValueOfHand();
	}
	
	public String getHandType() {
		return handType;
	}
	
	public int getValueOfHand() {
		return valueOfHand;
	}

	@Override
	public String toString() {
		return "Hand [handType = " + handType + ", valueOfHand = " + valueOfHand + "]";
	}

}
