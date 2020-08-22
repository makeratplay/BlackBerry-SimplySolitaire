//#preprocess

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.*;
import net.rim.device.api.collection.util.*;

import java.util.Vector;


//#ifdef V47
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.VirtualKeyboard;
//#endif 



/*package*/
final class SolitaireGame extends FullScreen implements ActivationScreen.KeyDlgListener
{
  private SolitaireScreen m_mainScreen;

  public static SSOptionsProperties m_optionProperties = null;
  private int DOUBLE_CLICK_RANGE = 400;

  Deck m_theDeck;
  Vector m_cardStacks;

  static final int TOP_ROW = 10;
  static int MARGIN  = 4;
  public static boolean touchScreen = false;

  private int m_currentFocus;
  private CardStack m_currentSelectedStack;
  private boolean m_gameOver;

  private int m_clickedTime;

  private int m_xTouch;
  private int m_yTouch;
  private int m_xTmpTouch;
  private int m_yTmpTouch;
  private boolean m_dragging;
  private boolean m_startDrag;
  private int m_trialLeft;
  private boolean m_presentExpiredDlg;
  private boolean m_bDisplayUrl;
  private int m_moveCount;
  private boolean m_solved;
  private boolean m_animateFinal;

  public SolitaireGame()
  {
    super(DEFAULT_MENU | DEFAULT_CLOSE);
    m_clickedTime = 0;
    m_bDisplayUrl = true;
    m_moveCount = 0;
    m_animateFinal = false;

    m_optionProperties = SSOptionsProperties.fetch();

    if ( m_optionProperties != null )
    {
      DOUBLE_CLICK_RANGE = m_optionProperties.getDoubleClickSpeed();
    }

    m_xTouch = 0;
    m_yTouch = 0;
    m_dragging = false;
    m_startDrag = false;
    touchScreen = false;
    m_trialLeft = -1;
    m_presentExpiredDlg = false;
    m_cardStacks = null;

    int width = Graphics.getScreenWidth();
 //#ifdef V47
    VirtualKeyboard vKeyboard = getVirtualKeyboard();
    if ( vKeyboard != null )
    {
      vKeyboard.setVisibility( VirtualKeyboard.HIDE_FORCE );
      touchScreen = true;
    }
    Ui.getUiEngineInstance().setAcceptableDirections( Display.DIRECTION_LANDSCAPE );
    if ( Graphics.getScreenHeight() > width )
    {
      width = Graphics.getScreenHeight();
    }
 //#endif 

    MARGIN = ( width - ( Card.width * 7 ) ) / 8;
    newGame();
  }



//#ifdef V47
  protected boolean touchEvent( TouchEvent message )
  {
    if ( m_cardStacks != null )
    {
      // Retrieve the new x and y touch positions.               
      int x = message.getX( 1 );
      int y = message.getY( 1 );

      m_xTouch = x;
      m_yTouch = y;

      int eventCode = message.getEvent();

      switch ( eventCode )
      {
        case TouchEvent.GESTURE:
        {
          TouchGesture gesture = message.getGesture();
          switch ( gesture.getEvent() )
          {
            case TouchGesture.TAP:
            {
              if ( m_optionProperties != null )
              {
                if ( !m_optionProperties.getClickSelect() )
                {
                  userSelect( message.getTime() );
                }
              }
              break;
            }
            case TouchGesture.SWIPE:
            {
              if ( gesture.getSwipeDirection() == TouchGesture.SWIPE_SOUTH )
              {
              }
              break;
            }
          }
          break;
        }
        case TouchEvent.DOWN:
        {
          int newFocus = -1;
          for ( int i = 0; i < m_cardStacks.size(); i++ )
          {
            CardStack tmp = (CardStack)m_cardStacks.elementAt( i );
            if ( tmp.hitTest( x, y ) )
            {
              newFocus = i;
              break;
            }
          }

          if ( newFocus != -1 )
          {
            CardStack tmpStack = (CardStack)m_cardStacks.elementAt( m_currentFocus );
            tmpStack.clearFocus();
            m_currentFocus = newFocus;
            tmpStack = (CardStack)m_cardStacks.elementAt( m_currentFocus );
            tmpStack.setFocus();
            invalidate();

            if ( tmpStack.canDrag() )
            {
              m_startDrag = true;
              m_xTmpTouch = m_xTouch;
              m_yTmpTouch = m_yTouch;
            }
          }
          break;
        }
        case TouchEvent.MOVE:
        {
          if ( !m_dragging && m_startDrag &&
               (m_xTouch > m_xTmpTouch + 3 || m_xTouch < m_xTmpTouch - 3 || 
               m_yTouch > m_yTmpTouch + 3 || m_yTouch < m_yTmpTouch -3 ) )
          {
            m_dragging = true;
          }

          break;
        }
        case TouchEvent.UP:
        {
          int newFocus = -1;
          for ( int i = 0; i < m_cardStacks.size(); i++ )
          {
            CardStack tmp = (CardStack)m_cardStacks.elementAt( i );
            if ( tmp.hitTest( x, y ) )
            {
              newFocus = i;
              break;
            }
          }

          if ( newFocus != -1 )
          {
            if ( newFocus != m_currentFocus )
            {
              CardStack startStack = (CardStack)m_cardStacks.elementAt( m_currentFocus );
              CardStack endStack = (CardStack)m_cardStacks.elementAt( newFocus );
              if ( endStack.moveCard( startStack ) )
              {
                startStack.clearFocus();
              }
            }
            invalidate();
          }

          checkForWin();

          m_dragging = false;
          m_startDrag = false;

          break;
        }
      }

      invalidate();
    }

    return super.touchEvent( message );
  }
  //#endif 

  private void newGame(  )
  {
    deleteAll();
    if ( ActivationKeyStore.GetInstance().betaExpired() )
    {
      if ( !m_presentExpiredDlg )
      {
        add( new LabelField( "Beta has expired.", LabelField.FIELD_HCENTER ) );
      }
      m_presentExpiredDlg = true;
      return;
    }


    if ( !ActivationKeyStore.isKeyValid() )
    {
      if ( ActivationKeyStore.GetInstance().trialExpired() )
      {
        if ( !m_presentExpiredDlg )
        {
          add( new LabelField( "Free trial has expired.", LabelField.FIELD_HCENTER ) );
          add( new BuyButton() );
        }
        m_presentExpiredDlg = true;
        return;
      }
      else
      {
        m_trialLeft = ActivationKeyStore.GetInstance().incTrialCnt();
      }
    }

    m_animateFinal = false;
    m_presentExpiredDlg = false;
    int drawCount = 3;
    if ( m_optionProperties != null )
    {
      if ( !m_optionProperties.getDrawThree() )
      {
        drawCount = 1;
      }
    }

    m_gameOver = false;
    m_currentFocus = 0;
    m_currentSelectedStack = null;

    m_theDeck = new Deck();
    m_theDeck.setDrawCount( drawCount );
    m_cardStacks = new Vector();

    int xPos = MARGIN;
    
    m_theDeck.shuffle();
    m_theDeck.move( xPos, TOP_ROW );

    m_cardStacks.addElement( m_theDeck );

    CardStack tmp = new CardStack( CardStack.TYPE_DEALED_STACK );
    xPos += ( Card.width + MARGIN );
    tmp.move( xPos, TOP_ROW );
    m_theDeck.setDeckStack( tmp );
    m_cardStacks.addElement( tmp );

    xPos += ( ( Card.width + MARGIN ) * 2 );
    for ( int i = 0; i < 4; i++ )
    {
      tmp = new CardStack( CardStack.TYPE_SUIT_STACK );
      tmp.move( xPos, TOP_ROW );
      m_cardStacks.addElement( tmp );
      xPos += ( Card.width + MARGIN );
    }

    xPos = MARGIN;
    for ( int i = 0; i < 7; i++ )
    {
      tmp = new CardStack( CardStack.TYPE_ROW_STACK );
      tmp.move( xPos, TOP_ROW + MARGIN + Card.height );
      m_cardStacks.addElement( tmp );
      xPos += ( Card.width + MARGIN );
    }

    // deal

    for ( int x = 0; x < 7; x++ )
    {
      for ( int i = x; i < 7; i++ )
      {
        tmp = (CardStack)m_cardStacks.elementAt( i + 6 );
        Card card = m_theDeck.dealNextCard();
        if ( i == x )
        {
          card.flip();
        }
        tmp.addCard( card );
      }
    }

    invalidate();
  }


  protected void paint( Graphics graphics )
  {
    int screenWidth = Graphics.getScreenWidth();
    int screenHeight = Graphics.getScreenHeight();

    int backGroundColor = SSOptionsProperties.BACKGROUND_GREEN;
    if ( m_optionProperties != null )
    {
      backGroundColor = m_optionProperties.getBackground();
    }

    switch ( backGroundColor )
    {
      case SSOptionsProperties.BACKGROUND_GREEN:
      {
        graphics.setColor( 0x008000 );
        break;
      }
      case SSOptionsProperties.BACKGROUND_GRAY:
      {
        graphics.setColor( 0xC0C0C0 );
        break;
      }
      case SSOptionsProperties.BACKGROUND_BLUE:
      {
        graphics.setColor( 0x667CFF );
        break;
      }
      case SSOptionsProperties.BACKGROUND_RED:
      {
        graphics.setColor( 0xFF0000 );
        break;
      }
    }
    
    graphics.fillRect( 0, 0, screenWidth, screenHeight );

    //graphics.setColor( 0x249E24 );
    graphics.setColor( 0xFFFFFF );
    Font font = Font.getDefault().derive( Font.PLAIN, 12 );
    int strHeight = font.getHeight();
    graphics.setFont( font );

    int yPos = screenHeight - ( strHeight + 4 );
    String text = "mlhsoftware.com";
    int strWidth = font.getAdvance( text );
    if ( m_bDisplayUrl )
    {
      graphics.drawText( text, ( screenWidth / 2 ) - ( strWidth / 2 ), yPos );
    }


    if ( m_trialLeft != -1 )
    {
      yPos = screenHeight - (( strHeight + 4 ) * 2);
      text = m_trialLeft + " free games left";
      strWidth = font.getAdvance( text );
      graphics.drawText( text, ( screenWidth / 2 ) - ( strWidth / 2 ), yPos );
    }


    if ( !m_presentExpiredDlg && m_cardStacks != null )
    {
      //m_theDeck.paint( graphics );
      for ( int i = 0; i < m_cardStacks.size(); i++ )
      {
        CardStack tmp = (CardStack)m_cardStacks.elementAt( i );
        tmp.paint( graphics );
      }

      if ( m_dragging )
      {
        int halfWidth = Card.width / 2;
        int halfHeigth = Card.height / 2;
        //graphics.setColor( 0x000000 );
        //graphics.drawRect( m_xTouch - halfWidth, m_yTouch - halfHeigth, Card.width, Card.height );

        // draw the blank card face
        int alpha = graphics.getGlobalAlpha();
        graphics.setGlobalAlpha( 150 );
        graphics.drawBitmap( m_xTouch - halfWidth, m_yTouch - halfHeigth, Card.width, Card.height, Card.blankCard, 0, 0 );
        graphics.setGlobalAlpha( alpha );
      }

      /*
      if ( m_xTouch > 0 )
      {
        graphics.setColor( 0x000000 );
        graphics.drawLine( m_xTouch - 10, m_yTouch, m_xTouch + 10, m_yTouch );
        graphics.drawLine( m_xTouch, m_yTouch - 10, m_xTouch, m_yTouch + 10 );
        graphics.drawArc( m_xTouch - 10, m_yTouch - 10, 20, 20, 0, 360 );
      }
       * */

      if ( m_gameOver )
      {
        // Draw text
        graphics.setColor( 0x00000000 );

        font = Font.getDefault().derive( Font.PLAIN, 18 );
        graphics.setFont( font );

        text = "You won!";
        int margin = 20;
        strHeight = font.getHeight();
        strWidth = font.getAdvance( text );

        int x = ( ( screenWidth / 2 ) - ( strWidth / 2 ) ) - margin;
        int y = ( ( screenHeight / 2 ) - 30 ) - margin;

        graphics.setColor( 0x000000 );
        graphics.fillRoundRect( x, y, strWidth + ( margin * 2 ), strHeight + ( margin * 2 ), margin, margin );

        graphics.setColor( 0xFFFFFF );
        graphics.drawRoundRect( x, y, strWidth + ( margin * 2 ), strHeight + ( margin * 2 ), margin, margin );

        graphics.drawText( text, x + margin, y + margin );
      }

    }
    else
    {
      super.paint( graphics );
    }

    if ( m_animateFinal )
    {
      OnFinishGame();
    }

  }

  protected boolean navigationMovement( int dx, int dy, int status, int time )
  {
    if ( m_cardStacks != null )
    {
      CardStack tmpStack = (CardStack)m_cardStacks.elementAt( m_currentFocus );
      tmpStack.clearFocus();

      if ( dx < 0 )
      {
        m_currentFocus--;
        if ( m_currentFocus < 0 )
        {
          m_currentFocus = 0;
        }
      }
      else if ( dx > 0 )
      {
        m_currentFocus++;
        if ( m_currentFocus > m_cardStacks.size() - 1 )
        {
          m_currentFocus = m_cardStacks.size() - 1;
        }
      }

      if ( dy < 0 && m_currentFocus > 5 )
      {
        if ( m_currentFocus < 8 )
        {
          m_currentFocus -= 6;
        }
        else
        {
          m_currentFocus -= 7;
        }
      }
      else if ( dy > 0 && m_currentFocus < 6 )
      {
        if ( m_currentFocus < 2 )
        {
          m_currentFocus += 6;
        }
        else
        {
          m_currentFocus += 7;
        }
      }

      tmpStack = (CardStack)m_cardStacks.elementAt( m_currentFocus );
      tmpStack.setFocus();
      invalidate();

      return true;
    }
    else
    {
      return super.navigationMovement( dx, dy, status, time );
    }
  }


  protected boolean navigationClick( int status, int time )
  {
    boolean retVal = true;
    if ( !m_presentExpiredDlg )
    {
      if ( m_optionProperties != null )
      {
        if ( m_optionProperties.getClickSelect() )
        {
          retVal = userSelect( time );
        }
      }
    }
    else
    {
      retVal = super.navigationClick( status, time );
    }
    return retVal;
  }

  protected boolean userSelect( int time )
  {
    if ( m_cardStacks != null )
    {
      CardStack tmpStack = (CardStack)m_cardStacks.elementAt( m_currentFocus );
      boolean doubleClicked = false;
      boolean cardMoved = false;
      if ( m_clickedTime > 0 )
      {
        if ( time - m_clickedTime < DOUBLE_CLICK_RANGE )
        {
          doubleClicked = true;
        }
      }

      if ( doubleClicked )
      {
        cardMoved = tmpStack.doubleClickedCard( m_cardStacks );
        if ( m_currentSelectedStack != null )
        {
          m_currentSelectedStack.select();
          m_currentSelectedStack = null;
        }
      }

      // no card moved by double click then treat it as a single click
      if ( !cardMoved )
      {
        if ( m_currentSelectedStack == null )
        {
          if ( tmpStack.select() )
          {
            m_currentSelectedStack = tmpStack;
          }
        }
        else
        {
          tmpStack.moveCard( m_currentSelectedStack );
          m_currentSelectedStack.select();
          m_currentSelectedStack = null;
        }
      }

      m_clickedTime = time;
      invalidate();

      checkForWin();

      return true;
    }
    else
    {
      return true; // super.navigationClick( status, time );
    }
  }

  private boolean checkForWin()
  {
    m_moveCount++;

    if ( m_moveCount > 2 && m_bDisplayUrl && m_trialLeft == -1 )
    {
      m_bDisplayUrl = false;
    }

    boolean hasWon = true;
    for ( int i = 0; i < m_cardStacks.size(); i++ )
    {
      CardStack tmp = (CardStack)m_cardStacks.elementAt( i );
      if ( tmp.type() != CardStack.TYPE_SUIT_STACK )
      {
        if ( tmp.cardCount() > 0 )
        {
          hasWon = false;
          break;
        }
      }
    }

    if ( !hasWon && !m_solved )
    {
      // are all cards face up
      m_solved = true;
      for ( int i = 0; i < m_cardStacks.size(); i++ )
      {
        CardStack tmp = (CardStack)m_cardStacks.elementAt( i );
        int type = tmp.type();
        switch( type )
        {
          case CardStack.TYPE_DECK_STACK: 
          case CardStack.TYPE_DEALED_STACK: 
          {
            if ( tmp.cardCount() > 0 )
            {
              m_solved = false;
            }
            break;
          }
          case CardStack.TYPE_ROW_STACK: 
          {
            if ( tmp.hasHiddenCards() )
            {
              m_solved = false;
            }
            break;
          }
        }

        if ( !m_solved )
        {
          break; // exit loop
        }
      }
    }

    if ( hasWon )
    {
      m_solved = false;
      //tmpStack.clearFocus();
      m_gameOver = true;

      /*
      int answer = Dialog.ask( Dialog.D_YES_NO, "Deal Again?" );
      if ( answer == Dialog.YES )
      {
        OnNewGame();
      }
      else
      {
        close();
      }
       */
    }

    return hasWon;
  }

  public void OnNewGame()
  {
    newGame();
  }

  public boolean onClose()
  {
    boolean retVal = false;

    if ( !m_gameOver )
    {
      int answer = Dialog.ask( Dialog.D_YES_NO, "Exit?" );
      if ( answer == Dialog.YES )
      {
        //m_mainScreen.OnEndGame();
        SimplySolitaire._session.close();
        close();
        retVal = true;
      }
    }
    else
    {
      //m_mainScreen.OnEndGame();
      SimplySolitaire._session.close();
      close();
      retVal = true;
    }

    return retVal;
  }

  public void OnHelpMenu()
  {
    HelpDialog helpDialog = new HelpDialog();
    helpDialog.show();
  }

  public void OnAboutMenu()
  {
    AboutScreen dlg = new AboutScreen(); //AboutDialog dlg = new AboutDialog();
    UiApplication.getUiApplication().pushScreen( dlg );      
  }

  public void OnActivate()
  {
    ActivationScreen activationScreen = new ActivationScreen( this );
    UiApplication.getUiApplication().pushScreen( activationScreen );
  }

  public void DlgClosed()
  {
  }

  public void KeyActivated()
  {
    m_trialLeft = -1;
    m_presentExpiredDlg = false;
    newGame();
  }

  public void OnFinishGame()
  {
    checkForWin();
    boolean bDone = false;
    m_animateFinal = false;
    while ( !bDone )
    {
      bDone = true;
      for ( int i = 0; i < m_cardStacks.size(); i++ )
      {
        CardStack tmp = (CardStack)m_cardStacks.elementAt( i );
        if ( tmp.type() != CardStack.TYPE_SUIT_STACK )
        {
          if ( tmp.cardCount() > 0 )
          {
            bDone = false;
            if ( tmp.doubleClickedCard( m_cardStacks ) )
            {
              m_animateFinal = true;
              invalidate();
              return; // exit to allow screen to paint
            }
          }
        }
      }
    }
    
  }

  public void OnOptionsMenu()
  {
    SSOptionsDialog optionsDls = new SSOptionsDialog();
    UiApplication.getUiApplication().pushScreen( optionsDls );
  }

  protected void makeMenu( Menu menu, int instance )
  {
    MenuItem finishGameMenu = new MenuItem( "Finish Game", 50, 50 )
    {
      public void run()
      {
        OnFinishGame();
      }
    };

    
    MenuItem newMenu = new MenuItem( "New Game", 50, 50 )
    {
      public void run()
      {
        OnNewGame();
      }
    };

    MenuItem activateMenu = new MenuItem( "Buy/Activate", 50, 50 )
    {
      public void run()
      {
        OnActivate();
      }
    };


    MenuItem optionsMenu = new MenuItem( "Options", 50, 50 )
    {
      public void run()
      {
        OnOptionsMenu();
      }
    };

    MenuItem aboutMenu = new MenuItem( "About", 50, 50 )
    {
      public void run()
      {
        OnAboutMenu();
      }
    };

    MenuItem hideMenu = new MenuItem( "Hide", 50, 50 )
    {
      public void run()
      {
        UiApplication.getUiApplication().requestBackground();
      }
    };



    if ( m_solved )
    {
      menu.add( finishGameMenu );
    }

    menu.add( newMenu );

    if ( !ActivationKeyStore.isKeyValid() )
    {
      menu.add( activateMenu );
    }
    menu.add( optionsMenu );
    menu.add( aboutMenu );
    menu.addSeparator();
    menu.add( hideMenu );

    super.makeMenu( menu, instance );
  }

  private final class BuyButton extends ButtonField
  {
    private BuyButton()
    {
      super( "Buy / Activate", ButtonField.CONSUME_CLICK | LabelField.USE_ALL_WIDTH | LabelField.FIELD_HCENTER );
    }

    protected void fieldChangeNotify( int context )
    {
      if ( ( context & FieldChangeListener.PROGRAMMATIC ) == 0 )
      {
        OnActivate();
      }
    }
  }
}
