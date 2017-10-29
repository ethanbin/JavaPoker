package TexasHoldem;

import java.util.*;

import TexasHoldem.Hand.HandType;

/**
 * Created by Kailie on 10/28/2017.
 */

public class Showdown {
	
	public static void main(String [] args) {
		Hand a = new Hand(HandType.FLUSH);
		System.out.println(a.toString());
	}
	
}
