package api.database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TripListener implements Runnable
{
	private DatabaseConnection	database_connection;
	private List<Trip>			trips;
	private Thread				thread;

	public TripListener(String database_ip, String databaseName, String user, String password)
	{
		this.database_connection = new DatabaseConnection(database_ip, databaseName, user, password);
		this.trips = new ArrayList<Trip>();
		this.thread = new Thread(this, "api - trips thread");
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

	// format yyyy-mm-dd hh:mm:ss:ff
	private int getDifInMin(Timestamp past, Timestamp recent)
	{
		String past_string = past.toString();
		String recent_string = recent.toString();

		// 2015-12-09 07:55:16
		// CHECK YEAR
		int past_offset = 0;
		int recent_offset = 0;
		String past_year = "";
		String recent_year = "";
		for (int i = 0; i < 4; i++)
		{
			past_year += past_string.charAt(past_offset++);
			recent_year += recent_string.charAt(recent_offset++);
		}
		if (!past_year.equals(recent_year))
			return 420;

		// 2015-12-09 07:55:16
		// CHECK MONTH
		past_offset++;
		recent_offset++;
		String past_month = "";
		String recent_month = "";
		for (int i = 0; i < 2; i++)
		{
			past_month += past_string.charAt(past_offset++);
			recent_month += recent_string.charAt(recent_offset++);
		}
		if (!past_month.equals(recent_month))
			return 420;

		// 2015-12-09 07:55:16
		// CHECK DAY
		past_offset++;
		recent_offset++;
		String past_day = "";
		String recent_day = "";
		for (int i = 0; i < 2; i++)
		{
			past_day += past_string.charAt(past_offset++);
			recent_day += recent_string.charAt(recent_offset++);
		}
		if (!past_day.equals(recent_day))
			return 420;

		// 2015-12-09 07:55:16
		// CHECK HOUR
		past_offset++;
		recent_offset++;
		String past_hour = "";
		String recent_hour = "";
		for (int i = 0; i < 2; i++)
		{
			past_hour += past_string.charAt(past_offset++);
			recent_hour += recent_string.charAt(recent_offset++);
		}
		if (!past_hour.equals(recent_hour))
			return 420;

		// 2015-12-09 07:55:16
		// CHECK MINUTES
		past_offset++;
		recent_offset++;
		String past_minutes_string = "";
		String recent_minutes_string = "";
		for (int i = 0; i < 2; i++)
		{
			if (past_string.charAt(past_offset) != ':')
				;
			past_minutes_string += past_string.charAt(past_offset++);
			if (recent_string.charAt(recent_offset) != ':')
				;
			recent_minutes_string += recent_string.charAt(recent_offset++);
		}
		int past_minutes = Integer.parseInt(past_minutes_string);
		int recent_minutes = Integer.parseInt(recent_minutes_string);

		// CALCULATE DIFFERENCE IN MINUTES
		int difference = recent_minutes - past_minutes;
		return difference;
	}

	// format MMM DD, YYYY hh:mm:ss
	private int getDifInMin2(Timestamp past, Timestamp recent)
	{
		String past_string = past.toString();
		String recent_string = recent.toString();

		// Dec 9, 2015 7:19:22 AM
		// CHECK MONTH
		int past_offset = 0;
		int recent_offset = 0;
		String past_month = "";
		String recent_month = "";
		for (int i = 0; i < 3; i++)
		{
			past_month += past_string.charAt(past_offset++);
			recent_month += recent_string.charAt(recent_offset++);
		}
		if (!past_month.equals(recent_month))
			return 420;

		// Dec 9, 2015 7:19:22 AM
		// CHECK DAY
		past_offset++;
		String past_day = "";
		past_day += past_string.charAt(past_offset++);
		if (past_string.charAt(past_offset) != ',')
			past_day += past_string.charAt(past_offset++);
		recent_offset++;
		String recent_day = "";
		recent_day += recent_string.charAt(recent_offset++);
		if (recent_string.charAt(recent_offset) != ',')
			recent_day += recent_string.charAt(recent_offset++);
		if (!past_day.equals(recent_day))
			return 420;

		// Dec 9, 2015 7:19:22 AM
		// CHECK YEAR
		past_offset += 2;
		String past_year = "";
		for (int i = 0; i < 4; i++)
			past_year += past_string.charAt(past_offset++);
		recent_offset += 2;
		String recent_year = "";
		for (int i = 0; i < 4; i++)
			recent_year += recent_string.charAt(recent_offset++);
		if (!past_year.equals(recent_year))
			return 420;

		// Dec 9, 2015 7:19:22 AM
		// CHECK HOUR
		past_offset++;
		String past_hour = "";
		past_hour += past_string.charAt(past_offset++);
		if (past_string.charAt(past_offset) != ':')
			past_hour += past_string.charAt(past_offset++);
		recent_offset++;
		String recent_hour = "";
		recent_hour += recent_string.charAt(recent_offset++);
		if (recent_string.charAt(recent_offset) != ':')
			recent_hour += recent_string.charAt(recent_offset++);
		if (!past_hour.equals(recent_hour))
			return 420;

		// Dec 9, 2015 7:19:22 AM
		// CHECK MINUTES
		past_offset++;
		String past_minutes_string = "";
		past_minutes_string += past_string.charAt(past_offset++);
		past_minutes_string += past_string.charAt(past_offset++);
		int past_minutes = Integer.parseInt(past_minutes_string);
		recent_offset++;
		String recent_minutes_string = "";
		recent_minutes_string += recent_string.charAt(recent_offset++);
		recent_minutes_string += recent_string.charAt(recent_offset++);
		int recent_minutes = Integer.parseInt(recent_minutes_string);

		// CALCULATE DIFFERENCE IN MINUTES
		int difference = recent_minutes - past_minutes;
		return difference;
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
					trip.addTripPoint(row);
					continue;
				}
				points_length++;
				if (row.getSpeed() > max_speed)
					max_speed = row.getSpeed();
				sum_speed += row.getSpeed();

				trip.addTripPoint(row);

				end = i == data.size() - 1;
			} else
				end = true;

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
		String query = "SELECT * FROM telemetry_copy ORDER BY imei";
		List<Row> data = database_connection.selectQuery(query);
		List<Row> temp_list = new ArrayList<Row>();
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
