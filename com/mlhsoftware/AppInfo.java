//#preprocess
/*
 * WebApiBase.java
 *
 * MLH Software
 * Copyright 2010
 * 
 */

package com.mlhsoftware.SimplySolitaire;

public class AppInfo
{
  static public boolean APPWORLD = false;
  static public boolean BETA = false;
  
  //#ifdef V47
  static public boolean V47 = true;
  static public boolean TOUCHSCREEN = true;
  //#else
  static public boolean V47 = false;
  static public boolean TOUCHSCREEN = false;
  //#endif


  static public String APP_VERSION = "1.0.0.3";

  static public String APP_NAME = "Simply Solitaire";
  static public long LOGGER_ID = 0xa0467ae36a5aa547L;      //com.mlhsoftware.SimplySolitaire

  static public String SECRET_KEY = "*MLHSOLITAIRE011410*"; // this app is no longer sold so you can have the key
  static public long PERSISTENCE_ID = 0x5e65ff560f7b8fb7L;   //Hash of com.mlhsoftware.SimplySolitaire.keycode

  static public int MAX_TRIALS = 11;
  static public String MOBIHAND_PID = "40759";
  static public String APPWORLD_PID = "6973";

  static public String APP_URL = "http://m.mlhsoftware.com";
  static public String HELP_URL = "http://www.mlhsoftware.com/SimplySolitaire";
  static public String TELL_A_FRIEND = "Simply Solitaire for your BlackBerry, check it out at http://www.mlhsoftware.com";

  public static final int BACKGROUND_COLOR = 0x00008000;
  public static final int COLOR_BORDER = 0x222222;

  // Titlebar colors
  public static int[] TITLEBAR_COLORS = { 0x006C6C6A, 0x006C6C6A, 0x00ACAEAB, 0x00ACAEAB };
  public static final int TITLEBAR_TOPLINE_COLORS = 0x6F6F6F;
  public static final int TITLEBAR_BOTTOMLINE_COLORS = 0x444645;
  public static final int TITLEBAR_FONT_SIZE = 8;
}
