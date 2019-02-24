package com.netikalyan.usbhostmanager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "UsbHostDeviceMgr";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private TextView txtDeviceName, txtDeviceProps;
    private PendingIntent permissionIntent;
    private UsbDevice usbDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
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
            StringBuilder buffer = new StringBuilder();
            buffer.append("Device Name = ");
            buffer.append(usbDevice.getDeviceName());
            buffer.append("\nVendor ID = ");
            buffer.append(usbDevice.getVendorId());
            buffer.append("\nProduct ID = ");
            buffer.append(usbDevice.getProductId());
            buffer.append("\nManufacturer Name = ");
            buffer.append(usbDevice.getManufacturerName());
            buffer.append("\nProduct Name = ");
            buffer.append(usbDevice.getProductName());
            buffer.append("\nVersion = ");
            buffer.append(usbDevice.getVersion());
            buffer.append("\nSerial No = ");
            buffer.append(usbDevice.getSerialNumber());
            for (int idx = 0; idx < usbDevice.getConfigurationCount(); idx++) {
                UsbConfiguration configuration = usbDevice.getConfiguration(idx);
                buffer.append("\n\nConfiguration ID = ");
                buffer.append(configuration.getId());
                buffer.append("\nSelf-powered = ");
                buffer.append(configuration.isSelfPowered());
                buffer.append("\nMax power consumption (millamps) = ");
                buffer.append(configuration.getMaxPower());
                for (int cnt = 0; cnt < usbDevice.getConfigurationCount(); cnt++) {
                    UsbInterface usbinterface = configuration.getInterface(cnt);
                    buffer.append("\nInterface Class = ");
                    buffer.append(getClassName(usbinterface.getInterfaceClass()));
                    buffer.append("\nInterface Subclass = ");
                    buffer.append(usbinterface.getInterfaceSubclass());
                    buffer.append("\nInterface Protocol = ");
                    buffer.append(usbinterface.getInterfaceProtocol());
                    for (int endPts = 0; endPts < usbinterface.getEndpointCount(); endPts++) {
                        UsbEndpoint endpoint = usbinterface.getEndpoint(endPts);
                        buffer.append("\nEndPoint No = ");
                        buffer.append(endpoint.getEndpointNumber());
                        buffer.append("\nEndPoint Direction = ");
                        buffer.append(endpoint.getDirection() == UsbConstants.USB_DIR_OUT ? "Out" : "In");
                        buffer.append("\nEndPoint Max Packet Size = ");
                        buffer.append(endpoint.getMaxPacketSize());
                        buffer.append("\nEndPoint Type = ");
                        buffer.append(getTypeName(endpoint.getType()));
                    }
                }
            }
            txtDeviceProps.setText(buffer.toString());
        }
    }

    private String getTypeName(int typeid) {
        String name = null;
        switch (typeid) {
            case UsbConstants.USB_ENDPOINT_XFER_BULK:
                name = "Bulk endpoint";
                break;
            case UsbConstants.USB_ENDPOINT_XFER_CONTROL:
                name = "Control endpoint";
                break;
            case UsbConstants.USB_ENDPOINT_XFER_INT:
                name = "Interrupt endpoint";
                break;
        }
        return name;
    }

    private String getClassName(int classid) {
        String name = null;
        switch (classid) {
            case UsbConstants.USB_CLASS_AUDIO:
                name = "Audio Device";
                break;
            case UsbConstants.USB_CLASS_COMM:
                name = "Communication Device";
                break;
            case UsbConstants.USB_CLASS_HID:
                name = "Human Interface Device (mouse, keyboard...)";
                break;
            case UsbConstants.USB_CLASS_PHYSICA:
                name = "Physical Device";
                break;
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                name = "Still Image Device (digital camera)";
                break;
            case UsbConstants.USB_CLASS_PRINTER:
                name = "Printer Device";
                break;
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                name = "Mass Storage Device";
                break;
            case UsbConstants.USB_CLASS_HUB:
                name = "Usb Hub Device";
                break;
            case UsbConstants.USB_CLASS_CDC_DATA:
                name = "Communications Device";
                break;
            case UsbConstants.USB_CLASS_CSCID:
                name = "Smart Card Device";
                break;
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                name = "Security Device";
                break;
            case UsbConstants.USB_CLASS_VIDEO:
                name = "Video Device";
                break;
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                name = "Wireless Controller Device";
                break;
            case UsbConstants.USB_CLASS_MISC:
                name = "Wireless Misc Device";
                break;
            case UsbConstants.USB_CLASS_APP_SPEC:
                name = "Application specific";
                break;
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                name = "Vendor specific";
                break;
        }
        return name;
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
                        txtDeviceName.setText(String.format(getString(R.string.perms_denied), usbDevice.getDeviceName()));
                        Log.d(TAG, "permission denied for device " + usbDevice.getDeviceName());
                        usbDevice = null;
                    }
                }
            }
        }
    };
}
