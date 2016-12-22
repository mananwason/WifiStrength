package mananwason.me.mewlibrary.interfaces;

import android.content.Context;

/**
 * Created by garvitab on 06-12-2016.
 */
public interface BluetoothListener {

	void start(Context context, int sampleRate, String queryNumber, String requester);

	void stop();

	void getPairedDevices();

	void getDevicesInVicinity();
}
