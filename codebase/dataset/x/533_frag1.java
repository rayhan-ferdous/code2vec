import javax.microedition.rms.RecordStoreException;

import javax.microedition.rms.RecordStoreNotOpenException;

import mujmail.ordering.Criterion;

import mujmail.ordering.Ordering;

import mujmail.ui.FileSystemBrowser;

import mujmail.util.Callback;

import mujmail.util.StartupModes;

import mujmail.ui.SSLTypeChooser;



public class Settings implements ItemStateListener {



    /** Flag signals if we want to print debug prints */

    private static final boolean DEBUG = false;
