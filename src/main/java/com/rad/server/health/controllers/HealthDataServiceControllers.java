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
	
	@GetMapping("/corona")
	@ResponseBody
	public List<CoronaVirusData> getCoronaData()
	{
		List<CoronaVirusData> result = (List<CoronaVirusData>) coronaVirusService.getData();
		System.out.println("getCoronaData: " + result);
		return result;
	}
}