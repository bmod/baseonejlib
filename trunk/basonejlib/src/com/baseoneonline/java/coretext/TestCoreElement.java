package com.baseoneonline.java.coretext;

import java.net.URISyntaxException;

public class TestCoreElement {
	public static void main(String[] args) throws URISyntaxException {

		String fname = "AimHelpers.CoreText";
		
		CoreElement elm = new CoreElement();
		elm
				.readFromFile("src/com/baseoneonline/java/assets/"+fname);
		
		
		
	}
}
