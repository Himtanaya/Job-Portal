package com.jobportal.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnectionUtil
{
	private static Properties	properties	= null;

	static
	{

	}

	public static Connection createConnection() throws Exception
	{
		Connection connection = null;

		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbms_project", "root", "Pass@1234");

		return connection;
	}

	public static void closeConnection(Connection connection)
	{
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{}
	}
}
