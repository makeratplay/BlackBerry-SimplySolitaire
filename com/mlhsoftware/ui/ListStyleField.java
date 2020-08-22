/*
 * ListStyleField.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;


public abstract class ListStyleField extends Field
{
  public static final int DRAWPOSITION_UNKNOWN = -1;
  public static final int DRAWPOSITION_TOP     = 0;
  public static final int DRAWPOSITION_BOTTOM  = 1;
  public static final int DRAWPOSITION_MIDDLE  = 2;
  public static final int DRAWPOSITION_SINGLE  = 3;
  public static final int DRAWPOSITION_HEADER  = 4;

  protected static final int CORNER_RADIUS = 18;

  public static final int VPADDING = Display.getWidth() <= 320 ? 6 : 10;
  protected static final int HPADDING = Display.getWidth() <= 320 ? 6 : 10;


  public static final int COLOR_INNER_BACKGROUND = 0xFFFFFF;
  public static final int COLOR_INNER_BACKGROUND_FOCUS = 0x186DEF;
  public static final int COLOR_BACKGROUND = 0xFFFFFF;
  public static final int COLOR_BORDER = AppInfo.COLOR_BORDER;
  public static final int COLOR_BACKGROUND_FOCUS = 0x186DEF;
  public static final int COLOR_HEADER_FONT = 0x777777;
  public static final int COLOR_LEFT_LABEL_FONT = Color.BLACK;
  public static final int COLOR_RIGHT_LABEL_FONT = 0x777777;
  public static final Font FONT_LEFT_LABLE = Font.getDefault().derive( Font.BOLD, 8, Ui.UNITS_pt, Font.ANTIALIAS_STANDARD, 0 );
  public static final Font FONT_RIGHT_LABLE = FONT_LEFT_LABLE; // Font.getDefault().derive( Font.PLAIN, 14, Ui.UNITS_px, Font.ANTIALIAS_STANDARD, 0 );


  protected int drawPosition = -1;
  protected int fontSize = FONT_LEFT_LABLE.getHeight();

  public ListStyleField( long style )
  {
    super( style );
    
  }

  /**
   * DRAWPOSITION_TOP | DRAWPOSITION_BOTTOM | DRAWPOSITION_MIDDLE
   * Determins how the field is drawn (borders)
   * If none is set, then no borders are drawn
   */
  public void setDrawPosition( int drawPosition )
  {
    this.drawPosition = drawPosition;
  }


  protected void paintBackground( Graphics g )
  {
    if ( this.drawPosition < 0 )
    {
      // it's like a list field, let the default background be drawn
      super.paintBackground( g );
      return;
    }

    int oldColour = g.getColor();

    // paint outer background color
    g.setColor( AppInfo.BACKGROUND_COLOR );
    g.fillRect( 0, 0, getWidth(), getHeight() );


    int background = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ? ListStyleField.COLOR_INNER_BACKGROUND_FOCUS : ListStyleField.COLOR_INNER_BACKGROUND;
    try
    {
      switch ( this.drawPosition )
      {
        case ListStyleField.DRAWPOSITION_HEADER:
        {
          g.setColor( background );
          g.fillRoundRect( 0, 0, getWidth(), getHeight() + ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          g.setColor( ListStyleField.COLOR_BORDER );
          g.drawRoundRect( 0, 0, getWidth(), getHeight() + ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          break;
        }
        case ListStyleField.DRAWPOSITION_TOP:
        {
          g.setColor( background );
          g.fillRoundRect( 0, 0, getWidth(), getHeight() + ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          g.setColor( ListStyleField.COLOR_BORDER );
          g.drawRoundRect( 0, 0, getWidth(), getHeight() + ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          g.drawLine( 0, getHeight() - 1, getWidth() - 1, getHeight() - 1 );
          break;
        }
        case ListStyleField.DRAWPOSITION_BOTTOM:
        {
          g.setColor( background );
          g.fillRoundRect( 0, -ListStyleField.CORNER_RADIUS, getWidth(), getHeight() + ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          g.setColor( ListStyleField.COLOR_BORDER );
          g.drawRoundRect( 0, -ListStyleField.CORNER_RADIUS, getWidth(), getHeight() + ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          break;
        }
        case ListStyleField.DRAWPOSITION_MIDDLE:
        {
          g.setColor( background );
          g.fillRect( 0, 0, getWidth(), getHeight() );
          g.setColor( ListStyleField.COLOR_BORDER );
          g.drawLine( 0, 0, 0, getHeight() - 1 ); // left border
          g.drawLine( getWidth() - 1, 0, getWidth() - 1, getHeight() - 1 ); // right border
          g.drawLine( 0, getHeight() - 1, getWidth() - 1, getHeight() - 1 ); // bottom border

          break;
        }
        case ListStyleField.DRAWPOSITION_SINGLE:
        default:
        {
          g.setColor( background );
          g.fillRoundRect( 0, 0, getWidth(), getHeight(), ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          g.setColor( ListStyleField.COLOR_BORDER );
          g.drawRoundRect( 0, 0, getWidth(), getHeight(), ListStyleField.CORNER_RADIUS, ListStyleField.CORNER_RADIUS );
          break;
        }
      }

      //g.setColor( 0x00FF0000 );
      //g.drawRect( 0, 0, getWidth(), getHeight() );

    }
    finally
    {
      g.setColor( oldColour );
    }
  }

  protected void drawFocus( Graphics g, boolean on )
  {
    if ( this.drawPosition < 0 )
    {
      super.drawFocus( g, on );
    }
    else
    {
      boolean oldDrawStyleFocus = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS );
      try
      {
        if ( on )
        {
          g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, true );
        }
        paintBackground( g );
        paint( g );
      }
      finally
      {
        g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus );
      }
    }
  }

  protected int getVPadding()
  {
    int vPadding = 0;
    switch ( this.drawPosition )
    {
      case DRAWPOSITION_TOP:
      {
        vPadding = VPADDING * 3;
        break;
      }
      case DRAWPOSITION_BOTTOM:
      {
        vPadding = VPADDING * 3;
        break;
      }
      case DRAWPOSITION_MIDDLE:
      {
        vPadding = VPADDING * 2;
        break;
      }
      case DRAWPOSITION_SINGLE:
      default:
      {
        vPadding = VPADDING * 4;
        break;
      }
    }
    return vPadding;
  }

  protected int getTopPos()
  {
    int topPos = 0;
    switch ( this.drawPosition )
    {
      case DRAWPOSITION_TOP:
      {
        topPos = VPADDING * 2;
        break;
      }
      case DRAWPOSITION_BOTTOM:
      {
        topPos = VPADDING;
        break;
      }
      case DRAWPOSITION_MIDDLE:
      {
        topPos = VPADDING;
        break;
      }
      case DRAWPOSITION_SINGLE:
      default:
      {
        topPos = VPADDING ;
        break;
      }
    }
    return topPos;
  }

  public void setDirty( boolean dirty ) { }
  public void setMuddy( boolean muddy ) { }

  protected int sizeFont( String labelText, int labelWidth )
  {
    int fontSize = this.fontSize;
    Font lableFont = FONT_LEFT_LABLE.derive( FONT_LEFT_LABLE.isBold() ? Font.BOLD : Font.PLAIN, fontSize, Ui.UNITS_px );
    int textWidth = lableFont.getAdvance( labelText );
    while ( textWidth > labelWidth && fontSize > 2 )
    {
      fontSize--;
      lableFont = FONT_LEFT_LABLE.derive( FONT_LEFT_LABLE.isBold() ? Font.BOLD : Font.PLAIN, fontSize, Ui.UNITS_px );
      textWidth = lableFont.getAdvance( labelText );
    }
    return fontSize;
  }
}



