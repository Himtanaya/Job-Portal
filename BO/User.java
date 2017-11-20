package com.jobportal.bo;

import java.io.Serializable;

/**
 * 
 * @author himtanaya.bhadada
 *
 */

public class User implements Serializable
{
	private static final long	serialVersionUID	= -7413162228250750925L;

	private int					id					= -1;
	private String				name				= null;
	private String				userName			= null;
	private String				passWord			= null;
	private String				emailAddress		= null;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassWord()
	{
		return passWord;
	}

	public void setPassWord(String passWord)
	{
		this.passWord = passWord;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

}
