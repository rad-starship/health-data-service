package com.rad.server.health.services;

import java.io.*;
import java.util.*;
import org.elasticsearch.core.internal.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.springframework.core.io.*;

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
	private static HashMap<String, Long> populationMap = new HashMap<>();
	private static HashMap<String,List<String>> countriesMap = new HashMap<>();
	
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
			
			//File   file = ResourceUtils.getFile("classpath:countries.json");
       		//Object obj = parser.parse(new FileReader(file.getPath()));
       		
       		ClassPathResource classPathResource = new ClassPathResource("countries.json");
       		InputStream inputStream = classPathResource.getInputStream();
       		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
       		
       		Object obj = parser.parse(in);
			JSONArray countryList = (JSONArray) obj;

			Iterator<JSONObject> iterator = countryList.iterator();
			while (iterator.hasNext())
			{
				JSONObject next = iterator.next();
				String name   = next.get("name").toString();
				String region = next.get("region").toString();
				continentMap.put(name, region);
				countriesMap.computeIfAbsent(region, k -> new ArrayList<>());
				countriesMap.get(region).add(name);

				
				long population = Long.parseLong(next.get("population").toString());
				populationMap.put(name, population);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	public static String getContinent(String countryName)
	{
		countryName = normalizeCountryName(countryName);
		
		if (continentMap.containsKey(countryName)) {
			String result = continentMap.get(countryName);
			if(result.equals("Americas")) return "America";
			return result;
		}
		
		return "Unknown";
	}

	public static List<String> getCountries(List<String> continent){
		List<String> countries = new ArrayList<>();
		if(continent.contains("Admin")){
			for(List<String> countryList : countriesMap.values()){
				countries.addAll(countryList);
			}
		}
		else {
			for (String cont : continent) {
				cont = cont.equals("America")?cont+"s":cont;
				List<String> listCountry = countriesMap.get(cont);
				if(listCountry!=null)
					countries.addAll(listCountry);
			}
		}
		return countries;
	}

	public static long getPopulation(String countryName)
	{
		countryName = normalizeCountryName(countryName);
		
		if (populationMap.containsKey(countryName))
			return populationMap.get(countryName).longValue();
		
		return 0;
	}
	
	private static String normalizeCountryName(String countryName)
	{
		if (countryName.equals("USA"))
			return "United States";
		
		return countryName;
	}
}
