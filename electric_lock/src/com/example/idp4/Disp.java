/*
* Disp
*
* Version 1.1
*
* Copyright notice : ?
*/

package com.example.idp4;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

/**
* Class Disp extends Activity creates a Window in which we placed a UI. This 
* UI allows the user to display the key (after a password has been asked).
* <p>
* Some stuffs have still to be done ! (the password)
*
* @version	1.1 08 Feb 2013
* @author	Sylvain Deffet
*/
public class Disp extends Activity
{	
	TextView dispKey = null ;
	Button cancel = null ;
	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disp_layout) ;
		
		cancel = (Button)findViewById(R.id.cancel) ;
		cancel.setOnClickListener(cancelListener) ;
		
		dispKey = (TextView)findViewById(R.id.dispKey) ;
		
		try
		{
			Crypto c = new Crypto(getApplicationContext()) ;
			dispKey.setText(c.getDecryptedKey()) ; // For now
		}
		catch(Exception e)
		{
			dispKey.setText("Error : " + e.toString()) ;
		}	
	}
	
	/**
	 * Listener for the "Close this activity" button.
	 * Close this activity
	 */
	private OnClickListener cancelListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Disp.this.finish() ;
		}
	} ;
}
