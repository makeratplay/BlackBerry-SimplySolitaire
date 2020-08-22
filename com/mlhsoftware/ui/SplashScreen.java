/*
 * SystemInfoScreen.java
 *
 * MLH Software
 * Copyright 2010
 * 
 * Dependant Files:
 *    splashScreen.png
 * 
 */

package com.mlhsoftware.SimplySolitaire;


import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import java.util.*;



public class SplashScreen extends MainScreen 
{
  private Screen next;
  private UiApplication application;
  private boolean bDismissed = false;

  private Bitmap SplashScreenImg = Bitmap.getBitmapResource("splashScreen.png");
  //private Bitmap SplashScreenTitle = null;
  //private Bitmap SplashScreenCopyRight = null;
   

  public SplashScreen(UiApplication ui, Screen next) 
  {
    super(Field.USE_ALL_HEIGHT | Field.FIELD_LEFT);
    this.application = ui;
    this.next = next;


    application.pushScreen(this);

    CountDown countDown = new CountDown();
    countDown.start();
  }
   
  protected void paint(Graphics graphics)
  {

    int screenWidth = Graphics.getScreenWidth();
    int screenHeight = Graphics.getScreenHeight();
    int bitmapWidth = SplashScreenImg.getWidth();
    int bitmapHeight = SplashScreenImg.getHeight();

    //int titleWidth = SplashScreenTitle.getWidth();
    //int titleHeight = SplashScreenTitle.getHeight();
    //int copyrightWidth = SplashScreenCopyRight.getWidth();
    //int copyrightHeight = SplashScreenCopyRight.getHeight();

    graphics.setColor( 0x000000 );
    graphics.fillRect( 0, 0, screenWidth, screenHeight );

    int x = (screenWidth - bitmapWidth) / 2;
    int y = (screenHeight - bitmapHeight) / 2;
    
    graphics.drawBitmap(x, y, bitmapWidth, bitmapHeight, SplashScreenImg, 0, 0);

    //graphics.drawBitmap( 0, 10, titleWidth, titleHeight, SplashScreenTitle, 0, 0 );
    //graphics.drawBitmap( screenWidth - copyrightWidth, screenHeight - copyrightHeight, copyrightWidth, copyrightHeight, SplashScreenCopyRight, 0, 0 );
  }   
   
   public void dismiss() 
   {
      if ( !bDismissed )
      {
        application.popScreen(this);
        application.pushScreen(next);
        bDismissed = true;
      }
   }
   
   private class CountDown extends Thread 
   {
      public void run() 
      {
        invalidate();
        setSleep( 3000 );

        application.invokeLater( new Runnable() { public void run() { dismiss(); } }  );
      }
      
      private void setSleep(int ms)
      {
        try
        {
          sleep(ms);
        }
        catch (InterruptedException e)
        {
        }         
      }
   }
   
   protected boolean navigationClick(int status, int time) 
   {
      dismiss();
      return true;
   }

  public boolean keyChar( char key, int status, int time )
  {
    //intercept the ESC and MENU key - exit the splash screen
    boolean retval = false;
    switch ( key )
    {
      case Characters.ENTER:
      case Characters.CONTROL_MENU:
      case Characters.ESCAPE:
      {
        dismiss();
        retval = true;
        break;
      }
    }
    return retval;
  }
} 


