package api.database;

import java.sql.Timestamp;

public class Trip
{
	private long id;
	private long imei;
	private int status;
	private int average_speed;
	private int max_speed;
	private Timestamp start_time;
	private Timestamp end_time;
	
	public Trip(long id, long imei, Timestamp time)
	{
		this.id = id;
		this.imei = imei;
		this.average_speed = 0;
		this.max_speed = 0;
		this.status = 1;
		this.start_time = time;
	}
	
	public void setStatus(boolean status)
	{
		if (status)
			this.status = 1;
		else
			this.status = 0;
	}
	
	public void setAverageSpeed(int average_speed)
	{
		this.average_speed = average_speed;
	}

	public void setMaximumSpeed(int max_speed)
	{
		this.max_speed = max_speed;
	}
	
	public void setStartTime(Timestamp time)
	{
		this.start_time = time;
	}
	
	public void setEndTime(Timestamp time)
	{
		this.end_time = time;
	}
	
}
