package com.app.printer;


import java.util.Set;

import org.simple.eventbus.Subscriber;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

public class BluetoothDeviceListActivity extends BaseActivity{

	// changes the title when discovery is finished
	private BroadcastReceiver mFindBlueToothReceiver=null;
	private BluetoothAdapter mBluetoothAdapter;
	private ListView lvPairedDevice,lvNewDevice;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private TextView tvPairedDevice = null, tvNewDevice = null;
	private Button btDeviceScan = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_device_list);
		initActionBar("蓝牙设备", "打印测试", null);
		initBlueToothBroadcastReceiver();
		initViews();
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		App.printerUtil.sendTestLabel();
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tvPairedDevice = (TextView)findViewById(R.id.tvPairedDevices);
		tvNewDevice = (TextView)findViewById(R.id.tvNewDevices);
		lvPairedDevice = (ListView)findViewById(R.id.lvPairedDevices);
		lvNewDevice = (ListView)findViewById(R.id.lvNewDevices);
		
		btDeviceScan = (Button)findViewById(R.id.btBluetoothScan);
		btDeviceScan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setVisibility(View.GONE);
				discoveryDevice();
			}		
		});			
		getDeviceList();
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	private void getDeviceList() {
		// Initialize array adapters. One for already paired devices and
		// one for newly discovered devices
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.bluetooth_device_name_item);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.bluetooth_device_name_item);
				
		lvPairedDevice.setAdapter(mPairedDevicesArrayAdapter);
		lvPairedDevice.setOnItemClickListener(mDeviceClickListener);
		lvNewDevice.setAdapter(mNewDevicesArrayAdapter);
		lvNewDevice.setOnItemClickListener(mDeviceClickListener);
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mFindBlueToothReceiver, filter);
		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mFindBlueToothReceiver, filter);
		// Get the local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			tvPairedDevice.setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			String noDevices = "没有配对的设备";
			mPairedDevicesArrayAdapter.add(noDevices);
		}
	}
	
	private void discoveryDevice() {
        // Turn on sub-title for new devices
        tvNewDevice.setVisibility(View.VISIBLE);
        lvNewDevice.setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        if(mFindBlueToothReceiver != null){
        	this.unregisterReceiver(mFindBlueToothReceiver);
        }
    }
	
	private void initBlueToothBroadcastReceiver(){
		mFindBlueToothReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// If it's already paired, skip it, because it's been listed
					// already
					if (device.getBondState() != BluetoothDevice.BOND_BONDED&&!isEmpty(device.getName())) {
						mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
					}
					// When discovery is finished, change the Activity title
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					if (mNewDevicesArrayAdapter.getCount() == 0) {
						String noDevices = "没有找到蓝牙设备";
	  				    mNewDevicesArrayAdapter.add(noDevices);
					}
				}
			}
		};
	}
	
	 // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String noDevices = "没有配对的设备";
            String noNewDevice = "没有找到蓝牙设备";
            if (! info.equals(noDevices) && ! info.equals(noNewDevice)) {
				String address = info.substring(info.length() - 17);
				App.printerUtil.connectPrinter(address);
			}
        }
    };
    
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(GprinterUtil.EVENT_CONNECTING_PRINTER)){
			showProgress();
		}else if(event.equals(GprinterUtil.EVENT_CONNECT_PRINTER_RESULT)){
			closeProgress();
		}
	}
    
}
