package com.jobportal.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jobportal.bo.Employer;
import com.jobportal.bo.Job;
import com.jobportal.bo.JobSeeker;
import com.jobportal.bo.Profile;
import com.jobportal.bo.User;
import com.jobportal.dao.EmployerDao;
import com.jobportal.dao.JobDao;
import com.jobportal.dao.JobSeekerDao;
import com.jobportal.dao.impl.EmployerDaoImpl;
import com.jobportal.dao.impl.JobDaoImpl;
import com.jobportal.dao.impl.JobSeekerDaoImpl;
import com.jobportal.services.JobSeekerService;
import com.jobportal.util.ConversionUtil;
import com.jobportal.util.GenerateId;

public class JobSeekerServiceImpl implements JobSeekerService
{
	private static EmployerDao	employerDao		= new EmployerDaoImpl();
	private static JobDao		jobDao			= new JobDaoImpl();
	private static JobSeekerDao	jobSeekerDao	= new JobSeekerDaoImpl();

	@Override
	public boolean addUser(User user)
	{
		user.setId(GenerateId.getRandomNumber());
		return jobSeekerDao.addUser(user);
	}

	@Override
	public boolean addJobSeeker(JobSeeker jobSeeker)
	{
		return jobSeekerDao.addJobSeeker(jobSeeker);
	}

	@Override
	public boolean deleteUser(int userId)
	{
		return jobSeekerDao.deleteUser(userId);
	}

	@Override
	public User getUser(int userId)
	{
		return jobSeekerDao.getUser(userId);
	}

	@Override
	public User getUserByUserNamePassword(String userName, String password)
	{
		return jobSeekerDao.getUserByUserName(userName, password);
	}

	@Override
	public String determaineType(User user)
	{
		Employer employer = employerDao.getEmployer(user.getId());

		if (employer != null)
		{
			return "Employer";
		}
		else
		{
			return "JobSeeker";
		}
	}

	@Override
	public JobSeeker getJobSeeker(int jobSeekerId)
	{
		return jobSeekerDao.getJobSeeker(jobSeekerId);
	}

	@Override
	public boolean createProfile(Profile profile)
	{
		profile.setProfileId(GenerateId.getRandomNumber());
		return jobSeekerDao.createProfile(profile);
	}

	@Override
	public boolean deleteProfile(int profileId)
	{
		return jobSeekerDao.deleteProfile(profileId);
	}

	@Override
	public Profile getJobSeekerProfile(int userId)
	{
		return jobSeekerDao.getProfile(userId);
	}

	@Override
	public List<Job> getSuitableJobs(List<String> filters, int userId, String title)
	{
		Map<String, String> criterias = new HashMap<String, String>();

		Profile profile = jobSeekerDao.getProfile(userId);

		criterias.put("JOB.TITLE", title);

		if (profile != null && filters != null)
		{
			for (String filter : filters)
			{
				switch (filter)
				{
					case "LOCATION":
						criterias.put("JOBLOCATION.LOCATION",
								profile.getLocation() == null ? "" : profile.getLocation());
						break;
					case "POSITION":
						criterias.put("JOB.TITLE",
								profile.getCurrentPosition() == null ? "" : profile.getCurrentPosition());
						break;
					default:
						break;
				}
			}
		}

		List<Job> jobs = jobDao.getJobs(jobDao.searchJobs(criterias));
		List<Job> updatedJobs = new ArrayList<Job>();

		for (Job job : jobs)
		{
			Employer employer = employerDao.getEmployer(job.getEmployer().getId());
			job.setEmployer(employer);
			updatedJobs.add(job);
		}

		return updatedJobs;
	}

	@Override
	public List<Job> getSuitableJobsByPostion(String position)
	{
		Map<String, String> criterias = new HashMap<String, String>();

		criterias.put("JOB.JOBPOSITION", position);

		return jobDao.getJobs(jobDao.searchJobs(criterias));
	}

	@Override
	public List<Job> getJobsAppliedTo(int userId)
	{
		List<Job> jobs = jobDao.getJobs(jobDao.getJobByJobSeeker(userId));
		List<Job> updatedJobs = new ArrayList<Job>();

		for (Job job : jobs)
		{
			Employer employer = employerDao.getEmployer(job.getEmployer().getId());
			job.setEmployer(employer);
			updatedJobs.add(job);
		}

		return updatedJobs;
	}

	@Override
	public boolean applyForJob(int jobSeekerId, int jobId)
	{
		return jobDao.applyForJob(jobSeekerId, jobId);
	}

	@Override
	public boolean editUser(User newUser)
	{
		User oldUser = jobSeekerDao.getUser(newUser.getId());

		Map<String, String> userUpdates = new HashMap<String, String>();

		if (!oldUser.getName().equals(newUser.getName()))
		{
			userUpdates.put("NAME", newUser.getName());
		}

		if (!oldUser.getEmailAddress().equals(newUser.getEmailAddress()))
		{
			userUpdates.put("EMAIL", newUser.getEmailAddress());
		}

		if (userUpdates.size() > 0)
		{
			return jobSeekerDao.updateUser(userUpdates, newUser.getId());
		}

		return true;
	}

	@Override
	public boolean editJobSeeker(JobSeeker newJobSeeker)
	{
		JobSeeker oldJobSeeker = jobSeekerDao.getJobSeeker(newJobSeeker.getId());

		Map<String, String> jobSeekerUpdates = new HashMap<String, String>();

		if (oldJobSeeker.getLocation().equals(newJobSeeker.getLocation()))
		{
			jobSeekerUpdates.put("LOCATION", newJobSeeker.getLocation());
		}

		if (oldJobSeeker.getCurrentPosition().equals(newJobSeeker.getCurrentPosition()))
		{
			jobSeekerUpdates.put("CURRENTPOSITION", newJobSeeker.getCurrentPosition());
		}

		if (jobSeekerUpdates.size() > 0)
		{
			return jobSeekerDao.updateJobSeeker(jobSeekerUpdates, newJobSeeker.getId());
		}

		return true;
	}

	@Override
	public boolean editProfile(int userId, String education, String experience, String interests, String skills,
			String projects)
	{

		Profile profile = jobSeekerDao.getProfile(userId);

		if (profile.getProfileId() == -1)
		{
			profile = new Profile(jobSeekerDao.getJobSeeker(userId));

			if (education != null && !StringUtils.isBlank(education))
			{
				profile.setEducation(ConversionUtil.stringToList(education));
			}

			if (experience != null && !StringUtils.isBlank(experience))
			{
				profile.setExperience(ConversionUtil.stringToList(experience));
			}

			if (interests != null && !StringUtils.isBlank(interests))
			{
				profile.setInterests(ConversionUtil.stringToList(interests));
			}

			if (skills != null && !StringUtils.isBlank(skills))
			{
				profile.setSkills(ConversionUtil.stringToList(skills));
			}

			if (projects != null && !StringUtils.isBlank(projects))
			{
				profile.setProjects(ConversionUtil.stringToList(projects));
			}

			return createProfile(profile);
		}

		if (profile.getEducation() != null)
		{
			if (!education.equals(ConversionUtil.listToString(profile.getEducation())))
			{
				jobSeekerDao.updateProfile(profile.getProfileId(), ConversionUtil.stringToList(education),
						profile.getEducation(), "EDUCATION", "SEEKEREDUCATION");
			}
		}

		if (profile.getExperience() != null)
		{
			if (!experience.equals(ConversionUtil.listToString(profile.getExperience())))
			{
				jobSeekerDao.updateProfile(profile.getProfileId(), ConversionUtil.stringToList(experience),
						profile.getExperience(), "EXPERIENCE", "SEEKEREXPERIENCE");
			}
		}

		if (profile.getInterests() != null)
		{
			if (!interests.equals(ConversionUtil.listToString(profile.getInterests())))
			{
				jobSeekerDao.updateProfile(profile.getProfileId(), ConversionUtil.stringToList(interests),
						profile.getInterests(), "INTERESTS", "SEEKERINTERESTS");
			}
		}

		if (profile.getSkills() != null)
		{
			if (!skills.equals(ConversionUtil.listToString(profile.getSkills())))
			{
				jobSeekerDao.updateProfile(profile.getProfileId(), ConversionUtil.stringToList(skills),
						profile.getSkills(), "SKILLS", "SEEKERSKILLS");
			}
		}

		if (profile.getProjects() != null)
		{
			if (!projects.equals(ConversionUtil.listToString(profile.getProjects())))
			{
				jobSeekerDao.updateProfile(profile.getProfileId(), ConversionUtil.stringToList(projects),
						profile.getProjects(), "PROJECT", "SEEKERPROJECT");
			}
		}

		return true;
	}

	@Override
	public boolean revokeJob(int userId, int jobId)
	{
		return jobDao.revokeAppliedJob(userId, jobId);
	}

}
