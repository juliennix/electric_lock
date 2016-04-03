/*
* AutoOpen
*
* Version 1.0
*
* Copyright notice : ?
*/

package com.example.idp4;

import java.io.IOException;
import java.util.Calendar;

import com.example.idp4.R;

import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;


/**
* Class Open extends Activity creates a Window in which we placed a UI. This 
* UI allows the user to select a device to connect to and to ask the opening 
* of the lock.
* <p>
* Some stuffs have still to be done ! (a cancel the connection button)
*
* @version	1.0 11 Feb 2013
* @author	Sylvain Deffet
*/
public class AutoOpen extends Activity
{
	private Button bluetoothButton = null ;
	private Button cancelButton = null ;
	private TextView info = null ;
	private Button stopButton = null ;
	
	private AlarmManager manager = null ;
	private PendingIntent pending = null ;

	
	/**
	 * Initializes the activity.
	 * <p>
	 * Derived classes must call through to the super class's implementation 
	 * of this method. If they do not, an exception will be thrown.
	 *
	 * @param	savedInstanceState	If the activity is being re-initialized 
	 * 								after previously being shut down then this 
	 * 								Bundle contains the data it most recently 
	 * 								supplied in onSaveInstanceState(Bundle). 
	 * 								Note: Otherwise it is null.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_layout) ;
		
		cancelButton = (Button)findViewById(R.id.cancel) ;
		cancelButton.setOnClickListener(cancelListener) ;
		
		info = (TextView)findViewById(R.id.info) ;
		
		stopButton = (Button)findViewById(R.id.stop) ;
		stopButton.setOnClickListener(stopListener) ;

		bluetoothButton = (Button)findViewById(R.id.bluetooth) ;
		bluetoothButton.setOnClickListener(bluetoothListener) ;
	}

	
	/**
	 * Listener for the "Open with Bluetooth" button.
	 * Send the data required to open the door and analyze the Bluetooth 
	 * response.
	 */
	private OnClickListener bluetoothListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.SECOND, 6);
			
			Intent intent = new Intent(AutoOpen.this, 
					AutoOpen.AutoOpenService.class) ;
			
			pending = PendingIntent.getService(AutoOpen.this.
					getApplicationContext(), 0, intent, 0);
			
			manager = (AlarmManager)AutoOpen.this.
					getApplicationContext().
					getSystemService(Context.ALARM_SERVICE) ;
				
			manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+10000,
					20000, pending);
			
			stopButton.setVisibility(0) ;
		}
	} ;
	
	
	/**
	 * Listener for the "Close this activity" button.
	 * Close this activity
	 */
	private OnClickListener cancelListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			AutoOpen.this.finish() ;
		}
	} ;
	
	
	private OnClickListener stopListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (manager != null)
			{
			    manager.cancel(pending);
			}
			
			AutoOpen.this.finish() ;
		}
	} ;
	

	static public class AutoOpenService extends Service
	{
		private BluetoothSocket bluetoothSocket ;
		private BluetoothConnection bConnection ;
		private final String NAME = "SYLVAIN-VAIO" ;
		
		private BluetoothOperation bluetoothOperation ;
		private boolean operational = false ;
		
		
		@Override
		public IBinder onBind(Intent arg0)
		{
			return null ;
		}
		
		
		@Override
		public void onCreate()
		{
			Toast.makeText(getBaseContext(), "Tentative de connexion", Toast.LENGTH_SHORT).show();
			bluetoothSocket = null ;
			bConnection = null ;
			
			try
			{
				bConnection = new BluetoothConnection(NAME) ;
			}
			catch(BluetoothException e)
			{
				Toast.makeText(getBaseContext(), "new BluetoothConnection error", Toast.LENGTH_SHORT).show();
				stopSelf() ;
			}
			
			try
			{
				bluetoothSocket = bConnection.getBluetoothSocket() ;
				//BluetoothOpen bluetoothOpen = new BluetoothOpen() ;
				//bluetoothOpen.start() ;
				
				bluetoothOperation = new BluetoothOperation(bluetoothSocket) ;
				try
				{
					Crypto c = new Crypto(AutoOpenService.this) ;
					String s = c.getCryptedKey() ;
					
					try
					{
						bluetoothOperation.connect() ;
						bluetoothOperation.send("open"+s+"\n") ;
						
						String receivedStr = null ;
						receivedStr = bluetoothOperation.receive('\n') ;
					}
					catch(BluetoothException e)
					{
						try
						{
						bluetoothSocket.close() ;
						}
						catch(IOException e2)
						{}
						stopSelf() ;
					}
				}
				catch(KeyException keyException)
				{
					try
					{
					bluetoothSocket.close() ;
					}
					catch(IOException e2)
					{}
					stopSelf() ;
				}
			}
			catch(BluetoothException connectException)
			{
				Toast.makeText(getBaseContext(), "Socket error", Toast.LENGTH_SHORT).show();
				try
				{
				bluetoothSocket.close() ;
				}
				catch(IOException e2)
				{}
				stopSelf() ;
			}
			
			if(bluetoothSocket!=null)
			{
				try
				{
				bluetoothSocket.close() ;
				}
				catch(IOException e)
				{}
			}

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
//					setInfoText("Opérations Bluetooth impossible") ;
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
					
					
//					setInfoText("Connexion...") ;
					try
					{
						bluetoothOperation.connect() ;
					}
					catch(BluetoothException connectionException)
					{
//						setInfoText("Connexion Bluetooth impossible") ;
						return ;
					}

//					setInfoText("Envoi...") ;
					try
					{
						bluetoothOperation.send("open"+s+"\n") ;
					}
					catch(BluetoothException connectionException)
					{
//						setInfoText("Envoi Bluetooth impossible") ;
						return ;
					}
					
//					setInfoText("Réception...") ;
					String receivedStr = null ;
					try
					{
						receivedStr = bluetoothOperation.receive('\n') ;
//						if(receivedStr.equals("open"))
//							setInfoText("Serrure ouverte") ;
//						else
//							setInfoText("Ouverture refusée par la serrure : ") ;
					}
					catch(BluetoothException e)
					{
//						setInfoText("Réception Bluetooth a échoué") ;
					}
				}
				catch(KeyException e2)
				{
//					setInfoText("Problème avec le stockage interne : " + 
//							e2.toString()) ;
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
}
	