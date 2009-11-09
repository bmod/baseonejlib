package com.baseoneonline.java.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.baseoneonline.java.tools.FileUtils;

public class SplitDictionary {
	public static void main(String[] args) {
		new SplitDictionary();
	}

	public SplitDictionary() {
		String file = FileUtils.readFile("dictionary");
		String[] lines = file.split("\n");
		char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		try {
			for (char letter : letters) {
				File f = new File(letter + ".txt");
				System.out.println("Writing "+f);
				FileWriter writer = new FileWriter(f);
				for (String word : lines) {
					if (word.charAt(0) == letter && word.length() > 2) {
						writer.append(word + "\n");
					}
					
				}
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
