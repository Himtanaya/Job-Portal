package com.jobportal.bo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author himtanaya.bhadada
 *
 */

public class Job implements Serializable
{
	private static final long	serialVersionUID	= -6948210036693425174L;

	private int					id					= -1;
	private String				title				= null;
	private String				description			= null;
	private String				jobPosition			= null;
	private List<String>		locations			= null;
	private Employer			employer			= null;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getJobPosition()
	{
		return jobPosition;
	}

	public void setJobPosition(String jobPosition)
	{
		this.jobPosition = jobPosition;
	}

	public List<String> getLocations()
	{
		return locations;
	}

	public void setLocations(List<String> locations)
	{
		this.locations = locations;
	}

	public Employer getEmployer()
	{
		return employer;
	}

	public void setEmployer(Employer employer)
	{
		this.employer = employer;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
