package com.rad.server.health.services;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.springframework.util.*;

/**
 * @author raz_o
 */
public class ContinentUtils
{
	public static String CONTINENT_ASIA      = "Asia";
	public static String CONTINENT_EUROPE    = "Europe";
	public static String CONTINENT_AFRICA    = "Africa";
	public static String CONTINENT_AMERICA   = "America";
	public static String CONTINENT_AUSTRALIA = "Australia";	
	
	private static HashMap<String, String> continentMap = new HashMap<>();
	
	static
	{
		parse();
	}

	public static void init()
	{
	}
	
	@SuppressWarnings("unchecked")
	public static void parse()
	{
		try
		{
			JSONParser parser = new JSONParser();
			
			File file = ResourceUtils.getFile("classpath:countriesData.json");
			Object obj = parser.parse(new FileReader(file.getPath()));

			JSONArray countryList = (JSONArray) obj;

			Iterator<JSONObject> iterator = countryList.iterator();
			while (iterator.hasNext())
			{
				JSONObject next = iterator.next();
				String name   = next.get("name").toString();
				String region = next.get("region").toString();
				continentMap.put(name, region);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	public static String getContinent(String countryName)
	{
		if (countryName.equals("USA"))
			countryName = "United States";
		
		if (continentMap.containsKey(countryName))
			return continentMap.get(countryName);
		
		return "Unknown";
	}
}
