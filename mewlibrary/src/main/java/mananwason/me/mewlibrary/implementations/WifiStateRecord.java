package mananwason.me.mewlibrary.implementations;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import mananwason.me.mewlibrary.interfaces.WifiStateListener;
import mananwason.me.mewlibrary.utils.Constants;


/**
 * Created by Manan Wason on 01/12/16.
 */

public class WifiStateRecord implements WifiStateListener {
    private WifiManager wifiManager;
    private ArrayList<String> recordedValues;

    private static int POLL_INTERVAL = 1000;
    private static final String TAG = "WifiRecord";

    private boolean mRunning = false;

    private Handler mHandler = new Handler();

    private String queryNo = "";
    private String requester;
    private Context context;
    public OutputStream out;


    @Override
    public void start(Context context, int samplingRate, String queryNumber, String requesterID, OutputStream outputStream) {
        this.queryNo = queryNumber;
        this.context = context;
        this.requester = requesterID;
        POLL_INTERVAL = samplingRate;
        this.out = outputStream;
        if (wifiManager == null) {
            recordedValues = new ArrayList<>();
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);

    }

    private Runnable mPollTask = new Runnable() {
        public void run() {
            int strength = getStrength();
            try {
                out.write(strength);
            } catch (IOException e) {
                e.printStackTrace();
            }
            recordedValues.add(System.currentTimeMillis() + ", " + strength + ", " + wifiManager.isWifiEnabled());
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };


    @Override
    public List<WifiConfiguration> getConfiguredNetworks() {
        return wifiManager.getConfiguredNetworks();
    }

    @Override
    public WifiInfo getConnectionInfo() {
        return wifiManager.getConnectionInfo();
    }

    @Override
    public DhcpInfo getDhcpInfo() {
        return wifiManager.getDhcpInfo();
    }

    @Override
    public List<ScanResult> getScanResults() {
        return wifiManager.getScanResults();
    }

    @Override
    public boolean is5GHzBandSupported() {
        return is5GHzBandSupported();
    }

    @Override
    public boolean isDeviceToApRttSupported() {
        return isDeviceToApRttSupported();
    }

    @Override
    public boolean isEnhancedPowerReportingSupported() {
        return isEnhancedPowerReportingSupported();
    }

    @Override
    public boolean isP2pSupported() {
        return isP2pSupported();
    }

    @Override
    public boolean isPreferredNetworkOffloadSupported() {
        return isPreferredNetworkOffloadSupported();
    }

    @Override
    public boolean isScanAlwaysAvailable() {
        return isScanAlwaysAvailable();
    }

    @Override
    public boolean isTdlsSupported() {
        return isTdlsSupported();
    }

    @Override
    public boolean isWifiEnabled() {
        return isWifiEnabled();
    }


    public int getStrength() {
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
        } else {
            return 0;
        }

    }

    @Override
    public void stop() {
        writeDataToFile(recordedValues);
        mHandler.removeCallbacks(mPollTask);
        mRunning = false;

    }

    public void writeDataToFile(ArrayList<String> arrayList) {
        FileOutputStream fileOut = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + Constants.FILE_BASE_PATH);
            file.mkdirs();
            File file1 = new File(file, queryNo.split("_")[0] + "/" + Constants.SENSORS.WIFI.getValue().toUpperCase() + ".csv");
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
