package com.rad.server.health.services;

import java.util.*;
import com.rad.server.health.entities.*;
import org.springframework.http.HttpHeaders;

public interface CoronaVirusService
{
	CoronaVirusData getTotalsLatest(HttpHeaders headers);
	CoronaVirusData getTotalsDaily(long date,HttpHeaders headers);
	
	List<CoronaVirusData> getCountriesLatest(HttpHeaders headers);
	List<CoronaVirusData> getCountryDaily(long date, String countryName,HttpHeaders headers);
}