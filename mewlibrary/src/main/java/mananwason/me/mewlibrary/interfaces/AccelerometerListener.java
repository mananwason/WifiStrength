package mananwason.me.mewlibrary.interfaces;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by Manan Wason on 28/11/16.
 */

public interface AccelerometerListener extends SensorEventListener{

    void onSensorChanged(SensorEvent var1);

    void onAccuracyChanged(Sensor var1, int var2);

    void start(Context context, int samplingRate, String queryNumber, String requester);

    void stop();

}
