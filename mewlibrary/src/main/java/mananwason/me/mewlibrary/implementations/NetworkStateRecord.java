package mananwason.me.mewlibrary.implementations;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import mananwason.me.mewlibrary.interfaces.NetworkStateListener;
import mananwason.me.mewlibrary.utils.Constants;


/**
 * Created by Manan Wason on 29/11/16.
 */

public class NetworkStateRecord implements NetworkStateListener {
    private TelephonyManager telephonyManager;
    private myPhoneStateListener psListener;
    private Context context;
    private ArrayList<String> readings;
    private String queryNo;
    private Handler mHandler = new Handler();
    private static int POLL_INTERVAL = 1000;
    private String requester;


    @Override
    public void start(Context context, int sampleRate, String queryNo, String requesterID) {
        this.context = context;
        this.queryNo = queryNo;
        this.requester = requesterID;
        POLL_INTERVAL = sampleRate;

        if (psListener == null || telephonyManager == null) {
            readings = new ArrayList<>();
            psListener = new myPhoneStateListener();
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);

    }

    private Runnable mPollTask = new Runnable() {
        public void run() {
            getStrength();
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    public void getStrength(){
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }


    @Override
    public void stop() {
        writeDataToFile(readings);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mHandler.removeCallbacks(mPollTask);
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public List<CellInfo> getAllCellInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return telephonyManager.getAllCellInfo();
        } else {
            return null;
        }
    }

    @Override
    public int getCallState() {
        return telephonyManager.getCallState();
    }

    @Override
    public CellLocation getCellLocation() {
        return telephonyManager.getCellLocation();
    }

    @Override
    public int getDataActivity() {
        return telephonyManager.getDataActivity();
    }

    @Override
    public int getDataState() {
        return telephonyManager.getDataState();
    }

    @Override
    public String getDeviceId() {
        return telephonyManager.getDeviceId();
    }

    @Override
    public String getDeviceSoftwareVersion() {
        return telephonyManager.getDeviceSoftwareVersion();
    }

    @Override
    public String getGroupIdLevel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return telephonyManager.getGroupIdLevel1();
        } else {
            return null;
        }
    }

    @Override
    public String getLine1Number() {
        return telephonyManager.getLine1Number();
    }

    @Override
    public String getMmsUAProfUrl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return telephonyManager.getMmsUAProfUrl();
        } else {
            return null;
        }
    }

    @Override
    public String getMmsUserAgent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return telephonyManager.getMmsUserAgent();
        } else {
            return null;
        }
    }

    @Override
    public List<NeighboringCellInfo> getNeighboringCellInfo() {
        return telephonyManager.getNeighboringCellInfo();
    }

    @Override
    public String getNetworkCountryIso() {
        return telephonyManager.getNetworkCountryIso();
    }

    @Override
    public String getNetworkOperator() {
        return telephonyManager.getNetworkOperator();
    }

    @Override
    public String getNetworkOperatorName() {
        return telephonyManager.getNetworkOperatorName();
    }

    @Override
    public int getNetworkType() {
        return telephonyManager.getNetworkType();
    }

    @Override
    public int getPhoneCount() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return telephonyManager.getPhoneCount();
        } else {
            return -1;
        }


    }

    @Override
    public int getPhoneType() {
        return telephonyManager.getPhoneType();
    }

    @Override
    public String getSimCountryIso() {
        return telephonyManager.getSimCountryIso();
    }

    @Override
    public String getSimOperator() {
        return telephonyManager.getSimOperator();
    }

    @Override
    public String getSimOperatorName() {
        return telephonyManager.getSimOperatorName();
    }

    @Override
    public String getSimSerialNumber() {
        return telephonyManager.getSimSerialNumber();
    }

    @Override
    public int getSimState() {
        return telephonyManager.getSimState();
    }

    @Override
    public String getSubscriberId() {
        return telephonyManager.getSubscriberId();
    }

    @Override
    public String getVoiceMailAlphaTag() {
        return telephonyManager.getVoiceMailAlphaTag();
    }

    @Override
    public String getVoiceMailNumber() {
        return telephonyManager.getVoiceMailNumber();
    }

    @Override
    public boolean hasCarrierPrivileges() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return telephonyManager.hasCarrierPrivileges();
        } else {
            return false;
        }

    }

    @Override
    public boolean hasIccCard() {
        return telephonyManager.hasIccCard();
    }

    @Override
    public boolean isHearingAidCompatibilitySupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return telephonyManager.isHearingAidCompatibilitySupported();
        } else {
            return false;
        }

    }

    @Override
    public boolean isNetworkRoaming() {
        return telephonyManager.isNetworkRoaming();
    }

    @Override
    public boolean isSmsCapable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return telephonyManager.isSmsCapable();
        } else {
            return false;
        }

    }

    @Override
    public boolean isTtyModeSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return telephonyManager.isTtyModeSupported();
        } else {
            return false;
        }

    }

    @Override
    public boolean isVoiceCapable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return telephonyManager.isVoiceCapable();
        } else {
            return false;
        }

    }

    @Override
    public boolean isWorldPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return telephonyManager.isWorldPhone();
        } else {
            return false;
        }

    }

    public class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }
            long time = System.currentTimeMillis();
            Log.d("STR", signalStrengthValue+"");
            readings.add(time + ", " + signalStrengthValue + "," + getCallState() + "," + getCellLocation() + "," + getDataActivity() + "," + getDataState() + ","
                    + getDeviceId() + "," + getDeviceSoftwareVersion() + "," + getGroupIdLevel1() + "," + getLine1Number() + "," + getMmsUAProfUrl() + "," + getMmsUserAgent() + ","
                    + getNeighboringCellInfo() + "," + getNetworkCountryIso() + "," + getNetworkCountryIso() + "," + getNetworkOperator() + "," + getNetworkOperatorName() + ","
                    + getNetworkType() + "," + getPhoneCount() + "," + getPhoneType() + "," + getSimCountryIso() + "," + getSimOperator() + "," + getSimOperatorName() + ","
                    + getSimSerialNumber() + "," + getSimState() + "," + getSubscriberId() + "," + getVoiceMailAlphaTag() + "," + getVoiceMailNumber() + ","
                    + hasCarrierPrivileges() + "," + hasIccCard() + "," + isHearingAidCompatibilitySupported() + "," + isNetworkRoaming() + "," + isSmsCapable() + ","
                    + isTtyModeSupported() + "," + isVoiceCapable() + "," + isWorldPhone());
        }
    }

    public void writeDataToFile(ArrayList<String> arrayList) {
        FileOutputStream fileOut = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + Constants.FILE_BASE_PATH);
            file.mkdirs();
            File file1 = new File(file, queryNo.split("_")[0] + "/" + Constants.SENSORS.NETWORK.getValue().toUpperCase() + ".csv");
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
