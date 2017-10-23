package com.company;

import java.util.*;

/**
 * Created by Ethan on 9/27/2017.
 */
public class Card {

    private int suit;
    private int value;

    public int getSuit(){
        return suit;
    }

    public int getValueValue(){
        return value;
    }

    Card(int suit, int value){
        this.suit = suit;
        this.value = value;
    }
}
