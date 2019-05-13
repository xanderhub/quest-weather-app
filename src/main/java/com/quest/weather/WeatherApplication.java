package com.quest.weather;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.weather.model.Monitor;
import com.quest.weather.runable.MonitorTask;
import com.quest.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class WeatherApplication implements CommandLineRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(WeatherApplication.class);

	@Autowired
	private WeatherService weatherService;

	private List<Monitor> monitors;

	public static void main(String[] args) {
		SpringApplication.run(WeatherApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// read JSON config file
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = TypeReference.class.getResourceAsStream("/static/configuration.json");

		try {
			monitors = mapper.readValue(inputStream, new TypeReference<List<Monitor>>(){});
			ExecutorService MONITOR_THREAD_POOL = Executors.newFixedThreadPool(monitors.size());

			for(Monitor monitor : monitors)
				MONITOR_THREAD_POOL.execute(new MonitorTask(monitor,weatherService));

		} catch (IOException e){
			System.out.println("Unable to read weather configuration file: " + e.getMessage());
		}
	}
}
