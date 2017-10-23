package com.company;

/**
 * Created by Ethan on 9/27/2017.
 */
public class Card {

    /**
     * Suit enum class preset with appropriate string and int values.
     */
    enum Suit {

        DIAMONDS("Diamonds", 1), CLUBS("Clubs", 2), HEARTS("Hearts", 3), SPADES("Spades", 4);

        private final String suitName;
        private final Integer suitValue;

        Suit(String suit, Integer suitValue) {
            this.suitName = suit;
            this.suitValue = suitValue;
        }

        public String getSuitName() {
            return suitName;
        }
        public Integer getSuitValue() {
            return suitValue;
        }
    }

    /**
     * Card member variables.
     */
    private String suitName;
    private int suitValue;
    private int value;

    /**
     * Return suitName value.
     *
     * @return String
     */
    public String getSuitName(){
        return suitName;
    }

    /**
     * Return suitValue value.
     *
     * @return int
     */
    public int getSuitValue(){
        return suitValue;
    }

    /**
     * Return value value.
     *
     * @return int
     */
    public int getValue(){
        return value;
    }

    /**
     * Card constructor.
     *
     * @param suit Suit
     * @param value int
     */
    Card(Suit suit, int value){
        this.suitName = suit.getSuitName();
        this.suitValue = suit.getSuitValue();
        this.value = value;
    }

    @Override
    public String toString(){
        return "The " + value + " of " + suitName + "\n";
    }
}
