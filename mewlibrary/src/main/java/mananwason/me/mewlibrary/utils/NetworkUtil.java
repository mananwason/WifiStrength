package mananwason.me.mewlibrary.utils;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class NetworkUtil {

	public static int speedInKbps=0;
	public static final String TAG = "NETUTIL";
	PowerManager.WakeLock wakeLock;
	public static Context mContext;
	private static BroadcastReceiver mConnectionReceiver;
	private volatile static CountDownLatch latch;

	private PowerManager.WakeLock mWakeLock;

	public int getSpeed(Context context)
	{
//		int conn=NetworkUtil.getConnectivityStatus(context);
		mContext = context;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
			{
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				if (wifiInfo != null) {
					speedInKbps = wifiInfo.getLinkSpeed()*1024; //measured in WifiInfo.LINK_SPEED_UNITS
					Log.d(TAG, "Connected to WiFi, speed: "+speedInKbps);
					if(speedInKbps == -1024){
						Log.d(TAG, "Link Speed Negative lets wake up the screen");
						KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
						final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
						kl.disableKeyguard();

						PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
						PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
																				| PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
						wakeLock.acquire();
						String log = "DEBUG,";
						log+=wifiInfo.getIpAddress() +","+ wifiInfo.getRssi() +","+wifiInfo.getSSID()+","+ wifiManager.pingSupplicant() +"\n";
						Log.d(TAG, log);
						Log.d(TAG, "Wake Wifi Up");
						wakeWifiUp();

						boolean urlTest = isURLReachable();
						wifiInfo = wifiManager.getConnectionInfo();
						speedInKbps = wifiInfo.getLinkSpeed()*1024;
						Log.d(TAG, "URL Test = " + urlTest);
						if(speedInKbps == -1024){
							Log.d(TAG, "Now I don't know");
						}

					}
					else{
						if(wakeLock!=null){
						wakeLock.release();}
					}
				}
			}

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			{
				int subType=activeNetwork.getSubtype();
				switch(subType){
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					speedInKbps= 75; // ~ 50-100 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_CDMA:
					speedInKbps= 39; // ~ 14-64 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_EDGE:
					speedInKbps= 75; // ~ 50-100 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					speedInKbps= 700; // ~ 400-1000 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					speedInKbps= 1000; // ~ 600-1400 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					speedInKbps= 100; // ~ 100 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					speedInKbps= 8*1024; // ~ 2-14 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_HSPA:
					speedInKbps= 1200; // ~ 700-1700 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					speedInKbps= 12*1024; // ~ 1-23 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
					speedInKbps= 3700; // ~ 400-7000 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
					speedInKbps= (int) (1.25*1024); // ~ 1-2 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
					speedInKbps= 5*1024; // ~ 5 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
					speedInKbps= 15*1024; // ~ 10-20 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
					speedInKbps= 25; // ~25 kbps 
					break;
				case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
					speedInKbps= 10*1024; // ~ 10+ Mbps
					break;
					// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					speedInKbps= 0;
					break;
				}
				Log.d(Constants.TAG,"Connected to mobile data, speed: "+speedInKbps);
			}
				
		}
		Log.d(Constants.TAG,"Network speed: "+speedInKbps+" kbps");
		return speedInKbps;		
	}


	public boolean wakeWifiUp() {
		ConnectivityManager _androidConnectivityMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = _androidConnectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		WifiManager _wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
		final int wifiState = _wifiManager.getWifiState();

		if (!_wifiManager.isWifiEnabled()
					|| wifiState == WifiManager.WIFI_STATE_DISABLED
					|| wifiState == WifiManager.WIFI_STATE_DISABLING) {

			Log.d(TAG, "!_wifiManager.isWifiEnabled()");
			_wifiManager.setWifiEnabled(true);
			// do not enable if not enabled ! TODO
			return false;
		}
		if (!wifiInfo.isConnected()) {

			Log.d(TAG, "Wifi is NOT Connected Or Connecting - "
						+ "wake it up and wait till is up");
			// Do not wait for the OS to initiate a reconnect to a Wi-Fi router

			_wifiManager.pingSupplicant();
			if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
				try {
					// Brute force methods required for some devices
					_wifiManager.setWifiEnabled(false);
					_wifiManager.setWifiEnabled(true);
				} catch (SecurityException e) {
					// Catching exception which should not occur on most
					// devices. OS bug details at :
					// https://code.google.com/p/android/issues/detail?id=22036
				}
			}
			_wifiManager.disconnect();
			_wifiManager.startScan();
			_wifiManager.reassociate();
			_wifiManager.reconnect();
			// THIS IS WHAT I DO TO WAIT FOR A CONNECTION
			try {
				mConnectionReceiver = new WifiConnectionMonitor();
				startMonitoringConnection();
				latch = new CountDownLatch(1);
				Log.d(TAG, "Let's  wait");
				latch.await();
				Log.d(TAG, "Woke up.. Victory is mine !!!");
				return true; // made it
			} catch (InterruptedException e) {
				Log.d(TAG, "Interrupted while waiting for connection");
				return false;
			} finally {
				stopMonitoringConnection();
			}
		}
		if(wifiInfo.isConnected()){
			Log.d("WIFI", "Still connected to Wifi, network speed: "+this.getSpeed(mContext));
			Log.d("WIFI", "trying to reach google.com "+isURLReachable());
		}
		return true;
	}

	static public boolean isURLReachable() {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL url = new URL("http://google.com");   // Change to "http://google.com" for www  test.
				HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
				urlc.setConnectTimeout(10 * 1000);          // 10 s.
				urlc.connect();
				if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
					Log.d("Connection", "Success !");
					return true;
				} else {
					return false;
				}
			} catch (MalformedURLException e1) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}
	static void downTheLatch() {
		latch.countDown();
	}


	private static final class WifiConnectionMonitor extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent in) {
			String action = in.getAction();
			if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo networkInfo = in.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				Log.d(TAG, networkInfo + "");
				if (networkInfo.isConnected()) {
					Log.d(TAG, "Wifi is connected!");
					NetworkUtil.downTheLatch(); // HERE THE SERVICE IS WOKEN!
				}
			}
		}
	}

	private static synchronized void stopMonitoringConnection() {
		mContext.unregisterReceiver(mConnectionReceiver);
	}

	private static synchronized void startMonitoringConnection() {
		IntentFilter aFilter = new IntentFilter(
													   ConnectivityManager.CONNECTIVITY_ACTION);
		aFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mContext.registerReceiver(mConnectionReceiver, aFilter);
	}



}