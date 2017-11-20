package com.jobportal.bo;

import java.io.Serializable;

/**
 * 
 * @author himtanaya.bhadada
 *
 */

public class JobSeeker extends User implements Serializable
{
	private static final long	serialVersionUID	= 1283556812741066433L;

	private String				location			= null;
	private String				currentPosition		= null;

	public JobSeeker(User user)
	{
		super.setId(user.getId());
		super.setName(user.getName());
		super.setUserName(user.getUserName());
		super.setPassWord(user.getPassWord());
		super.setEmailAddress(user.getEmailAddress());
	}

	public JobSeeker()
	{}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getCurrentPosition()
	{
		return currentPosition;
	}

	public void setCurrentPosition(String currentPosition)
	{
		this.currentPosition = currentPosition;
	}

}
