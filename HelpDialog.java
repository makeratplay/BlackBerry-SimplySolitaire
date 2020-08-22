//#preprocess

package com.mlhsoftware.SimplySolitaire;


import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.Bitmap;

public final class HelpDialog extends Dialog 
{   
    
  public HelpDialog() 
  {
    super( Dialog.D_OK, "Simply Solitaire Help", 1, Bitmap.getPredefinedBitmap( Bitmap.QUESTION ), 0 );



    add( new NullField( NullField.FOCUSABLE ) );
    add( new LabelField( "The four foundations (rectangles in the upper right of screen)"  ) );
    add( new NullField( NullField.FOCUSABLE ) );
    add( new LabelField( "are built up by suit from Ace to King, " ) );
    add( new NullField( NullField.FOCUSABLE ) );
    add( new LabelField( "and the tableau piles (lower row of cards) can be built down by alternate colors, " ) );
    add( new NullField( NullField.FOCUSABLE ) );
    add( new LabelField( "and partial or complete piles can be moved if they are built down by alternate colors also. " ) );
    add( new NullField( NullField.FOCUSABLE ) );
    add( new LabelField( "Any empty piles can be filled with a King or a pile" ) );
    add( new NullField( NullField.FOCUSABLE ) );
    add( new LabelField( "of cards with a King at the top." ) );
    add( new NullField( NullField.FOCUSABLE ) );
  }
}
