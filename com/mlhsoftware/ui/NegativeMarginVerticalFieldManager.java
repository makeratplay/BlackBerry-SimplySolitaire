package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;

/**
 * A basic vertical field manager that supports negative vertical margins
 * It also supports horizontal style bits.
 */
public class NegativeMarginVerticalFieldManager extends Manager
{
  private static final int MAX_EXTENT = Integer.MAX_VALUE >> 1;

  public NegativeMarginVerticalFieldManager( long style )
  {
    super( style );
  }

  protected void sublayout( int maxWidth, int maxHeight )
  {
    Field field;
    int width = 0;
    int height = 0;

    // how much height do we have?
    int heightAvail = maxHeight;
    int widthAvail = maxWidth;

    if ( isStyle( Manager.VERTICAL_SCROLL ) && !isStyle( Manager.NO_VERTICAL_SCROLL ) )
    {
      heightAvail = MAX_EXTENT;
    }

    int prevMarginBottom = 0;
    int marginTop = 0;
    int marginBottom = 0;
    int marginHorizontal = 0;
    int numFields = this.getFieldCount();

    int fieldMarginLeft = 5;
    int fieldMarginRight = 5;
    int fieldMarginTop = 5;
    int fieldMarginBottom = 5;

    for ( int i = 0; i < numFields; ++i )
    {
      field = getField( i );
     
      marginHorizontal = fieldMarginLeft + fieldMarginRight;
      marginTop = calculateVerticalMargin( prevMarginBottom, fieldMarginTop );
      marginBottom = fieldMarginBottom;

      layoutChild( field, widthAvail - marginHorizontal, heightAvail - marginTop - marginBottom );

      heightAvail -= field.getHeight() + marginTop;
      height += field.getHeight() + marginTop;

      prevMarginBottom = marginBottom;

      // remember the largest width
      int marginAndWidth = marginHorizontal + field.getWidth();
      if ( marginAndWidth > width )
      {
        width = marginAndWidth;
      }
    }
    height += prevMarginBottom;

    if ( width < maxWidth && isStyle( Field.USE_ALL_WIDTH ) )
    {
      width = maxWidth;
    }

    if ( height < maxHeight && isStyle( Field.USE_ALL_HEIGHT ) )
    {
      height = maxHeight;
    }

    setVirtualExtent( width, height );

    // Set positions
    int x = 0;
    int y = 0;
    prevMarginBottom = 0;
    for ( int i = 0; i < numFields; ++i )
    {
      field = getField( i );

      marginTop = calculateVerticalMargin( prevMarginBottom, fieldMarginTop );

      if ( field.isStyle( Field.FIELD_HCENTER ) )
      {
        x = ( width - field.getWidth() ) / 2;
      }
      else if ( field.isStyle( Field.FIELD_RIGHT ) )
      {
        x = width - field.getWidth() - fieldMarginRight;
      }
      else
      {
        // Field.FIELD_LEFT
        x = fieldMarginLeft;
      }

      setPositionChild( field, x, y + marginTop );

      y += field.getHeight() + marginTop;
      prevMarginBottom = fieldMarginBottom;
    }

    setExtent( Math.min( width, maxWidth ), Math.min( height, maxHeight ) );
  }

  /**
     * To account for negative margins
     */
  private int calculateVerticalMargin( int prevMarginBottom, int marginTop )
  {
    int max = Math.max( prevMarginBottom, marginTop );
    int sum = prevMarginBottom + marginTop;
    if ( sum < max )
    {
      max += ( sum - max );
    }
    return max;
  }

}
