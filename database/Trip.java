package api.database;

import java.util.ArrayList;
import java.util.List;

public class Trip
{
	private int id;
	private long imei;
	private List<Row> data;
	
	public Trip()
	{
		data = new ArrayList<Row>();
	}

	public void start()
	{
		if (data == null)
		{
			data = new ArrayList<Row>();
		}
		else if (data.size() > 0)
		{
			data.clear();
		}
	}
	
	public void addRow(Row row)
	{
		if (row != null)
			data.add(row);
	}
	
	public Trip stop()
	{
		return this;
	}
	
	public void setImei(long imei)
	{
		this.imei = imei;
	}
	
	public int getID()
	{
		return id;
	}
	
	public long getImei()
	{
		return imei;
	}
	
}
