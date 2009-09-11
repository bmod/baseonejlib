package com.baseoneonline.java.test.romreader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class TestRomReader {

	private final String[] romFiles = { 
			"M:/games/Roms/Nintendo SNES/Addams Family Values.smc",
			"M:/games/Roms/Nintendo SNES/Adventures of Batman & Robin.smc", 
			"M:/games/Roms/Nintendo SNES/Seiken Densetsu 3.smc", 
			"M:/games/Roms/Nintendo SNES/Robotrek.smc", 
			};
	public static void main(String[] args) {
		new TestRomReader();
	}
	
	public TestRomReader() {
		readRoms();
	}
	
	private void readRoms() {
		for (String rf : romFiles) {
			try {
			//	log.info("Attempting to read: "+rf);
				readRom(new FileInputStream(rf));
			//	log.info("Done");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void readRom(InputStream in) {
		try {
			
			
			
			in.skip(0x81C0);
			read(in, 40);
			
			in.skip(0x8000-40);
			read(in, 40);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void read(InputStream in, int n) throws IOException {
		int c;
		StringBuffer buf = new StringBuffer(); 
		for (int i=0; i<n; i++) {
			c = in.read();
			buf.append((char)c);
		}
		System.out.println(buf);
	}
	

}
