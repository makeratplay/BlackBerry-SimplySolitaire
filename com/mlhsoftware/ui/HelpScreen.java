/*
 * HelpScreen.java
 *
 * MLH Software
 * Copyright 2010
 * 
 * Dependant Files:
 *    TitlebarManager.java
 *    ForegroundManager.java
 *    ListStyleFieldSet.java
 *    ListStyleRichTextlField.java
 */

package com.mlhsoftware.SimplySolitaire;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.Bitmap;

import net.rim.device.api.ui.container.*;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.device.api.system.CodeModuleManager;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.RadioInfo;

import net.rim.device.api.system.EventLogger;

public class HelpScreen extends MainScreen 
{
  public HelpScreen( String title, String helpText ) 
  {
    super( DEFAULT_MENU | DEFAULT_CLOSE | Manager.NO_VERTICAL_SCROLL );

    // Build the titlebar with Cancel and Save buttons
    TitlebarManager titlebarMgr = new TitlebarManager( title, false );
    add( titlebarMgr );

    ForegroundManager foreground = new ForegroundManager();
    add( foreground );

    ListStyleFieldSet fieldSet = new ListStyleFieldSet();
    foreground.add( fieldSet );

    ListStyleRichTextlField info = new ListStyleRichTextlField( helpText );
    fieldSet.add( info );
  }
}
