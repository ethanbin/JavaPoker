package com.company;

import java.util.*;

/**
 * Created by Ethan on 11/13/2017.
 */
public class HandValue implements Comparable<HandValue>{
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

	HandValue(List<Card> communityCards, List<Card> holeCards) {
		init();
		holeAndCommunityCards.addAll(communityCards);
		holeAndCommunityCards.addAll(holeCards);
		this.evaluateHand();
	}

	private void init() {
		handValue = new ArrayList<>();
		holeAndCommunityCards = new ArrayList<>();
		wasEvaluated = false;
	}

	private void evaluateHand() {

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
			findTwoPairs();
		}
		if (!wasEvaluated) {
			findPair();
		}
		if (!wasEvaluated) {
			findHighCard();
		}

	}

    @Override
    // if this is greater than other, should return positive
    // if this is less than other, should return negative
    // if this is equal to other, should return 0
    public int compareTo(HandValue other){
        int ret = 0;
        try {
            for (int i = 0; i < this.handValue.size() && ret == 0; i++) {
                ret = this.handValue.get(i).compareTo(other.handValue.get(i));
            }
        }
        catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundsException("Comparing players' hands failed. Both hands have same values but different lengths.");
        }

        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HandValue)) return false;
        HandValue handValue1 = (HandValue) o;
        return Objects.equals(handValue, handValue1.handValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handValue);
    }

    public boolean isGreaterThan(HandValue other){
        if (this.compareTo(other) > 0)
            return true;
        return false;
    }

	// TODO make this also work with the ace as a 2
	private void findRoyalFlushOrStraightFlush() {
		Collections.sort(holeAndCommunityCards, new SuitComparator());
		int highValue = holeAndCommunityCards.get(0).getValue();

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
		int highValue = holeAndCommunityCards.get(0).getValue();
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
		int highValue = holeAndCommunityCards.get(0).getValue();

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
		int highValue = holeAndCommunityCards.get(0).getValue();

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
	
	private void findTwoPairs() {
		Collections.sort(holeAndCommunityCards, new ValueComparator());
		int highValue = holeAndCommunityCards.get(0).getValue();

		// represents how many cards of the same suit we found in a row so far
		int pairs = 0;
		final int pairCount = 2;

		// bool value represents whether or not we might be able to have a straight
		// flush at the moment
		// (true when last card(s) and current card are the same suit)
		for (int i = 1; i < holeAndCommunityCards.size() && pairs < pairCount; i++) {
			Card lastCard = holeAndCommunityCards.get(i - 1);
			Card currentCard = holeAndCommunityCards.get(i);
			if (lastCard.getValue() == currentCard.getValue()) {
				pairs++;
			} else {
				highValue = currentCard.getValue();
			}
		}
		
		if (pairs == pairCount) {
			handValue.add(HandRankings.TWO_PAIRS.getHandRankingStrength());
			handValue.add(highValue);
			wasEvaluated = true;
		}
	}
	
	private void findPair() {
		Collections.sort(holeAndCommunityCards, new ValueComparator());
		int highValue = holeAndCommunityCards.get(0).getValue();

		// represents how many cards of the same suit we found in a row so far
		int numberOfMatchingValues = 1;
		final int pairCount = 2;

		// bool value represents whether or not we might be able to have a straight
		// flush at the moment
		// (true when last card(s) and current card are the same suit)
		for (int i = 1; i < holeAndCommunityCards.size() && numberOfMatchingValues < pairCount; i++) {
			Card lastCard = holeAndCommunityCards.get(i - 1);
			Card currentCard = holeAndCommunityCards.get(i);
			if (lastCard.getValue() == currentCard.getValue()) {
				numberOfMatchingValues++;
			} else {
				highValue = currentCard.getValue();
				numberOfMatchingValues = 1;
			}
		}

		if (numberOfMatchingValues == pairCount) {
			handValue.add(HandRankings.PAIR.getHandRankingStrength());
			handValue.add(highValue);
			wasEvaluated = true;
		}
	}

	private void findHighCard() {
		Collections.sort(holeAndCommunityCards, new ValueComparator());
		int highValue = holeAndCommunityCards.get(0).getValue();
		
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
        System.out.println(hv);


        holeCards = new ArrayList<>();
        communityCards = new ArrayList<>();
        holeCards.add(new Card(Card.Suit.SPADES, 9));
        holeCards.add(new Card(Card.Suit.SPADES, 10));
        communityCards.add(new Card(Card.Suit.SPADES, 9));
        communityCards.add(new Card(Card.Suit.SPADES, 12));
        communityCards.add(new Card(Card.Suit.SPADES, 2));
        communityCards.add(new Card(Card.Suit.CLUBS, 1));
        communityCards.add(new Card(Card.Suit.CLUBS, 7));
        // should be flush
        HandValue hv2 = new HandValue(holeCards, communityCards);
        System.out.println(hv2);

        if (hv2.isGreaterThan(hv))
            System.out.println("hv2 is greater than hv");
        else if (hv2.compareTo(hv) < 0)
            System.out.println("hv2 is less than hv");
        else if (hv2.equals(hv))
            System.out.println("hv2 equals hv");
        else
            System.err.println("Something went wrong in comparing hv2 with hv");
    }
}
