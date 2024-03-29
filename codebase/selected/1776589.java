package org.jsynthlib.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * A base class of a patch editor.
 */
public class PatchEditorFrame extends Actions.MenuFrame implements PatchBasket {

    private AppConfig appConfig;

    /** This is the patch we are working on. */
    protected ISinglePatch p;

    /** Scroll Pane for the editor frame. */
    protected JPanel scrollPane;

    /** Note that calling addWidget() method may change the value of this. */
    protected GridBagConstraints gbc = new GridBagConstraints();

    /** A list of widget added by addWidget method. */
    protected List<SysexWidget> widgetList = new ArrayList<SysexWidget>();

    /** For Alignment, a size to scrollbar labels, zero disables*/
    protected int forceLabelWidth;

    /**
     * Information about BankEditorFrame which created this
     * PatchEditor frame (if applicable) so we can update that frame
     * with the edited data on close.
     */
    protected BankEditorFrame bankFrame;

    /** Bank of fader.  Set by faderMoved method. */
    private int faderBank;

    /** Number of fader banks.  Set by show method. */
    private int numFaderBanks;

    /** Number of patch editor frame opened. */
    private static int nFrame;

    /** send a patch when patch editor frame is activated. */
    private static boolean sendPatchOnActivated = true;

    /**
     * This is a copy of the patch when we started editing (in case
     * user wants to revert).
     */
    private final IPatch originalPatch;

    /** which patch in bank we're editing */
    private int patchRow;

    private int patchCol;

    /** The last recently moved widget by fader. */
    private SysexWidget recentWidget;

    private int lastFader;

    private JScrollPane scroller;

    /**
     * Creates a new <code>PatchEditorFrame</code> instance.
     *
     * @param name
     *            a name to display in the title bar.
     * @param patch
     *            a reference to <code>ISinglePatch</code> object stored in a
     *            patch library or a bank patch.
     */
    protected PatchEditorFrame(String name, ISinglePatch patch) {
        this(name, patch, new JPanel(new GridBagLayout()));
    }

    protected PatchEditorFrame(String name, ISinglePatch patch, JPanel panel) {
        super(App.getDesktop(), name);
        appConfig = AppConfig.getInstance();
        nFrame++;
        p = patch;
        originalPatch = (IPatch) p.clone();
        scrollPane = panel;
        scroller = new JScrollPane(scrollPane);
        getContentPane().add(scroller);
        faderInEnable(appConfig.getFaderEnable());
        scroller.getVerticalScrollBar().addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        });
        scroller.getHorizontalScrollBar().addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        });
        addJSLFrameListener(new JSLFrameListener() {

            public void JSLFrameClosing(JSLFrameEvent e) {
                frameClosing();
            }

            public void JSLFrameOpened(JSLFrameEvent e) {
                frameOpened();
            }

            public void JSLFrameActivated(JSLFrameEvent e) {
                frameActivated();
            }

            public void JSLFrameClosed(JSLFrameEvent e) {
            }

            public void JSLFrameDeactivated(JSLFrameEvent e) {
                frameDeactivated();
            }

            public void JSLFrameDeiconified(JSLFrameEvent e) {
            }

            public void JSLFrameIconified(JSLFrameEvent e) {
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            numFaderBanks = getNumFaderBank();
            Logger.reportStatus("PatchEditorFrame.show(): Num Fader Banks = " + numFaderBanks);
            faderHighlight();
            pack();
            Dimension screenSize = App.getDesktop().getSize();
            Dimension frameSize = this.getSize();
            Logger.reportStatus("PatchEditorFrame.setVisible(): scrollPane size = " + scrollPane.getSize() + ", frame size = " + frameSize);
            if (frameSize.height > screenSize.height) {
                frameSize.width += scroller.getVerticalScrollBar().getPreferredSize().width;
                setSize(frameSize.width, screenSize.height);
            }
            if (frameSize.width > screenSize.width) {
                frameSize.height += scroller.getHorizontalScrollBar().getPreferredSize().height;
                if (frameSize.height > screenSize.height) {
                    frameSize.height = screenSize.height;
                }
                setSize(screenSize.width, frameSize.height);
            }
        }
        super.setVisible(b);
    }

    /**
     * Called when the frame is closed. Default ask for keep changes. May be
     * redefined in sub-classes.
     */
    protected void frameClosing() {
        Logger.reportStatus("###PE.FrameCloseing: nFrame = " + nFrame);
        nFrame--;
        sendPatchOnActivated = (nFrame < 2);
        String[] choices = new String[] { "Keep Changes", "Revert to Original", "Place Changed Version on Clipboard" };
        int choice;
        do {
            choice = JOptionPane.showOptionDialog((Component) null, "What do you wish to do with the changed copy of the Patch?", "Save Changes?", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        } while (choice == JOptionPane.CLOSED_OPTION);
        if (choice == 0) {
            if (bankFrame != null) {
                bankFrame.setPatchAt(p, patchRow, patchCol);
            }
        } else {
            if (choice == 2) {
                copySelectedPatch();
            }
            p.useSysexFromPatch(originalPatch);
        }
    }

    /**
     * Called when the frame is opened. May be redefined
     * in sub-classes.
     */
    protected void frameOpened() {
    }

    /**
     * Called when the frame is activated. May be redefined in sub-classes.
     */
    protected void frameActivated() {
        Logger.reportStatus("###PE.FrameActivated: nFrame = " + nFrame);
        if (sendPatchOnActivated || nFrame > 1) {
            sendPatchOnActivated = false;
            p.send();
        }
        Actions.setEnabled(false, Actions.EN_ALL);
        Actions.setEnabled(true, Actions.EN_COPY | Actions.EN_PLAY | Actions.EN_SEND | Actions.EN_SEND_TO | Actions.EN_REASSIGN);
        Actions.setEnabled(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this) != null, Actions.EN_PASTE);
    }

    /**
     * Called when the frame is deactivated. May be redefined in sub-classes.
     */
    protected void frameDeactivated() {
        Actions.setEnabled(false, Actions.EN_ALL);
    }

    public List<Patch> getPatchCollection() {
        return null;
    }

    public void importPatch(File file) throws FileNotFoundException {
    }

    public void exportPatch(File file) throws FileNotFoundException {
    }

    public void deleteSelectedPatch() {
    }

    public void copySelectedPatch() {
        ClipboardUtil.storePatch(p);
    }

    public IPatch getSelectedPatch() {
        return p;
    }

    public void sendSelectedPatch() {
        p.send();
    }

    public void sendToSelectedPatch() {
        new SysexSendToDialog(p);
    }

    public void reassignSelectedPatch() {
        new ReassignPatchDialog(p);
    }

    public void playSelectedPatch() {
        p.send();
        p.play();
    }

    public void storeSelectedPatch() {
    }

    public JSLFrame editSelectedPatch() {
        return null;
    }

    public void pastePatch() {
    }

    public void pastePatch(IPatch _p) {
    }

    public void pastePatch(IPatch _p, int bankNum, int patchNum) {
    }

    /**
     * Add <code>SysexWidget</code> <code>widget</code> to
     * <code>JComponent</code> <code>parent</code> by using specified
     * GridBagConstraints.
     *
     * @param parent a parent <code>JComponent</code> to which
     * <code>widget</code> is added.
     * @param widget a <code>SysexWidget</code> to be added.
     * @param gridx see {@link GridBagConstraints#gridx}.
     * @param gridy see {@link GridBagConstraints#gridy}.
     * @param gridwidth see {@link GridBagConstraints#gridwidth}.
     * @param gridheight see {@link GridBagConstraints#gridheight}.
     * @param anchor see {@link GridBagConstraints#anchor}.
     * @param fill see {@link GridBagConstraints#fill}.
     * @param slidernum a slider number.  Only used by ScrollBar Widgets.
     * @see GridBagConstraints
     */
    protected void addWidget(JComponent parent, SysexWidget widget, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, int slidernum) {
        try {
            gbc.gridx = gridx;
            gbc.gridy = gridy;
            gbc.gridwidth = gridwidth;
            gbc.gridheight = gridheight;
            gbc.anchor = anchor;
            gbc.fill = fill;
            parent.add(widget, gbc);
            widgetList.add(widget);
            widget.setSliderNum(slidernum);
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
    }

    /**
     * Add <code>SysexWidget</code> <code>widget</code> to
     * <code>JComponent</code> <code>parent</code> by using specified
     * GridBagConstraints.<p>
     *
     * <code>NORTHEAST</code> is used for the <code>anchor</code>
     * constraint and <code>HORIZONTAL</code> is used for the
     * <code>fill</code> constraint.
     */
    protected void addWidget(JComponent parent, SysexWidget widget, int gridx, int gridy, int gridwidth, int gridheight, int slidernum) {
        addWidget(parent, widget, gridx, gridy, gridwidth, gridheight, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, slidernum);
    }

    /**
     * Add <code>SysexWidget</code> <code>widget</code> to
     * <code>scrollPane</code> by using specified
     * GridBagConstraints.<p>
     *
     * <code>EAST</code> is used for the <code>anchor</code>
     * constraint and <code>BOTH</code> is used for the
     * <code>fill</code> constraint.
     */
    protected void addWidget(SysexWidget widget, int gridx, int gridy, int gridwidth, int gridheight, int slidernum) {
        this.addWidget(scrollPane, widget, gridx, gridy, gridwidth, gridheight, GridBagConstraints.EAST, GridBagConstraints.BOTH, slidernum);
    }

    private Transmitter trns;

    private Receiver rcvr;

    private void faderInEnable(boolean enable) {
        if (enable) {
            try {
                trns = MidiUtil.getTransmitter(appConfig.getFaderPort());
                rcvr = new FaderReceiver();
                trns.setReceiver(rcvr);
            } catch (MidiUnavailableException exception) {
                ErrorDialog.showDialog(null, "Error", "Failed to get transmitter", exception);
            }
        } else {
            if (trns != null) {
                trns.close();
            }
            if (rcvr != null) {
                rcvr.close();
            }
        }
    }

    private class FaderReceiver implements Receiver {

        public void close() {
        }

        /**
         * <pre>
         *    Control Change MIDI Message
         *      1011nnnn : BnH, nnnn: Voice Channel number
         *      0ccccccc : control number (0-119)
         *      0vvvvvvv : control value
         * </pre>
         *
         * A fader number whose FaderChannel and FaderControl number matches
         * "Voice Channel number" and "control number" in a Control Change MIDI
         * message is selected. The faderMoved() method is called with the fader
         * number and "control value".
         */
        public void send(MidiMessage message, long timeStamp) {
            if (!isSelected()) {
                return;
            }
            ShortMessage msg = (ShortMessage) message;
            if (msg.getCommand() == ShortMessage.CONTROL_CHANGE) {
                int channel = msg.getChannel();
                int controller = msg.getData1();
                Logger.reportStatus("FaderReceiver: channel: " + channel + ", control: " + controller + ", value: " + msg.getData2());
                for (int i = 0; i < Constants.NUM_FADERS; i++) {
                    if ((appConfig.getFaderChannel(i) == channel) && (appConfig.getFaderControl(i) == controller)) {
                        faderMoved(i, msg.getData2());
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param fader fader number
     * <pre>
     *  0    : active slider
     *  1-16 : fader 1-16
     * 17-30 : button 1-14
     * 31    : button 15 : prev fader bank
     * 32    : button 16 : next fader bank
     * </pre>
     * @param value data value
     */
    private void faderMoved(int fader, int value) {
        Logger.reportStatus("FaderMoved: fader: " + fader + ", value: " + value);
        if (fader == 32) {
            nextFader();
            return;
        } else {
            if (fader == 31) {
                prevFader();
                return;
            } else {
                if (fader > 16) {
                    fader = (byte) (0 - (fader - 16) - (faderBank * 16));
                } else {
                    fader += (faderBank * 16);
                }
            }
        }
        if (recentWidget != null) {
            SysexWidget w = recentWidget;
            if (fader == faderBank * 16) {
                fader = lastFader;
            }
            if (w.getSliderNum() == fader && w.isShowing()) {
                if (w.getNumFaders() == 1) {
                    w.setValue((int) (w.getValueMin() + ((float) (value) / 127 * (w.getValueMax() - w.getValueMin()))));
                } else {
                    w.setFaderValue(fader, value);
                }
                w.repaint();
                return;
            }
        }
        lastFader = fader;
        for (SysexWidget w : widgetList) {
            if ((w.getSliderNum() == fader || (w.getSliderNum() < fader && w.getSliderNum() + w.getNumFaders() > fader)) && w.isShowing()) {
                if (w.getNumFaders() == 1) {
                    w.setValue((int) (w.getValueMin() + ((float) (value) / 127 * (w.getValueMax() - w.getValueMin()))));
                } else {
                    w.setFaderValue(fader, value);
                }
                w.repaint();
                recentWidget = w;
                return;
            }
        }
    }

    private static Color activeColor;

    private static Color inactiveColor;

    static {
        activeColor = UIManager.getColor("controlText");
        if (activeColor == null) {
            activeColor = new Color(75, 75, 100);
        }
        inactiveColor = UIManager.getColor("textInactiveText");
        if (inactiveColor == null) {
            inactiveColor = new Color(102, 102, 153);
        }
    }

    /**
     * > P.S. btw, anyone an idea why some labels are grayed-out?<p>
     *
     * If I remember correctly, the label color becomes darker if the label
     * is currently assigned to an active fader. the addWidget calls have the
     * last parameter which defines a fader number. By default the first 16
     * are active. Using the "Next Fader bank" Button on the toolbar makes
     * the next 16 active. Its not supposed to look 'greyed out' though, I
     * just wanted a visual cue about what bank of faders was active. Maybe
     * the color scheme should be changed to be less confusing.<p>
     *
     * Brian
     */
    private void faderHighlight() {
        for (SysexWidget w : widgetList) {
            if (w.getLabel() != null) {
                if (((Math.abs(w.getSliderNum() - 1) & 0xf0)) == faderBank * 16) {
                    w.getJLabel().setForeground(activeColor);
                } else {
                    w.getJLabel().setForeground(inactiveColor);
                }
                w.getJLabel().repaint();
            }
        }
    }

    void nextFader() {
        faderBank = (faderBank + 1) % numFaderBanks;
        faderHighlight();
    }

    void prevFader() {
        faderBank = faderBank - 1;
        if (faderBank < 0) {
            faderBank = numFaderBanks - 1;
        }
        faderHighlight();
    }

    /**
     * When showing the dialog, also check how many components there are to
     * determine the number of widget banks needed.
     */
    private int getNumFaderBank() {
        int high = 0;
        for (SysexWidget w : widgetList) {
            if ((w.getSliderNum() + w.getNumFaders() - 1) > high) {
                high = w.getSliderNum() + w.getNumFaders() - 1;
            }
        }
        return (high / 16) + 1;
    }

    /**
     * Let bankeditorframe set information about itself when it
     * creates a patch editor frame.
     */
    public void setBankEditorInformation(BankEditorFrame bf, int row, int col) {
        bankFrame = bf;
        patchRow = row;
        patchCol = col;
    }

    void revalidateDriver() {
        p.setDriver();
        if (p.hasNullDriver()) {
            try {
                setClosed(true);
            } catch (PropertyVetoException e) {
                Logger.reportStatus(e);
            }
            return;
        }
    }

    /**
     * return the Patch which is edited.
     */
    public IPatch getPatch() {
        return p;
    }
}
