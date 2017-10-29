package com.company;
import java.util.*;

/**
 * Created by Kailie on 10/17/2017.
 */

public class Player {

	private String username;
	private static double balance;
	private boolean smallBlind;
	private boolean bigBlind;
	private boolean canPlay;
	private int dealer = 4;
	private int playerID = 0;
	private static int playerCount = 0;
	//private Action action = new Action();

	Player(String username, int balance){
		playerCount++;
		playerID = playerCount;
		this.username = username;
		this.balance = balance;
	}

	public void display() {
		//should display their name, balance, and two cards
		//might also be displaying their possible actions when its their turn?
	}
	
/*
 * The official site said a dealer is determined by whoever 
 * has the highest card in a draw before the game starts
 */
	
/*
 	public boolean isDealer() {
	
 		dealer = _.playerID;
	}*/
	
	/*
	 * The small blind is played by the person one position left of the dealer
	 * This blind requires the player to pay .005 of their balance
	 */
	public boolean isSmallBlind() {
		smallBlind = false;
		if ((dealer + playerCount - 1) % playerCount == getPlayerID() % playerCount) {
			smallBlind = true;
			balance = balance * .995;
		}
		return smallBlind;
	}

	/*
	 * The big blind is played by the person two positions left of the dealer
	 * This blind requires the player to pay .01 of their balance
	 */
	public boolean isBigBlind() {
		bigBlind = false;
		if ((dealer + playerCount - 2) % playerCount == getPlayerID()% playerCount) {
			bigBlind = true;
			//balance = balance * .99;
		}
		return bigBlind;
	}

	public double getBalance() {
		return balance;
	}

	public boolean canPlay() {
		return getBalance() > 0;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public int getPlayerCount() {
		return playerCount;
	}
	
	@Override
	public String toString() {
		return "Player [username=" + username + ", balance=" + getBalance() + ", canPlay=" + canPlay() + ", PlayerID="
				+ playerID + "]";
	}
	
	public static void main(String [] args) {
		Player k = new Player("Kay", 2000);
		System.out.println(k.toString());
		Player l = new Player("Rob", 1500);
		System.out.println(l.toString());
		Player m = new Player("Vanessa", 1700);
		System.out.println(m.toString());
		Player n = new Player("David", 385);
		System.out.println(n.toString());
		System.out.println("\nBIG BLIND:");
		System.out.println(k.isBigBlind());
		System.out.println(l.isBigBlind());
		System.out.println(l.toString());
		System.out.println(m.isBigBlind());
		System.out.println(n.isBigBlind());
		System.out.println("\nSMALL BLIND:");
		System.out.println(k.isSmallBlind());
		System.out.println(l.isSmallBlind());
		System.out.println(m.isSmallBlind());
		System.out.println(m.toString());
		System.out.println(n.isSmallBlind());
	}
}
