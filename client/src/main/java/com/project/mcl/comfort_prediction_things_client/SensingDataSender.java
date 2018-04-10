package com.project.mcl.comfort_prediction_things_client;

import android.util.Log;

import com.project.mcl.comfort_prediction_things_client.Connector.Connector;
import com.project.mcl.comfort_prediction_things_client.Connector.Data;
import com.project.mcl.comfort_prediction_things_client.Sensor.SensorCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by myks7 on 2018-03-30.
 */

public class SensingDataSender implements SensorCallback {
    private Connector connector;
    private Data data;
    private int sendCnt = 0;

    SensingDataSender(Connector connector) {
        this.connector = connector;
        data = new Data();
    }

    private void sendData() {
        sendCnt = sendCnt % 3;
        if (sendCnt == 0) {
            String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
            data.setTime(time);
            connector.execute(data);
        }
        sendCnt++;
    }

    @Override
    public void changeTemperature(float temperature) {
        Log.d(this.getClass().getName(), "temperature : " + temperature);
        data.setTemperature(temperature);
        sendData();
    }

    @Override
    public void changePressure(float pressure) {
        Log.d(this.getClass().getName(), "pressure : " + pressure);
        data.setPressure(pressure);
        sendData();
    }

    @Override
    public void changeHumidity(float humidity) {
        Log.d(this.getClass().getName(), "humidity : " + humidity);
        data.setHumidity(humidity);
        sendData();
    }


}
