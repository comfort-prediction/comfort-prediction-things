package com.project.mcl.comfort_prediction_things_client.Sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver;

import java.io.IOException;

public class BME280Sensor {
    private static final String TAG = BME280Sensor.class.getSimpleName();
    private Context context;
    private String pin;
    private Bmx280SensorDriver bmx280SensorDriver;
    private SensorManager sensorManager;
    private float temperature;
    private int temperatureCount;
    private float pressure;
    private int pressureCount;
    private float humidity;
    private int humidityCount;
    private int sensingTerm;
    private SensorCallback sensorCallback;

    public BME280Sensor(Context context, String pin) {
        this.context = context;
        this.pin = pin;
        humidityCount = 0;
        pressureCount = 0;
        temperatureCount = 0;
    }

    private SensorEventListener temperatureSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temperature = event.values[0];
            temperatureCount = temperatureCount % sensingTerm;
            if (temperatureCount == 0) {
                sensorCallback.changeTemperature(temperature);
            }
            temperatureCount++;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "temperature sensor accuracy changed: " + accuracy);
        }
    };
    private SensorEventListener pressureSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            pressure = event.values[0];
            pressureCount = pressureCount % sensingTerm;
            if (pressureCount == 0) {
                sensorCallback.changePressure(pressure);
            }
            pressureCount++;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "pressure sensor accuracy changed: " + accuracy);
        }
    };
    private SensorEventListener humiditySensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            humidity = event.values[0];
            if (humidity > 50) {
                humidity = 0.8f * humidity - 0.2f * 50;
            } else {
                humidity = 0.2f * 50 - 0.8f * humidity;
            }
            humidityCount = humidityCount % sensingTerm;
            if (humidityCount == 0) {
                sensorCallback.changeHumidity(humidity);
            }
            humidityCount++;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "humidity sensor accuracy changed: " + accuracy);
        }
    };
    private SensorManager.DynamicSensorCallback dynamicSensorCallback = new SensorManager.DynamicSensorCallback() {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                Log.i(TAG, "TYPE_AMBIENT_TEMPERATURE connected");
                System.out.println(sensor.toString());
                sensorManager.registerListener(temperatureSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
                Log.i(TAG, "TYPE_PRESSURE connected");
                System.out.println(sensor.toString());
                sensorManager.registerListener(pressureSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                Log.i(TAG, "TYPE_RELATIVE_HUMIDITY connected");
                System.out.println(sensor.toString());
                sensorManager.registerListener(humiditySensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    };

    public void register(SensorCallback sensorCallback, int sensingTerm) {
        this.sensorCallback = sensorCallback;
        this.sensingTerm = sensingTerm;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerDynamicSensorCallback(dynamicSensorCallback);

        try {
            bmx280SensorDriver = new Bmx280SensorDriver(pin);
            bmx280SensorDriver.registerTemperatureSensor();
            bmx280SensorDriver.registerPressureSensor();
            bmx280SensorDriver.registerHumiditySensor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unregister() {
        sensorManager.unregisterDynamicSensorCallback(dynamicSensorCallback);
        sensorManager.unregisterListener(pressureSensorEventListener);
        sensorManager.unregisterListener(temperatureSensorEventListener);
        sensorManager.unregisterListener(humiditySensorEventListener);
        if (bmx280SensorDriver != null) {
            bmx280SensorDriver.unregisterTemperatureSensor();
            bmx280SensorDriver.unregisterPressureSensor();
            bmx280SensorDriver.registerHumiditySensor();
            try {
                bmx280SensorDriver.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                bmx280SensorDriver = null;
            }
        }
    }

    public float getTemperature() {
        return temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }
}
