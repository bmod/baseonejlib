package com.baseoneonline.java.speedtest;

public class SpeedTest
{

	public static void runTests(ITest[] tests, int iterations)
	{
		for (ITest t : tests)
			runTest(t, iterations);
	}

	public static void runTest(ITest test, int iterations)
	{
		long oldTime = System.nanoTime();
		System.out.println(String.format("Running test: %s",
				test.getDescription()));

		for (int i = 0; i < iterations; i++)
		{
			test.run();
		}

		long difTime = System.nanoTime() - oldTime;
		System.out.println(String.format("Time taken: %s", difTime));
	}
}
