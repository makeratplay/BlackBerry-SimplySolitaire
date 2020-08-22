package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import java.util.Stack;
import java.util.Vector;

public class CardStack
{
  public static final int CARD_COUNT = 52;

  public static final int TYPE_SUIT_STACK = 0;
  public static final int TYPE_DECK_STACK = 1;
  public static final int TYPE_ROW_STACK  = 2;
  public static final int TYPE_DEALED_STACK = 3;

  protected Stack m_cardStack;
  protected int m_xPos;
  protected int m_yPos;
  protected int m_type;
  protected boolean m_hasFocus;
  protected boolean m_isSelected;
  protected int m_maxHeight;
  protected int m_height;
  protected int m_width;
  protected int m_drawCount;
  protected int m_displayCount; // count of cards to currently display on the dealed stack

  public CardStack( int type )
  {
    m_type = type;
    m_cardStack = new Stack();
    m_xPos = 0;
    m_yPos = 0;
    m_hasFocus = false;
    m_maxHeight = 0;
    m_width = 0;
    m_height = 0;
    //setDrawCount( 3 );
  }

  public void setDrawCount( int value )
  {
    m_drawCount = value;
    m_displayCount = m_drawCount;
  }

  public int setMaxHeight( int maxHeight )
  {
    m_maxHeight = maxHeight;
    return m_maxHeight;
  }

  public int type()
  {
    return m_type;
  }

  public boolean canDrag()
  {
    boolean retVal = false;
    if ( cardCount() > 0 )
    {
      if ( m_type != TYPE_SUIT_STACK && m_type != TYPE_DECK_STACK )
      {
        retVal = true;
      }
    }
    return retVal;
  }

  public boolean hitTest( int x, int y )
  {
    boolean retVal = false;
    if ( x >= m_xPos && x <= m_xPos + m_width )
    {
      if ( y >= m_yPos && y <= m_yPos + m_height )
      {
        retVal = true;
      }
    }
    return retVal;
  }


  public boolean canPlaceCard( CardStack sourceStack )
  {
    boolean retVal = false;
    Card sourceCard = sourceStack.peekTopCard();
    Card destCard = peekTopCard();

    if ( sourceCard != null )
    {
      if ( type() == TYPE_SUIT_STACK  )
      {
        // placing card on suit stack
        if ( destCard == null )
        {
          // first card on suit stack must be an Ace
          if ( sourceCard.faceValue() == 1 )
          {
            retVal = true;
          }
        }
        else if ( sourceCard.faceValue() == destCard.faceValue() + 1 )
        {
          if ( sourceCard.suit() == destCard.suit() )
          {
            retVal = true;
          }
        }
      }

      else if ( type() == TYPE_ROW_STACK )
      {
        // placing card on row stack

        if ( destCard == null )
        {
          // first card on row stack must be a King
          Card tmpCard = sourceStack.findCard( 13 );
          if ( tmpCard != null )
          {
            retVal = true;
          }
        }
        else
        {
          if ( sourceStack.type() == TYPE_DEALED_STACK  ) 
          {
            // pulling card from deck 
            if ( sourceCard.faceValue() + 1 == destCard.faceValue() )
            {
              if ( sourceCard.isRed() != destCard.isRed() )
              {
                retVal = true;
              }
            }
          }
          else if ( sourceStack.type() == TYPE_ROW_STACK )
          {
            Card tmpCard = sourceStack.findCard( destCard.faceValue() - 1 );
            if ( tmpCard != null )
            {
              if ( tmpCard.isRed() != destCard.isRed() )
              {
                retVal = true;
              }
            }
          }
        }
      }
    }

    return retVal;
  }

  // return true if at least one card is face down in this stack
  public boolean hasHiddenCards()
  {
    boolean retVal = false;
    for ( int index = 0;  index < cardCount(); index++ )
    {
      Card tmpCard = (Card)m_cardStack.elementAt( index );
      if ( tmpCard != null )
      {
        if ( !tmpCard.isFaceUp() )
        {
          retVal = true;
          break;
        }
      }
    }
    return retVal;
  }

  private Card findCard( int value )
  {
    Card retVal = null;
    int index = cardCount() - 1;
    while ( index >= 0 )
    {
      Card tmpCard = (Card)m_cardStack.elementAt( index );
      if ( tmpCard != null )
      {
        if ( tmpCard.isFaceUp() )
        {
          if ( tmpCard.faceValue() == value )
          {
            retVal = tmpCard;
            break;
          }
        }
      }
      index--;
    }
    return retVal;
  }


  public boolean doubleClickedCard( Vector cardStacks )
  {
    boolean retVal = false;
    
    // try and move current card to a suit stack
    if ( type() == TYPE_DEALED_STACK || type() == TYPE_ROW_STACK )
    {
      Card sourceCard = peekTopCard();
      if ( sourceCard != null )
      {
        for ( int i = 0; i < cardStacks.size(); i++ )
        {
          CardStack destStack = (CardStack)cardStacks.elementAt( i );
          if ( destStack.type() == TYPE_SUIT_STACK )
          {
            Card destCard = destStack.peekTopCard();
            if ( destCard == null )
            {
              // first card on suit stack must be an Ace
              if ( sourceCard.faceValue() == 1 )
              {
                destStack.addCard( getTopCard() );
                retVal = true;
                break;
              }
            }
            else if ( sourceCard.faceValue() == destCard.faceValue() + 1 )
            {
              if ( sourceCard.suit() == destCard.suit() )
              {
                destStack.addCard( getTopCard() );
                retVal = true;
                break;
              }
            }
          }
        }
      }
    }
    return retVal;
  }


  private boolean moveCards( int value, CardStack destStack )
  {
    boolean retVal = false;

    Stack tmpStack = new Stack();
    int index = cardCount() - 1;
    while ( index >= 0 )
    {
      Card tmpCard = (Card)m_cardStack.elementAt( index );
      if ( tmpCard != null )
      {
        tmpStack.push( m_cardStack.pop() );
        if ( tmpCard.faceValue() == value )
        {
          break;
        }
      }
      index--;
    }

    for ( index = tmpStack.size(); index > 0; index-- )
    {
      destStack.addCard( (Card)tmpStack.pop() );
    }

    Card tmpCard = peekTopCard();
    if ( tmpCard != null )
    {
      if ( !tmpCard.isFaceUp() )
      {
        tmpCard.flip();
      }
    }
    return retVal;
  }

  public void move( int xPos, int yPos )
  {
    m_xPos = xPos;
    m_yPos = yPos;
  }

  public void setFocus()
  {
    m_hasFocus = true;
  }

  public void clearFocus()
  {
    m_hasFocus = false;
  }

  public boolean select()
  {
    if ( m_isSelected )
    {
      m_isSelected = false;
    }
    else if ( cardCount() > 0 )
    {
      m_isSelected = true;
    }
    return m_isSelected;
  }

  public void addCard( Card newCard )
  {
    m_cardStack.push( newCard );
  }

  public int cardCount()
  {
    int count = m_cardStack.size();
    return count;
  }

  public Card getTopCard()
  {
    Card retVal = null;
    if ( cardCount() > 0 )
    {
      retVal = (Card)m_cardStack.pop();

      if ( type() == TYPE_ROW_STACK )
      {
        if ( cardCount() > 0 )
        {
          Card nextCard = (Card)m_cardStack.peek();
          if ( nextCard != null )
          {
            if ( !nextCard.isFaceUp() )
            {
              nextCard.flip();
            }
          }
        }
      }
      else if ( type() == TYPE_DEALED_STACK )
      {
        m_displayCount--;
        if ( m_displayCount < 1 )
        {
          m_displayCount = m_drawCount;
        }
      }
    }
    return retVal;
  }

  public Card peekTopCard()
  {
    Card retVal = null;
    if ( cardCount() > 0 )
    {
      retVal = (Card)m_cardStack.peek();
    }
    return retVal;
  }

  public boolean moveCard( CardStack sourceStack )
  {
    boolean retVal = false; // canPlaceCard( sourceStack );

    Card sourceCard = sourceStack.peekTopCard();
    Card destCard = peekTopCard();

    if ( type() == TYPE_DEALED_STACK )
    {
      return false;
    }

    if ( sourceCard != null )
    {
      if ( type() == TYPE_SUIT_STACK  )
      {
        // placing card on suit stack
        if ( destCard == null )
        {
          // first card on suit stack must be an Ace
          if ( sourceCard.faceValue() == 1 )
          {
            addCard( sourceStack.getTopCard() );
            retVal = true;
          }
        }
        else if ( sourceCard.faceValue() == destCard.faceValue() + 1 )
        {
          if ( sourceCard.suit() == destCard.suit() )
          {
            addCard( sourceStack.getTopCard() );
            retVal = true;
          }
        }
      }

      else if ( type() == TYPE_ROW_STACK )
      {
        // placing card on row stack

        if ( destCard == null )
        {
          // first card on row stack must be a King
          Card tmpCard = sourceStack.findCard( 13 );
          if ( tmpCard != null )
          {
            sourceStack.moveCards( 13, this );
            retVal = true;
          }
        }
        else
        {
          if ( sourceStack.type() == TYPE_DEALED_STACK )
          {
            // pulling card from deck 
            if ( sourceCard.faceValue() + 1 == destCard.faceValue() )
            {
              if ( sourceCard.isRed() != destCard.isRed() )
              {
                addCard( sourceStack.getTopCard() );
                retVal = true;
              }
            }
          }
          else if ( sourceStack.type() == TYPE_ROW_STACK )
          {
            Card tmpCard = sourceStack.findCard( destCard.faceValue() - 1 );
            if ( tmpCard != null )
            {
              if ( tmpCard.isRed() != destCard.isRed() )
              {
                sourceStack.moveCards( destCard.faceValue() - 1, this );
                retVal = true;
              }
            }
          }
        }
      }
    }



    return retVal;
  }

  public void paint( Graphics graphics )
  {
    if ( SolitaireGame.touchScreen )
    {
      // disable focus on touch screen devices
      m_hasFocus = false;
    }

    int cardOffset = 12;
    if ( SolitaireGame.m_optionProperties != null )
    {
      cardOffset = (SolitaireGame.m_optionProperties.getCardOffset() + 1) * 5;
    }
    
    m_height = Card.height;
    m_width = Card.width;
    int x = m_xPos;
    int y = m_yPos;
    int count = cardCount();
    if ( count > 0 )
    {
      switch ( m_type )
      {
        case TYPE_SUIT_STACK:
        case TYPE_DECK_STACK:
        {
          Card tmp = peekTopCard();
          if ( tmp != null )
          {
            tmp.paint( graphics, m_xPos, m_yPos, m_isSelected, m_hasFocus );
          }
          break;
        }

        case TYPE_DEALED_STACK:
        {
          int index = cardCount() - m_displayCount; //m_drawCount;
          if ( index < 0 )
          {
            index = 0;
          }
          
          while ( index < count )
          {
            Card tmp = (Card)m_cardStack.elementAt(index);
            if ( tmp != null )
            {
              if ( index == ( count - 1 ) )
              {
                tmp.paint( graphics, x, m_yPos, m_isSelected, m_hasFocus );
              }
              else
              {
                tmp.paint( graphics, x, m_yPos, false, false );
              }
              x += cardOffset;
            }
            index++;
          }
          //x -= 6;
          break;
        }

        case TYPE_ROW_STACK:
        {
          int index = 0;
          int offsetBy = 5;
          while ( index < count )
          {
            Card tmp = (Card)m_cardStack.elementAt( index );
            if ( tmp != null )
            {
              boolean hasFocus = m_hasFocus;
              boolean isSelected = m_isSelected;
              //if ( !tmp.isFaceUp() )
              if ( index != (count - 1) )
              {
                hasFocus = false;
              }
              if ( tmp.isFaceUp() )
              {
                offsetBy = cardOffset;
              }
              else
              {
                isSelected = false;
              }
              tmp.paint( graphics, m_xPos, y, isSelected, hasFocus );
              y += offsetBy;
            }
            index++;
          }
          //y -= offsetBy;
          break;
        }
      }

      m_height += y - m_yPos;
      m_width += x - m_xPos;
    }
    else
    {
      if ( m_type == TYPE_SUIT_STACK || m_type == TYPE_DECK_STACK )
      {
        int offset = Card.width * 3;
        graphics.drawBitmap( m_xPos, m_yPos, Card.width, Card.blankCard.getHeight(), Card.blankCard, offset, 0 );
      }
      if ( m_hasFocus )
      {
        int offset = Card.width * 2;
        graphics.drawBitmap( m_xPos, m_yPos, Card.width, Card.blankCard.getHeight(), Card.blankCard, offset, 0 );
      }
    }
  }
}
