package com.jobportal.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jobportal.bo.Employer;
import com.jobportal.util.EmailUtility;

public class EmailController extends MultiActionController
{

	private static String	host	= "smtp.gmail.com";
	private static String	port	= "587";
	private static String	user	= "hemshah.nmims@gmail.com";
	private static String	pass	= "hem271094";

	public ModelAndView doGetEmployerEmailView(HttpServletRequest request, HttpServletResponse response)
	{
		String candidateEmail = request.getParameter("email");

		ModelAndView modelAndView = new ModelAndView("employeremailcandidate");
		modelAndView.addObject("candidateEmail", candidateEmail);
		return modelAndView;
	}

	public ModelAndView sendEmail(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		Employer employer = (Employer) session.getAttribute("employer");

		String recipient = request.getParameter("email");

		String subject = request.getParameter("subject");

		String content = request.getParameter("content");

		content = content + "\nAutomatically added: Sent by: " + employer.getEmailAddress();
		String resultMessage = "";

		try
		{
			EmailUtility.sendEmail(host, port, user, pass, recipient, subject, content);
			resultMessage = "The e-mail was sent successfully";
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			resultMessage = "There were an error: " + ex.getMessage();
		}
		finally
		{
			request.setAttribute("Message", resultMessage);
		}

		ModelAndView modelAndView = new ModelAndView("employeremailcandidate");
		modelAndView.addObject("candidateEmail", recipient);

		return modelAndView;

	}
}
