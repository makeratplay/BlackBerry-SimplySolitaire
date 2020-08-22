package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.system.*;
import net.rim.device.api.util.*;

//The configuration properties of the OptionsSample application. One instance holding
//the effective values resides in the persistent store.
class SSOptionsProperties implements Persistable
{
  private static final int NONE_SCORING     = 0;
  private static final int STANDARD_SCORING = 1;
  private static final int VEGAS_SCORING = 2;


  public static final int BACKGROUND_GREEN = 0;
  public static final int BACKGROUND_GRAY = 1;
  public static final int BACKGROUND_BLUE = 2;
  public static final int BACKGROUND_RED = 3;


  private boolean m_bDrawThree;
  private boolean m_bTimedGame;
  private int m_ScoringType;
  private int m_RedealCount;
  private boolean m_bClickSelect;
  private boolean m_bShowTimer;
  private int m_CardBack;
  private int m_Background;
  private boolean m_bCumulativeScore;
  private int m_CardOffset;
  private int m_DoubleClickSpeed;

  //Hash of com.mlhsoftware.SimplySolitaire.options.SSOptionsProperties
  private static final long PERSISTENCE_ID = 0xe4338fd03e68c1d5L;  

  //Persistent object wrapping the effective properties instance
  private static PersistentObject store;

  //Ensure that an effective properties set exists on startup.
  static
  {
    try
    {
      store = PersistentStore.getPersistentObject( PERSISTENCE_ID );
      synchronized ( store )
      {
        if ( store.getContents() == null )
        {
          store.setContents( new SSOptionsProperties() );
          store.commit();
        }
      }
    }
    catch ( Exception e )
    {

    }
  }

  // Constructs a properties set with default values.
  private SSOptionsProperties()
  {
    m_bDrawThree = true;
    m_bTimedGame = false;
    m_ScoringType = NONE_SCORING;
    m_RedealCount = 0;
    m_bClickSelect = true;
    m_bShowTimer = false;
    m_CardBack = 0;
    m_Background = BACKGROUND_GREEN;
    m_bCumulativeScore = false;
    m_CardOffset = 2;
    m_DoubleClickSpeed = 400;
  }

  //Cannonical copy constructor.
  private SSOptionsProperties( SSOptionsProperties other )
  {
    m_bDrawThree = other.m_bDrawThree;
    m_bTimedGame = other.m_bTimedGame;
    m_ScoringType = other.m_ScoringType;
    m_RedealCount = other.m_RedealCount;
    m_bClickSelect = other.m_bClickSelect;
    m_bShowTimer = other.m_bShowTimer;
    m_CardBack = other.m_CardBack;
    m_Background = other.m_Background;
    m_bCumulativeScore = other.m_bCumulativeScore;
    m_CardOffset = other.m_CardOffset;
    m_DoubleClickSpeed = other.m_DoubleClickSpeed;
  }

  // return true if all values match
  public boolean compare( SSOptionsProperties other )
  {
    boolean retVal = true;

    if ( m_bDrawThree != other.m_bDrawThree )
    {
      retVal = false;
    }

    if ( m_bTimedGame != other.m_bTimedGame )
    {
      retVal = false;
    }

    if ( m_ScoringType != other.m_ScoringType )
    {
      retVal = false;
    }

    if ( m_bCumulativeScore != other.m_bCumulativeScore )
    {
      retVal = false;
    }

    return retVal;
  }




  //Retrieves a copy of the effective properties set from storage.
  public static SSOptionsProperties fetch()
  {
    SSOptionsProperties savedProps = (SSOptionsProperties)store.getContents();
    return new SSOptionsProperties( savedProps );
  }

  //Causes the values within this instance to become the effective
  //properties for the application by saving this instance to the store.
  public void save()
  {
    store.setContents( this );
    store.commit();
  }

  public boolean getDrawThree()
  {
    return m_bDrawThree;
  }

  public void setDrawThree( boolean value )
  {
    m_bDrawThree = value;
  }

  public boolean getTimedGame()
  {
    return m_bTimedGame;
  }

  public void setTimedGame( boolean value )
  {
    m_bTimedGame = value;
  }

  public int getScoringType()
  {
    return m_ScoringType;
  }

  public void setScoringType( int value )
  {
    m_ScoringType = value;
  }

  public boolean getCumulativeScore()
  {
    return m_bCumulativeScore;
  }

  public void setCumulativeScore( boolean value )
  {
    m_bCumulativeScore = value;
  }

  public int getRedealCount()
  {
    if ( m_RedealCount < 0 )
    {
      m_RedealCount = 0;
    }
    return m_RedealCount;
  }

  public void setRedealCount( int value )
  {
    m_RedealCount = value;
  }

  public boolean getClickSelect()
  {
    return m_bClickSelect;
  }

  public void setClickSelect( boolean value )
  {
    m_bClickSelect = value;
  }

  public boolean getShowTimer()
  {
    return m_bShowTimer;
  }

  public void setShowTimer( boolean value )
  {
    m_bShowTimer = value;
  }

  public int getCardBack()
  {
    return m_CardBack;
  }

  public void setCardBack( int value )
  {
    m_CardBack = value;
  }

  public int getBackground()
  {
    return m_Background;
  }

  public void setBackground( int value )
  {
    m_Background = value;
  }

  public int getCardOffset()
  {
    return m_CardOffset;
  }

  public void setCardOffset( int value )
  {
    m_CardOffset = value;
  }

  public int getDoubleClickSpeed()
  {
    return m_DoubleClickSpeed;
  }

  public void setDoubleClickSpeed( int value )
  {
    m_DoubleClickSpeed = value;
  }
  
}
