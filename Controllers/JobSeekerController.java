package com.jobportal.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jobportal.bo.Employer;
import com.jobportal.bo.Job;
import com.jobportal.bo.JobSeeker;
import com.jobportal.bo.Profile;
import com.jobportal.bo.User;
import com.jobportal.services.EmployerService;
import com.jobportal.services.JobSeekerService;
import com.jobportal.services.impl.EmployerServiceImpl;
import com.jobportal.services.impl.JobSeekerServiceImpl;
import com.jobportal.util.ConversionUtil;

public class JobSeekerController extends MultiActionController
{
	private static JobSeekerService	jobSeekerService	= new JobSeekerServiceImpl();
	private static EmployerService	employerService		= new EmployerServiceImpl();

	public ModelAndView doGetLogin(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("Login");
		return modelAndView;
	}

	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response)
	{
		String userName = request.getParameter("username");
		String password = request.getParameter("password");

		HttpSession session = request.getSession();

		User user = jobSeekerService.getUserByUserNamePassword(userName, password);

		ModelAndView modelAndView = null;

		if (user != null)
		{
			String type = jobSeekerService.determaineType(user);

			if (type.equals("Employer"))
			{
				modelAndView = new ModelAndView("employerjoblist");
				session.setAttribute("employer", employerService.getEmployer(user.getId()));
				modelAndView.addObject("jobs", employerService.getJobsByCompany(user.getId()));
				session.setAttribute("type", "Employer");
			}
			else
			{
				modelAndView = new ModelAndView("jobseekerhome");
				session.setAttribute("type", "Jobseeker");
			}

			session.setAttribute("user", user);
		}
		else
		{
			modelAndView = new ModelAndView("Login");
			modelAndView.addObject("Result", "Login Failed!");
		}

		return modelAndView;
	}

	public ModelAndView doGetEmployerHome(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		ModelAndView modelAndView = new ModelAndView("employerjoblist");

		modelAndView.addObject("jobs", employerService.getJobsByCompany(user.getId()));

		return modelAndView;
	}

	public ModelAndView doGetSearchPage(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("jobseekerhome");
	}

	public ModelAndView doSearchJob(HttpServletRequest request, HttpServletResponse response)
	{
		String jobTitle = request.getParameter("title");

		String[] filters = request.getParameterValues("filter");
		List<String> filtersList = null;

		if (filters != null && filters.length > 0)
		{
			filtersList = new ArrayList<String>(Arrays.asList(filters));
		}

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		List<Job> jobs = jobSeekerService.getSuitableJobs(filtersList, user.getId(), jobTitle);

		ModelAndView modelAndView = new ModelAndView("jobseekerresult");

		modelAndView.addObject("jobs", jobs);
		modelAndView.addObject("title", jobTitle);

		return modelAndView;
	}

	public ModelAndView doGetJob(HttpServletRequest request, HttpServletResponse response)
	{
		int jobId = Integer.parseInt(request.getParameter("jobid"));

		Job job = employerService.getJob(jobId);

		ModelAndView modelAndView = new ModelAndView("jobseekerresultsingle");

		modelAndView.addObject("job", job);

		return modelAndView;
	}

	public ModelAndView doGetAppliedJob(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		ModelAndView modelAndView = new ModelAndView("jobseekerapplied");

		List<Job> appliedjobs = jobSeekerService.getJobsAppliedTo(user.getId());
		modelAndView.addObject("jobs", appliedjobs);

		return modelAndView;
	}

	public ModelAndView doGetJobSeeker(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");
		JobSeeker jobSeeker = jobSeekerService.getJobSeeker(user.getId());

		ModelAndView modelAndView = new ModelAndView("jobseekeredit");

		modelAndView.addObject("user", user);
		modelAndView.addObject("jobseeker", jobSeeker);

		return modelAndView;
	}

	public ModelAndView doEditJobSeeker(HttpServletRequest request, HttpServletResponse response)
	{
		String name = request.getParameter("name");
		String emailAddress = request.getParameter("email");
		String location = request.getParameter("location");
		String position = request.getParameter("position");

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");
		JobSeeker jobSeeker = null;

		user.setName(name);
		user.setEmailAddress(emailAddress);

		boolean userSuccess = jobSeekerService.editUser(user);
		boolean jobSeekerSuccess = false;

		if (userSuccess)
		{
			jobSeeker = jobSeekerService.getJobSeeker(user.getId());

			if (jobSeeker == null)
			{
				jobSeeker = new JobSeeker(user);
				jobSeeker.setLocation(location);
				jobSeeker.setCurrentPosition(position);

			}
			else
			{
				jobSeeker.setLocation(location);
				jobSeeker.setCurrentPosition(position);
				jobSeekerSuccess = jobSeekerService.editJobSeeker(jobSeeker);
			}
		}

		ModelAndView modelAndView = new ModelAndView("jobseekeredit");
		modelAndView.addObject("jobseeker", jobSeeker);

		if (!userSuccess || !jobSeekerSuccess)
		{
			modelAndView.addObject("result", "Could not update one or more value! Sorry!");
		}

		return modelAndView;
	}

	public ModelAndView doGetJobSeekerProfile(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");
		Profile profile = jobSeekerService.getJobSeekerProfile(user.getId());

		ModelAndView modelAndView = new ModelAndView("jobseekerprofile");

		modelAndView.addObject("user", user);
		modelAndView.addObject("education", ConversionUtil.listToString(profile.getEducation()));
		modelAndView.addObject("experience", ConversionUtil.listToString(profile.getExperience()));
		modelAndView.addObject("interests", ConversionUtil.listToString(profile.getInterests()));
		modelAndView.addObject("skills", ConversionUtil.listToString(profile.getSkills()));
		modelAndView.addObject("projects", ConversionUtil.listToString(profile.getProjects()));

		return modelAndView;
	}

	public ModelAndView doEditProfile(HttpServletRequest request, HttpServletResponse response)
	{
		String education = request.getParameter("education");
		String experience = request.getParameter("experience");
		String interests = request.getParameter("interests");
		String skills = request.getParameter("skills");
		String projects = request.getParameter("projects");

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");
		JobSeeker jobSeeker = jobSeekerService.getJobSeeker(user.getId());

		ModelAndView modelAndView = new ModelAndView("jobseekerprofile");
		modelAndView.addObject("user", user);

		if (jobSeeker == null)
		{
			modelAndView.addObject("result", "Create Job Seeker First");
			return modelAndView;
		}

		boolean success = jobSeekerService
				.editProfile(user.getId(), education, experience, interests, skills, projects);

		Profile profile = jobSeekerService.getJobSeekerProfile(user.getId());

		modelAndView.addObject("education", ConversionUtil.listToString(profile.getEducation()));
		modelAndView.addObject("experience", ConversionUtil.listToString(profile.getExperience()));
		modelAndView.addObject("interests", ConversionUtil.listToString(profile.getInterests()));
		modelAndView.addObject("skills", ConversionUtil.listToString(profile.getSkills()));
		modelAndView.addObject("projects", ConversionUtil.listToString(profile.getProjects()));

		if (!success)
		{
			modelAndView.addObject("result", "Could not update one or more value! Sorry!");
		}

		return modelAndView;
	}

	public ModelAndView doApplyForJob(HttpServletRequest request, HttpServletResponse response)
	{
		int jobId = Integer.parseInt(request.getParameter("jobId"));

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		boolean success = jobSeekerService.applyForJob(user.getId(), jobId);

		ModelAndView modelAndView = null;

		if (!success)
		{
			modelAndView = new ModelAndView("jobseekerresultsingle");

			Job job = employerService.getJob(jobId);
			modelAndView.addObject("job", job);
			modelAndView.addObject("result", "Could not apply for job or already applied");
			return modelAndView;
		}
		else
		{
			modelAndView = new ModelAndView("jobseekerapplied");

			List<Job> appliedjobs = jobSeekerService.getJobsAppliedTo(user.getId());
			modelAndView.addObject("jobs", appliedjobs);

			return modelAndView;
		}
	}

	public ModelAndView doRevokeForJob(HttpServletRequest request, HttpServletResponse response)
	{
		int jobId = Integer.parseInt(request.getParameter("jobid"));

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		boolean success = jobSeekerService.revokeJob(user.getId(), jobId);

		ModelAndView modelAndView = null;

		modelAndView = new ModelAndView("jobseekerapplied");

		List<Job> appliedjobs = jobSeekerService.getJobsAppliedTo(user.getId());
		modelAndView.addObject("jobs", appliedjobs);

		if (!success)
		{
			modelAndView.addObject("result", "Could not revoke job application! Sorry!");
		}

		return modelAndView;
	}

	public ModelAndView doLogout(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		session.invalidate();

		ModelAndView modelAndView = new ModelAndView("Login");
		return modelAndView;
	}

	public ModelAndView doGetSignUpUser(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("createnewuser");
		return modelAndView;
	}

	public ModelAndView doGetSignUpEmployer(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("createnewemployer");
		return modelAndView;
	}

	public ModelAndView doGetSignUpJobSeeker(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("createjobseeker");
		return modelAndView;
	}

	public ModelAndView doCreateUser(HttpServletRequest request, HttpServletResponse response)
	{
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String emailAddress = request.getParameter("email");

		User user = new User();
		user.setName(name);
		user.setUserName(username);
		user.setPassWord(password);
		user.setEmailAddress(emailAddress);

		boolean success = jobSeekerService.addUser(user);

		ModelAndView modelAndView = null;

		if (!success)
		{
			modelAndView = new ModelAndView("createnewuser");
			modelAndView.addObject("result", "Could not create user!");
			modelAndView.addObject("user", user);
		}
		else
		{
			modelAndView = new ModelAndView("Login");
		}

		return modelAndView;
	}

	public ModelAndView doCreateEmployer(HttpServletRequest request, HttpServletResponse response)
	{
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String location = request.getParameter("location");
		String description = request.getParameter("description");

		Employer employer = new Employer();
		employer.setName(name);
		employer.setUserName(username);
		employer.setPassWord(password);
		employer.setEmailAddress(email);
		employer.setLocation(location);
		employer.setDescription(description);

		boolean success = employerService.addEmployer(employer);

		ModelAndView modelAndView = null;

		if (!success)
		{
			modelAndView = new ModelAndView("createnewuser");
			modelAndView.addObject("result", "Could not create user!");
			modelAndView.addObject("employer", employer);
		}
		else
		{
			modelAndView = new ModelAndView("Login");
		}

		return modelAndView;
	}

	public ModelAndView doCreateJobSeeker(HttpServletRequest request, HttpServletResponse response)
	{
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String location = request.getParameter("location");
		String position = request.getParameter("position");

		User user = new User();
		user.setName(name);
		user.setUserName(username);
		user.setPassWord(password);
		user.setEmailAddress(email);

		boolean userSuccess = jobSeekerService.addUser(user);
		boolean successJobSeeker = false;

		JobSeeker jobSeeker = new JobSeeker(user);
		jobSeeker.setLocation(location);
		jobSeeker.setCurrentPosition(position);

		ModelAndView modelAndView = null;

		if (userSuccess)
		{
			successJobSeeker = jobSeekerService.addJobSeeker(jobSeeker);
		}

		if (!userSuccess || !successJobSeeker)
		{
			modelAndView = new ModelAndView("createjobseeker");
			modelAndView.addObject("result", "Could not create user!");
			modelAndView.addObject("jobseeker", jobSeeker);
		}
		else
		{
			modelAndView = new ModelAndView("Login");
		}

		return modelAndView;
	}

}
