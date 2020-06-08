package com.rad.server.health.controllers;

import java.util.*;

import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import com.rad.server.health.entities.*;
import com.rad.server.health.services.*;

/**
 * @author raz_o
 */
@Controller
public class HealthDataServiceControllers
{
	@Autowired
	private CoronaVirusService	coronaVirusService;


	@GetMapping("/totalsLatest")
	@ResponseBody
	public CoronaVirusData getTotalsLatest(@RequestHeader HttpHeaders headers)
	{
		CoronaVirusData result = (CoronaVirusData) coronaVirusService.getTotalsLatest(headers);
		System.out.println("getTotalsLatest: " + result);
		return result;
	}
	
	@GetMapping("/totalsDaily")
	@ResponseBody
	public CoronaVirusData getTotalsDaily(@RequestParam String date,@RequestHeader HttpHeaders headers)
	{
		CoronaVirusData result = (CoronaVirusData) coronaVirusService.getTotalsDaily(Long.parseLong(date),headers);
		System.out.println("getTotalsDaily: " + result);
		return result;
	}
	
	@GetMapping("/corona")
	@ResponseBody
	public List<CoronaVirusData> corona(@RequestHeader HttpHeaders headers)
	{
		return coronaVirusService.getDataByTeanant(headers);
	}
	
	@GetMapping("/countriesLatest")
	@ResponseBody
	public List<CoronaVirusData> getCountriesLatest(@RequestHeader HttpHeaders headers)
	{
		List<CoronaVirusData> result = (List<CoronaVirusData>) coronaVirusService.getCountriesLatest(headers);
		System.out.println("getCountryLatest: " + result);
		return result;
	}
	
	
	@GetMapping("/countryDaily")
	@ResponseBody
	public List<CoronaVirusData> getCountryDaily(@RequestParam("date") long date, @RequestParam("countryName") String countryName,@RequestHeader HttpHeaders headers)
	{
		List<CoronaVirusData> result = (List<CoronaVirusData>) coronaVirusService.getCountryDaily(date, countryName,headers);
		System.out.println("getCountryDaily: " + result);
		return result;
	}
}