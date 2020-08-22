/*
 * AboutScreen.java
 *
 * MLH Software
 * Copyright 2010
 * 
 * Dependant Files:
 *    AppInfo.java
 *    icon.png
 *    TitlebarManager.java
 *    ForegroundManager.java
 *    AboutbarManager.java
 *    ListStyleFieldSet.java
 *    ListStyleLabelField.java
 *    ListStyleButtonField.java
 *    SystemInfoScreen.java
 *    greenArrow.png
 *    users_two_24.png
 *    comment_add_24.png
 *    mlhIcon.png
 *    twitter_24.png
 *    spanner_24.png
 *    crackberry_24.png
 *    facebook_24.png
 *    helpIconBlack.png
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.Bitmap;

import net.rim.device.api.ui.container.*;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.device.api.system.CodeModuleManager;

import net.rim.device.api.system.DeviceInfo;

import net.rim.device.api.system.EventLogger;

public class AboutScreen extends MainScreen 
{
  private static final Bitmap ICON = Bitmap.getBitmapResource( "icon.png" );

  public AboutScreen() 
  {
    super( DEFAULT_MENU | DEFAULT_CLOSE | Manager.NO_VERTICAL_SCROLL );

    // Build the titlebar with Cancel and Save buttons
    TitlebarManager titlebarMgr = new TitlebarManager( "About " + AppInfo.APP_NAME, false );
    //titlebarMgr.handleLeftBtn( new FieldChangeListener()
    //{
    //  public void fieldChanged( Field field, int context )
    //  {
    //    onDoneBtn();
    //  }
    //} );
    add( titlebarMgr );



    ForegroundManager foreground = new ForegroundManager();

    // About bar - App Icon, App Name, App Version, Vendor
    String appVersion = AppInfo.APP_VERSION;
    if ( AppInfo.APPWORLD )
    {
      appVersion += " a";
    }
    if ( AppInfo.BETA == true )
    {
      appVersion += " Beta";
    }

    String extraLine = "";
    if ( AppInfo.TOUCHSCREEN )
    {
      extraLine = "with touchscreen support";
    }

    AboutbarManager aboutBar = new AboutbarManager( ICON, AppInfo.APP_NAME, appVersion, extraLine );
    foreground.add( aboutBar );


    // Action buttons
    Bitmap caret = Bitmap.getBitmapResource( "greenArrow.png" );
    Bitmap tellIcon = Bitmap.getBitmapResource( "users_two_24.png" );
    Bitmap feedbackIcon = Bitmap.getBitmapResource( "comment_add_24.png" );
    Bitmap webIcon = Bitmap.getBitmapResource( "mlhIcon.png" );
    Bitmap twitterIcon = Bitmap.getBitmapResource( "twitter_24.png" );
    Bitmap systemIcon = Bitmap.getBitmapResource( "spanner_24.png" );
    Bitmap crackberryIcon = Bitmap.getBitmapResource( "crackberry_24.png" );
    Bitmap facebookIcon = Bitmap.getBitmapResource( "facebook_24.png" );
    Bitmap helpIcon = Bitmap.getBitmapResource( "helpIconBlack.png" );



    // Info Section
    ListStyleFieldSet buttonSet = new ListStyleFieldSet();

    ListStyleLabelField info = new ListStyleLabelField( "More Info" );
    buttonSet.add( info );

    ListStyleButtonField link = new ListStyleButtonField( systemIcon, "System info", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onSystemInfo();
      }
    } );
    buttonSet.add( link );

    link = new ListStyleButtonField( helpIcon, "Help", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onHelp();
      }
    } );
    buttonSet.add( link );

    link = new ListStyleButtonField( webIcon, "MLHSoftware.com", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onMoreInfo();
      }
    } );
    buttonSet.add( link );
    foreground.add( buttonSet );

    // Share
    buttonSet = new ListStyleFieldSet();

    info = new ListStyleLabelField( "Share" );
    buttonSet.add( info );

    link = new ListStyleButtonField( tellIcon, "Tell a friend", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onTellAFriend();
      }
    } );
    buttonSet.add( link );

    link = new ListStyleButtonField( twitterIcon, "Follow us on twitter", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onTwitter();
      }
    } );
    buttonSet.add( link );

    link = new ListStyleButtonField( facebookIcon, "Fan us on Facebook", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onFacebook();
      }
    } );
    buttonSet.add( link );
    foreground.add( buttonSet );

    // Feedback
    buttonSet = new ListStyleFieldSet();

    info = new ListStyleLabelField( "Feedback" );
    buttonSet.add( info );

    link = new ListStyleButtonField( feedbackIcon, "Email us", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onFeedback();
      }
    } );
    buttonSet.add( link );

    link = new ListStyleButtonField( crackberryIcon, "User forum on CrackBerry", caret );
    link.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onCrackBerry();
      }
    } );
    buttonSet.add( link );


    foreground.add( buttonSet );


    add( foreground );

    addMenuItem( _debug );
    addMenuItem( _showLog );
  }

  private void onDoneBtn()
  {
    close();
  }

  private void onSystemInfo()
  {
    SystemInfoScreen dlg = new SystemInfoScreen( AppInfo.APP_NAME, AppInfo.APP_VERSION );
    UiApplication.getUiApplication().pushScreen( dlg );
  }

  private void onTellAFriend()
  {
    String arg = MessageArguments.ARG_NEW;
    String to = "";
    String subject = "Check out this Blackberry application: " + AppInfo.APP_NAME;
    String body = AppInfo.TELL_A_FRIEND;

    MessageArguments msgArg = new MessageArguments( arg, to, subject, body );
    Invoke.invokeApplication( Invoke.APP_TYPE_MESSAGES, msgArg );
  }

  private void onFeedback()
  {
    String key = "";
    ActivationKeyStore keyStore = ActivationKeyStore.GetInstance();
    if ( keyStore != null )
    {
      key = keyStore.getKey();
    }
    int deviceId = DeviceInfo.getDeviceId();
    String deviceIdText = java.lang.Integer.toHexString( deviceId );
    String pin = deviceIdText.toUpperCase();

    String arg = MessageArguments.ARG_NEW;
    String to = "feedback@mlhsoftware.com";
    String subject = "Feedback: " + AppInfo.APP_NAME;
    String body = "Tell us what you think about " + AppInfo.APP_NAME + ". \r\n" +
                  "\r\n App: " + AppInfo.APP_NAME + 
                  "\r\n Key: " + key + 
                  "\r\n PIN: " + pin +
                  "\r\n Version: " + AppInfo.APP_VERSION + 
                  "\r\n Device: " + DeviceInfo.getDeviceName() + 
                  "\r\n OS Version: " + CodeModuleManager.getModuleVersion( CodeModuleManager.getModuleHandleForObject( "" ) ) + 
                  "\r\n\r\n";

    MessageArguments msgArg = new MessageArguments( arg, to, subject, body );
    Invoke.invokeApplication( Invoke.APP_TYPE_MESSAGES, msgArg ); 
  }

  private void onMoreInfo()
  {
    Browser.getDefaultSession().displayPage( AppInfo.APP_URL );
  }

  private void onHelp()
  {
    String url = AppInfo.HELP_URL + "/?a=help&v=" + AppInfo.APP_VERSION + "&b=" + DeviceInfo.getDeviceName();
    if ( AppInfo.BETA )
    {
      url += "&beta=true";
    }
    Browser.getDefaultSession().displayPage( url );
  }

  private void onTwitter()
  {
    Browser.getDefaultSession().displayPage( "http://m.twitter.com/mlhsoftware" );
  }

  private void onFacebook()
  {
    Browser.getDefaultSession().displayPage( "http://m.facebook.com/pages/MLH-Software/137362215699" );
  }

  private void onCrackBerry()
  {
    Browser.getDefaultSession().displayPage( "http://forums.crackberry.com/f188/" );
  }

  private MenuItem _debug = new MenuItem( "Enable Debug Logging", 3001, 1003 )
  {
    public void run()
    {
      EventLogger.setMinimumLevel( EventLogger.DEBUG_INFO );
      Dialog.alert( "Debug Logging has been enabled" );
    }
  };

  private MenuItem _showLog = new MenuItem( "Open Event Logs", 3002, 1004 )
  {
    public void run()
    {
      EventLogger.startEventLogViewer();
    }
  };

}
