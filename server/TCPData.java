package server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import database.Row;

public class TCPData
{
	private boolean success;
	private List<String> errors;
	private List<Row> vehicles;
	
	public TCPData()
	{}
	
	public TCPData(boolean success)
	{
		this.success = success;
		this.errors = new ArrayList<String>();
		this.vehicles = new ArrayList<Row>();
	}
	
	public void toggleSuccess()
	{
		success = !success;
	}
	
	@Override
	public String toString() {
		return "TCPData [success=" + success +
				", errors=" + errors +
				", vehicles=" + vehicles + "]";
	}

	private Row getRecentRow(List<Row> data)
	{
		Row maximum_row = data.get(0);
		Timestamp maximum_time =  maximum_row.getTime();
		
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
		if (error != null)
		{
			errors.add(error);
		}
	}
	
	public void setVehicles(List<Row> data)
	{
		if (hasData(data))
		{
			this.vehicles = data;
		}
	}
	
	public List<Row> getVehicles()
	{
		return vehicles;
	}
	
	private boolean hasData(List<Row> data)
	{
		if (data == null)
			return false;
		if (data.size() == 0)
			return false;
		return true;
	}
	
	public boolean hasData()
	{
		return hasData(vehicles);
	}
}
