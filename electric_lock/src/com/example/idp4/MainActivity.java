/*
* MainActivity
*
* Version 1.0
*
* Copyright notice : ?
*/

package com.example.idp4;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
* Class MainActivity extends Activity creates a Window in which we placed our
* UI. This UI allows the user to select the main things that he can do with
* our application (Open, Display the key, Change the Key).
*
* @version	1.0 08 Feb 2013
* @author	Sylvain Deffet
*/
public class MainActivity extends Activity
{
	private Button open = null ;
	private Button disp = null ;
	private Button modif = null ;
	private Button auto = null ;

	
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
		setContentView(R.layout.activity_main) ;
		
		/* Get the view by id and set the corresponding listener for all the 
		   buttons */
		open = (Button)findViewById(R.id.open) ;
		open.setOnClickListener(openListener) ;
		
		disp = (Button)findViewById(R.id.disp) ;
		disp.setOnClickListener(dispListener) ;
		
		modif = (Button)findViewById(R.id.modif) ;
		modif.setOnClickListener(modifListener) ;
		
		auto = (Button)findViewById(R.id.auto) ;
		auto.setOnClickListener(autoListener) ;
	}

	
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		/* Inflate the menu; this adds items to the action bar if it is
		   present. */
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	/** Listener for the "Open" button. */
	private OnClickListener openListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// Start Open activity
			Intent intent = new Intent(MainActivity.this, Open.class);
			MainActivity.this.startActivity(intent);
		}
	} ;
	
	
	/** Listener for the "Display key" button. */
	private OnClickListener dispListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// Start Disp activity
			Intent intent = new Intent(MainActivity.this, Disp.class);
			MainActivity.this.startActivity(intent);
		}
	} ;
	
	
	/** Listener for the "Modify key" button. */
	private OnClickListener modifListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// Start Modif activity
			Intent intent = new Intent(MainActivity.this, Modif.class);
			MainActivity.this.startActivity(intent);
		}
	} ;
	
	
	/** Listener for the "Auto connect" button. */
	private OnClickListener autoListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// Start AutoOpen activity
			Intent intent = new Intent(MainActivity.this, AutoOpen.class);
			MainActivity.this.startActivity(intent);
		}
	} ;
}
