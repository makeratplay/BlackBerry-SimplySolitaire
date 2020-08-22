//#preprocess

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.system.EncodedImage;


//#ifdef V47
import net.rim.device.api.ui.*;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.VirtualKeyboard;
//#endif 

public class Card
{
  static public Bitmap blankCard = null;
  static public Bitmap numbers = null;
  static public Bitmap suits = null;
  static public Bitmap suits_sm = null; 

  static int width = 0;
  static int height= 0;
  static int numbersWidth = 0;
  static int numbersHeight = 0;
  static int suitsWidth = 0;
  static int suitsHeight = 0;
  static int suitsSmWidth = 0;
  static int suitsSmHeight = 0;


  static
  {
    boolean bStorm = false;
    //#ifdef V47
    Ui.getUiEngineInstance().setAcceptableDirections( Display.DIRECTION_LANDSCAPE );
    if ( Graphics.getScreenHeight() == 480 )
    {
      bStorm = true;
    }
    //#endif 

    int screenWidth = Graphics.getScreenWidth();
    if ( screenWidth < 320 && !bStorm )
    {
      blankCard = Bitmap.getBitmapResource( "img/blank_card_240.png" );
      numbers = Bitmap.getBitmapResource( "img/numbers_240.png" );
      suits = Bitmap.getBitmapResource( "img/suits_240.png" );
      suits_sm = Bitmap.getBitmapResource( "img/suits_sm_240.png" );
    }
    else if ( screenWidth < 480 && !bStorm )
    {
      blankCard = Bitmap.getBitmapResource( "img/blank_card_320.png" );
      numbers = Bitmap.getBitmapResource( "img/numbers_320.png" );
      suits = Bitmap.getBitmapResource( "img/suits_320.png" );
      suits_sm = Bitmap.getBitmapResource( "img/suits_sm_320.png" );
    }
    else
    {
      blankCard = Bitmap.getBitmapResource( "img/blank_card_480.png" );
      numbers = Bitmap.getBitmapResource( "img/numbers_480.png" );
      suits = Bitmap.getBitmapResource( "img/suits_480.png" );
      suits_sm = Bitmap.getBitmapResource( "img/suits_sm_480.png" );
    }

    width = blankCard.getWidth() / 4;
    height = blankCard.getHeight();

    numbersWidth = numbers.getWidth() / 26;
    numbersHeight = numbers.getHeight();

    suitsWidth = suits.getWidth() / 4;
    suitsHeight = suits.getHeight();

    suitsSmWidth = suits_sm.getWidth() / 4;
    suitsSmHeight = suits_sm.getHeight();
  }

  static final int CLOVERS  = 0;
  static final int SPADES   = 1;
  static final int HEARTS   = 2;
  static final int DIAMONDS = 3;

  private int m_type;

  private int m_value;
  private int m_suit;
  private boolean m_red;
  private boolean m_faceUp;


  public Card( int type )
  {


    m_type = type;
    m_faceUp = false;
    m_value = type % 13;
    m_suit = type / 13;
    if ( m_suit == CLOVERS || m_suit == SPADES )
    {
      m_red = false;
    }
    else
    {
      m_red = true;
    }
  }

  public boolean isFaceUp()
  {
    return m_faceUp;
  }

  public void flip()
  {
    m_faceUp = !m_faceUp;
  }

  public int faceValue()
  {
    return m_value + 1;
  }

  public boolean isRed()
  {
    return m_red;
  }

  public int suit()
  {
    return m_suit;
  }

  public void paint( Graphics graphics, int xPos, int yPos, boolean selected, boolean hasFocus )
  {
    if ( SolitaireGame.touchScreen )
    {
      // disable focus on touch screen devices
      hasFocus = false;
    }


    if ( m_faceUp )
    {
      int x = xPos;
      int y = yPos;
      int offSet = 0;

      // draw the blank card face
      graphics.drawBitmap( x, y, width, height, blankCard, offSet, 0 );


      // draw the card number in upper left corner
      x += -2;
      y += 2;
      offSet = (m_value * numbersWidth) ;
      if ( m_red == true )
      {
        offSet += ( numbersWidth * 13 );
      }
      graphics.drawBitmap( x, y, numbersWidth, numbersHeight, numbers, offSet, 0 );

      // draw the card suit
      x += width - ( suitsWidth + 3 );
      y += numbersHeight + 3;
      offSet = m_suit * suitsWidth;
      graphics.drawBitmap( x, y, suitsWidth, suitsHeight, suits, offSet, 0 );


      // draw the card small suit top right
      x = xPos;
      y = yPos;

      x += width - (suitsSmWidth + 6 );
      y += 5;
      offSet = m_suit * suitsSmWidth;
      graphics.drawBitmap( x, y, suitsSmWidth, suitsSmHeight, suits_sm, offSet, 0 );
    }
    else
    {
      int offSet = width * 1;
      graphics.drawBitmap( xPos, yPos, width, height, blankCard, offSet, 0 );
    }

    if ( selected == true || hasFocus == true )
    {
      int offSet = width * 2;
      graphics.drawBitmap( xPos, yPos, width, height, blankCard, offSet, 0 );
    }
  }


  static public Bitmap scaleImage( String imageName ) 
  {
    Bitmap bmp = null;

/*    EncodedImage ei = EncodedImage.getEncodedImageResource( imageName );  
    ei.setScale(2); // divide size by this number  
    Bitmap bmp = ei.getBitmap();
    return bmp;
    */


    int oldWidth = 480;
    int oldHeight = 360;
    int newWidthScale;
    int newHeightScale;
    int displayWidth = 320;
    int displayHeight = 240;

    EncodedImage ei = EncodedImage.getEncodedImageResource( imageName );
     
     //oldWidth = ei.getWidth();
     //oldHeight = ei.getHeight();
    // displayWidth = Display.getWidth();
    // displayHeight = Display.getHeight();
     
     int numerator = net.rim.device.api.math.Fixed32.toFP(oldWidth);
     int denominator = net.rim.device.api.math.Fixed32.toFP(displayWidth);
     int widthScale = net.rim.device.api.math.Fixed32.div(numerator, denominator);

     numerator = net.rim.device.api.math.Fixed32.toFP(oldHeight);
     denominator = net.rim.device.api.math.Fixed32.toFP(displayHeight);
     int heightScale = net.rim.device.api.math.Fixed32.div(numerator, denominator);

     EncodedImage newEi = ei.scaleImage32(widthScale, heightScale);
     bmp = newEi.getBitmap();
     return bmp;
  }
}
