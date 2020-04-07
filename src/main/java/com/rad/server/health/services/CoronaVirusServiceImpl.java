/**
 * 
 */
package com.rad.server.health.services;

import java.util.*;
import org.keycloak.adapters.springsecurity.client.*;
import org.springframework.stereotype.*;
import com.google.gson.*;
import com.rad.server.health.entities.*;

/**
 * @author raz_o
 */
@Service
public class CoronaVirusServiceImpl implements CoronaVirusService
{
    final Gson gson = new Gson();

	final String coronaVirusServiceUri = "https://rapidapi.com/collection/coronavirus-covid-19"; //real URL should be here
	
	//@Autowired
	KeycloakRestTemplate keycloakRestTemplate;
	
	public List<CoronaVirusData> getData()
	{
	    return getSampleList();
	}
	
	public JsonArray getSample()
	{
	    List<CoronaVirusData> list = getSampleList();
	    JsonElement element = gson.toJsonTree(list, CoronaVirusData.class);
	    JsonArray jsonArray = element.getAsJsonArray();
	    return jsonArray;
	}
	
	public static List<CoronaVirusData> getSampleList()
	{
	    List<CoronaVirusData> list = new ArrayList<CoronaVirusData>();
	    
	    int counter = 1;
	    list.add(new CoronaVirusData(""+counter++, "America", "USA",    System.currentTimeMillis(),  0, 0, 12,  44,    55));
	    list.add(new CoronaVirusData(""+counter++, "Asia",    "Isreal", System.currentTimeMillis(),  0, 0, 66,  1231,  234234));
	    list.add(new CoronaVirusData(""+counter++, "Europe",  "Italy",  System.currentTimeMillis(),  0, 0, 545, 56456, 5671));
	    list.add(new CoronaVirusData(""+counter++, "Europe",  "Sapin",  System.currentTimeMillis(),  0, 0, 333, 56777, 121212));
	    list.add(new CoronaVirusData(""+counter++, "Asia",    "China",  System.currentTimeMillis(),  0, 0, 66,  1231,  234234));

	    return list;
	}
}