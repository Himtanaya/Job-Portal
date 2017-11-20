package com.jobportal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConversionUtil
{
	public static String listToString(List<String> profiles)
	{
		String profilesString = "";

		int current = 1;

		for (String profile : profiles)
		{
			if (current == 1)
			{
				profilesString = profile;
				current++;
			}
			else
			{
				profilesString = profilesString + "," + profile;
			}
		}

		return profilesString;
	}

	public static List<String> stringToList(String profiles)
	{
		String[] profilesArray = profiles.split(",");

		List<String> profilesList = null;

		if (profilesArray != null && profilesArray.length > 0)
		{
			profilesList = new ArrayList<String>(Arrays.asList(profilesArray));
		}

		return profilesList;
	}
}
