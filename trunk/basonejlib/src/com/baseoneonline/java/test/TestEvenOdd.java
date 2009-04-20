package com.baseoneonline.java.test;

public class TestEvenOdd {
	public static void main(String[] args) {
		for (int i = 0; i < 30; i++) {
			boolean isEven = (i % 2 == 0);
			System.out.println(i + " is " + (isEven ? "even" : "odd"));
		}
	}
}
