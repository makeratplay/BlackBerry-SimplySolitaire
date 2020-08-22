/*
 * EditFieldManager.java
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

/**
 * 
 */
public class ChoiceFieldManager extends ListStyleManager
{
  private ObjectChoiceField objectChoiceField;
  private FieldChangeListener _listener;

  public ChoiceFieldManager( String labelText, Object[] appChoices, int initialValue  )
  {
    super( USE_ALL_WIDTH );

    _listener = null;

    this.objectChoiceField = new ObjectChoiceField( labelText, appChoices, initialValue );
    this.objectChoiceField.setFont( ListStyleField.FONT_LEFT_LABLE );
    add( this.objectChoiceField );
  }

  public void setChangeListener( FieldChangeListener listener )
  {
    _listener = listener;
  }

  public String toString()
  {
    return this.objectChoiceField.toString();
  }

  public int getSelectedIndex()
  {
    return this.objectChoiceField.getSelectedIndex();
  }

  protected void sublayout( int width, int height )
  {
    int finalVPadding = getVPadding();
    int topPos = getTopPos();

    layoutChild( this.objectChoiceField, width - ( ListStyleField.HPADDING * 4 ), height - finalVPadding );
    setPositionChild( this.objectChoiceField, ( width - this.objectChoiceField.getWidth() ) / 2, topPos );

    setExtent( width, this.objectChoiceField.getHeight() + finalVPadding );
  }

  private void onFieldChanged( Field field, int context )
  {
    if ( _listener != null )
    {
      _listener.fieldChanged( field, context );
    }

    fieldChangeNotify( 0 );
  }
}
