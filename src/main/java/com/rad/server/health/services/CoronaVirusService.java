package com.rad.server.health.services;

import java.util.*;
import com.rad.server.health.entities.*;

public interface CoronaVirusService
{
	CoronaVirusData getTotalsLatest();
	CoronaVirusData getTotalsDaily(long date);
	
	List<CoronaVirusData> getCountriesLatest();
	List<CoronaVirusData> getCountryDaily(long date, String countryName);
}