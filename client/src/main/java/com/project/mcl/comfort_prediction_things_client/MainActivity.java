package com.project.mcl.comfort_prediction_things_client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.project.mcl.comfort_prediction_things_client.Connector.HttpConnector;
import com.project.mcl.comfort_prediction_things_client.Sensor.BME280Sensor;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final Activity mActivity = this;

    BME280Sensor bmp280Sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        bmp280Sensor = new BME280Sensor(mActivity, "I2C1");
        SensingDataSender sensingDataSender = new SensingDataSender(new HttpConnector());
        bmp280Sensor.register(sensingDataSender, 25);

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        bmp280Sensor.unregister();
        super.onDestroy();
    }


}
