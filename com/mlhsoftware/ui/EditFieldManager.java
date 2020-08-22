/*
 * EditFieldManager.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;

/**
 * 
 */
public class EditFieldManager extends ListStyleManager
{
  private AutoTextEditField editField;
  private MyLabelField labelField;
  private String labelText;
  private FieldChangeListener listener;
  private Bitmap leftIcon;

  public EditFieldManager( String initialValue, String labelText, int maxNumChars, long style )
  {
    this( null, initialValue, labelText, maxNumChars, style  );
  }

  public EditFieldManager( Bitmap leftIcon, String initialValue, String labelText, int maxNumChars, long style  )
  {
    super( USE_ALL_WIDTH );

    this.leftIcon = leftIcon;
    this.listener = null;
    this.labelText = labelText;
    if ( initialValue.length() > 0 )
    {
      this.labelField = new MyLabelField( "", 0 );
    }
    else
    {
      this.labelField = new MyLabelField( labelText, 0 );
    }
    this.labelField.setFont( ListStyleField.FONT_LEFT_LABLE );
    add( this.labelField );


    this.editField = new AutoTextEditField( null, initialValue, maxNumChars, style );
    this.editField.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        onFieldChanged( field, context );
      }
    } );

    this.editField.setFont( ListStyleField.FONT_LEFT_LABLE );
    add( this.editField );
  }

  public void setChangeListener( FieldChangeListener listener )
  {
    this.listener = listener;
  }

  public String toString()
  {
    return this.editField.toString();
  }

  public void setText( String text )
  {
    this.editField.setText( text );
  }

  protected void sublayout( int width, int height )
  {
    int finalHeight = 0;
    int topPos = ListStyleField.VPADDING;

    int labelWidth = width;

    int leftOffset = ListStyleField.HPADDING;
    if ( this.leftIcon != null )
    {
      leftOffset += this.leftIcon.getWidth() + ListStyleField.HPADDING;
      finalHeight = Math.max( finalHeight, this.leftIcon.getHeight() + ListStyleField.VPADDING + ListStyleField.VPADDING );
    }


    labelWidth -= leftOffset;
    labelWidth -= ListStyleField.HPADDING;

    layoutChild( this.editField, labelWidth, height );
    finalHeight = Math.max( finalHeight, this.editField.getHeight() + ListStyleField.VPADDING + ListStyleField.VPADDING );

    layoutChild( this.labelField, labelWidth, height );
    finalHeight = Math.max( finalHeight, this.labelField.getHeight() + ListStyleField.VPADDING + ListStyleField.VPADDING );

    topPos = ( finalHeight - this.editField.getHeight() ) / 2;

    setPositionChild( this.editField, leftOffset, topPos );
    setPositionChild( this.labelField, this.editField.getLeft() + 5, topPos );

    setExtent( width, finalHeight );
  }

  protected void paint( Graphics g )
  {
    super.paint( g );

    // Left Bitmap
    if ( this.leftIcon != null )
    {
      g.drawBitmap( ListStyleField.HPADDING, ListStyleField.VPADDING, this.leftIcon.getWidth(), this.leftIcon.getHeight(), this.leftIcon, 0, 0 );
    }
  }

  private void onFieldChanged( Field field, int context )
  {
    setDirty( true );
    String currentText = this.editField.getText();

    if ( currentText.length() == 0 )
    {
      this.labelField.setText( this.labelText );
    }
    else
    {
      this.labelField.setText( "" );
    }

    if ( this.listener != null )
    {
      this.listener.fieldChanged( field, context );
    }

    fieldChangeNotify( 0 );
  }

  private static class MyLabelField extends LabelField
  {

    public MyLabelField( String text, long style )
    {
      super( text, style );
    }

    public void paint( Graphics g )
    {
      // change font to grey
      int currentColor = g.getColor();
      g.setColor( 0x00AAAAAA );
      super.paint( g );
      g.setColor( currentColor );
    }
  }
}
