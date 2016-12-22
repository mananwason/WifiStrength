package mananwason.me.mewlibrary.implementations;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;

import mananwason.me.mewlibrary.interfaces.BluetoothListener;
import mananwason.me.mewlibrary.utils.Constants;


/**
 * Created by garvitab on 06-12-2016.
 */
public class BluetoothRecord implements BluetoothListener {

    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private String queryNo;
    private ArrayList<String> recordedValues;
    private static int POLL_INTERVAL = 1000;
    private Handler mHandler = new Handler();
    private BroadcastReceiver mReceiver;
    private IntentFilter filter;
    private String requester;

    @Override
    public void start(Context context, int sampleRate, String queryNumber, String requesterId) {
        this.mContext = context;
        this.queryNo = queryNumber;
        this.requester = requesterId;
        recordedValues = new ArrayList<>();
        POLL_INTERVAL = sampleRate;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(enableBtIntent, 0);
                } else {
                    Log.d("TT", "Unable to use startActivityForResult");
                }
            }
        }
        filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //bluetooth device found
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //everytime a device is found, write its details to recordedValues
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        recordedValues.add(System.currentTimeMillis() + ", " + device.getName().toString() + ", " + device.getType() + ", " + device.getBondState() + ", " + device.getAddress() + ", " + device.getBluetoothClass());
                    } else {
                        recordedValues.add(System.currentTimeMillis() + ", " + device.getName().toString() + ", " + " null " + ", " + device.getBondState() + ", " + device.getAddress() + ", " + device.getBluetoothClass());
                    }
                }
            }
        };

        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }


    private Runnable mPollTask = new Runnable() {
        public void run() {
            //execute the code to scan bluetooth devices in vicinity
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    @Override
    public void stop() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
            mHandler.removeCallbacks(mPollTask);
            //TODO : Fix Error : Receiver not registered
            mContext.unregisterReceiver(mReceiver);
        }
        writeDataToFile(recordedValues);
    }

    @Override
    public void getPairedDevices() {
        Set<BluetoothDevice> paired = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice d : paired) {
            recordedValues.add(System.currentTimeMillis() + ", " + d.getName().toString() + ", " + d.getType() + ", " + d.getBondState() + ", " + d.getAddress() + ", " + d.getBluetoothClass());

        }
    }

    @Override
    public void getDevicesInVicinity() {
        mContext.registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    public void writeDataToFile(ArrayList<String> arrayList) {
        FileOutputStream fileOut = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + Constants.FILE_BASE_PATH);
            file.mkdirs();
            File file1 = new File(file, queryNo.split("_")[0] + "/" + Constants.SENSORS.BLUETOOTH.getValue().toUpperCase() + ".csv");
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
