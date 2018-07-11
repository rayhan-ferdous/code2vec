import org.dcm4che.dict.DictionaryFactory;

import org.dcm4che.dict.Tags;

import org.dcm4che.dict.UIDDictionary;

import org.dcm4che.dict.UIDs;

import org.dcm4che.dict.VRs;

import org.dcm4che.net.AAssociateAC;

import org.dcm4che.net.AAssociateRQ;

import org.dcm4che.net.ActiveAssociation;

import org.dcm4che.net.Association;

import org.dcm4che.net.AssociationFactory;

import org.dcm4che.net.DataSource;

import org.dcm4che.net.PDU;

import org.dcm4che.net.PresContext;

import org.dcm4che.server.PollDirSrv;

import org.dcm4che.server.PollDirSrvFactory;

import org.dcm4che.util.DcmURL;

import org.dcm4che.util.SSLContextAdapter;



/**

 *

 * @author  gunter.zeilinger@tiani.com

 */

public class DcmSnd implements PollDirSrv.Handler {



    private static final String[] DEF_TS = { UIDs.ImplicitVRLittleEndian };



    private static final int PCID_ECHO = 1;



    static final Logger log = Logger.getLogger("DcmSnd");



    private static ResourceBundle messages = ResourceBundle.getBundle("DcmSnd", Locale.getDefault());



    private static final UIDDictionary uidDict = DictionaryFactory.getInstance().getDefaultUIDDictionary();



    private static final AssociationFactory aFact = AssociationFactory.getInstance();



    private static final DcmObjectFactory oFact = DcmObjectFactory.getInstance();



    private static final DcmParserFactory pFact = DcmParserFactory.getInstance();



    private static final int ECHO = 0;



    private static final int SEND = 1;



    private static final int POLL = 2;



    private final int mode;



    private DcmURL url = null;



    private int repeatSingle = 1;



    private int repeatWhole = 1;



    private int priority = Command.MEDIUM;



    private int acTimeout = 5000;



    private int dimseTimeout = 0;



    private int soCloseDelay = 500;



    private String uidSuffix = null;



    private AAssociateRQ assocRQ = aFact.newAAssociateRQ();



    private boolean packPDVs = false;



    private boolean truncPostPixelData = false;



    private int bufferSize = 2048;



    private byte[] buffer = null;



    private SSLContextAdapter tls = null;



    private String[] cipherSuites = null;



    private Dataset overwrite = oFact.newDataset();



    private PollDirSrv pollDirSrv = null;



    private File pollDir = null;



    private long pollPeriod = 5000L;



    private ActiveAssociation activeAssociation = null;



    private int sentCount = 0;



    private long sentBytes = 0L;



    private static final LongOpt[] LONG_OPTS = new LongOpt[] { new LongOpt("ac-timeout", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("dimse-timeout", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("so-close-delay", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("prior-high", LongOpt.NO_ARGUMENT, null, 'P'), new LongOpt("prior-low", LongOpt.NO_ARGUMENT, null, 'p'), new LongOpt("max-pdu-len", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("max-op-invoked", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("pack-pdvs", LongOpt.NO_ARGUMENT, null, 'k'), new LongOpt("trunc-post-pixeldata", LongOpt.NO_ARGUMENT, null, 't'), new LongOpt("buf-len", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("set", LongOpt.REQUIRED_ARGUMENT, null, 's'), new LongOpt("tls-key", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("tls-key-passwd", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("tls-cacerts", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("tls-cacerts-passwd", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("poll-dir", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("poll-period", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("poll-retry-open", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("poll-delta-last-modified", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("poll-done-dir", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("repeat-dimse", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("repeat-assoc", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("uid-suffix", LongOpt.REQUIRED_ARGUMENT, null, 2), new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'), new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v') };
