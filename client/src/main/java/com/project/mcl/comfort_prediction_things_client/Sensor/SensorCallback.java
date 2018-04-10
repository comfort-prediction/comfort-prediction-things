package com.project.mcl.comfort_prediction_things_client.Sensor;

/**
 * Created by myks7 on 2018-03-30.
 */

public interface SensorCallback {
    void changeTemperature(float temperature);
    void changePressure(float pressure);
    void changeHumidity(float humidity);

}
