/*
* Crypto
*
* Version 1.1
*
* Copyright notice : ?
*/


package com.example.idp4;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;

/**
* Description : To do !
*
* @version	1.1 08 Feb 2013
* @author	Sylvain Deffet
*/
public class Crypto
{
	private String FILENAME = "key_file" ;
	private Context context ;
	private final static String HEX = "0123456789ABCDEF";
	
	/**
	 * Initializes a file stored in internal storage if it is the first time 
	 * this class is instantiated.
	 * <p>
	 * Throws an exception if internal storage access fails.
	 *
	 * @param	c	The context of the application
	 * @throws	KeyException
	 */
	Crypto(Context context) throws KeyException
	{
		this.context = context ;
		
		String defaultKey = "123456789";
		String[] filenames = context.fileList() ;
		
		if(Arrays.asList(filenames).contains(FILENAME))
			return ;
		
		try
		{
			setKey(defaultKey) ;
		}
		catch(KeyException e)
		{
			throw new KeyException(e.toString()) ;
		}
	}
	
	/**
	 * This method returns the key stored in the internal storage or throws an 
	 * exception if an error occurs.
	 *
	 * @return	The key stored in the internal storage
	 * @throws	KeyException
	 */
	private String getKey() throws KeyException
	{
		int ch ;
		StringBuffer fileContent = new StringBuffer("");

		try
		{
			FileInputStream fis = context.openFileInput(FILENAME) ;
			if(fis==null)
				throw new KeyException("FileInputStream = null") ;
			while( (ch = fis.read()) != -1)
				fileContent.append((char)ch) ;
			
			fis.close() ;
			
			return new String(fileContent) ;
		}
		catch(Exception e2)
		{
			throw new KeyException(e2.toString()) ;
		}
	}
	
	/**
	 * This method write (or overwrite) the key in the internal storage.
	 *
	 * @return	The key to be stored in the internal storage
	 * @throws	KeyException
	 */
	private void setKey(String key) throws KeyException
	{
		try
		{
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			if(fos==null)
				throw new KeyException("FileOutputStream = null") ;
			fos.write(key.getBytes());
			fos.close() ;
		}
		catch(IOException e)
		{
			throw new KeyException(e.toString()) ;
		}
	}
	
	/**
	 * This method replace the key stored in the internal storage by a new one.
	 *
	 * @param	formerKey	The former key
	 * @param	newKey		The new key
	 * @return	false if the "formerKey" isn't the same as the key that was 
	 * 			stored in the internal storage, true otherwise.
	 * @throws	KeyException
	 */
	public boolean changeKey(String formerKey, String newKey) throws KeyException
	{
		String currentKey = getKey() ;
		
		if(currentKey.equals(formerKey))
		{
			try
			{
				setKey(newKey) ;
				return true ;
			}
			catch(KeyException e)
			{
				throw new KeyException(e.toString()) ;
			}
		}

		else
			return false ;
	}

	/**
	 * For debugging purpose. Not explained.
	 */
	public String debug() throws KeyException
	{
		int ch ;
		StringBuffer fileContent = new StringBuffer("");
		try
		{
			FileInputStream fis = context.openFileInput(FILENAME) ;
			if(fis==null)
				throw new KeyException("FileInputStream = null") ;	
			
			while( (ch = fis.read()) != -1)
				fileContent.append((char)ch) ;
			
			fis.close() ;
			
			return new String(fileContent) ;
		}
		catch(Exception e2)
		{
			throw new KeyException(e2.toString()) ;
		}
	}
	
	public String getCryptedKey() throws KeyException
	{
		String key = "Charles Dickens.";
		String message = "It was the best ";
		byte [] byteKey = key.getBytes();
		byte [] byteMessage = message.getBytes();
		try {
			byte [] result ;
			result = Crypto.encrypt(byteKey, byteMessage);
			return toHex(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDecryptedKey() throws KeyException
	{
		String key = "Charles Dickens.";
		String cipher = "3FD869084483504CA70E246064DD76CA";
		byte [] byteKey = key.getBytes();
		byte [] byteCipher = fromHex (cipher);
		try {
			byte [] result ;
			result = Crypto.decrypt(byteKey, byteCipher);
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
    protected static String toHex( byte[] value )
    {
        if ( value == null )
        {
            return "";
        }
        StringBuffer result = new StringBuffer( 2 * value.length );
        for ( int i = 0; i < value.length; i++ )
        {
            byte b = value[i];

            result.append( HEX.charAt( ( b >> 4 ) & 0x0f ) );
            result.append( HEX.charAt( b & 0x0f ) );
        }
        return result.toString();
    }
    
    protected static byte[] fromHex( String value )
    {
        int len = value.length() / 2;
        byte[] result = new byte[len];
        for ( int i = 0; i < len; i++ )
        {
            result[i] = Integer.valueOf( value.substring( 2 * i, 2 * i + 2 ), 16 ).byteValue();
        }
        return result;
    }

    
    private static byte[] encrypt(byte[] key, byte[] message) throws Exception 
    {
    	SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	    Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	    byte[] encrypted = cipher.doFinal(message);
	    return encrypted;
	}


	private static byte[] decrypt(byte[] key, byte[] encryptedMessage) throws Exception
	{
	    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	    Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    byte[] decrypted = cipher.doFinal(encryptedMessage);
	    return decrypted;
	}
}