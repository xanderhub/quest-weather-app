package com.quest.weather.runable;

import com.quest.weather.model.Monitor;
import com.quest.weather.model.Weather;
import com.quest.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorTask implements Runnable {
    private final Monitor monitor;
    private final WeatherService weatherService;

    private double prev_measured_temp;

    private static Logger LOG = LoggerFactory.getLogger(MonitorTask.class);

    public MonitorTask(Monitor monitor, WeatherService weatherService) {
        this.monitor = monitor;
        this.weatherService = weatherService;
    }


    @Override
    public void run() {
        while (true) {
            Weather weather = weatherService.getWeather(monitor.getCity_name());
            LOG.info("{}", weather);

            //check threshold
            if(Math.abs(100 - (prev_measured_temp / weather.getTemperature()) * 100) - monitor.getThreshold() > monitor.getThreshold())
                LOG.warn("Temperature changed in {}: Previous measure: {}, Current measure: {}"
                        ,monitor.getCity_name(), prev_measured_temp, weather.getTemperature());

            prev_measured_temp = weather.getTemperature();

            try {
                Thread.sleep(monitor.getFrequency() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
