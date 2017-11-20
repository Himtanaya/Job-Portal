package com.jobportal.bo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author himtanaya.bhadada
 *
 */

public class Profile extends JobSeeker implements Serializable
{
	private static final long	serialVersionUID	= -2985622999438666030L;

	private int					profileId			= 0;
	private List<String>		education			= null;
	private List<String>		experience			= null;
	private List<String>		projects			= null;
	private List<String>		skills				= null;
	private List<String>		interests			= null;

	public Profile(JobSeeker jobSeeker)
	{
		super.setId(jobSeeker.getId());
		super.setName(jobSeeker.getName());
		super.setUserName(jobSeeker.getUserName());
		super.setPassWord(jobSeeker.getPassWord());
		super.setEmailAddress(jobSeeker.getEmailAddress());
		super.setLocation(jobSeeker.getLocation());
		super.setCurrentPosition(jobSeeker.getCurrentPosition());
	}

	public Profile()
	{}

	public int getProfileId()
	{
		return profileId;
	}

	public void setProfileId(int profileId)
	{
		this.profileId = profileId;
	}

	public List<String> getEducation()
	{
		return education;
	}

	public void setEducation(List<String> education)
	{
		this.education = education;
	}

	public List<String> getExperience()
	{
		return experience;
	}

	public void setExperience(List<String> experience)
	{
		this.experience = experience;
	}

	public List<String> getProjects()
	{
		return projects;
	}

	public void setProjects(List<String> projects)
	{
		this.projects = projects;
	}

	public List<String> getSkills()
	{
		return skills;
	}

	public void setSkills(List<String> skills)
	{
		this.skills = skills;
	}

	public List<String> getInterests()
	{
		return interests;
	}

	public void setInterests(List<String> interests)
	{
		this.interests = interests;
	}

}
