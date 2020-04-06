/**
 * 
 */
package com.rad.server.health.services;

import java.util.*;
import org.keycloak.adapters.springsecurity.client.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import com.google.gson.*;
import com.rad.server.health.entities.*;

/**
 * @author raz_o
 */
@Service
public class CoronaVirusServiceImpl implements CoronaVirusService
{
    final Gson gson = new Gson();

	final String coronaVirusServiceUri = "http://localhost:8082/coronavirus/data";
	
	//@Autowired
	KeycloakRestTemplate keycloakRestTemplate;
	
	public List<CoronaVirusData> getData()
	{
	    RestTemplate restTemplate = new RestTemplate();
	    // String result = restTemplate.getForObject(coronaVirusServiceUri, String.class);
	    // System.out.println(result);
	    return getSampleList();
	}
	
	public List<CoronaVirusData> getDataByKeycloakRestTemplate()
	{
		ResponseEntity<String> response = keycloakRestTemplate.getForEntity(coronaVirusServiceUri, String.class);
	    System.out.println(response.getBody());
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