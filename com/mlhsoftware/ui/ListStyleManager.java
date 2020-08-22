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


public abstract class ListStyleManager extends Manager
{
  protected int drawPosition = ListStyleField.DRAWPOSITION_UNKNOWN;

  private boolean hasFocus = false;
  protected boolean allowFocus = false;

  public ListStyleManager( long style )
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
    g.fillRect( 0, getVerticalScroll(), getWidth(), getHeight() );


    int background = hasFocus && allowFocus ? ListStyleField.COLOR_INNER_BACKGROUND_FOCUS : ListStyleField.COLOR_INNER_BACKGROUND;
    //int background = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ? ListStyleField.COLOR_INNER_BACKGROUND_FOCUS : ListStyleField.COLOR_INNER_BACKGROUND;
    try
    {
      switch ( this.drawPosition )
      {
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
          //g.drawRect( 0, 0, getWidth(), getHeight() );
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


  public boolean isFocusable()
  {
    return true;
  }

  protected void onFocus( int direction )
  {
    hasFocus = true;
    super.onFocus( direction );
    invalidate();
  }

  protected void onUnfocus()
  {
    hasFocus = false;
    super.onUnfocus();
    invalidate();
  }

  protected int getVPadding()
  {
    int vPadding = 0;
    switch ( this.drawPosition )
    {
      case ListStyleField.DRAWPOSITION_TOP:
      {
        vPadding = ListStyleField.VPADDING * 3;
        break;
      }
      case ListStyleField.DRAWPOSITION_BOTTOM:
      {
        vPadding = ListStyleField.VPADDING * 3;
        break;
      }
      case ListStyleField.DRAWPOSITION_MIDDLE:
      {
        vPadding = ListStyleField.VPADDING * 2;
        break;
      }
      case ListStyleField.DRAWPOSITION_SINGLE:
      default:
      {
        vPadding = ListStyleField.VPADDING * 4;
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
      case ListStyleField.DRAWPOSITION_TOP:
      {
        topPos = ListStyleField.VPADDING * 2;
        break;
      }
      case ListStyleField.DRAWPOSITION_BOTTOM:
      {
        topPos = ListStyleField.VPADDING;
        break;
      }
      case ListStyleField.DRAWPOSITION_MIDDLE:
      {
        topPos = ListStyleField.VPADDING;
        break;
      }
      case ListStyleField.DRAWPOSITION_SINGLE:
      default:
      {
        topPos = ListStyleField.VPADDING * 2;
        break;
      }
    }
    return topPos;
  }

  public void setDirty( boolean dirty ) { }
  public void setMuddy( boolean muddy ) { }
}



