package com.rad.server.health.entities;

import java.util.*;
import javax.persistence.*;
import com.rad.server.health.services.*;

/**
 * @author raz_o
 */
@Entity
public class CoronaVirusData
{
	@Id
	String	id;				// example: 81
	String  continent;		// example: Europe
	String	country;		// example: "United Kingdom"
	String	provinceState;	// example: "British Virgin Islands"
	long	lastChange;		// example: 1586081775000
	long	lastUpdate;		// example: 1586081775000
	double	lat;			// example: 53.9333
	double	long_;			// example: -116.5765
	int		confirmed;		// example: 1181
	int		recovered;		// example: 0
	int     active;         // example: 7
	int 	critical;		// example: 3
	int		deaths;			// example: 20
	

	public CoronaVirusData()
	{

	}

	public CoronaVirusData(String id, String continent, String country, long lastUpdate, double lat, double long_, int confirmed, int recovered, int deaths)
	{
		super();
		this.id = id;
		this.continent = continent;
		this.country = country;
		this.provinceState = provinceState;
		this.lastUpdate = lastUpdate;
		this.lat = lat;
		this.long_ = long_;
		this.confirmed = confirmed;
		this.recovered = recovered;
		this.deaths = deaths;
	}

	public CoronaVirusData(String id, Map<String, Object> countryData)
	{
		super();

		this.id = id;
		
		if (countryData.get("provinceState") != null)
			this.provinceState = (String) countryData.get("provinceState");
		if (countryData.get("province") != null)
			this.provinceState = (String) countryData.get("province");
		if (countryData.get("country") != null)
		{
			this.country = (String) countryData.get("country");
			this.continent = ContinentUtils.getContinent(country);
		}
		if (countryData.get("lastChange") != null)
			this.lastChange = javax.xml.bind.DatatypeConverter.parseDateTime(countryData.get("lastChange").toString()).getTimeInMillis();
		if (countryData.get("lastUpdate") != null)
			this.lastUpdate = javax.xml.bind.DatatypeConverter.parseDateTime(countryData.get("lastUpdate").toString()).getTimeInMillis();	
		if (countryData.get("date") != null)
			this.lastUpdate = javax.xml.bind.DatatypeConverter.parseDateTime(countryData.get("date").toString()).getTimeInMillis();			
		if (countryData.get("latitude") != null)
			this.lat = (double) countryData.get("latitude");
		if (countryData.get("longitude") != null)
			this.long_ = (double) countryData.get("longitude");
		if (countryData.get("confirmed") != null)
			this.confirmed = (int) countryData.get("confirmed");
		if (countryData.get("recovered") != null)
			this.recovered = (int) countryData.get("recovered");
		if (countryData.get("active") != null)
			this.active = (int) countryData.get("active");		
		if (countryData.get("critical") != null)
			this.critical = (int) countryData.get("critical");
		if (countryData.get("deaths") != null)
			this.deaths = (int) countryData.get("deaths");		
	}

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getProvinceState()
	{
		return this.provinceState;
	}

	public void setProvinceState(String provinceState)
	{
		this.provinceState = provinceState;
	}

	public String getCountry()
	{
		return this.country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public long getLastUpdate()
	{
		return this.lastUpdate;
	}

	public void setLastUpdate(long lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public double getLat()
	{
		return this.lat;
	}

	public void setLat(double lat)
	{
		this.lat = lat;
	}

	public double getLong_()
	{
		return this.long_;
	}

	public void setLong_(double long_)
	{
		this.long_ = long_;
	}

	public int getConfirmed()
	{
		return this.confirmed;
	}

	public void setConfirmed(int confirmed)
	{
		this.confirmed = confirmed;
	}

	public int getRecovered()
	{
		return this.recovered;
	}

	public void setRecovered(int recovered)
	{
		this.recovered = recovered;
	}

	public int getDeaths()
	{
		return this.deaths;
	}

	public void setDeaths(int deaths)
	{
		this.deaths = deaths;
	}

	public long getLastChange()
	{
		return this.lastChange;
	}

	public void setLastChange(long lastChange)
	{
		this.lastChange = lastChange;
	}

	@Override
	public String toString()
	{
		return "CoronaVirusData [id=" + this.id + ", continent=" + this.continent + ", country=" + this.country + ", provinceState=" + this.provinceState + ", lastChange=" + this.lastChange + ", lastUpdate=" + this.lastUpdate + ", lat=" + this.lat + ", long_=" + this.long_ + ", confirmed=" + this.confirmed + ", recovered=" + this.recovered + ", active=" + this.active + ", critical=" + this.critical + ", deaths=" + this.deaths + "]";
	}

	public String getContinent()
	{
		return this.continent;
	}

	public void setContinent(String continent)
	{
		this.continent = continent;
	}
}