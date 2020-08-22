/*
 * ListStyleButtonField.java
 *
 * MLH Software
 * Copyright 2010
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;


public class ListStyleButtonField extends ListStyleField implements Runnable
{

  private Bitmap leftIcon = null;
  private Bitmap actionIcon = null;
  private Bitmap badgeIcon = null;

  private int labelTop;
  private int labelWidth;
  private int labelLeft;
  private String labelText;
  private Font lableFont = null;
  

  // Progress Animation info
  private Bitmap progressBitmap;
  private int numFrames;
  private int frameWidth;
  private int currentFrame;
  private int timerID = -1;
  private Application application;
  private boolean animate = false;
  private boolean visible = true;

  public ListStyleButtonField( String label, Bitmap actionIcon )
  {
    this( null, label, actionIcon, 0 );
  }

  public ListStyleButtonField( Bitmap icon, String label, Bitmap actionIcon )
  {
    this( icon, label, actionIcon, 0 );
  }

  public ListStyleButtonField( Bitmap icon, String label, Bitmap actionIcon, long style )
  {
    super( USE_ALL_WIDTH | Field.FOCUSABLE );

    this.labelText = label;
    this.actionIcon = actionIcon;
    this.leftIcon = icon;
    this.animate = false;
    this.timerID = -1;
  }

  public void setLabelText( String label )
  {
    this.labelText = label;
  }

  public void setIcon( Bitmap icon )
  {
    this.leftIcon = icon;
  }

  public void setBadgeIcon( Bitmap icon )
  {
    this.badgeIcon = icon;
  }

  public void setProgressAnimationInfo( Bitmap bitmap, int numFrames )
  {
    this.progressBitmap = bitmap;
    this.numFrames = numFrames;
    this.frameWidth = this.progressBitmap.getWidth() / this.numFrames;
    this.application = Application.getApplication();
    this.animate = false;
    this.visible = true;
    this.timerID = -1;
  }

  public void startAnimation()
  {
    this.animate = true;
    startTimer();
  }

  public void stopAnimation()
  {
    this.animate = false;
    stopTimer();
  }

  private void startTimer()
  {
    if ( this.timerID == -1 && this.animate == true )
    {
      this.timerID = this.application.invokeLater( this, 200, true );
    }
  }

  private void stopTimer()
  {
    if ( this.timerID != -1 )
    {
      this.application.cancelInvokeLater( this.timerID );
      this.timerID = -1;
    }
  }

  public void run()
  {
    if ( this.visible )
    {
      invalidate();
    }
  }

  protected void onDisplay()
  {
    super.onDisplay();
    this.visible = true;
    startTimer();
  }

  protected void onUndisplay()
  {
    super.onUndisplay();
    this.visible = false;
    stopTimer();
  }

  public boolean isFocusable()
  {
    return true;
  }

  //protected void sublayout( int width, int height )
  public void layout( int width, int height )
  {
    int finalHeight = 0;
    int topPos = ListStyleField.VPADDING;

    this.labelWidth = width;

    this.labelLeft = ListStyleField.HPADDING + ListStyleField.HPADDING;
    if ( this.leftIcon != null )
    {
      this.labelLeft += this.leftIcon.getWidth();
      finalHeight = Math.max( finalHeight, this.leftIcon.getHeight() + ListStyleField.VPADDING + ListStyleField.VPADDING );
    }

    int rightOffset = ListStyleField.HPADDING;
    if ( this.actionIcon != null )
    {
      rightOffset += this.actionIcon.getWidth() + ListStyleField.HPADDING;
      finalHeight = Math.max( finalHeight, this.actionIcon.getHeight() + ListStyleField.VPADDING + ListStyleField.VPADDING );
    }

    this.labelWidth -= this.labelLeft;
    this.labelWidth -= rightOffset;


    int fontSize = sizeFont( this.labelText, this.labelWidth );
    this.lableFont = FONT_LEFT_LABLE.derive( FONT_LEFT_LABLE.isBold() ? Font.BOLD : Font.PLAIN, fontSize, Ui.UNITS_px );

    finalHeight = Math.max( finalHeight, FONT_LEFT_LABLE.getHeight() + ListStyleField.VPADDING + ListStyleField.VPADDING );

    this.labelTop = ( finalHeight - FONT_LEFT_LABLE.getHeight() ) / 2;

    setExtent( width, finalHeight );
  }

  protected void paint( Graphics g )
  {
    int oldColor = g.getColor();
    Font oldFont = g.getFont();
    try
    {
      int topPos = ListStyleField.VPADDING;
      
      // Left Bitmap
      if ( this.leftIcon != null )
      {
        g.drawBitmap( ListStyleField.HPADDING, ListStyleField.VPADDING, this.leftIcon.getWidth(), this.leftIcon.getHeight(), this.leftIcon, 0, 0 );

      }

      // Animated Bitmap
      if ( this.animate == true && this.progressBitmap != null )
      {
        g.drawBitmap( getWidth() - ( this.frameWidth + ListStyleField.HPADDING ), ( getHeight() - this.actionIcon.getHeight() ) / 2, this.frameWidth, this.progressBitmap.getHeight(), this.progressBitmap, this.frameWidth * this.currentFrame, 0 );
        this.currentFrame++;
        if ( this.currentFrame >= this.numFrames )
        {
          this.currentFrame = 0;
        }
      }

      // Right (Action) Bitmap
      else if ( this.actionIcon != null )
      {
        g.drawBitmap( getWidth() - ( this.actionIcon.getWidth() + ListStyleField.HPADDING ), ( getHeight() - this.actionIcon.getHeight() ) / 2, this.actionIcon.getWidth(), this.actionIcon.getHeight(), this.actionIcon, 0, 0 );
      }

      int labelWidth = this.labelWidth;

      if ( this.badgeIcon != null )
      {
        labelWidth -= this.badgeIcon.getWidth();
      }


      // Left Label Text
      int leftPos = this.labelLeft;
      g.setColor( COLOR_LEFT_LABEL_FONT );
      g.setFont( this.lableFont );
      g.drawText( this.labelText, leftPos, this.labelTop, DrawStyle.ELLIPSIS, labelWidth );
      leftPos += labelWidth;

      // Badge icon (to the left of Action bitmap)
      if ( this.badgeIcon != null )
      {
        g.drawBitmap( leftPos, ( getHeight() - this.badgeIcon.getHeight() ) / 2, this.badgeIcon.getWidth(), this.badgeIcon.getHeight(), this.badgeIcon, 0, 0 );
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

  //#ifndef VER_4.1.0 | 4.0.0
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
  //#endif        

  /**
     * A public way to click this button
     */
  public void clickButton()
  {
    fieldChangeNotify( 0 );
  }

  /*       
//#ifndef VER_4.6.1 | VER_4.6.0 | VER_4.5.0 | VER_4.2.1 | VER_4.2.0
    protected boolean touchEvent( TouchEvent message )
    {
        int x = message.getX( 1 );
        int y = message.getY( 1 );
        if( x < 0 || y < 0 || x > getExtent().width || y > getExtent().height ) {
            // Outside the field
            return false;
        }
        switch( message.getEvent() ) {
       
            case TouchEvent.UNCLICK:
                clickButton();
                return true;
        }
        return super.touchEvent( message );
    }
//#endif 
*/

  public boolean isDirty()
  {
    return false;
  }

  public boolean isMuddy()
  {
    return false;
  }

  public String toString()
  {
    return this.labelText;
  }
}



