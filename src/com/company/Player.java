package com.company;
import java.util.*;

/**
 * Created by Kailie on 10/17/2017.
 */

public class Player {

	private String username;
	private int turnID;
	private int balance;
	private boolean smallBlind;
	private boolean bigBlind;
	private boolean canPlay;

	Player(String username, int balance){
		this.username = username;
		this.balance = balance;
	}

	public void display() {
		//should display their name, balance, and two cards
		//might also be displaying their possible actions when its their turn?
	}

//	public boolean isDealer() {
//
//	}

	public boolean isSmallBlind() {
		//we need a counter to know which player its up to to determine this, bigBlind and Dealer too
		//we also need a player ID to be able to do ^^^^^^
		return smallBlind;
	}

	public boolean isBigBlind() {
		return bigBlind;
	}

	public int getBalance() {
		return balance;
	}

	public boolean canPlay() {
		return balance > 0;
	}
}
