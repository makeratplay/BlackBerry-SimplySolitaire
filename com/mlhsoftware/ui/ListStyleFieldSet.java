/*
 * ListStyleFieldSet.java
 *
 * MLH Software
 * Copyright 2010
 * 
 * Dependant Files:
 *    AppInfo.java
 *    ListStyleField.java
 *    ListStyleManager.java
 * 
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;

/**
 * 
 */
public class ListStyleFieldSet extends VerticalFieldManager
{
  public ListStyleFieldSet()
  {
    super( VERTICAL_SCROLL | VERTICAL_SCROLLBAR );
  }

  protected void sublayout( int maxWidth, int maxHeight )
  {
    int topOffset = ListStyleField.VPADDING;

    int numChildren = this.getFieldCount();
    if ( numChildren > 0 )
    {
      if ( numChildren == 1 )
      {
        Field child = getField( 0 );
        if ( child instanceof ListStyleField )
        {
          ( (ListStyleField)child ).setDrawPosition( ListStyleField.DRAWPOSITION_SINGLE );
        }
        else if ( child instanceof ListStyleManager )
        {
          ( (ListStyleManager)child ).setDrawPosition( ListStyleField.DRAWPOSITION_SINGLE );
        }
        layoutChild( child, maxWidth, maxHeight );
        setPositionChild( child, 0, topOffset );
        topOffset += child.getHeight();
      }
      else
      {
        int index = 0;
        Field child = getField( index );
        if ( child instanceof ListStyleField )
        {
          ( (ListStyleField)child ).setDrawPosition( ListStyleField.DRAWPOSITION_TOP );
        }
        else if ( child instanceof ListStyleManager )
        {
          ( (ListStyleManager)child ).setDrawPosition( ListStyleField.DRAWPOSITION_TOP );
        }
        layoutChild( child, maxWidth, maxHeight );
        setPositionChild( child, 0, topOffset );
        topOffset += child.getHeight();

        for ( index = 1; index < numChildren - 1; index++ )
        {
          child = getField( index );
          if ( child instanceof ListStyleField )
          {
            ( (ListStyleField)child ).setDrawPosition( ListStyleField.DRAWPOSITION_MIDDLE );
          }
          else if ( child instanceof ListStyleManager )
          {
            ( (ListStyleManager)child ).setDrawPosition( ListStyleField.DRAWPOSITION_MIDDLE );
          }
          layoutChild( child, maxWidth, maxHeight );
          setPositionChild( child, 0, topOffset );
          topOffset += child.getHeight();
        }

        child = getField( index );
        if ( child instanceof ListStyleField )
        {
          ( (ListStyleField)child ).setDrawPosition( ListStyleField.DRAWPOSITION_BOTTOM );
        }
        else if ( child instanceof ListStyleManager )
        {
          ( (ListStyleManager)child ).setDrawPosition( ListStyleField.DRAWPOSITION_BOTTOM );
        }
        layoutChild( child, maxWidth, maxHeight );
        setPositionChild( child, 0, topOffset );
        topOffset += child.getHeight();
      }
    }

    //super.sublayout( maxWidth, maxHeight );

    topOffset += ListStyleField.VPADDING;

    setExtent( maxWidth, topOffset );
    setVirtualExtent( maxWidth, topOffset );
  }


  protected void paintBackground( Graphics g )
  {
    int oldColor = g.getColor();
    try
    {
      g.setColor( AppInfo.BACKGROUND_COLOR );
      g.fillRect( 0, getVerticalScroll(), getWidth(), getHeight() );
    }
    finally
    {
      g.setColor( oldColor );
    }
  }

}
