package com.company;

import java.util.*;

/**
 * Created by Ethan on 11/13/2017.
 */
public class HandValue {
	private enum HandRankings {
		ROYAL_FLUSH(9), STRAIGHT_FLUSH(8), FOUR_OF_A_KIND(7), FULL_HOUSE(6), FLUSH(5), STRAIGHT(4), THREE_OF_A_KIND(
				3), TWO_PAIRS(2), PAIR(1), HIGH_CARD(0);

		private int handRankingStrength;

		HandRankings(int strength) {
			handRankingStrength = strength;
		}

		int getHandRankingStrength() {
			return handRankingStrength;
		}
	}

	// first value in the list is the hand ranking value, then the high card
	private List<Integer> handValue;
	private List<Card> holeAndCommunityCards;
	private boolean wasEvaluated;
	private final int aceLowValue = 1;
	private final int aceHighValue = 14;
	private final int handValueSize = 5;
	private int highValue;

	HandValue(List<Card> communityCards, List<Card> holeCards) {
		init();
		holeAndCommunityCards.addAll(communityCards);
		holeAndCommunityCards.addAll(holeCards);
	}

	private void init() {
		handValue = new ArrayList<>();
		holeAndCommunityCards = new ArrayList<>();
		wasEvaluated = false;
	}

	public void evaluateHand() {

		if (!wasEvaluated) {
			findRoyalFlushOrStraightFlush();
		}
		if (!wasEvaluated) {
			findFourOfAKind();
		}
		if (!wasEvaluated) {
			// findFullHouse();
		}
		if (!wasEvaluated) {
			findFlush();
		}
		if (!wasEvaluated) {
			// findStraight();
		}
		if (!wasEvaluated) {
			findThreeOfAKind();
		}
		if (!wasEvaluated) {
			// findTwoPairs();
		}
		if (!wasEvaluated) {
			findPair();
		}
		if (!wasEvaluated) {
			findHighCard();
		}

	}

	// TODO make this also work with the ace as a 2
	private void findRoyalFlushOrStraightFlush() {
		Collections.sort(holeAndCommunityCards, new SuitComparator());
		highValue = holeAndCommunityCards.get(0).getValue();

		// represents how many cards of the same suit we found in a row so far
		int matchingCardsInARow = 1;

		// bool value represents whether or not we might be able to have a straight
		// flush at the moment
		// (true when last card(s) and current card are the same suit)
		boolean potentiallyAStraightFlush = false;
		for (int i = 1; i < holeAndCommunityCards.size() && !(i >= 4 && !potentiallyAStraightFlush)
				&& matchingCardsInARow < handValueSize; i++) {
			Card lastCard = (holeAndCommunityCards.get(i - 1));
			Card currentCard = holeAndCommunityCards.get(i);
			if (lastCard.getSuitValue() == currentCard.getSuitValue()
					&& lastCard.getValue() == currentCard.getValue() + 1) {
				potentiallyAStraightFlush = true;
				matchingCardsInARow++;
			} else {
				highValue = currentCard.getValue();
				potentiallyAStraightFlush = false;
				matchingCardsInARow = 1;
			}
		}
		if (matchingCardsInARow == handValueSize) {
			if (highValue == aceHighValue)
				handValue.add(HandRankings.ROYAL_FLUSH.getHandRankingStrength());
			else
				handValue.add(HandRankings.STRAIGHT_FLUSH.getHandRankingStrength());
			handValue.add(highValue);
			wasEvaluated = true;
		}
	}

	private void findFourOfAKind() {
		Collections.sort(holeAndCommunityCards, new ValueComparator());
		highValue = holeAndCommunityCards.get(0).getValue();
		int kicker = highValue;

		// represents how many cards of the same suit we found in a row so far
		int numberOfMatchingValues = 1;
		final int fourOfAKindCount = 4;

		// bool value represents whether or not we might be able to have a straight
		// flush at the moment
		// (true when last card(s) and current card are the same suit)
		for (int i = 1; i < holeAndCommunityCards.size() && numberOfMatchingValues < fourOfAKindCount; i++) {
			Card lastCard = holeAndCommunityCards.get(i - 1);
			Card currentCard = holeAndCommunityCards.get(i);
			if (lastCard.getValue() == currentCard.getValue()) {
				numberOfMatchingValues++;
			} else {
				highValue = currentCard.getValue();
				numberOfMatchingValues = 1;
			}
		}

		// find kicker
		for (Card c : holeAndCommunityCards) {
			if (c.getValue() != highValue) {
				kicker = c.getValue();
				break;
			}
		}

		if (numberOfMatchingValues == fourOfAKindCount) {
			handValue.add(HandRankings.FOUR_OF_A_KIND.getHandRankingStrength());
			handValue.add(highValue);
			handValue.add(kicker);
			wasEvaluated = true;
		}
	}

	private void findFlush() {
		Collections.sort(holeAndCommunityCards, new SuitComparator());
		highValue = holeAndCommunityCards.get(0).getValue();

		// represents how many cards of the same suit we found in a row so far
		int matchingCardsInARow = 1;

		// bool value represents whether or not we might be able to have a flush at the
		// moment
		// (true when last card(s) and current card are the same suit)
		boolean potentiallyAFlush = false;
		for (int i = 1; i < holeAndCommunityCards.size() && !(i >= 4 && !potentiallyAFlush)
				&& matchingCardsInARow < handValueSize; i++) {
			Card lastCard = (holeAndCommunityCards.get(i - 1));
			Card currentCard = holeAndCommunityCards.get(i);
			if (lastCard.getSuitValue() == currentCard.getSuitValue()) {
				potentiallyAFlush = true;
				matchingCardsInARow++;
			} else {
				highValue = currentCard.getValue();
				potentiallyAFlush = false;
				matchingCardsInARow = 1;
			}
		}
		if (matchingCardsInARow == handValueSize) {
			handValue.add(HandRankings.FLUSH.getHandRankingStrength());
			handValue.add(highValue);
			wasEvaluated = true;
		}
	}

	private void findThreeOfAKind() {
		Collections.sort(holeAndCommunityCards, new ValueComparator());
		highValue = holeAndCommunityCards.get(0).getValue();

		// represents how many cards of the same suit we found in a row so far
		int numberOfMatchingValues = 1;
		final int ThreeOfAKindCount = 3;

		// bool value represents whether or not we might be able to have a straight
		// flush at the moment
		// (true when last card(s) and current card are the same suit)
		for (int i = 1; i < holeAndCommunityCards.size() && numberOfMatchingValues < ThreeOfAKindCount; i++) {
			Card lastCard = holeAndCommunityCards.get(i - 1);
			Card currentCard = holeAndCommunityCards.get(i);
			if (lastCard.getValue() == currentCard.getValue()) {
				numberOfMatchingValues++;
			} else {
				highValue = currentCard.getValue();
				numberOfMatchingValues = 1;
			}
		}

		if (numberOfMatchingValues == ThreeOfAKindCount) {
			handValue.add(HandRankings.THREE_OF_A_KIND.getHandRankingStrength());
			handValue.add(highValue);
			wasEvaluated = true;
		}
	}
	
	private void findPair() {
		Collections.sort(holeAndCommunityCards, new ValueComparator());
		highValue = holeAndCommunityCards.get(0).getValue();

		for (int i = 1; i < holeAndCommunityCards.size(); i++) {
			Card lastCard = (holeAndCommunityCards.get(i - 1));
			Card currentCard = holeAndCommunityCards.get(i);
			if (lastCard.getSuitValue() == currentCard.getSuitValue()) {
				handValue.add(HandRankings.PAIR.getHandRankingStrength());
				handValue.add(highValue);
				wasEvaluated = true;
			}
		}
	}

	private void findHighCard() {
		Collections.sort(holeAndCommunityCards, new ValueComparator());
		highValue = holeAndCommunityCards.get(0).getValue();
		
		handValue.add(HandRankings.HIGH_CARD.getHandRankingStrength());
		handValue.add(highValue);
		wasEvaluated = true;
	}
	
	@Override
	public String toString() {
		if (handValue.size() == 0)
			return "Hand not evaluated.";
		HandRankings[] rankings = HandRankings.values();
		Collections.reverse(Arrays.asList(rankings));
		String ret = rankings[handValue.get(0)].toString() + ": ";
		ret += handValue.get(1).toString();
		if (handValue.size() > 2)
			ret += ", Kicker(s): ";
		for (int i = 2; i < handValue.size(); i++) {
			ret += handValue.get(i) + " ";
		}
		return ret;
	}

	public static void main(String[] args) {
		List<Card> holeCards = new ArrayList<>();
		holeCards.add(new Card(Card.Suit.SPADES, 9));
		holeCards.add(new Card(Card.Suit.SPADES, 10));

		List<Card> communityCards = new ArrayList<>();
		communityCards.add(new Card(Card.Suit.SPADES, 9));
		communityCards.add(new Card(Card.Suit.DIAMONDS, 12));
		communityCards.add(new Card(Card.Suit.SPADES, 2));
		communityCards.add(new Card(Card.Suit.CLUBS, 1));
		communityCards.add(new Card(Card.Suit.CLUBS, 7));

		HandValue hv = new HandValue(holeCards, communityCards);
		hv.evaluateHand();
		System.out.println(hv);
	}
}
