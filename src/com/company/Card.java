package com.company;

import java.util.*;

/**
 * Created by Ethan on 9/27/2017.
 */
public class Card {
    // values set up by lowest to highest value
    enum Suite {
        DIAMONDS, CLUBS, HEARTS, SPADES
    }

    private Suite suite;
    private int value;

    public Suite getSuite(){
        return suite;
    }

    public int returnValue(){
        return value;
    }

    Card(Suite suite, int value){
        this.suite = suite;
        this.value = value;
    }
}
