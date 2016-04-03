/*
* BluetoothConnection
*
* Version 1.0
*
* Copyright notice : ?
*/

package com.example.idp4;

import android.bluetooth.BluetoothSocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;


/**
*
* @version	1.0 10 Feb 2013
* @author	Sylvain Deffet
*/
public class BluetoothOperation
{
	BluetoothSocket mmSocket ;
	private BufferedOutputStream mmOutStream ;
	private BufferedInputStream mmInStream ;
	
	public BluetoothOperation(BluetoothSocket mmSocket) throws BluetoothException
	{
		this.mmSocket = mmSocket ;
		
		try
		{	
			mmOutStream = new BufferedOutputStream(mmSocket.getOutputStream());
			mmInStream = new BufferedInputStream(mmSocket.getInputStream(), 
					19200);
		}
		catch(IOException e)
		{
			throw new BluetoothException(e.toString()) ;
		}
	}
	
	
	/**
	 * Connect to the selected (through the spinner) paired device.
	 * <p>
	 * If, for some reason, the connection becomes impossible, an exception 
	 * will be thrown.
	 *
	 * @throws	BluetoothException
	 */
	public void connect() throws BluetoothException
	{
		try
		{	
			/*boolean cd = bluetoothAdapter.cancelDiscovery() ;
			if(cd==false)
				throw new BluetoothException("Unable to desable discovery process") ;*/
			
			// A new thread is needed to abort Bluetooth connection
			
			mmSocket.connect() ;
		}
		catch(IOException e)
		{
			try
            {
                mmSocket.close();
            }
            catch (IOException closeException)
            { 
            	throw new BluetoothException(e.toString() + closeException.toString()) ;
            }
			
			throw new BluetoothException(e.toString()) ;
		}
	}

	
	/**
	 * Send the data given as parameter to the paired device. This method is 
	 * blocking (the instance of the class will wait until the data has be 
	 * sent).
	 *
	 * @param	The data to send
	 * @throws	BluetoothException
	 */
	public void send(String str) throws BluetoothException
	{
		try
		{
			byte[] count2send = str.getBytes("US-ASCII");
			mmOutStream.write(count2send);
			mmOutStream.flush();
		}
		catch(IOException e)
		{
			throw new BluetoothException(e.toString()) ;
		}
	}
	
	
	/**
	 * Receives some data from the paired device and returns them as a String.
	 * This method is blocking (the instance of the class will wait until some 
	 * data have been received).
	 *
	 * @return	The received data
	 * @throws	BluetoothException
	 */
	public String receive(char limit) throws BluetoothException
	{
		byte[] buffer = new byte[19200] ;
		
		try
		{			
			int offset = 0;
			mmInStream.read(buffer, offset, 1);
			while (buffer[offset]!=limit)
			{
				offset++;
				mmInStream.read(buffer, offset, 1);
			}
				
			int count = offset;
			byte[] msg = new byte[count];
			System.arraycopy(buffer, 0, msg, 0, count);
			return new String(msg, "ASCII");
		}
		catch(IOException e)
		{
			throw new BluetoothException(e.toString()) ;
		}
	}
}
