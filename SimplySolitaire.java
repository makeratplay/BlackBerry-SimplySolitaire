package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.*;
import net.rim.device.api.collection.util.*;
import net.rim.device.api.system.EventLogger;

import com.Localytics.LocalyticsSession.*;

//import net.rim.device.api.ui.component.Dialog


import java.util.Vector;



public class SimplySolitaire extends UiApplication
{
  public static final long LOGGER_ID = 0xa0467ae36a5aa547L; //com.mlhsoftware.SimplySolitaire


  private final static String APPLICATION_KEY = "";
  public static LocalyticsSession _session = new LocalyticsSession( APPLICATION_KEY );

  private SolitaireGame m_mainScreen;

  public static void main(String[] args)
  {
    SimplySolitaire theApp = new SimplySolitaire();
    theApp.enterEventDispatcher();
  }

  public SimplySolitaire()
  {
    m_mainScreen = new SolitaireGame();

    _session.open();
    _session.upload();

    SplashScreen spScreen = new SplashScreen( UiApplication.getUiApplication(), m_mainScreen );         
  }
}

/*package*/
final class SolitaireScreen extends FullScreen
{

  SolitaireGame m_game;

  public SolitaireScreen()
  {
    super(DEFAULT_MENU | DEFAULT_CLOSE);
    m_game = null;
  }

  protected void paint( Graphics graphics )
  {
    int screenWidth = Graphics.getScreenWidth();
    int screenHeight = Graphics.getScreenHeight();
    int margin = 6;
    int yPos = margin;

    Font font = Font.getDefault().derive( Font.PLAIN, 16 );
    int strHeight = font.getHeight();
    graphics.setFont( font );
    


    graphics.setColor( 0x008000 );
    graphics.fillRect( 0, 0, screenWidth, screenHeight );

    graphics.setColor( 0xFFFFFF );

    String text = "Simply Solitaire";
    int strWidth = font.getAdvance( text );
    graphics.drawText( text, ( screenWidth / 2 ) - ( strWidth / 2 ), yPos );

    font = Font.getDefault().derive( Font.PLAIN, 14 );
    strHeight = font.getHeight();
    graphics.setFont( font );


    yPos = screenHeight - ( strHeight + 4 );
    text = "mlhsoftware.com";
    strWidth = font.getAdvance( text );
    graphics.drawText( text, ( screenWidth / 2 ) - ( strWidth / 2 ), yPos );

  }

  protected boolean navigationMovement( int dx, int dy, int status, int time )
  {
    return navigationMovement( dx, dy, status, time );
  }

  protected boolean navigationClick( int status, int time )
  {
    return navigationClick( status, time );
  }

}
