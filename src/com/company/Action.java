package com.company;

import java.util.*;

/**
 * Created by Kailie on 10/24/2017.
 */

public class Action {

	private boolean fold = false;
	private int call = 10; // call starts as the bigBlind value from Player
	private int raise;
	private int pot;
	private int balance = 500;

	public void check() {
		balance = balance - 0;
		pot += 0;
	}

	public void call() {
		if (balance == 0) {
			System.err.print("You have NO money left... \nSo you must fold.");
			fold();
		} else if (call > balance) {
			System.err.print("You don't have this much money... \nSo you must jam.");
			jam();
		} else {
			balance = balance - call;
			pot += call;
		}
	}

	public void raise() {
		Scanner r = new Scanner(System.in);
		System.out.println("Raise by how much?");
		raise = r.nextInt();// new call value increments by the raise value
		while (call + raise > balance) {
			System.err.println("You don't have this much money... \nSo how much would you like to raise?");
			raise = r.nextInt();
		}
		call += raise;
		balance = balance - call;
		pot += call;
	}

	public void jam() {
		call = balance;
		pot += balance;
		balance = 0;
	}

	public void fold() {
		fold = true;
	}

	public boolean hasFold() {
		return fold;
	}

	public int getPot() {
		return pot;
	}

	@Override
	public String toString() {
		return "Action [pot=" + pot + ", balance=" + balance + "]";
	}

	public static void main(String[] args) {
		Player k = new Player("Kay", 2000);
		Action a = new Action();
		System.out.println(k.toString());
		System.out.println(a.toString());
		a.call();
		System.out.println(a.toString());
		a.raise();
		System.out.println(a.toString());
		a.call();
	}
}
