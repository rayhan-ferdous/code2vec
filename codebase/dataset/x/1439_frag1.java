import javax.swing.event.*;

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

 * Provides a tabbed pane control for <code>ExtendedPanel</code>s. The component

 * is specialised on managing <code>IRCChatPanel</code>s and

 * <code>IRCServerPanel</code>s. Panels added to this component are always

 * visible and can not be detached. This component shouldn't be used in direct

 * conjunction with a <code>WindowManager</code> object. 

 * 

 * @author Christoph Daniel Schulze

 * @version $Id: TabbedChannelContainer.java 3 2003-01-07 14:16:38Z captainnuss $

 */

public class TabbedChannelContainer extends JComponent implements ExtendedPanelContainer, ChangeListener {
