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
	private final int ACE_LOW_VALUE = 1;
	private final int ACE_HIGH_VALUE = 14;
	private final int HAND_VALUE_CARD_COUNT = 5;
	private final int PAIR_VALUE_LIST_SIZE = 5;
	private final int THREE_OF_A_KIND_VALUE_LIST_SIZE= 4;
	private final int HAND_CARD_VALUE_LIST_SIZE = 6;


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
			findFullHouse();
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

    /**
     * Overrider for compareTo.
     * 
     * Returns 0 if equal, less than 0 if this < other, or greater than 0 if this > other.
     *
     * @param other to be compared with.
     * @return int
     * @throws IndexOutOfBoundsException if other was assigned incorrectly and either has fewer indexes than it should,
     *         or was given the wrong hand type value.
     */
    // if this is greater than other, should return positive
    // if this is less than other, should return negative
    // if this is equal to other, should return 0
    @Override
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
				&& matchingCardsInARow < HAND_VALUE_CARD_COUNT; i++) {
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
		if (matchingCardsInARow == HAND_VALUE_CARD_COUNT) {
			if (highValue == ACE_HIGH_VALUE)
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

		// represents how many cards of the same value we found in a row so far
		int numberOfMatchingValues = 1;
		final int fourOfAKindCount = 4;

		// bool value represents whether or not we might be able to have a four of a kind at the moment
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

    private void findFullHouse(){
        // a full house is essentially both a 3 of a kind and a pair.
        // therefore, check if a 3 of a kind exists, and record what value it is
        // then, disregarding the cards that make the 3 of a kind, check if a pair exists and record its value

        // if both found, fill handValue with fullHouse value, 3 of a kind value, then pair value.
        Collections.sort(holeAndCommunityCards, new ValueComparator());

        // to keep track of what the three of a kind is
        int threeOfAKindValue;

        // to keep track of what the pair is
        int pairValue;

        findThreeOfAKind();
        // if no 3 of a kind found, return.
        if (handValue.size() == 0)
            return;
        else {
            // because we are actually checking for full house, we didn't really evaluate the hand entirely yet,
            // even though findThreeOfAKind set wasEvaluated to true.
            wasEvaluated = false;
        }

        threeOfAKindValue = handValue.get(1);

        // reset handValue
        handValue = new ArrayList<>();

        // keep backup of holeAndCommunityCards so we can restore it after we modify it and call findPair
        List<Card> originalListOfCards = new ArrayList<>(holeAndCommunityCards);

        // set up cards to exclude the found 3 of a kind cards so findPair won't falsely evaluate
        holeAndCommunityCards = new ArrayList<>();
        for (Card c : originalListOfCards){
            if (c.getValue() != threeOfAKindValue)
                holeAndCommunityCards.add(c);
        }

        findPair();

        // if no pair found, there is no full house, stop this method
        if (handValue.size() == 0) {
            // reset handValue
            handValue = new ArrayList<>();
            // restore holeAndCommunityCards
            holeAndCommunityCards = new ArrayList<>(originalListOfCards);
            return;
        }

        pairValue = handValue.get(1);

        // reset handValue
        handValue = new ArrayList<>();

        // restore holeAndCommunityCards
        holeAndCommunityCards = new ArrayList<>(originalListOfCards);

        // fill out handValue
        handValue.add(HandRankings.FULL_HOUSE.getHandRankingStrength());
        handValue.add(threeOfAKindValue);
        handValue.add(pairValue);
        wasEvaluated = true;
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
				&& matchingCardsInARow < HAND_VALUE_CARD_COUNT; i++) {
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
		if (matchingCardsInARow == HAND_VALUE_CARD_COUNT) {
			handValue.add(HandRankings.FLUSH.getHandRankingStrength());
			handValue.add(highValue);
			wasEvaluated = true;
		}
	}

	// straight

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
			// add kickers
            for (int i = 0; i < holeAndCommunityCards.size() && handValue.size() < THREE_OF_A_KIND_VALUE_LIST_SIZE; i++)
            {
                if (holeAndCommunityCards.get(i).getValue() != highValue)
                    handValue.add(holeAndCommunityCards.get(i).getValue());
            }
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

        handValue.add(HandRankings.HIGH_CARD.getHandRankingStrength());

        for (int i = 0; i < HAND_VALUE_CARD_COUNT; i++)
            handValue.add(holeAndCommunityCards.get(i).getValue());

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

		// will be true when either the hand has kickers or when the hand is a two pair or full house
        if (handValue.size() > 2) {
            // these hands have 2 values, not just 1
            if (handValue.get(0).intValue() == HandRankings.TWO_PAIRS.getHandRankingStrength() ||
                    handValue.get(0).intValue() == HandRankings.FULL_HOUSE.getHandRankingStrength()){
                ret += ", " + handValue.get(2).toString();
            }
            else {
                ret += ", Kicker(s): ";
                for (int i = 2; i < handValue.size(); i++) {
                    ret += handValue.get(i) + " ";
                }
            }
        }

		return ret;
	}

	public static void main(String[] args) {
        // test: STRAIGHT_FLUSH(with that ace correction), STRAIGHT(4),
        // THREE_OF_A_KIND(3), TWO_PAIRS(2), PAIR(1), HIGH_CARD(0);
		List<Card> holeCards = new ArrayList<>();
		holeCards.add(new Card(Card.Suit.SPADES, 9));
		holeCards.add(new Card(Card.Suit.CLUBS, 9));

		List<Card> communityCards = new ArrayList<>();
		communityCards.add(new Card(Card.Suit.HEARTS, 9));
		communityCards.add(new Card(Card.Suit.DIAMONDS, 4));
		communityCards.add(new Card(Card.Suit.DIAMONDS, 10));
		communityCards.add(new Card(Card.Suit.DIAMONDS, 11));
		communityCards.add(new Card(Card.Suit.DIAMONDS, 12));

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
        //System.out.println(hv2);

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
