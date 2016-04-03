package com.example.idp4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BluetoothConnectionServ
{
	private BluetoothAdapter bluetoothAdapter ;
	private Set<BluetoothDevice> pairedDevices;
	
	private String[] addr ;
	private static final UUID MY_UUID = 
			UUID.fromString("00001101-0000-1000-8000-00805f9b34fb") ;

	private String mac ;
	private BluetoothDevice remoteDevice ;
	private BluetoothSocket mmSocket ;
	
	private String name ;
	
	
	/**
	 * Initializes a Bluetooth connection and add the paired devices to a 
	 * spinner (given as parameter). The spinner item selected by the user 
	 * will be the paired device to connect to.
	 * <p>
	 * If a Bluetooth connection is impossible (for reasons such as : no 
	 * Bluetooth adapter are available, the user didn't enable Bluetooth, 
	 * etc.) an exception will be thrown.
	 *
	 * @param	a		the activity in which context the Bluetooth connection 
	 * 					has to be held.
	 * @param	spin	a spinner to which this constructor will add the 
	 * 					paired devices.
	 * @throws	BluetoothException
	 */
	BluetoothConnectionServ(String name) throws BluetoothException
	{	
		this.name = name ;
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ;
		
		if (bluetoothAdapter == null)
			throw new BluetoothException
				("No bluetooth adapter has been found!") ;
		else
		{		
			if(!bluetoothAdapter.isEnabled())
			{
				throw new BluetoothException
					("Bluetooth must be enabled!") ;
			}
		}
		
		pairedDevices = bluetoothAdapter.getBondedDevices() ;
		
		
		List<String> list = new ArrayList<String>() ;

		int nbPairedDevices = pairedDevices.size() ;
		if (nbPairedDevices > 0)
		{
			addr = new String[pairedDevices.size()] ;
			
			int k = 0 ;
			for (BluetoothDevice device : pairedDevices)
			{
				addr[k] = device.getAddress() ;
				list.add(device.getName()) ;
				k++ ;
			}
		}
		else
			throw new BluetoothException("No paired devices") ;

		mac = addr[list.indexOf(name)] ;
	}
	
	
	public BluetoothSocket getBluetoothSocket() throws BluetoothException
	{
		try
		{
			remoteDevice = bluetoothAdapter.getRemoteDevice(mac);
		}
		catch (IllegalArgumentException e0)
		{
			throw new BluetoothException("Not a valid MAC address!");
		}
		
		try
		{	
			mmSocket = remoteDevice.createRfcommSocketToServiceRecord(MY_UUID);
			return mmSocket ;
		}
		catch(IOException e)
		{
			throw new BluetoothException(e.toString()) ;
		}
	}


}
