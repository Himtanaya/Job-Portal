package com.jobportal.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jobportal.bo.Employer;
import com.jobportal.bo.Job;
import com.jobportal.bo.Profile;
import com.jobportal.dao.EmployerDao;
import com.jobportal.dao.JobDao;
import com.jobportal.dao.JobSeekerDao;
import com.jobportal.dao.impl.EmployerDaoImpl;
import com.jobportal.dao.impl.JobDaoImpl;
import com.jobportal.dao.impl.JobSeekerDaoImpl;
import com.jobportal.services.EmployerService;
import com.jobportal.util.GenerateId;

public class EmployerServiceImpl implements EmployerService
{
	private static EmployerDao	employerDao		= new EmployerDaoImpl();
	private static JobDao		jobDao			= new JobDaoImpl();
	private static JobSeekerDao	jobSeekerDao	= new JobSeekerDaoImpl();

	@Override
	public boolean addEmployer(Employer employer)
	{
		employer.setId(GenerateId.getRandomNumber());
		return employerDao.addEmployer(employer);
	}

	@Override
	public boolean deleteEmployer(int userId)
	{
		return employerDao.deleteEmployer(userId);
	}

	@Override
	public boolean updateEmployer(Employer newEmployer)
	{
		Employer oldEmployer = employerDao.getEmployer(newEmployer.getId());

		Map<String, String> userUpdates = new HashMap<String, String>();
		Map<String, String> employerUpdates = new HashMap<String, String>();

		if (!oldEmployer.getName().equals(newEmployer.getName()))
		{
			userUpdates.put("NAME", newEmployer.getName());
		}

		if (!oldEmployer.getUserName().equals(newEmployer.getUserName()))
		{
			userUpdates.put("USERNAME", newEmployer.getUserName());
		}

		if (!oldEmployer.getPassWord().equals(newEmployer.getPassWord()))
		{
			userUpdates.put("PASSWORD", newEmployer.getPassWord());
		}

		if (!oldEmployer.getEmailAddress().equals(newEmployer.getEmailAddress()))
		{
			userUpdates.put("EMAIL", newEmployer.getEmailAddress());
		}

		if (!oldEmployer.getLocation().equals(newEmployer.getLocation()))
		{
			employerUpdates.put("LOCATION", newEmployer.getLocation());
		}

		if (!oldEmployer.getDescription().equals(newEmployer.getDescription()))
		{
			employerUpdates.put("COMPANYINFO", newEmployer.getDescription());
		}

		boolean userUpdate = employerDao.edit(userUpdates, "USER", newEmployer.getId());
		boolean employerUpdate = employerDao.edit(employerUpdates, "EMPLOYER", newEmployer.getId());

		if (userUpdate && employerUpdate)
		{
			return true;
		}

		return false;
	}

	@Override
	public Employer getEmployer(int userId)
	{
		return employerDao.getEmployer(userId);
	}

	@Override
	public List<Employer> getEmployers(List<Integer> userIds)
	{
		return employerDao.getEmployersById(userIds);
	}

	@Override
	public boolean postJob(Job job)
	{
		job.setId(GenerateId.getRandomNumber());
		return jobDao.postJob(job);
	}

	@Override
	public boolean editJob(Job newJob)
	{
		Job oldJob = jobDao.getJob(newJob.getId());

		Map<String, String> jobUpdates = new HashMap<String, String>();

		if (!oldJob.getJobPosition().equals(newJob.getJobPosition()))
		{
			jobUpdates.put("JOBPOSITION", newJob.getJobPosition());
		}

		if (!oldJob.getDescription().equals(newJob.getDescription()))
		{
			jobUpdates.put("DESCRIPTION", newJob.getDescription());
		}

		if (!oldJob.getTitle().equals(newJob.getTitle()))
		{
			jobUpdates.put("TITLE", newJob.getTitle());
		}

		boolean editSuccess = jobDao.editJob(jobUpdates, newJob.getId());
		boolean deleteLocation = true;
		boolean insertLocation = true;

		List<String> oldLocations = oldJob.getLocations();
		List<String> newLocations = newJob.getLocations();

		for (String location : oldLocations)
		{
			if (!newLocations.contains(location))
			{
				deleteLocation = jobDao.deleteJobLocation(newJob.getId(), location);

				if (!deleteLocation)
				{
					return false;
				}
			}
		}

		for (String location : newLocations)
		{
			if (!oldLocations.contains(location))
			{
				insertLocation = jobDao.addJobLocation(newJob.getId(), location);

				if (!insertLocation)
				{
					return false;
				}
			}
		}

		if (editSuccess && deleteLocation && insertLocation)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean removeJob(int jobId)
	{
		return jobDao.deleteJob(jobId);
	}

	@Override
	public boolean addJobLocation(int jobId, String location)
	{
		return jobDao.addJobLocation(jobId, location);
	}

	@Override
	public boolean deleteLocation(int jobId, String location)
	{
		return jobDao.deleteJobLocation(jobId, location);
	}

	@Override
	public List<Profile> getAppliedProfiles(int jobId)
	{
		return jobSeekerDao.getProfilesById(jobDao.searchCandidate(jobId));
	}

	@Override
	public List<Job> getJobsByCompany(int employerId)
	{
		return jobDao.getJobs(jobDao.getJobByCompany(employerId));
	}

	@Override
	public Job getJob(int jobId)
	{
		Job job = jobDao.getJob(jobId);

		Employer employer = employerDao.getEmployer(job.getEmployer().getId());

		job.setEmployer(employer);

		return job;
	}

}
