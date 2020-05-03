package com.rad.server.health.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.*;
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
	public CoronaVirusData getTotalsLatest()
	{
		CoronaVirusData result = (CoronaVirusData) coronaVirusService.getTotalsLatest();
		System.out.println("getTotalsLatest: " + result);
		return result;
	}
	
	@GetMapping("/totalsDaily")
	@ResponseBody
	public CoronaVirusData getTotalsDaily(@RequestParam String date)
	{
		CoronaVirusData result = (CoronaVirusData) coronaVirusService.getTotalsDaily(Long.parseLong(date));
		System.out.println("getTotalsDaily: " + result);
		return result;
	}
	
	@GetMapping("/corona")
	@ResponseBody
	public List<CoronaVirusData> corona()
	{
		return getCountriesLatest();
	}
	
	@GetMapping("/countriesLatest")
	@ResponseBody
	public List<CoronaVirusData> getCountriesLatest()
	{
		List<CoronaVirusData> result = (List<CoronaVirusData>) coronaVirusService.getCountriesLatest();
		System.out.println("getCountryLatest: " + result);
		return result;
	}
	
	
	@GetMapping("/countryDaily")
	@ResponseBody
	public List<CoronaVirusData> getCountryDaily(@RequestParam("date") long date, @RequestParam("countryName") String countryName)
	{
		List<CoronaVirusData> result = (List<CoronaVirusData>) coronaVirusService.getCountryDaily(date, countryName);
		System.out.println("getCountryDaily: " + result);
		return result;
	}
}