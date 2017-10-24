package com.company;

import java.util.*;

/**
 * Created by Kailie on 10/24/2017.
 */  

public class Action {
	
	
	private boolean fold = false;
	private int call = 10;
	private int raise;
	private int pot;
	private int balance = Player.getBalance();

	public void check() {
		balance = balance - 0;
		pot += 0;
	}

	public void call() {
		balance = balance - call;
		pot += call;
	}

	public void raise() {
		Scanner r = new Scanner(System.in);
		System.out.println("Raise by how much?");
		raise = r.nextInt();
		call = raise;		//new call value becomes the raise value
		balance = balance - raise;
		pot += raise;
	}

	public void jam() {
		balance = 0;
		pot += balance;
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
		return "Action [balance=" + balance + "]";
	}

	public static void main(String[]args) {
		Player k = new Player("Kay", 2000);
		Action a = new Action();
		System.out.println(k.toString());
		System.out.println(a.toString());
		a.call();
		System.out.println(a.toString());
		a.raise();
		System.out.println(a.toString());
		a.call();
		System.out.println(a.toString());
		a.jam();
		System.out.println(a.toString());
	}
}
