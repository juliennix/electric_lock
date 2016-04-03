package com.example.idp4;

import java.io.IOException;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class AutoOpenService extends Service
{
	private BluetoothSocket bluetoothSocket ;
	private BluetoothConnection bConnection ;
	
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null ;
	}
	
	
	@Override
	public void onCreate()
	{
		
		Toast.makeText(getBaseContext(), "Blabla", Toast.LENGTH_LONG).show();
		
		stopSelf() ;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId) ;	
	}
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy() ;
	}
	
	
	private class BluetoothOpen extends Thread
	{	
		private BluetoothOperation bluetoothOperation ;
		private boolean operational = false ;
		
		
		public BluetoothOpen()
		{
			try
			{
				bluetoothOperation = new BluetoothOperation(bluetoothSocket) ;
				operational = true ;
			}
			catch(BluetoothException e)
			{
//				setInfoText("Opérations Bluetooth impossible") ;
			}
		}
		
		
		public void run()
		{
			if(!operational)
				return ;
			
			try
			{
				Crypto c = new Crypto(AutoOpenService.this) ;
				String s = c.getCryptedKey() ;
				
				
//				setInfoText("Connexion...") ;
				try
				{
					bluetoothOperation.connect() ;
				}
				catch(BluetoothException connectionException)
				{
//					setInfoText("Connexion Bluetooth impossible") ;
					return ;
				}

//				setInfoText("Envoi...") ;
				try
				{
					bluetoothOperation.send("open"+s+"\n") ;
				}
				catch(BluetoothException connectionException)
				{
//					setInfoText("Envoi Bluetooth impossible") ;
					return ;
				}
				
//				setInfoText("Réception...") ;
				String receivedStr = null ;
				try
				{
					receivedStr = bluetoothOperation.receive('\n') ;
//					if(receivedStr.equals("open"))
//						setInfoText("Serrure ouverte") ;
//					else
//						setInfoText("Ouverture refusée par la serrure : ") ;
				}
				catch(BluetoothException e)
				{
//					setInfoText("Réception Bluetooth a échoué") ;
				}
			}
			catch(KeyException e2)
			{
//				setInfoText("Problème avec le stockage interne : " + 
//						e2.toString()) ;
			}
			
			try
			{
				bluetoothSocket.close() ;
			}
			catch(IOException closeException)
			{}
		}
	}
}
