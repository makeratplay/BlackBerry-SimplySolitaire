package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.system.Bitmap;

/**
 * Dependant Files:
 * NegativeMarginVerticalFieldManager.java
 * 
 * 
 */




public class ForegroundManager extends NegativeMarginVerticalFieldManager
{
  private static int BACKGROUND_COLOR = AppInfo.BACKGROUND_COLOR;
  //public static final int[] BACKGROUND_COLORS = { 0x0042454A, 0x0042454A, 0x00293439, 0x00293439 };  

  private boolean BackgroundTransparent = false;
  private Bitmap backgroundImage = null;

  public ForegroundManager()
  {
    super( USE_ALL_HEIGHT | VERTICAL_SCROLL | VERTICAL_SCROLLBAR | USE_ALL_WIDTH );
  }

  public void setBackgroundTransparent( boolean value )
  {
    BackgroundTransparent = value;
  }

  public void setBackgroundColor( int color )
  {
    BackgroundTransparent = false;
    BACKGROUND_COLOR = color;
  }

  public void setBackgroundImage( Bitmap image )
  {
    backgroundImage = image;
  }

  protected void paintBackground( Graphics graphics )
  {
    int mgrWidth = getWidth();
    int mgrHeight = getHeight();

    if ( !BackgroundTransparent )
    {
      graphics.clear();
      int oldColor = graphics.getColor();
      try
      {
        graphics.setColor( BACKGROUND_COLOR );
        graphics.fillRect( 0, getVerticalScroll(), mgrWidth, mgrHeight );
      }
      finally
      {
        graphics.setColor( oldColor );
      }
    }

    if ( backgroundImage != null )
    {
      int imgWidth = backgroundImage.getWidth();
      int imgHeight = backgroundImage.getHeight();
      graphics.drawBitmap( ( mgrWidth >> 2 ) - ( imgWidth >> 2 ), ( mgrHeight >> 2 ) - ( imgHeight >> 2 ), imgWidth, imgHeight, backgroundImage, 0, 0 );
    }

    super.paint( graphics );
  }  
}
