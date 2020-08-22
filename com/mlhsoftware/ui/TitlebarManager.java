package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;

/**
 * 
 */
public class TitlebarManager extends Manager
{
  private Field _selectedField;

  //ADBACB
  //6F86A4

  private static final int LEFT_MARGIN = 4;
  private static final int TOP_MARGIN = 4;
  private static final int BOTTOM_MARGIN = 4;
  private static final int RIGHT_MARGIN = 4;

  //public static final int[] cols = { 0x00ADBACB, 0x00ADBACB, 0x006F86A4, 0x006F86A4 };  
  public static int[] colors = { 0x00484D54, 0x00484D54, 0x001F252D, 0x001F252D };

  public static final Font BUTTON_FONT = ListStyleField.FONT_LEFT_LABLE; // .derive( Font.BOLD, 7, Ui.UNITS_pt, Font.ANTIALIAS_SUBPIXEL, 0 );
 

  private static final int SYSTEM_STYLE_SHIFT = 32;

  private ButtonField leftBtn = null;
  private ButtonField rightBtn = null;
  private LabelField labelField = null;
  private Font font = null;

  private boolean center = true;


  public TitlebarManager( String titleText, boolean center )
  {
    super( 0 );
    this.center = center;
    if ( titleText != null )
    {
      this.labelField = new LabelField( titleText );
      this.labelField.setFont( ListStyleField.FONT_LEFT_LABLE );
      add( this.labelField );
    }
  }

  public TitlebarManager( String titleText, String leftBtnText, String rightBtnText )
  {
    super( 0 );

    if ( leftBtnText != null )
    {
      this.leftBtn = new ButtonField( leftBtnText, ButtonField.CONSUME_CLICK );
      this.leftBtn.setFont( BUTTON_FONT );
      add( this.leftBtn );
    }

    if ( titleText != null )
    {
      this.labelField = new LabelField( titleText );
      this.labelField.setFont( ListStyleField.FONT_LEFT_LABLE );
      add( this.labelField );
    }

    if ( rightBtnText != null )
    {
      this.rightBtn = new ButtonField( rightBtnText, ButtonField.CONSUME_CLICK );
      this.rightBtn.setFont( BUTTON_FONT );
      add( this.rightBtn );
    }
  }

  public TitlebarManager( String titleText, Field leftField, Field rightField )
  {
    super( 0 );

    if ( leftField != null )
    {
      add( leftField );
    }

    if ( titleText != null )
    {
      this.labelField = new LabelField( titleText );
      this.labelField.setFont( ListStyleField.FONT_LEFT_LABLE );
      add( this.labelField );
    }

    if ( rightField != null )
    {
      add( rightField );
    }
  }

  public void setTitleText( String text )
  {
    this.labelField.setText( text );
  }


  public void setBackgroundColor( int color1, int color2, int color3, int color4 )
  {
    colors[0] = color1;
    colors[1] = color2;
    colors[2] = color3;
    colors[3] = color4;
  }

  public void handleLeftBtn( FieldChangeListener listener )
  {
    if ( this.leftBtn != null )
    {
      this.leftBtn.setChangeListener( listener );
    }
  }

  public void handleRightBtn( FieldChangeListener listener )
  {
    if ( this.rightBtn != null )
    {
      this.rightBtn.setChangeListener( listener );
    }
  }

  public TitlebarManager( long style )
  {
    super( USE_ALL_WIDTH | style );
  }

  public void paint( Graphics g )
  {
    int oldColour = g.getColor();
    try
    {
      /*
      try
      {
        if ( this.labelField != null )
        {
          this.labelField.setFont( this.font );
        }
        if ( this.leftBtn != null )
        {
          this.leftBtn.setFont( BUTTON_FONT );
        }
        if ( this.rightBtn != null )
        {
          this.rightBtn.setFont( BUTTON_FONT );
        }
      }
      catch ( Exception e )
      {
        String msg = "setFont failed: " + e.toString();
        System.out.println( msg );
      }
      */

      //paintBackground( g );
      //g.clear();
      g.setColor( Color.WHITE );
      super.paint( g );
    }
    finally
    {
      g.setColor( oldColour );
    }
  }


  protected void paintBackground( Graphics g )
  {
    super.paintBackground( g );
    // Sets the BackgroundColor
    //XYRect redrawRect = g.getClippingRect();
    //int[] yInds = new int[] { redrawRect.y, redrawRect.y, redrawRect.height, redrawRect.height };
    //int[] xInds = new int[] { redrawRect.x, redrawRect.width, redrawRect.width, redrawRect.x };

    int[] yInds = new int[] { 0, 0, getHeight(), getHeight() };
    int[] xInds = new int[] { 0, getWidth(), getWidth(), 0 };
    g.drawShadedFilledPath( xInds, yInds, null, colors, null );
  }

  protected void sublayout( int width, int height )
  {
    int availableWidth = width - ( LEFT_MARGIN + RIGHT_MARGIN );
    int availableHeight = height - ( TOP_MARGIN + BOTTOM_MARGIN );

    int numFields = getFieldCount();
    int maxPreferredWidth = 0;
    int maxHeight = 0;


    // There may be a few remaining pixels after dividing up the space
    // we must split up the space between the first and last buttons
    int fieldWidth = availableWidth / numFields;
    int firstFieldExtra = 0;
    int lastFieldExtra = 0;

    int unUsedWidth = availableWidth - fieldWidth * numFields;
    if ( unUsedWidth > 0 )
    {
      firstFieldExtra = unUsedWidth / 2;
      lastFieldExtra = unUsedWidth - firstFieldExtra;
    }


    int maxFieldWidth = 0;

    // Position the Right Field
    if ( numFields == 3 )
    {
      Field rightField = getField( 2 );
      layoutChild( rightField, fieldWidth + lastFieldExtra, availableHeight );
      maxHeight = rightField.getHeight() + ( TOP_MARGIN + BOTTOM_MARGIN );
      maxFieldWidth = rightField.getWidth();
      //setPositionChild( rightField, width - ( rightField.getWidth() + RIGHT_MARGIN ), ( maxHeight - rightField.getHeight() ) / 2 );
    }

    // Position the Left Field
    if ( numFields > 1 )
    {
      Field leftField = getField( 0 );
      layoutChild( leftField, fieldWidth + firstFieldExtra, availableHeight );
      maxHeight = Math.max( maxHeight, leftField.getHeight() + ( TOP_MARGIN + BOTTOM_MARGIN ) );
      maxFieldWidth = Math.max( maxFieldWidth, leftField.getWidth() );
      //setPositionChild( leftField, LEFT_MARGIN, ( maxHeight - leftField.getHeight() ) / 2 );
    }


    Field centerField = null;
    if ( numFields == 1 )
    {
      centerField = getField( 0 );
    }
    else
    {
      centerField = getField( 1 );
    }

    // Position the Center Field
    if ( centerField != null )
    {
      if ( this.center )
      {
        int fontSize = ListStyleField.FONT_LEFT_LABLE.getHeight();
        String titleText = centerField.toString();
        this.font = ListStyleField.FONT_LEFT_LABLE.derive( Font.BOLD, fontSize, Ui.UNITS_px );
        int centerWidth = this.font.getAdvance( titleText );
        int availableCenterWidth = availableWidth - ( ( maxFieldWidth * 2 ) + ( LEFT_MARGIN * 2 ) );
        while ( centerWidth > availableCenterWidth && fontSize > 5 )
        {
          fontSize--;
          this.font = ListStyleField.FONT_LEFT_LABLE.derive( Font.BOLD, fontSize, Ui.UNITS_px );
          centerWidth = this.font.getAdvance( titleText );
        }


        layoutChild( centerField, availableCenterWidth, availableHeight );
        maxHeight = Math.max( maxHeight, centerField.getHeight() + ( TOP_MARGIN + BOTTOM_MARGIN ) );
        setPositionChild( centerField, ( availableWidth - centerField.getWidth() ) / 2, ( maxHeight - centerField.getHeight() ) / 2 );
      }
      else
      {
        layoutChild( centerField, availableWidth, availableHeight );
        maxHeight = Math.max( maxHeight, centerField.getHeight() + ( TOP_MARGIN + BOTTOM_MARGIN ) );
        setPositionChild( centerField, LEFT_MARGIN, ( maxHeight - centerField.getHeight() ) / 2 );
      }

    }

    if ( numFields == 3 )
    {
      Field rightField = getField( 2 );
      setPositionChild( rightField, width - ( rightField.getWidth() + RIGHT_MARGIN ), ( maxHeight - rightField.getHeight() ) / 2 );
    }

    // Position the Left Field
    if ( numFields > 1 )
    {
      Field leftField = getField( 0 );
      setPositionChild( leftField, LEFT_MARGIN, ( maxHeight - leftField.getHeight() ) / 2 );
    }

    setExtent( width, maxHeight );
  }

  public boolean isDirty()
  {
    return false;
  }

  public boolean isMuddy()
  {
    return false;
  }
}
