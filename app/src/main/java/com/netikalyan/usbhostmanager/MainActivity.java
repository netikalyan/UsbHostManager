package com.netikalyan.usbhostmanager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "UsbHostDeviceMgr";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private Button btnEnumerate, btnProperties;
    private TextView txtDeviceName, txtDeviceProps;
    private PendingIntent permissionIntent;
    private UsbDevice usbDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        btnEnumerate = findViewById(R.id.btnEnumerate);
        btnProperties = findViewById(R.id.btnProperties);
        txtDeviceName = findViewById(R.id.textDeviceName);
        txtDeviceProps = findViewById(R.id.textProperties);
        permissionIntent =
                PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        btnEnumerate = null;
        btnProperties = null;
        txtDeviceName = null;
        txtDeviceProps = null;
    }

    public void enumerateDevices(View view) {
        UsbManager manager = (UsbManager) getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        if (deviceList.size() == 0) {
            txtDeviceName.setText(getString(R.string.no_devices));
        } else {
            for (UsbDevice device : deviceList.values()) {
                manager.requestPermission(device, permissionIntent);
            }
        }
    }

    public void listProperties(View view) {
        if (null != usbDevice) {
            txtDeviceProps.setText(usbDevice.toString());
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        txtDeviceName.setText(usbDevice.getDeviceName());
                    } else {
                        txtDeviceName.setText(getString(R.string.perms_denied) + usbDevice.getDeviceName());
                        Log.d(TAG, "permission denied for device " + usbDevice.getDeviceName());
                        usbDevice = null;
                    }
                }
            }
        }
    };
}
