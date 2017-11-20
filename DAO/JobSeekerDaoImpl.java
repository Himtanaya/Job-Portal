package com.jobportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jobportal.bo.JobSeeker;
import com.jobportal.bo.Profile;
import com.jobportal.bo.User;
import com.jobportal.dao.JobSeekerDao;
import com.jobportal.util.JDBCConnectionUtil;

public class JobSeekerDaoImpl implements JobSeekerDao
{

	@Override
	public boolean addUser(User user)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("INSERT INTO USER VALUES(?,?,?,?,?)");
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getUserName());
			preparedStatement.setString(4, user.getPassWord());
			preparedStatement.setString(5, user.getEmailAddress());

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
	public User getUser(int userId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		User user = null;
		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection
					.prepareStatement("SELECT ID, NAME, USERNAME, PASSWORD, EMAIL FROM USER WHERE ID=?");
			preparedStatement.setInt(1, userId);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				user = new User();
				user.setId(resultSet.getInt("ID"));
				user.setName(resultSet.getString("NAME"));
				user.setUserName(resultSet.getString("USERNAME"));
				user.setPassWord(resultSet.getString("PASSWORD"));
				user.setEmailAddress(resultSet.getString("EMAIL"));
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

		return user;
	}

	@Override
	public User getUserByUserName(String userName, String passWord)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		User user = null;
		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection
					.prepareStatement("SELECT ID, NAME, USERNAME, PASSWORD, EMAIL FROM USER WHERE USERNAME=? AND PASSWORD=?");
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, passWord);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				user = new User();
				user.setId(resultSet.getInt("ID"));
				user.setName(resultSet.getString("NAME"));
				user.setUserName(resultSet.getString("USERNAME"));
				user.setPassWord(resultSet.getString("PASSWORD"));
				user.setEmailAddress(resultSet.getString("EMAIL"));
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

		return user;
	}

	@Override
	public boolean deleteUser(int userId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("DELETE FROM USER WHERE ID=?");
			preparedStatement.setInt(1, userId);

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
	public boolean addJobSeeker(JobSeeker jobSeeker)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("INSERT INTO JOBSEEKER VALUES(?,?,?,?,?)");

			preparedStatement.setInt(1, jobSeeker.getId());
			preparedStatement.setString(2, jobSeeker.getLocation());
			preparedStatement.setString(3, jobSeeker.getCurrentPosition());
			preparedStatement.setNull(4, Types.BLOB);
			preparedStatement.setNull(5, Types.INTEGER);

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
	public JobSeeker getJobSeeker(int id)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		JobSeeker jobSeeker = null;
		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection
					.prepareStatement("SELECT ID, LOCATION, CURRENTPOSITION, CREATES FROM JOBSEEKER WHERE ID=?");
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				int userId = resultSet.getInt("ID");

				jobSeeker = new JobSeeker(getUser(userId));

				jobSeeker.setLocation(resultSet.getString("LOCATION"));
				jobSeeker.setCurrentPosition(resultSet.getString("CURRENTPOSITION"));
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

		return jobSeeker;
	}

	@Override
	public boolean createProfile(Profile profile)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("INSERT INTO SEEKERPROFILE VALUES(?,?)");
			preparedStatement.setInt(1, profile.getProfileId());
			preparedStatement.setInt(2, profile.getId());

			int success = preparedStatement.executeUpdate();

			if (success > 0)
			{
				preparedStatement.close();

				preparedStatement = connection.prepareStatement("UPDATE JOBSEEKER SET CREATES=? WHERE ID=?");
				preparedStatement.setInt(1, profile.getProfileId());
				preparedStatement.setInt(2, profile.getId());

				preparedStatement.executeUpdate();

				addProfileInternal(profile.getEducation(), profile.getProfileId(), "SEEKEREDUCATION");
				addProfileInternal(profile.getExperience(), profile.getProfileId(), "SEEKEREXPERIENCE");
				addProfileInternal(profile.getProjects(), profile.getProfileId(), "SEEKERPROJECT");
				addProfileInternal(profile.getInterests(), profile.getProfileId(), "SEEKERINTERESTS");
				addProfileInternal(profile.getSkills(), profile.getProfileId(), "SEEKERSKILLS");
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

	private void addProfileInternal(List<String> profiles, int profileId, String table) throws Exception
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			if (profiles != null)
			{
				for (String seekerProfile : profiles)
				{
					preparedStatement = connection.prepareStatement("INSERT INTO " + table + " VALUES(?,?)");
					preparedStatement.setInt(1, profileId);
					preparedStatement.setString(2, seekerProfile);

					preparedStatement.executeUpdate();
				}
			}
		}
		catch (Exception e)
		{
			throw new Exception(e);
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
	public boolean updateUser(Map<String, String> updatedValues, int userId)
	{
		Set<String> columns = updatedValues.keySet();

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			String prepareSql = "UPDATE USER SET ";

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
			preparedStatement.setInt(1, userId);

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
	public boolean updateJobSeeker(Map<String, String> updatedValues, int userId)
	{
		Set<String> columns = updatedValues.keySet();

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			String prepareSql = "UPDATE JOBSEEKER SET ";

			int maxUpdatedColumns = columns.size();
			int updatedColumns = 1;

			for (String column : columns)
			{
				if (updatedColumns == maxUpdatedColumns)
				{
					prepareSql = prepareSql + " " + column + "=" + updatedValues.get(column);
				}
				else
				{
					prepareSql = prepareSql + " " + column + "=" + updatedValues.get(column) + ",";
					updatedColumns++;
				}
			}

			prepareSql = prepareSql + " WHERE id=?";

			preparedStatement = connection.prepareStatement(prepareSql);
			preparedStatement.setInt(1, userId);

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
	public boolean updateProfile(int profileId, List<String> values, List<String> orignalValues, String column,
			String table)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			String prepareSql = "UPDATE " + table + " SET " + column + "=? WHERE SEEKERPROFILE=? AND " + column + "=?";

			for (String value : values)
			{
				preparedStatement = connection.prepareStatement(prepareSql);
				preparedStatement.setString(1, value);
				preparedStatement.setInt(2, profileId);
				preparedStatement.setString(3, value);

				int result = preparedStatement.executeUpdate();

				if (result == 0)
				{
					switch (table)
					{
						case "SEEKEREDUCATION":
							addProfileInternal(new ArrayList<String>(Arrays.asList(value)), profileId,
									"SEEKEREDUCATION");
							break;
						case "SEEKEREXPERIENCE":
							addProfileInternal(new ArrayList<String>(Arrays.asList(value)), profileId,
									"SEEKEREXPERIENCE");
							break;
						case "SEEKERINTERESTS":
							addProfileInternal(new ArrayList<String>(Arrays.asList(value)), profileId,
									"SEEKERINTERESTS");
							break;
						case "SEEKERSKILLS":
							addProfileInternal(new ArrayList<String>(Arrays.asList(value)), profileId, "SEEKERSKILLS");
							break;
						case "SEEKERPROJECT":
							addProfileInternal(new ArrayList<String>(Arrays.asList(value)), profileId, "SEEKERPROJECT");
							break;
						default:
							break;
					}
				}

				preparedStatement.close();
			}

			for (String orignalValue : orignalValues)
			{
				if (!values.contains(orignalValue))
				{
					preparedStatement = connection.prepareStatement("DELETE FROM " + table
							+ " WHERE SEEKERPROFILE=? AND " + column + "=?");
					preparedStatement.setInt(1, profileId);
					preparedStatement.setString(2, orignalValue);

					preparedStatement.executeUpdate();

					preparedStatement.close();
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
	public boolean deleteProfile(int seekerProfileId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("DELETE FROM SEEKERPROFILE WHERE ID=?");
			preparedStatement.setInt(1, seekerProfileId);

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
	public List<Profile> getProfilesById(List<Integer> userIds)
	{
		List<Profile> profiles = new ArrayList<Profile>();

		for (int userId : userIds)
		{
			profiles.add(getProfile(userId));
		}

		return profiles;
	}

	@Override
	public Profile getProfile(int userId)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Profile profile = null;
		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT ID FROM SEEKERPROFILE WHERE CREATEDBY=?");
			preparedStatement.setInt(1, userId);

			ResultSet resultSet = preparedStatement.executeQuery();

			int profileId = -1;

			if (resultSet.next())
			{
				profileId = resultSet.getInt("ID");
			}

			profile = new Profile(getJobSeeker(userId));
			profile.setProfileId(profileId);

			profile.setEducation(getProfileInternal(profile.getProfileId(), "SEEKEREDUCATION", "EDUCATION"));
			profile.setExperience(getProfileInternal(profile.getProfileId(), "SEEKEREXPERIENCE", "EXPERIENCE"));
			profile.setProjects(getProfileInternal(profile.getProfileId(), "SEEKERPROJECT", "PROJECT"));
			profile.setInterests(getProfileInternal(profile.getProfileId(), "SEEKERINTERESTS", "INTERESTS"));
			profile.setSkills(getProfileInternal(profile.getProfileId(), "SEEKERSKILLS", "SKILLS"));

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

		return profile;
	}

	private List<String> getProfileInternal(int profileId, String table, String column) throws Exception
	{
		List<String> profiles = new ArrayList<String>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try
		{
			connection = JDBCConnectionUtil.createConnection();

			preparedStatement = connection.prepareStatement("SELECT " + column + " FROM " + table
					+ " WHERE SEEKERPROFILE=?");
			preparedStatement.setInt(1, profileId);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				profiles.add(resultSet.getString(column));
			}

		}
		catch (Exception e)
		{
			throw new Exception(e);
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

		return profiles;
	}

}
