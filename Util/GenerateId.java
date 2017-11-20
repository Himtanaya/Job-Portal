package com.jobportal.util;

import java.util.Random;

public class GenerateId
{
	public static int getRandomNumber()
	{
		Random random = new Random();
		int randomNumber = random.nextInt(Integer.MAX_VALUE);
		return randomNumber;
	}
}
