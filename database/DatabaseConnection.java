package api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection
{
	private String		connectionUrl;
	private Connection	con;

	public DatabaseConnection(String database_ip, String databaseName, String user, String password)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		connectionUrl = "jdbc:mysql://" + database_ip + "/" + databaseName;
		try
		{
			con = DriverManager.getConnection(connectionUrl, user, password);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<Row> selectQuery(String query)
	{
		if (query == null || con == null)
			return null;

		try
		{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<Row> data = new ArrayList<Row>();

			while (rs.next())
			{
				Row row = new Row();
				row.setID(rs.getLong("id"));
				row.setTripID(rs.getLong("trip_id"));
				row.setImei(rs.getLong("imei"));
				row.setFuel_level(rs.getShort("fuel_levels0"));
				row.setAnalog_inputs1(rs.getInt("analog_inputs0"));
				row.setDigital_inputs(rs.getInt("digital_inputs"));
				row.setLongitude(rs.getFloat("longitude"));
				row.setLatitude(rs.getFloat("latitude"));
				row.setSpeed(rs.getShort("speed"));
				row.setOdometer(rs.getFloat("odometer"));
				row.setPrev_digital_inputs(rs.getShort("prev_digital_inputs"));
				row.setTime(rs.getTimestamp("time"));
				data.add(row);
			}
			rs.close();
			return data;
		} catch (Exception e)
		{
			return null;
		}
	}
}
