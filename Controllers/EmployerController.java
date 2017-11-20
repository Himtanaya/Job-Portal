package com.jobportal.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jobportal.bo.Employer;
import com.jobportal.bo.Job;
import com.jobportal.bo.Profile;
import com.jobportal.services.EmployerService;
import com.jobportal.services.JobSeekerService;
import com.jobportal.services.impl.EmployerServiceImpl;
import com.jobportal.services.impl.JobSeekerServiceImpl;
import com.jobportal.util.ConversionUtil;

public class EmployerController extends MultiActionController
{
	private static JobSeekerService	jobSeekerService	= new JobSeekerServiceImpl();
	private static EmployerService	employerService		= new EmployerServiceImpl();

	public ModelAndView doGetJob(HttpServletRequest request, HttpServletResponse response)
	{
		int id = Integer.parseInt(request.getParameter("jobId"));

		ModelAndView modelAndView = new ModelAndView("employerjobsingle");

		Job job = employerService.getJob(id);

		modelAndView.addObject("job", job);

		return modelAndView;
	}

	public ModelAndView doGetCandidates(HttpServletRequest request, HttpServletResponse response)
	{
		int id = Integer.parseInt(request.getParameter("jobId"));

		ModelAndView modelAndView = new ModelAndView("employerjobcandidates");

		List<Profile> profiles = employerService.getAppliedProfiles(id);

		modelAndView.addObject("profiles", profiles);

		return modelAndView;
	}

	public ModelAndView doGetCandidateProfile(HttpServletRequest request, HttpServletResponse response)
	{
		int id = Integer.parseInt(request.getParameter("jobSeekerId"));

		ModelAndView modelAndView = new ModelAndView("employerjobcandidatesingle");

		Profile profile = jobSeekerService.getJobSeekerProfile(id);

		modelAndView.addObject("profile", profile);

		return modelAndView;
	}

	public ModelAndView doPostJob(HttpServletRequest request, HttpServletResponse response)
	{
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String position = request.getParameter("position");
		String locations = request.getParameter("locations");

		HttpSession session = request.getSession();

		Employer employer = (Employer) session.getAttribute("employer");

		Job job = new Job();

		job.setTitle(title);
		job.setDescription(description);
		job.setJobPosition(position);
		job.setEmployer(employer);

		job.setLocations(ConversionUtil.stringToList(locations));

		boolean success = employerService.postJob(job);

		ModelAndView modelAndView = new ModelAndView("employerpostjob");

		if (!success)
		{
			modelAndView.addObject("result", "Could Not Post Job");
		}

		return modelAndView;
	}

	public ModelAndView doGetPostJobView(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("employerpostjob");
		return modelAndView;
	}

	public ModelAndView doGetEmployerEditView(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("employeredit");

		HttpSession session = request.getSession();

		Employer employer = (Employer) session.getAttribute("employer");

		modelAndView.addObject("employer", employer);

		return modelAndView;
	}

	public ModelAndView doEditEmployer(HttpServletRequest request, HttpServletResponse response)
	{
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String location = request.getParameter("location");
		String description = request.getParameter("description");

		HttpSession session = request.getSession();

		Employer employer = (Employer) session.getAttribute("employer");

		employer.setName(name);
		employer.setEmailAddress(email);
		employer.setLocation(location);
		employer.setDescription(description);

		ModelAndView modelAndView = new ModelAndView("employeredit");

		boolean success = employerService.updateEmployer(employer);

		if (!success)
		{
			modelAndView.addObject("result", "Could Not Edit Employer");
		}

		modelAndView.addObject("employer", employerService.getEmployer(employer.getId()));

		return modelAndView;
	}

	public ModelAndView doGetEmployerJobEditView(HttpServletRequest request, HttpServletResponse response)
	{
		int jobId = Integer.parseInt(request.getParameter("jobId"));

		ModelAndView modelAndView = new ModelAndView("employerjobedit");

		Job job = employerService.getJob(jobId);

		modelAndView.addObject("job", job);
		modelAndView.addObject("locations", ConversionUtil.listToString(job.getLocations()));

		return modelAndView;
	}

	public ModelAndView doJobEdit(HttpServletRequest request, HttpServletResponse response)
	{
		int jobId = Integer.parseInt(request.getParameter("jobId"));
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String position = request.getParameter("position");
		String locations = request.getParameter("locations");

		HttpSession session = request.getSession();

		Employer employer = (Employer) session.getAttribute("employer");

		Job newJob = new Job();
		newJob.setId(jobId);
		newJob.setTitle(title);
		newJob.setDescription(description);
		newJob.setJobPosition(position);
		newJob.setLocations(ConversionUtil.stringToList(locations));
		newJob.setEmployer(employer);

		boolean success = employerService.editJob(newJob);

		ModelAndView modelAndView = new ModelAndView("employerjobedit");

		if (!success)
		{
			modelAndView.addObject("result", "Could Not Edit Job");
		}

		Job job = employerService.getJob(jobId);

		modelAndView.addObject("job", job);
		modelAndView.addObject("locations", ConversionUtil.listToString(job.getLocations()));

		return modelAndView;
	}
}
