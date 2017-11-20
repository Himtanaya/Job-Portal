package com.jobportal.bo;

import java.io.Serializable;

/**
 * 
 * @author himtanaya.bhadada
 *
 */

public class Employer extends User implements Serializable
{
	private static final long	serialVersionUID	= -5680835640734747142L;

	private String				location			= null;
	private String				description			= null;

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
