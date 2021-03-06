/*
 * ListStyleLabelField.java
 *
 * MLH Software
 * Copyright 2010
 * 
 * Dependant Files:
 *    ListStyleManager.java
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;


public class ListStyleRichTextlField extends ListStyleManager
{

  private RichTextField _richTextField;

  public ListStyleRichTextlField( String text )
  {
    super( USE_ALL_WIDTH | Field.NON_FOCUSABLE );

    _richTextField = new RichTextField(text);
    add( _richTextField );
  }



  protected void sublayout( int width, int height )
  //public void layout( int width, int height )
  {
    int finalVPadding = getVPadding();
    int topPos = getTopPos();

    layoutChild( _richTextField, width - ( ListStyleField.HPADDING * 4 ), height - finalVPadding );
    setPositionChild( _richTextField, ( width - _richTextField.getWidth() ) / 2, topPos );
    setExtent( width, _richTextField.getHeight() + finalVPadding );
  }

  protected void paint( Graphics g )
  {
    super.paint( g );
  }
}



