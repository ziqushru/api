package api.database;

import java.util.ArrayList;
import java.util.List;

public class TripListener implements Runnable
{
	private DatabaseConnection 	database_connection;
	private List<Trip> 			trips;
	private Thread 				thread;
	
	public TripListener(String database_ip, String databaseName, String user, String password)
	{
		this.database_connection = 	new DatabaseConnection(database_ip, databaseName, user, password);
		this.trips = 				new ArrayList<Trip>();
		this.thread = 				new Thread(this, "api - trip thread");
	}
	
	public void start()
	{
		thread.start();
	}
	
	private void getTrips(List<Row> data)
	{
		for (int i = 0; i < data.size(); i++)
		{
			Trip trip = new Trip();
			trip.start();
			
			
		}
	}

	@Override
	public void run()
	{
		String query = 			"SELECT * FROM telemetry ORDER BY imei";
		database_connection.selectQuery(query);
		List<Row> data = 		database_connection.getData();
		List<Row> temp_list =	new ArrayList<Row>();
		long imei;
		
		for (int i = 1; i <= data.size(); i++)
		{
			imei = data.get(i - 1).getImei();
			temp_list.add(data.get(i - 1));
			
			if (i == data.size())
			{
				getTrips(temp_list);
				temp_list.clear();
				break;
			}
			
			if (imei != data.get(i).getImei())
			{
				getTrips(temp_list);
				temp_list.clear();
			}
		}
		
		
		// RUNNING - CYKA
		while (true)
		{
			query = "SELECT * FROM telemetry ORDER BY imei";
			database_connection.selectQuery(query);
			database_connection.getRecentData();
			data = database_connection.getData();
			
			
			for (int i = 0; i < data.size(); i++)
			{
				if (data.get(i).getDigital_inputs() == 128)
				{
					
				}
				else if (data.get(i).getDigital_inputs() == 129)
				{
					
				}
				
			}
			
			
			try
			{
				Thread.sleep(1500);
			}
			catch (InterruptedException e) { e.printStackTrace(); }
		}
		
	}

}
