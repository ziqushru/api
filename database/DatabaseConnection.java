package api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import api.server.TCPData;

public class DatabaseConnection
{
	private String 		connectionUrl;
	private Connection 	con;
	private TCPData 	tcp_data;
	
	public DatabaseConnection(String database_ip, String databaseName)
	{
		this(database_ip, databaseName, null, null);
	}
	
	public DatabaseConnection(String database_ip, String databaseName, String user, String password)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
        catch (ClassNotFoundException e) { e.printStackTrace(); }
		
		connectionUrl = "jdbc:mysql://" + database_ip + "/" + databaseName;
		try
		{
			if (user == null || password == null)
				con = DriverManager.getConnection(connectionUrl);
			else
				con = DriverManager.getConnection(connectionUrl, user, password);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
        
		tcp_data = new TCPData(true);
	}
	
	public TCPData getRecentData()
	{
		return tcp_data.getRecentData();
	}
	
	public String showDatabase()
	{
		return showDatabase(tcp_data.getVehicles());
	}
	
	public String showDatabase(List<Row> data)
	{	
		String result = "";
		
		result += " ~ DATABASE ~ " + '\n';		
		for (int i = 0; i < data.size(); i++)
			result += data.get(i).toString() + '\n';
		result += "  ~  END  ~  " + '\n';
		
		return result;
	}
	
	public void loadDatabase() {
		selectQuery("SELECT * FROM telemetry");
	}
	

	public void executeQuery(String query)
	{
		if (query == null || con == null)
			return;

		Statement stmt;
		try
		{
			stmt = con.createStatement();
			stmt.executeQuery(query);
		}
		catch (SQLException e) { e.printStackTrace(); }
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
//            	
            	row.setID(rs.getLong("id"));
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
            tcp_data.setVehicles(data);
            return data;
        }
        catch (Exception e)
		{
        	return null;
        }
	}
	
	public TCPData getTCPData()
	{
		return tcp_data;
	}
	
	public List<Row> getData()
	{
		return tcp_data.getVehicles();
	}	
}
