package com.rad.server.health.entities;

import javax.persistence.*;

/**
 * @author raz_o
 */
@Entity
public class CoronaVirusData
{
	@Id
	String	id;				// example: 81
	String	provinceState;	// example: "British Virgin Islands"
	String	countryRegion;	// example: "United Kingdom"
	long	lastUpdate;		// example: 1586081775000
	double	lat;			// example: 53.9333
	double	long_;			// example: -116.5765
	int		confirmed;		// example: 1181
	int		recovered;		// example: 0
	int		deaths;			// example: 20
	
	public CoronaVirusData(String id, String provinceState, String countryRegion, long lastUpdate, double lat, double long_, int confirmed, int recovered, int deaths)
	{
		super();
		this.id = id;
		this.provinceState = provinceState;
		this.countryRegion = countryRegion;
		this.lastUpdate = lastUpdate;
		this.lat = lat;
		this.long_ = long_;
		this.confirmed = confirmed;
		this.recovered = recovered;
		this.deaths = deaths;
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

	public String getCountryRegion()
	{
		return this.countryRegion;
	}

	public void setCountryRegion(String countryRegion)
	{
		this.countryRegion = countryRegion;
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

	@Override
	public String toString()
	{
		return "CoronaVirusData [id=" + this.id + ", provinceState=" + this.provinceState + ", countryRegion=" + this.countryRegion + ", lastUpdate=" + this.lastUpdate + ", lat=" + this.lat + ", long_=" + this.long_ + ", confirmed=" + this.confirmed + ", recovered=" + this.recovered + ", deaths=" + this.deaths + "]";
	}
}