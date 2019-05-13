package com.quest.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class Weather implements Serializable {
    private String name;
    private long time;
    private double temperature;

    public String getName() {
        return name;
    }

    @JsonProperty("dt")
    public long getTime() {
        return time;
    }

    @JsonProperty("temp")
    public double getTemperature() {
        return temperature;
    }

    @JsonProperty("main")
    private void unpackNested(Map<String, Object> main) {
        this.temperature = (Double) main.get("temp");
    }

    @Override
    public String toString() {
        return "{ Name: " + name + ", Temperature: " + temperature + ", Time: "+ time + "}";
    }
}
