/**
 * 
 */
package com.rad.server.health.services;

import java.util.*;

import com.rad.server.health.repositories.CoronaRepository;
import org.keycloak.adapters.springsecurity.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import com.google.gson.*;
import com.rad.server.health.entities.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author raz_o
 */
@Service
public class CoronaVirusServiceImpl implements CoronaVirusService
{
    final Gson gson = new Gson();

	final String coronaVirusServiceUri = "https://covid-19-data.p.rapidapi.com/country/all"; //real URL should be here

	boolean isReal = true;
	//@Autowired
	KeycloakRestTemplate keycloakRestTemplate;

	@Autowired
	CoronaRepository coronaRepository;




	public List<CoronaVirusData> getData()
	{
		if(isReal)
			return getRealList();
		else
	    	return getSampleList();
	}
	
	public JsonArray getSample()
	{
	    List<CoronaVirusData> list = getSampleList();
	    JsonElement element = gson.toJsonTree(list, CoronaVirusData.class);
	    JsonArray jsonArray = element.getAsJsonArray();
	    return jsonArray;
	}

	public  List<CoronaVirusData> getRealList(){
		List<CoronaVirusData> list = new ArrayList<CoronaVirusData>();
		if(repoIsUpdated()) {
			list = (List<CoronaVirusData>) coronaRepository.findAll();
		}
		else {
			List<Map> dataFromApi = getDataFromApi();
			int counter = 1;
			if (dataFromApi != null) {
				for (Map countryData : dataFromApi) {
					if (countryData != null) {
						list.add(new CoronaVirusData("" + counter++, countryData));
					}
				}
				coronaRepository.saveAll(list);
			}
		}
		return list;
	}

	//Check If the data in the repository is up to date
	private boolean repoIsUpdated() {
		//For now - updated repo is an existing repo :)
		return coronaRepository.count() > 0;
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

	private static List<Map> getDataFromApi(){
		RestTemplate restTemplate = new RestTemplate();
		final String coronaVirusServiceUri = "https://covid-19-data.p.rapidapi.com/country/all";

		final HttpHeaders headers = new HttpHeaders();
		//Headers from Rapid-API
		headers.set("X-RapidAPI-Host", "covid-19-data.p.rapidapi.com");
		headers.set("X-RapidAPI-Key", "10e0755bcfmsh70fb720b18c677cp1b66efjsn8136ec80a4cc");

		//Create a new HttpEntity
		final HttpEntity<String> entity = new HttpEntity<String>(headers);

		//Execute the method writing your HttpEntity to the request
		ResponseEntity<ArrayList> response = restTemplate.exchange(coronaVirusServiceUri, HttpMethod.GET, entity, ArrayList.class);
		if(response.getStatusCode().value() == 200)
		{
			return response.getBody();
		}
		return null;
	}
}