package test;

import com.baseoneonline.java.tools.StringUtils;

public class TestStringUtils {
	public static void main(String[] args) {
		String s1 = "myPerfectlyCamelStyledString";
		System.out.println(StringUtils.deCamelize(s1));
	}
}
