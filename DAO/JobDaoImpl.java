package com.jobportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jobportal.bo.Employer;
import com.jobportal.bo.Job;
import com.jobportal.dao.JobDao;
import com.jobportal.util.JDBCConnectionUtil;

public class JobDaoImpl implements JobDao
{

	@Override
	public boolean postJob(Job job)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("INSERT INTO JOB VALUES(?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, job.getId());
			preparedStatement.setString(2, job.getTitle());
			preparedStatement.setString(3, job.getDescription());
			preparedStatement.setString(4, job.getJobPosition());
			preparedStatement.setInt(5, job.getEmployer().getId());

			int success = preparedStatement.executeUpdate();

			if (success > 0)
			{
				List<String> jobLocations = job.getLocations();

				for (String jobLocation : jobLocations)
				{
					addJobLocation(job.getId(), jobLocation);
				}
			}

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}
	}

	@Override
	public boolean editJob(Map<String, String> updatedValues, int jobId)
	{
		Set<String> columns = updatedValues.keySet();

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			String prepareSql = "UPDATE JOB SET ";

			int maxUpdatedColumns = columns.size();
			int updatedColumns = 1;

			for (String column : columns)
			{
				if (updatedColumns == maxUpdatedColumns)
				{
					prepareSql = prepareSql + " " + column + "='" + updatedValues.get(column) + "'";
				}
				else
				{
					prepareSql = prepareSql + " " + column + "='" + updatedValues.get(column) + "',";
					updatedColumns++;
				}
			}

			prepareSql = prepareSql + " WHERE id=?";

			preparedStatement = connection.prepareStatement(prepareSql);
			preparedStatement.setInt(1, jobId);

			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);
			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

	}

	@Override
	public boolean deleteJob(int jobId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("DELETE FROM JOB WHERE ID=?");
			preparedStatement.setInt(1, jobId);

			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);
			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}
	}

	@Override
	public boolean addJobLocation(int jobId, String location)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();
			preparedStatement = connection.prepareStatement("INSERT INTO JOBLOCATION VALUES(?,?)");
			preparedStatement.setInt(1, jobId);
			preparedStatement.setString(2, location);

			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}
	}

	@Override
	public boolean deleteJobLocation(int jobId, String location)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM JOBLOCATION WHERE JOB=? AND LOCATION=?");
			preparedStatement.setInt(1, jobId);
			preparedStatement.setString(2, location);

			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}
	}

	@Override
	public boolean applyForJob(int jobSeekerId, int jobId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();
			preparedStatement = connection.prepareStatement("INSERT INTO APPLYTOJOB VALUES(?,?)");
			preparedStatement.setInt(1, jobSeekerId);
			preparedStatement.setInt(2, jobId);

			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}
	}

	@Override
	public boolean revokeAppliedJob(int appliedBy, int appliedTo)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("DELETE FROM APPLYTOJOB WHERE APPLIEDBY=? AND APPLIESTO=?");
			preparedStatement.setInt(1, appliedBy);
			preparedStatement.setInt(2, appliedTo);

			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

	}

	@Override
	public List<Integer> searchCandidate(int jobId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		List<Integer> candidates = new ArrayList<Integer>();

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT APPLIEDBY FROM APPLYTOJOB WHERE APPLIESTO=?");
			preparedStatement.setInt(1, jobId);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{
				candidates.add(resultSet.getInt(1));
			}
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

		return candidates;
	}

	@Override
	public List<Integer> searchJobs(Map<String, String> criterias)
	{
		Set<String> columns = criterias.keySet();

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		List<Integer> jobs = new ArrayList<Integer>();

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			String prepareSql = "SELECT DISTINCT(JOB.id) FROM JOB INNER JOIN JOBLOCATION ON JOB.ID=JOBLOCATION.JOB WHERE ";

			int maxColumns = columns.size();
			int currentColumns = 1;

			for (String column : columns)
			{
				if (currentColumns == maxColumns)
				{
					prepareSql = prepareSql + " " + column + " LIKE '%" + criterias.get(column) + "%' ";
				}
				else
				{
					prepareSql = prepareSql + " " + column + " LIKE '%" + criterias.get(column) + "%' AND ";
					currentColumns++;
				}
			}

			preparedStatement = connection.prepareStatement(prepareSql);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				jobs.add(resultSet.getInt("id"));
			}
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);
			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

		return jobs;
	}

	@Override
	public Job getJob(int jobId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Job job = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT ID, TITLE, DESCRIPTION, JOBPOSITION, POSTEDBY"
					+ " FROM JOB WHERE ID=?");
			preparedStatement.setInt(1, jobId);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				job = new Job();
				job.setId(resultSet.getInt("ID"));
				job.setTitle(resultSet.getString("TITLE"));
				job.setDescription(resultSet.getString("DESCRIPTION"));
				job.setJobPosition(resultSet.getString("JOBPOSITION"));

				Employer employer = new Employer();
				employer.setId(resultSet.getInt("POSTEDBY"));

				job.setEmployer(employer);
				job.setLocations(getJobLocations(job.getId()));
			}
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

		return job;
	}

	@Override
	public List<String> getJobLocations(int jobId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		List<String> jobLocation = new ArrayList<String>();

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT LOCATION FROM JOBLOCATION WHERE JOB=?");
			preparedStatement.setInt(1, jobId);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				jobLocation.add(resultSet.getString("LOCATION"));
			}
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);

			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

		return jobLocation;
	}

	@Override
	public List<Job> getJobs(List<Integer> jobIds)
	{
		List<Job> jobs = new ArrayList<Job>();

		if (jobIds != null)
		{
			for (int jobId : jobIds)
			{
				jobs.add(getJob(jobId));
			}

		}

		return jobs;
	}

	@Override
	public List<Integer> getJobByCompany(int employerId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		List<Integer> jobsPostedByCompany = new ArrayList<Integer>();

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT ID FROM JOB WHERE POSTEDBY=?");
			preparedStatement.setInt(1, employerId);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				jobsPostedByCompany.add(resultSet.getInt(1));
			}
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);
			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

		return jobsPostedByCompany;
	}

	@Override
	public List<Integer> getJobByJobSeeker(int jobSeekerId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		List<Integer> jobsAppliedByUser = new ArrayList<Integer>();

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT APPLIESTO FROM APPLYTOJOB WHERE APPLIEDBY=?");
			preparedStatement.setInt(1, jobSeekerId);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				jobsAppliedByUser.add(resultSet.getInt(1));
			}
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			JDBCConnectionUtil.closeConnection(connection);
			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{}
		}

		return jobsAppliedByUser;
	}

}
