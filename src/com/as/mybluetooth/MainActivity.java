package com.as.mybluetooth;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button On, Off, Visible, list;
	private ListView lv;
	private BluetoothAdapter BA;
	private Set<BluetoothDevice> pairedDevices;
	ArrayAdapter adapter;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		On = (Button) findViewById(R.id.button1);
		Visible = (Button) findViewById(R.id.button2);
		list = (Button) findViewById(R.id.button3);
		Off = (Button) findViewById(R.id.button4);
		lv = (ListView) findViewById(R.id.listView1);
		
		BA = BluetoothAdapter.getDefaultAdapter();
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		lv.setAdapter(adapter);
	}

	public void on(View view) {
		if(!BA.isEnabled()) {
			Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOn, 0);
			Toast.makeText(this, "Turned On", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this, "Already Turned On", Toast.LENGTH_LONG).show();
		}
	}	
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            adapter.add(device.getName() + "\n" + device.getAddress());
	            adapter.notifyDataSetChanged();
	        }
	    }
	};
	
	public void findDevices(View view) {
		if (BA.isDiscovering()) {
			BA.cancelDiscovery();
		}
		else {
			adapter.clear();
			BA.startDiscovery();
			
			registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	public void visible(View view){
		Intent vsble = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(vsble, 0);
	}
	
	public void list(View view) {
		pairedDevices = BA.getBondedDevices();

		for(BluetoothDevice bt: pairedDevices)
			adapter.add(bt.getName()+"\n"+bt.getAddress());
	}
	
	public void off(View view) {
		BA.disable();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
