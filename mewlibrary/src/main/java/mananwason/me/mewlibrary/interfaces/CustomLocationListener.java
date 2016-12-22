package mananwason.me.mewlibrary.interfaces;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;


/**
 * Created by Manan Wason on 29/11/16.
 */

public interface CustomLocationListener extends LocationListener {
    void onLocationChanged(Location var1);

    float getAccuracy();

    double getAltitude();

    String getProvider();

    float getSpeed();

    void stop();

    void start(Context context, long samplingRate, String queryNumber, String requester);

    double getLatitude();

    double getLongitude();

    boolean canGetLocation();
}
