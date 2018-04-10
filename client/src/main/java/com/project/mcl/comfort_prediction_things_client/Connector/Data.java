package com.project.mcl.comfort_prediction_things_client.Connector;

/**
 * Created by myks7 on 2018-03-30.
 */

public class Data {
    private float temperature = 0;
    private float pressure = 0;
    private float humidity = 0;
    private String time;

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    String getGETString() {
        return "time="+time+"&temperature=" + temperature + "&pressure=" + pressure + "&humidity=" + humidity;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
