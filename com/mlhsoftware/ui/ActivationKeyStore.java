/*
 * ActivationScreen.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.SimplySolitaire;


import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.notification.NotificationsManager;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.ControlledAccessException;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.crypto.MD5Digest;
import net.rim.device.api.system.*;
import net.rim.device.api.util.*;

import net.rim.device.api.ui.MenuItem;

import java.util.*;

import net.rim.blackberry.api.phone.phonelogs.PhoneCallLogID;
import net.rim.blackberry.api.phone.phonelogs.PhoneCallLog;
import net.rim.blackberry.api.phone.phonelogs.PhoneCallLogID;
import net.rim.blackberry.api.phone.phonelogs.PhoneLogs;

import net.rim.device.api.system.EventLogger;
import net.rim.blackberry.api.browser.Browser;







public class ActivationKeyStore implements Persistable
{
  //Persistent object wrapping the effective properties instance
  private static PersistentObject Store;
  private static ActivationKeyStore Instance = null;
  
  public static ActivationKeyStore GetInstance()
  {
    if ( Instance == null )
    {
      try
      {
        Store = PersistentStore.getPersistentObject( AppInfo.PERSISTENCE_ID );
        synchronized ( Store )
        {
          if ( Store.getContents() == null )
          {
            Store.setContents( new ActivationKeyStore() );
            Store.commit();
          }
        }
        Instance = (ActivationKeyStore)Store.getContents();
      }
      catch ( Exception e )
      {
        String msg = "Persistent Store (" + AppInfo.PERSISTENCE_ID + "): " + e.toString();
        EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
      }
    }
    //String msg = "Activation Key fetch";
    //EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.DEBUG_INFO );

    return Instance;
  }

  public static void reset()
  {
    try
    {
      Store = PersistentStore.getPersistentObject( AppInfo.PERSISTENCE_ID );
      synchronized ( Store )
      {
        Store.setContents( null );
        Store.commit();
      }
      Instance = null;
    }
    catch ( Exception e )
    {
      String msg = "reset Persistent Store (" + AppInfo.PERSISTENCE_ID + "): " + e.toString();
      EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
    }
  }

  public static String getBuyUrl()
  {
    String url = "https://www.mobihand.com/mobilecart/mc1.asp?posid=234&tracking1=bb&pid=" + AppInfo.MOBIHAND_PID + "&did=" + getPin();
    if ( AppInfo.APPWORLD )
    {
      url = "http://appworld.blackberry.com/webstore/content/" + AppInfo.APPWORLD_PID;
    }
    return url;
  }

  private String _key;
  private int _trialCnt;

  private ActivationKeyStore()
  {
    _key = readKeyFromAppWorld();
    _trialCnt = 0;
  }

  //Cannonical copy constructor.
  private ActivationKeyStore( ActivationKeyStore other )
  {
    _key = other._key;
    _trialCnt = other._trialCnt;
  }

  //Causes the values within this instance to become the effective
  //properties for the application by saving this instance to the store.
  public void save()
  {
    try
    {
      Store.setContents( this );
      Store.commit();
    }
    catch ( Exception e )
    {
      String msg = "Activation Key save failed: " + e.toString();
      EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
    }
  }

  public String getKey()
  {
    return _key;
  }

  public void setKey( String val )
  {
    _key = val;
  }

  public int getTrialCnt()
  {
    return _trialCnt;
  }

  public void setTrialCnt( int val )
  {
    _trialCnt = val;
  }


  void clearKey()
  {
    setKey( "" );
    save();
  }

  public static boolean isKeyValid()
  {
    boolean retVal = false;
    try
    {
      ActivationKeyStore keyStore = ActivationKeyStore.GetInstance();
      if ( keyStore != null )
      {
        int deviceId = DeviceInfo.getDeviceId();
        String deviceIdText = getPin() + AppInfo.SECRET_KEY;
        String key = keyStore.getKey();
        if ( key.length() > 0 )
        {

          byte[] bytes = deviceIdText.getBytes( "UTF-8" );
          MD5Digest digest = new MD5Digest();
          digest.update( bytes, 0, bytes.length );
          int length = digest.getDigestLength();
          byte[] md5 = new byte[length];
          digest.getDigest( md5, 0, true );

          String value = toHexString( md5 );
          String tmp1 = value.substring( 4, 10 ).toUpperCase();
          String tmp2 = key.trim().toUpperCase();

          if ( tmp1.compareTo( tmp2 ) == 0 )
          {
            retVal = true;
          }
          else
          {
            String msg = "Invalid Key: " + tmp2 + " (" + getPin() + ")";
            EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
          }
        }
      }
      else
      {
        String msg = "ActivationKeyStore.GetInstance() return null";
        EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
      }
    }
    catch (Exception e )
    {
      String msg = "isKeyValid Error: " + e.toString();
      EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
    }
    return retVal;
  }

  public static String toHexString( byte bytes[] )
  {
    if ( bytes == null )
    {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    for ( int iter = 0; iter < bytes.length; iter++ )
    {
      byte high = (byte)( ( bytes[iter] & 0xf0 ) >> 4 );
      byte low = (byte)( bytes[iter] & 0x0f );
      sb.append( nibble2char( high ) );
      sb.append( nibble2char( low ) );
    }

    return sb.toString();
  }

  private static char nibble2char( byte b )
  {
    byte nibble = (byte)( b & 0x0f );
    if ( nibble < 10 )
    {
      return (char)( '0' + nibble );
    }
    return (char)( 'a' + nibble - 10 );
  }

  public static String getPin()
  {
    int deviceId = DeviceInfo.getDeviceId();
    String deviceIdText = java.lang.Integer.toHexString( deviceId );
    return deviceIdText.toUpperCase();
  }

  public boolean trialExpired()
  {
    boolean Expired = true;

    if ( isKeyValid() )
    {
      Expired = false;
    }
    else if ( getTrialCnt() < AppInfo.MAX_TRIALS )
    {
      Expired = false;
    }
    return Expired;
  }

  // returns number of free trials left
  int incTrialCnt()
  {
    int cnt = getTrialCnt();
    cnt++;
    setTrialCnt( cnt );
    save();

    if ( cnt == AppInfo.MAX_TRIALS )
    {
      //SimplySolitaire._session.tagEvent( "Expired" );
    }
    return AppInfo.MAX_TRIALS - cnt;
  }

  public boolean betaExpired()
  {
    return false;
    /*
    boolean retVal = false;
    long now = new Date().getTime();
    Calendar expiration = Calendar.getInstance();
    expiration.set( Calendar.YEAR, 2010 );
    expiration.set( Calendar.MONTH, Calendar.FEBRUARY );
    expiration.set( Calendar.DAY_OF_MONTH, 1 );
    expiration.set( Calendar.HOUR, 11 );
    expiration.set( Calendar.MINUTE, 00 );
    if ( now > expiration.getTime().getTime() )
    {
      retVal = true;
    }
    return retVal;
     * */
  }


  public String readKeyFromAppWorld()
  {
    String key = "";
    try
    {
      // Get name of your app
      String myAppName = ApplicationDescriptor.currentApplicationDescriptor().getName();
      // It must be exact name of your application as
      // registered with the App World ISV portal
      //String myAppName = "AppName:Vendor";

      // If you are targeting 4.3+, use this:
      //CodeModuleGroup group = CodeModuleGroupManager.load( myAppName );

      // On 4.2 you would need to use the following:
      CodeModuleGroup group = null;
      if( myAppName != null ) 
      {
        CodeModuleGroup[] groups = CodeModuleGroupManager.loadAll();
        if( groups != null ) 
        {
          for( int i = 0; i < groups.length; ++i ) 
          {
            if( groups[ i ].containsModule( myAppName ) ) 
            {
              group = groups[ i ];
              break;
            }
          }
        }
      } 

      // Pull out the App World data from the CodeModuleGroup
      if ( group != null )
      {
        key = group.getProperty( "RIM_APP_WORLD_LICENSE_KEY" );
        if ( key == null )
        {
          key = "";
        }
      }
    }
    catch ( Exception e )
    {
      String msg = "readKeyFromAppWorld failed: " + e.toString();
      EventLogger.logEvent( AppInfo.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
    }
    return key;
  }
}
