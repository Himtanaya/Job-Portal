package com.jobportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jobportal.bo.Employer;
import com.jobportal.dao.EmployerDao;
import com.jobportal.util.JDBCConnectionUtil;

public class EmployerDaoImpl implements EmployerDao
{

	@Override
	public boolean addEmployer(Employer employer)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("INSERT INTO USER VALUES(?,?,?,?,?)");
			preparedStatement.setInt(1, employer.getId());
			preparedStatement.setString(2, employer.getName());
			preparedStatement.setString(3, employer.getUserName());
			preparedStatement.setString(4, employer.getPassWord());
			preparedStatement.setString(5, employer.getEmailAddress());

			int success = preparedStatement.executeUpdate();

			if (success > 0)
			{
				preparedStatement.close();

				preparedStatement = connection.prepareStatement("INSERT INTO EMPLOYER VALUES(?,?,?,?)");
				preparedStatement.setInt(1, employer.getId());
				preparedStatement.setString(2, employer.getLocation());
				preparedStatement.setString(3, employer.getDescription());
				preparedStatement.setNull(4, Types.BLOB);

				preparedStatement.executeUpdate();
			}
			else
			{
				return false;
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
	public boolean deleteEmployer(int userId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("DELETE FROM USER WHERE id=?");
			preparedStatement.setInt(1, userId);

			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean edit(Map<String, String> updatedValues, String table, int id)
	{
		Set<String> columns = updatedValues.keySet();

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			String prepareSql = "UPDATE " + table + " SET ";

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
			preparedStatement.setInt(1, id);

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
	public Employer getEmployer(int employerUserId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Employer employer = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT USER.*, EMPLOYER.location, EMPLOYER.companyInfo"
					+ " FROM USER INNER JOIN EMPLOYER ON USER.id=EMPLOYER.id WHERE EMPLOYER.id=?");

			preparedStatement.setInt(1, employerUserId);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				employer = new Employer();

				employer.setId(resultSet.getInt("id"));
				employer.setName(resultSet.getString("name"));
				employer.setUserName(resultSet.getString("username"));
				employer.setPassWord(resultSet.getString("password"));
				employer.setEmailAddress(resultSet.getString("email"));
				employer.setLocation(resultSet.getString("location"));
				employer.setDescription(resultSet.getString("companyInfo"));
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

		return employer;
	}

	@Override
	public List<Employer> getEmployersById(List<Integer> employerUserIds)
	{
		List<Employer> employers = new ArrayList<Employer>();

		for (int employerUserid : employerUserIds)
		{
			Employer employer = getEmployer(employerUserid);

			if (employer != null)
			{
				employers.add(employer);
			}
		}

		return employers;
	}
}
