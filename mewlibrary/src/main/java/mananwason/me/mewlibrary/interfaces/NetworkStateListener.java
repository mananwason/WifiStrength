package mananwason.me.mewlibrary.interfaces;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;

import java.util.List;

/**
 * Created by Manan Wason on 30/11/16.
 */

public interface NetworkStateListener {

    void start(Context context, int sampleRate, String queryNumber, String requester);

    void stop();

    List<CellInfo> getAllCellInfo();

    int getCallState();

    CellLocation getCellLocation();

    int getDataActivity();

    int getDataState();

    String getDeviceId();

    String getDeviceSoftwareVersion();

    String getGroupIdLevel1();

    String getLine1Number();

    String getMmsUAProfUrl();

    String getMmsUserAgent();

    List<NeighboringCellInfo> getNeighboringCellInfo();

    String getNetworkCountryIso();

    String getNetworkOperator();

    String getNetworkOperatorName();

    int getNetworkType();

    int getPhoneCount();

    int getPhoneType();

    String getSimCountryIso();

    String getSimOperator();

    String getSimOperatorName();

    String getSimSerialNumber();

    int getSimState();

    String getSubscriberId();

    String getVoiceMailAlphaTag();

    String getVoiceMailNumber();

    boolean hasCarrierPrivileges();

    boolean hasIccCard();

    boolean isHearingAidCompatibilitySupported();

    boolean isNetworkRoaming();

    boolean isSmsCapable();

    boolean isTtyModeSupported();

    boolean isVoiceCapable();

    boolean isWorldPhone();
}
