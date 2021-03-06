package com.rad.server.health;

import java.net.*;

import com.rad.server.health.adapters.MultitenantConfiguration;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.context.event.*;
import org.springframework.context.*;
import org.springframework.core.env.*;

//Exclude RestClientAutoConfiguration to work with elastic
@SpringBootApplication(exclude = RestClientAutoConfiguration.class)
@ImportAutoConfiguration(MultitenantConfiguration.class)
public class HealthDataServiceApplication implements ApplicationListener<ApplicationReadyEvent>
{
    @Autowired
    private ApplicationContext applicationContext;

	public static void main(String[] args)
	{
		SpringApplication.run(HealthDataServiceApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event)
	{
		try
		{
			String ip       = InetAddress.getLocalHost().getHostAddress();
			String hostName = InetAddress.getLocalHost().getHostName();
			int port        = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
			
			System.out.println("*****************************************************");
			System.out.println("* Health Data Service is Ready ");
			System.out.println("* Host=" + hostName + ", IP=" + ip + ", Port=" + port);
			System.out.println("*****************************************************");
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}