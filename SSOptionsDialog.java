//#preprocess
package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;

//#ifdef V47
import net.rim.device.api.ui.VirtualKeyboard;
//#endif

class SSOptionsDialog extends MainScreen
{
  private ChoiceFieldManager drawThreeOption;
  private ChoiceFieldManager backgroundColorOption;
  private ChoiceFieldManager cardOffsetOption;
  private ChoiceFieldManager redealOption;
  private ChoiceFieldManager clickOption;
  //private ObjectChoiceField m_CumulativeScoreOption;

  private SSOptionsProperties optionProperties;

  public SSOptionsDialog()
  {
    super( DEFAULT_MENU | DEFAULT_CLOSE | Manager.NO_VERTICAL_SCROLL );

    boolean bStorm = false;
    //#ifdef V47
    VirtualKeyboard vKeyboard = getVirtualKeyboard();
    if ( vKeyboard != null )
    {
      bStorm = true;
    }
    //#endif 



    //Read in the properties from the persistent store.
    optionProperties = SolitaireGame.m_optionProperties;

    // Build the titlebar
    TitlebarManager titlebarMgr = new TitlebarManager( AppInfo.APP_NAME + " Options", false );
    add( titlebarMgr );

  
    ForegroundManager foreground = new ForegroundManager();
    add( foreground );
 
    ListStyleFieldSet infoSet = new ListStyleFieldSet();
    foreground.add( infoSet );


    Object[] choices1 = { "Yes", "No" };
    int enableIndex = 0;
    if ( !optionProperties.getDrawThree() )
    {
      enableIndex = 1;
    }
    this.drawThreeOption = new ChoiceFieldManager( "Draw Three: ", choices1, enableIndex );
    infoSet.add( this.drawThreeOption );

    Object[] choices4 = { "Unlimited", "1", "2" };
    enableIndex = optionProperties.getRedealCount();
    this.redealOption = new ChoiceFieldManager( "Redeals: ", choices4, enableIndex );
    infoSet.add( this.redealOption );


    Object[] choices2 = { "Green", "Gray", "Blue", "Red" };
    enableIndex = optionProperties.getBackground();
    this.backgroundColorOption = new ChoiceFieldManager( "Background: ", choices2, enableIndex );
    infoSet.add( this.backgroundColorOption );

    Object[] choices3 = { "1", "2", "3", "4" };
    enableIndex = optionProperties.getCardOffset();
    this.cardOffsetOption = new ChoiceFieldManager( "Card Separation: ", choices3, enableIndex );
    infoSet.add( this.cardOffsetOption );

    this.clickOption = null;
    if ( bStorm )
    {
     Object[] choices5 = { "touch", "click" };
     enableIndex = 0;
     if ( optionProperties.getClickSelect() )
     {
       enableIndex = 1;
     }
     this.clickOption = new ChoiceFieldManager( "Select via: ", choices5, enableIndex );
     infoSet.add( this.clickOption );
    }

    ListStyleButtonField saveBtn = new ListStyleButtonField( null, "Save Changes", null );
    saveBtn.setChangeListener( new FieldChangeListener()
    {
      public void fieldChanged( Field field, int context )
      {
        OnDone();
      }
    } );
    infoSet.add( saveBtn );
  }

  public boolean onClose()
  {
    if ( isDirty() )
    {
      int answer = Dialog.ask( Dialog.D_SAVE, "Save Changes?", Dialog.SAVE );
      if ( answer == Dialog.SAVE )
      {
        OnDone();
      }
      else if ( answer == Dialog.DISCARD )
      {
        OnCancel();
      }
    }
    else
    {
      OnCancel();
    }
    return false;
  }

  public void save()
  {
    //Get the new values from the UI controls
    //and set them in optionProperties.
    optionProperties.setDrawThree( this.drawThreeOption.getSelectedIndex() == 0 );
    optionProperties.setBackground( this.backgroundColorOption.getSelectedIndex() );
    optionProperties.setCardOffset( this.cardOffsetOption.getSelectedIndex() );
    optionProperties.setRedealCount( this.redealOption.getSelectedIndex() );
    if ( this.clickOption != null )
    {
      optionProperties.setClickSelect( this.clickOption.getSelectedIndex() == 1 );
    }

    

    //Write our changes back to the persistent store.
    optionProperties.save();

    //Null out our member variables so that their objects can be garbage
    //collected. Note that this instance continues to be held by the
    //options manager even after the user exits the options app,
    //and will be re-used next time.

    this.drawThreeOption = null;
    this.backgroundColorOption = null;
    this.cardOffsetOption = null;
    optionProperties = null;
  }

  private void OnDone()
  {
    save();
    close();
  }

  private void OnCancel()
  {
    close();
  }


  private final class OkButton extends ButtonField
  {
    private OkButton()
    {
      super( "OK", ButtonField.CONSUME_CLICK );
    }

    protected void fieldChangeNotify( int context )
    {
      if ( ( context & FieldChangeListener.PROGRAMMATIC ) == 0 )
      {
        save();
        close();
      }
    }
  }

  /**
   * This inner class simply closes our 'Learn correction' PopupScreen.
   */
  private final class CancelButton extends ButtonField
  {
    private CancelButton()
    {
      super( "Cancel", ButtonField.CONSUME_CLICK );
    }
    protected void fieldChangeNotify( int context )
    {
      if ( ( context & FieldChangeListener.PROGRAMMATIC ) == 0 )
      {
        close();
      }
    }
  }
}
