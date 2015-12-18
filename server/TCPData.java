package api.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import api.database.Row;
import api.database.Trip;

public class TCPData
{
	@Override
	public String toString()
	{
		return "TCPData [success=" + success + ", errors=" + errors + ", vehicles=" + vehicles + ", trips=" + trips
				+ "]";
	}

	private boolean			success;
	private List<String>	errors;
	private List<Row>		vehicles;
	private List<Trip>		trips;

	public TCPData()
	{
		this(false);
	}

	public TCPData(boolean success)
	{
		this.success = success;
		this.errors = new ArrayList<String>();
		this.vehicles = new ArrayList<Row>();
		this.trips = new ArrayList<Trip>();
	}

	private Row getRecentRow(List<Row> data)
	{
		Row maximum_row = data.get(0);
		Timestamp maximum_time = maximum_row.getTime();

		for (int i = 1; i < data.size(); i++)
		{
			Row current_row = data.get(i);
			Timestamp curr_time = current_row.getTime();

			if (maximum_time.compareTo(curr_time) < 0)
			{
				maximum_time = curr_time;
				maximum_row = current_row;
			}
		}

		return maximum_row;
	}

	public TCPData getRecentData()
	{
		List<Row> list = new ArrayList<Row>();
		List<Row> temp_list = new ArrayList<Row>();
		long imei;

		for (int i = 1; i <= vehicles.size(); i++)
		{
			imei = vehicles.get(i - 1).getImei();
			temp_list.add(vehicles.get(i - 1));

			if (i == vehicles.size())
			{
				list.add(getRecentRow(temp_list));
				temp_list.clear();
				break;
			}

			if (imei != vehicles.get(i).getImei())
			{
				list.add(getRecentRow(temp_list));
				temp_list.clear();
			}
		}
		setVehicles(list);
		return this;
	}

	public TCPData getRecentData2()
	{
		List<Row> list = new ArrayList<Row>();

		long imei = vehicles.get(0).getImei();
		int imei_counter = 1;

		for (int i = 1; i < vehicles.size(); i++)
		{
			if (imei != vehicles.get(i).getImei())
			{
				imei_counter++;
				imei = vehicles.get(i).getImei();
			}
		}

		int j = 0;
		for (int i = 0; i < imei_counter; i++)
		{
			List<Row> cyka_list = new ArrayList<Row>();
			imei = vehicles.get(j).getImei();

			while (imei == vehicles.get(j).getImei())
			{
				cyka_list.add(vehicles.get(j));
				j++;
				if (j >= vehicles.size())
					break;
			}
			list.add(getRecentRow(cyka_list));
		}

		vehicles.clear();
		for (int i = 0; i < imei_counter; i++)
		{
			vehicles.add(list.get(i));
		}

		return this;
	}

	public void addError(String error)
	{
		if (error == null)
			errors = new ArrayList<String>();
		errors.add(error);
	}

	public void setVehicles(List<Row> vehicles)
	{
		this.vehicles.clear();
		for (int i = 0; i < vehicles.size(); i++)
		{
			this.vehicles.add(vehicles.get(i));
		}
	}

	public List<Row> getVehicles()
	{
		return vehicles;
	}

	public void setTrips(List<Trip> trips)
	{
		this.trips.clear();
		for (int i = 0; i < trips.size(); i++)
		{
			this.trips.add(trips.get(i));
		}
	}
	
	public List<Trip> getTrips()
	{
		return trips;
	}
}
