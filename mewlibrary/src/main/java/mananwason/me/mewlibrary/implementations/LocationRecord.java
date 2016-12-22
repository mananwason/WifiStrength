package mananwason.me.mewlibrary.implementations;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import mananwason.me.mewlibrary.interfaces.CustomLocationListener;
import mananwason.me.mewlibrary.utils.Constants;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Manan Wason on 29/11/16.
 */

public class LocationRecord implements CustomLocationListener {

    private Context mContext;

    private boolean canGetLocation = false;

    private Location location;
    private double latitude;
    private double longitude;
    private String queryNo;
    private String requester;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = Constants.DISTANCE_BETWEEN_LOCATION_UPDATES; // 10 meters

    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES = Constants.TIME_BETWEEN_LOCATION_UPDATES;

    private LocationManager locationManager;
    private ArrayList<String> readings;

    private Handler mHandler = new Handler();

    private Runnable mPollTask = new Runnable() {
        public void run() {
            getLocation();
            mHandler.postDelayed(mPollTask, MIN_TIME_BW_UPDATES);
        }
    };



    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!(mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            && !(mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                        // code to execute if runtime permissions are not available
                        new AlertDialog.Builder(mContext).setMessage("The application does not have permission to access device location").setTitle("Runtime permission not granted").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                }
            }
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                // what to do when network is unavailable
                new AlertDialog.Builder(mContext).setMessage("Unable to fetch location").setTitle("Network unavailable").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing on clicking the ok button
                    }
                }).show();
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        readings.add(System.currentTimeMillis() + ", " + getLatitude() + ", " + getLongitude() + ", " + getSpeed() + ", " + getAccuracy() + ", " + getProvider());

        return location;
    }


    @Override
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    @Override
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    @Override
    public void onLocationChanged(Location var1) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public float getAccuracy() {
        return location.getAccuracy();
    }

    @Override
    public double getAltitude() {
        return location.getAltitude();
    }

    @Override
    public String getProvider() {
        return location.getProvider();
    }

    @Override
    public float getSpeed() {
        return location.getSpeed();
    }

    @Override
    public void stop() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this);
                writeDataToFile(readings);
                mHandler.removeCallbacks(mPollTask);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(Context context, long samplingRate, String queryNumber, String requesterID) {
        this.mContext = context;
        this.queryNo = queryNumber;
        this.requester = requesterID;
        MIN_TIME_BW_UPDATES = samplingRate;
        readings = new ArrayList<>();
        mHandler.postDelayed(mPollTask, MIN_TIME_BW_UPDATES);

    }

    public void writeDataToFile(ArrayList<String> arrayList) {
        FileOutputStream fileOut = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + Constants.FILE_BASE_PATH);
            file.mkdirs();
            File file1 = new File(file, queryNo.split("_")[0] + "/" + Constants.SENSORS.GPS.getValue().toUpperCase() + ".csv");
            fileOut = new FileOutputStream(file1);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fileOut);
            for (String string : arrayList) {
                myOutWriter.append(string + "\n");
            }
            myOutWriter.close();

            fileOut.flush();
            fileOut.close();

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}
