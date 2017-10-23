package com.company;

import java.util.*;

/**
 * Created by Ethan on 9/27/2017.
 */
public class Card {
    // values set up by lowest to highest value
    enum Suits {
        DIAMONDS, CLUBS, HEARTS, SPADES
    }

    private Suits suits;
    private int value;

    public Suits getSuits(){
        return suits;
    }

    public int returnValue(){
        return value;
    }

    Card(Suits suits, int value){
        this.suits = suits;
        this.value = value;
    }
}
