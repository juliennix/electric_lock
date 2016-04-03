package com.example.idp4;

public class BluetoothException extends Exception
{
	public String attrName ;
	
	BluetoothException(String name)
	{
		super(name) ;
		attrName = name ;
	}
}
