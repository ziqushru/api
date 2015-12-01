package api.database;

import java.sql.Timestamp;

public class Row
{
	private long id;
	private long imei;
	private short fuel_level;
	private int analog_inputs1;
	private int digital_inputs;
	private float longitude;
	private float latitude;
	private short speed;
	private float odometer;
	private short prev_digital_inputs;
	private Timestamp time;
	
	// Represent Data in Row
	public Row()
	{}
	
	// Getters
	public long getID()
	{
		return id;
	}

	public long getImei()
	{
		return imei;
	}

	public short getFuel_level()
	{
		return fuel_level;
	}

	public int getAnalog_inputs1()
	{
		return analog_inputs1;
	}

	public int getDigital_inputs()
	{
		return digital_inputs;
	}

	public float getLongitude()
	{
		return longitude;
	}

	public float getLatitude()
	{
		return latitude;
	}

	public short getSpeed()
	{
		return speed;
	}

	public float getOdometer()
	{
		return odometer;
	}

	public short getPrev_digital_inputs()
	{
		return prev_digital_inputs;
	}

	public Timestamp getTime()
	{
		return time;
	}
	
	// Setters
	public void setID(long id)
	{
		this.id = id;
	}

	public void setImei(long imei)
	{
		this.imei = imei;
	}

	public void setFuel_level(short fuel_level)
	{
		this.fuel_level = fuel_level;
	}

	public void setAnalog_inputs1(int analog_inputs1)
	{
		this.analog_inputs1 = analog_inputs1;
	}

	public void setDigital_inputs(int digital_inputs)
	{
		this.digital_inputs = digital_inputs;
	}

	public void setLongitude(float longitude)
	{
		this.longitude = longitude;
	}

	public void setLatitude(float latitude)
	{
		this.latitude = latitude;
	}

	public void setSpeed(short speed)
	{
		this.speed = speed;
	}

	public void setOdometer(float odometer)
	{
		this.odometer = odometer;
	}

	public void setPrev_digital_inputs(short prev_digital_inputs)
	{
		this.prev_digital_inputs = prev_digital_inputs;
	}

	public void setTime(Timestamp time)
	{
		this.time = time;
	}

	@Override
	public String toString()
	{
		return "Data [id=" + id + ", imei=" + imei + ", fuel_level="
				+ fuel_level + ", analog_inputs1=" + analog_inputs1
				+ ", digital_inputs=" + digital_inputs + ", longitude="
				+ longitude + ", latitude=" + latitude + ", speed=" + speed
				+ ", odometer=" + odometer + ", prev_digital_inputs="
				+ prev_digital_inputs + ", time=" + time + "]";
	}
}
