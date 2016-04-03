package com.example.idp4;

public class KeyException extends Exception
{
	public String attrName ;
	
	KeyException(String name)
	{
		super("Key exception due to: " + name) ;
		attrName = name ;
	}
}
