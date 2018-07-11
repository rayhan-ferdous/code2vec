import javax.swing.*;

import javax.swing.text.*;

import de.teamwork.ctcp.*;

import de.teamwork.ctcp.msgutils.*;

import de.teamwork.irc.*;

import de.teamwork.irc.msgutils.*;

import de.teamwork.jaicwain.App;

import de.teamwork.jaicwain.gui.*;

import de.teamwork.jaicwain.options.*;

import de.teamwork.jaicwain.session.irc.*;

import de.teamwork.util.swing.JHistoryTextField;



/**

 * Provides a nice panel with the typical chat interface. Can be used with

 * <code>DefaultIRCChannel</code>s and derived classes only. Provides a user

 * list, a text area and an input field.

 * 

 * @author Christoph Daniel Schulze

 * @version $Id: IRCChatPanel.java 3 2003-01-07 14:16:38Z captainnuss $

 */

public class IRCChatPanel extends ExtendedPanel implements ActionListener, IRCChannelListener, KeyListener, OptionsChangedListener, IRCSessionListener {
