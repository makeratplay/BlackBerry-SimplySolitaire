package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import java.util.Stack;
import java.util.Random;
import java.util.Date;

public class Deck extends CardStack
{
  public static final int CARD_COUNT  = 52;

  private CardStack m_deckStack;
  private int m_redealCount;

  public Deck()
  {
    super( CardStack.TYPE_DECK_STACK );
    m_redealCount = 1;
    m_deckStack = null;

    for ( int i = 0; i < CARD_COUNT; i++ )
    {
      Card newCard = new Card( i );
      addCard( newCard );
    }
  }

  public void setDeckStack( CardStack stack )
  {
    m_deckStack = stack;
  }

  public boolean select()
  {


    Card card = null;
    if ( m_deckStack != null )
    {
      // reset deck, all cards have to drawn
      if ( cardCount() == 0 )
      {
        m_redealCount++;

        if ( SolitaireGame.m_optionProperties != null )
        {
          int redealLimit = SolitaireGame.m_optionProperties.getRedealCount();
          if ( redealLimit > 0 )
          {
            if ( m_redealCount > redealLimit )
            {
              return false; // redeal limit reached
            }
          }
        }

        do
        {
          card = m_deckStack.getTopCard();
          if ( card != null )
          {
            card.flip();
            addCard( card );
          }
        } while ( card != null );
      }
      else
      {
        int loopCnt = m_drawCount;
        if ( cardCount() < m_drawCount )
        {
          loopCnt = cardCount();
        }
        for ( int drawCount = 0; drawCount < loopCnt; drawCount++ )
        {
          card = dealNextCard();
          if ( card != null )
          {
            card.flip();
            m_deckStack.addCard( card );
          }
        }
        m_deckStack.setDrawCount( loopCnt ); // really rest m_displayCount

      }
    }
    return false;
  }

  public void shuffle()
  {
    Random rndGenerator;
    rndGenerator = new Random( new Date().getTime() );

    for ( int loop = cardCount() - 1; loop > 0; loop-- )
    {
      int index = rndGenerator.nextInt( loop + 1 );
      Card tmpCard = (Card)m_cardStack.elementAt( index );
      m_cardStack.removeElementAt( index );
      addCard( tmpCard );
    }
  }


  public Card dealNextCard()
  {
    Card card = null;
    if ( cardCount() > 0 )
    {
      card = (Card)m_cardStack.pop();
    }
    return card;
  }
}
