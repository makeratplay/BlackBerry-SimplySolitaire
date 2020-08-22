/*
 * ListStyleLabelField.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;


public class ListStyleLabelField extends ListStyleField
{

  private Bitmap leftIcon;
  
  private int targetHeight;
  private int rightOffset;
  private int leftOffset;
  private int labelHeight;
  private int topText;

  private String leftLabel = null;
  private String rightLabel = null;
  private Font lableFont = null;

  private int leftColor = COLOR_LEFT_LABEL_FONT;

  private boolean header = false;

  public ListStyleLabelField( String centerLabel )
  {
    super( USE_ALL_WIDTH | Field.NON_FOCUSABLE );

    this.leftLabel = centerLabel;
    this.rightLabel = null;
    this.leftIcon = null;
    this.header = true;
    this.leftColor = COLOR_HEADER_FONT;
  }


  public ListStyleLabelField( Bitmap icon, String leftLabel, String rightLabel )
  {
    super( USE_ALL_WIDTH | Field.FOCUSABLE );

    this.leftLabel = leftLabel;
    this.rightLabel = rightLabel;
    this.leftIcon = icon;
    this.header = false;
    this.leftColor = COLOR_LEFT_LABEL_FONT;
  }

  public void setRightText( String text )
  {
    this.rightLabel = text;
  }

  //protected void sublayout( int width, int height )
  public void layout( int width, int height )
  {
    int fontSize = sizeFont( this.leftLabel, width );
    this.lableFont = FONT_LEFT_LABLE.derive( FONT_LEFT_LABLE.isBold() ? Font.BOLD : Font.PLAIN, fontSize, Ui.UNITS_px );


    int labelHeight = ( this.lableFont.getHeight() * 3 ) / 2 + ( 2 * VPADDING );
    if ( this.header )
    {
      setDrawPosition( DRAWPOSITION_HEADER );
      labelHeight = this.lableFont.getHeight() + ( VPADDING ); 
    }
    
    int iconHeight = 0;
    if ( this.leftIcon != null )
    {
      iconHeight = this.leftIcon.getHeight() + ( 2 * VPADDING ); 
    }

    this.targetHeight = Math.max( labelHeight, iconHeight );

    this.leftOffset = HPADDING * 2;
    if ( this.leftIcon != null )
    {
      this.leftOffset += this.leftIcon.getWidth() + HPADDING;
    }

    if ( this.header )
    {
      int labelWidth = this.lableFont.getAdvance( this.leftLabel );
      this.leftOffset = ( width / 2 ) - ( labelWidth / 2 );
    }

    this.rightOffset = HPADDING * 2;
    if ( this.rightLabel != null )
    {
      this.rightOffset += FONT_RIGHT_LABLE.getAdvance( this.rightLabel );
    }
    this.rightOffset = width - this.rightOffset;

    this.labelHeight = this.lableFont.getHeight();

    this.topText = ( ( this.targetHeight / 2 ) - ( this.labelHeight / 2 ) );

    setExtent( width, this.targetHeight );
  }


  protected void paint( Graphics g )
  {
    int oldColor = g.getColor();
    Font oldFont = g.getFont();
    try
    {

      // Left Bitmap
      if ( this.leftIcon != null )
      {
        g.drawBitmap( HPADDING * 2, 0, this.leftIcon.getWidth(), this.leftIcon.getHeight(), this.leftIcon, 0, 0 );
      }

      // Left Label Text
      g.setColor( this.leftColor );
      g.setFont( this.lableFont );
      g.drawText( this.leftLabel, this.leftOffset, this.topText );

      // Right Label Text
      if ( this.rightLabel != null )
      {
        g.setColor( COLOR_RIGHT_LABEL_FONT );
        g.setFont( FONT_RIGHT_LABLE );
        g.drawText( this.rightLabel, this.rightOffset, this.topText );
      }
    }
    finally
    {
      g.setColor( oldColor );
      g.setFont( oldFont );
    }
  }


  protected boolean keyChar( char character, int status, int time )
  {
    if ( character == Characters.ENTER )
    {
      clickButton();
      return true;
    }
    return super.keyChar( character, status, time );
  }

  protected boolean navigationClick( int status, int time )
  {
    clickButton();
    return true;
  }

  protected boolean trackwheelClick( int status, int time )
  {
    clickButton();
    return true;
  }

  protected boolean invokeAction( int action )
  {
    switch ( action )
    {
      case ACTION_INVOKE:
      {
        clickButton();
        return true;
      }
    }
    return super.invokeAction( action );
  }

  public void clickButton()
  {
    fieldChangeNotify( 0 );
  }

}



