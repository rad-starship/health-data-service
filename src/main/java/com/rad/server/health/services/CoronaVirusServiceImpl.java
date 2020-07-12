package com.rad.server.health.services;

import java.io.IOException;
import java.net.ConnectException;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rad.server.health.presistance.EsConnectionHandler;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import com.google.gson.*;
import com.rad.server.health.entities.*;
import com.rad.server.health.repositories.*;

@Service
public class CoronaVirusServiceImpl implements CoronaVirusService
{
	private final static Gson	gson				= new Gson();

	private final static String	COVID_19_DATA_HOST_HEADER = "X-RapidAPI-Host";
	private final static String	COVID_19_DATA_KEY_HEADER  = "X-RapidAPI-Key";
	private final static String	COVID_19_DATA_HOST		  = "covid-19-data.p.rapidapi.com";
	private final static String	COVID_19_DATA_URL	      = "https://" + COVID_19_DATA_HOST + "/";
	private final static String nms = "http://localhost:8081/";
	private final static String nmsUri = nms +"users/getContinentsByToken/";
	private final static String isOnlineUri = nms+"settings/isOnline";

	private final static int MAX_REQUEST_PER_ACTION = 10;
	@Autowired
	private CoronaRepository	coronaRepository;

	@Autowired
	private EsConnectionHandler esConnectionHandler;

	@Autowired
	private KeycloakRestTemplate keycloakRestTemplate;
	private boolean isToUseKeycloakRestTemplate = true;

	@Autowired
	private AccessToken token;

	public CoronaVirusServiceImpl()
	{
		ContinentUtils.init();
	}

	
	public List<CoronaVirusData> getRealList()
	{
		List<CoronaVirusData> list = new ArrayList<CoronaVirusData>();
		if (repoIsUpdated())
		{
			list = (List<CoronaVirusData>) coronaRepository.findAll();
		}
		else
		{
			List<CoronaVirusData> dataFromApi = getCountriesLatest(null);
			coronaRepository.saveAll(list);
		}
		
		return list;
	}

	// Check If the data in the repository is up to date
	private boolean repoIsUpdated()
	{
		// For now - updated repo is an existing repo :)
		return coronaRepository.count() > 0;
	}

	/**
	 * Get data for each country.
	 * For example: 
		[
		    {
		        "name": "Afghanistan",
		        "alpha2code": "AF",
		        "alpha3code": "AFG",
		        "latitude": 33.93911,
		        "longitude": 67.709953
		    },
		    {
		        "name": "Åland Islands",
		        "alpha2code": "AX",
		        "alpha3code": "ALA",
		        "latitude": 60.1995487,
		        "longitude": 20.3711715
		    }
		]
	 * @return
	 */
	private List<CoronaVirusData> getListOfCountries(HttpHeaders headers)
	{
		return getDataFromApi("help/countries", "ebfe2c57a8msha005ee72e713c84p19ce3ejsn6f034987944d");
	}
	
	/**
	 * Get latest data for whole world.
	 * For example: 
	 * [
		    {
		        "confirmed": 3494535,
		        "recovered": 1136205,
		        "critical": 50863,
		        "deaths": 244987,
		        "lastChange": "2020-05-03T09:23:51+02:00",
		        "lastUpdate": "2020-05-03T09:30:03+02:00"
		    }
		]
	 */
	public CoronaVirusData getTotalsLatest(HttpHeaders headers)
	{
		List<CoronaVirusData> data = getDataFromApi("totals", "10e0755bcfmsh70fb720b18c677cp1b66efjsn8136ec80a4cc");
		getTenantsFromNMS(headers);
		return data == null || data.isEmpty() ? new CoronaVirusData() : data.get(0);
	}
	
	/**
	 * Get specific day data for the whole world. 
	 * For example: 
	 * Request: report/totals?date-format=YYYY-MM-DD&format=json&date=2020-04-20 
	 * Response: 
		[
		    {
		        "confirmed": 2472259,
		        "recovered": 560866,
		        "deaths": 169986,
		        "active": 1741407,
		        "date": "2020-04-20"
		    }
		]
	 */
	public CoronaVirusData getTotalsDaily(long date,HttpHeaders headers)
	{
		String requestedDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date(date));		
		List<CoronaVirusData> data = getDataFromApi("report/totals?date-format=YYYY-MM-DD&format=json&date="+requestedDay, "ebfe2c57a8msha005ee72e713c84p19ce3ejsn6f034987944d");
		return data == null || data.isEmpty() ? new CoronaVirusData() : data.get(0);
	}
	
	/**
	 * Get latest data for each country.
	 * For example: 
		[
		    {
		        "country": "Afghanistan",
		        "confirmed": 2469,
		        "recovered": 331,
		        "critical": 7,
		        "deaths": 72,
		        "latitude": 33.93911,
		        "longitude": 67.709953,
		        "lastChange": "2020-05-02T11:27:27+02:00",
		        "lastUpdate": "2020-05-03T09:30:03+02:00"
		    },
		    {
		        "country": "Åland Islands",
		        "confirmed": 0,
		        "recovered": 0,
		        "critical": 0,
		        "deaths": 0,
		        "latitude": 60.1995487,
		        "longitude": 20.3711715,
		        "lastChange": null,
		        "lastUpdate": null
		    }
		]
	 * @return
	 */
	public List<CoronaVirusData> getCountriesLatest(HttpHeaders headers)
	{
		return getDataFromApi("country/all", "10e0755bcfmsh70fb720b18c677cp1b66efjsn8136ec80a4cc");
	}
	
	/**
	 * Get specific day data for specific country. 
	 * For example: 
	 * Request: report/country/name?date-format=YYYY-MM-DD&format=json&date=2020-04-01&name=Italy
	 * Response: 
		[
		    {
		        "country": "Italy",
		        "provinces": [
		            {
		                "province": "Italy",
		                "confirmed": 110574,
		                "recovered": 16847,
		                "deaths": 13155,
		                "active": 80572
		            }
		        ],
		        "latitude": 41.87194,
		        "longitude": 12.56738,
		        "date": "2020-04-01"
		    }
		]
		
	 * Request: report/country/name?date-format=YYYY-MM-DD&format=json&date=2020-04-01&name=USA
	 * Response: 		
		[
		    {
		        "country": "USA",
		        "provinces": [
		            {
		                "province": "Alabama",
		                "confirmed": 1060,
		                "recovered": 0,
		                "deaths": 27,
		                "active": 0
		            },
		            {
		                "province": "Alaska",
		                "confirmed": 132,
		                "recovered": 0,
		                "deaths": 3,
		                "active": 0
		            },
		            {
		                "province": "Arizona",
		                "confirmed": 1530,
		                "recovered": 0,
		                "deaths": 29,
		                "active": 0
		            }
		     }
		]	
	 */
	public List<CoronaVirusData> getCountryDaily(long date, String countryName,HttpHeaders headers)
	{
		String requestedDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date(date));		
		return getDataFromApi("report/country/name?date-format=YYYY-MM-DD&format=json&date="+requestedDay+"&name=" + countryName, "ebfe2c57a8msha005ee72e713c84p19ce3ejsn6f034987944d");
	}

	public List<CoronaVirusData> getCountry(String countryName,HttpHeaders headers)
	{
		return getDataFromApi("country?name=" + countryName, "ebfe2c57a8msha005ee72e713c84p19ce3ejsn6f034987944d");
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<CoronaVirusData> getDataFromApi(String releativeUri, String key)
	{
		String apiUri = COVID_19_DATA_URL + releativeUri;

		// Headers from Rapid-API
		final HttpHeaders headers = new HttpHeaders();
		headers.set(COVID_19_DATA_HOST_HEADER, COVID_19_DATA_HOST);
		headers.set(COVID_19_DATA_KEY_HEADER,  key);

		// Create a new HttpEntity
		final HttpEntity<String> entity = new HttpEntity<String>(headers);

		// Execute the method writing your HttpEntity to the request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ArrayList> response = restTemplate.exchange(apiUri, HttpMethod.GET, entity, ArrayList.class);
		if (response.getStatusCode().value() == 200)
		{
			List<Map> dataFromApi =  response.getBody();
			if (dataFromApi != null)
			{
				List<CoronaVirusData> list = new ArrayList<CoronaVirusData>();

				int counter = 1;
				for (Map countryData : dataFromApi)
				{
					if (countryData != null)
					{
						if (countryData.containsKey("provinces"))
						{
							List<Map> provincesList = (List) countryData.get("provinces");
							for (Map province : provincesList)
							{
								province.put("country", countryData.get("country"));
								province.put("latitude", countryData.get("latitude"));
								province.put("longitude", countryData.get("longitude"));
								province.put("date", countryData.get("date"));								
								list.add(new CoronaVirusData("" + counter++, province));
							}
						}
						else
						{
							list.add(new CoronaVirusData("" + counter++, countryData));
						}						
					}
				}
				return list;
			}
		}
		
		return new ArrayList<CoronaVirusData>();
	}

	private static List<CoronaVirusData> getSampleList()
	{
		List<CoronaVirusData> list = new ArrayList<CoronaVirusData>();

		int counter = 1;
		list.add(new CoronaVirusData("" + counter++, "America", "USA", System.currentTimeMillis(), 0, 0, 12, 44, 55));
		list.add(new CoronaVirusData("" + counter++, "Asia",    "Isreal", System.currentTimeMillis(), 0, 0, 66, 1231, 234234));
		list.add(new CoronaVirusData("" + counter++, "Europe",  "Italy", System.currentTimeMillis(), 0, 0, 545, 56456, 5671));
		list.add(new CoronaVirusData("" + counter++, "Europe", "Sapin", System.currentTimeMillis(), 0, 0, 333, 56777, 121212));
		list.add(new CoronaVirusData("" + counter++, "Asia", "China", System.currentTimeMillis(), 0, 0, 66, 1231, 234234));

		return list;
	}



	/**
	 * The function recieves continents from token and then gets data from the API for the relevant continent Countries.
	 * @return list of corona info based on the continent.
	 */
	public List<CoronaVirusData> getDataByTeanant(HttpHeaders headers){
		List<CoronaVirusData> data = new ArrayList<>();
		List<String> continents = getTenantsFromNMS(headers);

		if(!isOnline(headers)) {
			data = getDataFromEs(continents);
			if (data.size() > 0)
				return data;
		}

		List<String> countries = ContinentUtils.getCountries(continents);
		int counter = 0;
		for(String country : countries){
			data.addAll(getCountry(country,headers));
			counter++;
		}

		saveToEs(data);
		return data;

	}

	private List<CoronaVirusData> getDataFromEs(List<String> continents) {

		if(continents.contains("Admin")){
			continents.remove("Admin");
			continents.addAll(Stream.of("Asia","America","Africa","Europe").collect(Collectors.toList()));
		}
		List<CoronaVirusData> data = new ArrayList<>();
		try {
			esConnectionHandler.makeConnection();

			for (String continent : continents) {
				data.addAll(esConnectionHandler.getByContinent(continent));
			}

			esConnectionHandler.closeConnection();
		} catch (IOException e) {
			System.out.println("An Error Accrued In Es : " + e.getMessage());
		} catch(Exception e){
			System.out.println("An Error Accrued In Es");
		}
		return data;
	}

	private void saveToEs(List<CoronaVirusData> data) {
		try {
			esConnectionHandler.makeConnection();
			for (CoronaVirusData cData : data)
				esConnectionHandler.insertData(cData);
			esConnectionHandler.closeConnection();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//***********************************************************************
	//                          NMS communication
	//***********************************************************************


	private List<String> getTenantsFromNMS(HttpHeaders headers){
		// Execute the method writing your HttpEntity to the request
		List<String> tenants= (List<String>)getForEntity(nmsUri+token.getPreferredUsername(),headers);
		for(String i:tenants){
			System.out.println(i);
		}
		return tenants;
	}

	private boolean isOnline(HttpHeaders headers){
		boolean result = (boolean)getForEntity(isOnlineUri,headers);
		return result;
	}

	private Object getForEntity(String url, HttpHeaders headers) {
		HttpEntity<Object> requestUpdate = new HttpEntity<>(headers);
		ResponseEntity<Object> response = new RestTemplate().exchange(url,HttpMethod.GET,requestUpdate, Object.class);
		return response.getBody();
	}

}