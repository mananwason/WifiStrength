package mananwason.me.mewlibrary.implementations;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import mananwason.me.mewlibrary.interfaces.AccelerometerListener;
import mananwason.me.mewlibrary.utils.Constants;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Manan Wason on 30/11/16.
 */

public class AccelerometerRecord implements AccelerometerListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ArrayList<String> readings;
    private String queryNo;
    private Context context;
    private String requester;

    private SensorManager getInstance(Context context) {
        this.context = context;
        if (mSensorManager == null || mAccelerometer == null) {
            mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        return mSensorManager;
    }

    @Override
    public void start(Context context, int samplingRate, String queryNumber, String requesterID) {
        getInstance(context);
        this.queryNo = queryNumber;
        this.readings = new ArrayList<>();
        this.requester = requesterID;
        mSensorManager.registerListener(this, mAccelerometer, samplingRate);
    }

    @Override
    public void stop() {
        mSensorManager.unregisterListener(this);
        Log.d("STOP", "AS");
        writeDataToFile(readings);
    }


    @Override
    public void onSensorChanged(SensorEvent var1) {
        long timeMillis = System.currentTimeMillis();
        Sensor sensor = var1.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d("READING: ", timeMillis + "," + var1.values[0] + "," + var1.values[1] + "," + var1.values[2] + "");
            readings.add(timeMillis + "," + var1.values[0] + "," + var1.values[1] + "," + var1.values[2]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor var1, int var2) {

    }

    public void writeDataToFile(ArrayList<String> arrayList) {
        FileOutputStream fileOut = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + Constants.FILE_BASE_PATH);
            file.mkdirs();
            File file1 = new File(file, queryNo.split("_")[0] + "/" + Constants.SENSORS.ACCELEROMETER.getValue().toUpperCase() + ".csv");
            fileOut = new FileOutputStream(file1);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fileOut);
            for (String string : arrayList) {
                myOutWriter.append(string + "\n");
            }
            myOutWriter.close();

            fileOut.flush();
            fileOut.close();
            Log.d("ACCEL ", requester);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
