
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




class KeyDlg extends PopupScreen
{

  public interface KeyDlgListener
  {
    // event dispatch methods
    public void DlgClosed();
    public void KeyActivated();
  }

  private static int MAX_TRIALS = 11;
  private static String SECRET_KEY = "*MLHSOLITAIRE011410*";  // this app is no longer sold so you can have the key
  private static final long PERSISTENCE_ID = 0x5e65ff560f7b8fb7L;   //Hash of com.mlhsoftware.SimplySolitaire.keycode

  public static String PID = "40759";
  public static String BUY_URL = "https://www.mobihand.com/mobilecart/mc1.asp?posid=234&pid=" + PID + "&did=";

  private EditField m_keyField;
  private static ActivationKeyStore ActivationKey = null;
  KeyDlgListener m_dlgListener;


  static class ActivationKeyStore implements Persistable
  {
    private String m_key;
    private int m_trialCnt;
    private ActivationKeyStore()
    {
      m_key = "";
      m_trialCnt = 0;
    }

    //Cannonical copy constructor.
    private ActivationKeyStore( ActivationKeyStore other )
    {
      m_key = other.m_key;
      m_trialCnt = other.m_trialCnt;
    }

    //Causes the values within this instance to become the effective
    //properties for the application by saving this instance to the store.
    public void save()
    {
      store.setContents( this );
      store.commit();
    }

    public String getKey()
    {
      return m_key;
    }

    public void setKey( String val )
    {
      m_key = val;
    }

    public int getTrialCnt()
    {
      return m_trialCnt;
    }

    public void setTrialCnt( int val )
    {
      m_trialCnt = val;
    }

  }

  public static void fetch()
  {
    ActivationKey = (ActivationKeyStore)store.getContents();
  }


  //Persistent object wrapping the effective properties instance
  private static PersistentObject store;

  //Ensure that an effective properties set exists on startup.
  static
  {
    try
    {
      store = PersistentStore.getPersistentObject( PERSISTENCE_ID );
      synchronized ( store )
      {
        if ( store.getContents() == null )
        {
          store.setContents( new ActivationKeyStore() );
          store.commit();
        }
      }
      fetch();
    }
    catch ( Exception e )
    {
      String msg = "Persistent Store: " + e.toString();
      EventLogger.logEvent( SimplySolitaire.LOGGER_ID, msg.getBytes(), EventLogger.SEVERE_ERROR );
    }
  }


  KeyDlg( Manager manager, KeyDlgListener dlgListener )
  {
    super( manager, DEFAULT_MENU | DEFAULT_CLOSE );

    m_dlgListener = dlgListener;

    add( new LabelField( "Enter activation code", LabelField.FIELD_HCENTER ) );
    add( new LabelField( "Your PIN: " + getPin(), LabelField.FIELD_HCENTER ) );
    m_keyField = new EditField( "", "", 50, EditField.NO_NEWLINE );
    add( m_keyField );


    HorizontalFieldManager hfm = new HorizontalFieldManager( Field.FIELD_HCENTER | Field.FIELD_BOTTOM );
    hfm.add( new OkButton() );
    hfm.add( new CancelButton() );
    hfm.add( new BuyButton() );
    add( hfm );

  }

  public void makeMenu( Menu menu, int instance )
  {
    if ( instance == Menu.INSTANCE_DEFAULT )
    {
      menu.add( _accept );
      menu.add( _cancel );
      menu.add( _buy );
    }

    super.makeMenu( menu, instance );
  }

  private void OnAcceptCode()
  {
    if ( m_keyField.getText().length() == 0 )
    {
      Dialog.alert( "Please enter your key" );
      m_keyField.setFocus();
    }
    else
    {
      if ( ActivationKey != null )
      {
        ActivationKey.setKey( m_keyField.getText() );
        if ( isKeyValid() )
        {
          SimplySolitaire._session.tagEvent( "Activated" );
          ActivationKey.save();
          m_dlgListener.KeyActivated();
          Dialog.alert( "Thank You" );
          close();
          m_dlgListener.DlgClosed();
        }
        else
        {
          ActivationKey.setKey( "" );
          Dialog.alert( "Invalid Key" );
        }
      }      
    }
  }

  private void OnCancel()
  {
    close();
  }

  private void OnBuy()
  {
    String url = BUY_URL + getPin();
    Browser.getDefaultSession().displayPage( url );
  }

  private final class OkButton extends ButtonField
  {
    private OkButton()
    {
      super( "OK", ButtonField.CONSUME_CLICK );
    }

    protected void fieldChangeNotify( int context )
    {
      if ( ( context & FieldChangeListener.PROGRAMMATIC ) == 0 )
      {
        OnAcceptCode();
      }
    }
  }

  private final class CancelButton extends ButtonField
  {
    private CancelButton()
    {
      super( "Cancel", ButtonField.CONSUME_CLICK );
    }

    protected void fieldChangeNotify( int context )
    {
      if ( ( context & FieldChangeListener.PROGRAMMATIC ) == 0 )
      {
        OnCancel();
      }
    }
  }

  private final class BuyButton extends ButtonField
  {
    private BuyButton()
    {
      super( "Buy", ButtonField.CONSUME_CLICK | LabelField.USE_ALL_WIDTH | LabelField.FIELD_HCENTER );
    }

    protected void fieldChangeNotify( int context )
    {
      if ( ( context & FieldChangeListener.PROGRAMMATIC ) == 0 )
      {
        OnBuy();
      }
    }
  }

  private MenuItem _accept = new MenuItem( "Accept", 80, 80 )
  {
    public void run()
    {
      OnAcceptCode();
    }
  };

  private MenuItem _cancel = new MenuItem( "Cancel", 80, 80 )
  {
    public void run()
    {
      OnCancel();
    }
  };

  private MenuItem _buy = new MenuItem( "Buy", 80, 80 )
  {
    public void run()
    {
      OnBuy();
    }
  };

  public boolean keyChar( char key, int status, int time )
  {
    if ( key == net.rim.device.api.system.Characters.ENTER )
    {
      OnAcceptCode();
    }
    return super.keyChar( key, status, time );
  }

  static public boolean isKeyValid()
  {
    boolean retVal = false;

    int deviceId = DeviceInfo.getDeviceId();
    String deviceIdText = getPin() + SECRET_KEY;
    if ( ActivationKey != null )
    {
      String key = ActivationKey.getKey();
      if ( key.length() > 0 )
      {
        try
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
            EventLogger.logEvent( SimplySolitaire.LOGGER_ID, msg.getBytes( "UTF-8" ), EventLogger.SEVERE_ERROR );
          }
        }
        catch ( java.io.UnsupportedEncodingException exc )
        {
        }
      }
    }
    return retVal;
  }

  static public String toHexString( byte bytes[] )
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

  static private char nibble2char( byte b )
  {
    byte nibble = (byte)( b & 0x0f );
    if ( nibble < 10 )
    {
      return (char)( '0' + nibble );
    }
    return (char)( 'a' + nibble - 10 );
  }

  static public String getPin()
  {
    int deviceId = DeviceInfo.getDeviceId();
    String deviceIdText = java.lang.Integer.toHexString( deviceId );
    return deviceIdText.toUpperCase();
  }

  static public boolean trialExpired()
  {
    boolean Expired = true;

    if ( isKeyValid() )
    {
      Expired = false;
    }
    else if ( ActivationKey.getTrialCnt() < MAX_TRIALS )
    {
      Expired = false;
    }
    return Expired;
  }

  // returns number of free trials left
  static int incTrialCnt()
  {
    int cnt = ActivationKey.getTrialCnt();
    cnt++;
    ActivationKey.setTrialCnt( cnt );
    ActivationKey.save();

    if ( cnt == MAX_TRIALS  )
    {
      SimplySolitaire._session.tagEvent( "Expired" );
    }

    return MAX_TRIALS - cnt;
  }

  static public boolean betaExpired()
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
}
