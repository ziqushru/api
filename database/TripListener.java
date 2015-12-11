package api.database;

import java.sql.Timestamp;
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
	
	private List<Row> sortByTime(List<Row> data)
	{
		int j;
		for (int i = 1; i < data.size(); i++)
		{
			j = i;
			
			while (j > 0 && data.get(j).getTime().getTime() < data.get(j - 1).getTime().getTime())
			{
				Row temp_row = data.get(j);
				
				data.remove(j);
				data.add(j - 1, temp_row);
				
				j--;
			}
		}
		return data;
	}
	
	private void createTrips(List<Row> data)
	{
		data = sortByTime(data);
		Row row;
		boolean end = false;
		int points_length = 0;
		short max_speed = 0;
		short sum_speed = 0;
		Timestamp time = null;
		Trip trip = null;
		
		for (int i = 0; i < data.size(); i++)
		{
			row = data.get(i);
			if (row.getDigital_inputs() > 160)
			{
				time = row.getTime();
				if (trip == null)
				{
					trip = new Trip(trips.size() + 1, row.getImei(), time);
					points_length = 1;
					max_speed = row.getSpeed();
					sum_speed = row.getSpeed();
					continue;
				}
				points_length++;
				
				if (row.getSpeed() > max_speed)
					max_speed = row.getSpeed();
				
				sum_speed += row.getSpeed();
				
				end = i == data.size() - 1;
			}
			else if (row.getTime().getTime() - time.getTime() > 20000L)
			{				
				end = true;
			}
			
			if (end)
			{
				if (points_length > 0 && trip != null)
				{
					trip.setAverageSpeed(sum_speed / points_length);
					trip.setMaximumSpeed(max_speed);
					trip.setStatus(i == data.size() - 1);
					trip.setEndTime(time);
					trips.add(trip);
					trip = null;
				}
				end = false;
			}
		}
	}
	
	@Override
	public void run()
	{
		String query = 			"SELECT * FROM telemetry_copy ORDER BY imei";
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
				createTrips(temp_list);
				temp_list.clear();
				break;
			}
			
			if (imei != data.get(i).getImei())
			{
				createTrips(temp_list);
				temp_list.clear();
			}
		}
	}

	public List<Trip> getTrips()
	{
		return trips;
	}
}
