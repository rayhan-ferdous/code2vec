package org.capocaccia.cne.jaer.multilinetracking;

import net.sf.jaer.chip.*;
import net.sf.jaer.eventprocessing.EventFilter2D;
import net.sf.jaer.event.*;
import net.sf.jaer.event.EventPacket;
import net.sf.jaer.graphics.*;
import net.sf.jaer.util.filter.*;
import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import net.sf.jaer.Description;
import net.sf.jaer.eventprocessing.tracking.LIFOEventBuffer;
import net.sf.jaer.eventprocessing.tracking.LineDetector;

/**
 * Tracks multiple lines in the scene using a cluster based method based on pairs of recent events.
 * The event pairs come from a buffer formed from recent events. Each pair defines a line with polar and angle parameters.
 * Lines are tracked using polar (rho) and angle (theta) parameters in a space of rho/theta, in analogy with the PairedEventLinearEdgeClusterTracker tracking
 * of rectangular objects in retinal coordinate space.
 *
 * @author tobi
@see LineDetector
 */
@Description("Tracks multiple lines in the scene using a cluster based method based on pairs of recent events")
public class PairedEventLinearEdgeClusterTracker extends EventFilter2D implements FrameAnnotater, Observer, LineDetector {

    static final double PI2 = Math.PI * 2;

    private java.util.List<LinearFeatureModel> clusters = new ArrayList<LinearFeatureModel>();

    private float rhoPixelsFiltered = 0;

    private float thetaDegFiltered = 0;

    private float tauMs = getPrefs().getFloat("MultiLineClusterTracker.tauMs", 10);

    {
        setPropertyTooltip("tauMs", "lowpass time const in ms for filtering strongest line properties (rho/theta)");
    }

    private int oriDiffAllowed = getPrefs().getInt("MultiLineClusterTracker.oriDiffAllowed", 1);

    {
        setPropertyTooltip("oriDiffAllowed", "orientation events can have at most this orientation difference to form line segments");
    }

    private int eventBufferLength = getPrefs().getInt("MultiLineClusterTracker.eventBufferLength", 8);

    {
        setPropertyTooltip("eventBufferLength", "Number of past events to form line segments from for clustering");
    }

    private LIFOEventBuffer<OrientationEvent> eventBuffer;

    LineClusterCanvas lineClusterCanvas = null;

    private int chipSize = 0, sizex = 0, sizey = 0;

    private boolean showLineSegments = false;

    {
        setPropertyTooltip("showLineSegments", "show valid LineSegments contributing to LineClusters");
    }

    private int minSegmentLength = getPrefs().getInt("MultiLineClusterTracker.minSegmentLength", 3);

    {
        setPropertyTooltip("minSegmentLength", "minium manhatten length of line segment in pixels");
    }

    private int maxSegmentLength = getPrefs().getInt("MultiLineClusterTracker.maxSegmentLength", 20);

    {
        setPropertyTooltip("maxSegmentLength", "max manhatten line segment length in pixels");
    }

    private int maxSegmentDt = getPrefs().getInt("MultiLineClusterTracker.maxSegmentDt", 20000);

    {
        setPropertyTooltip("maxSegmentDt", "maximum dt in ticks for line segment event pair");
    }

    /** scaling can't make cluster bigger or smaller than this ratio to default cluster size */
    public static final float MAX_SCALE_RATIO = 2;

    private float rhoRadius = getPrefs().getFloat("MultiLineClusterTracker.rhoRadius", 6);

    {
        setPropertyTooltip("rhoRadius", "\"radius\" of line cluster around line in pixels");
    }

    private float thetaRadiusRad = getPrefs().getFloat("MultiLineClusterTracker.thetaRadiusRad", (float) (30 * Math.PI / 180));

    {
        setPropertyTooltip("thetaRadiusDeg", "\"radius\" of line cluster in degrees around line cluster angle");
    }

    private float minDistanceNormalized = getPrefs().getFloat("MultiLineClusterTracker.minDistanceNormalized", 0.1f);

    {
        setPropertyTooltip("minDistanceNormalized", "minimum normalized rho and theta distance for cluster to contain segment");
    }

    private float mixingFactorRho = getPrefs().getFloat("MultiLineClusterTracker.mixingFactorRho", 0.01f);

    {
        setPropertyTooltip("mixingFactorRho", "how much line cluster rho (distance from origin) is moved by a segment event");
    }

    private float mixingFactorTheta = getPrefs().getFloat("MultiLineClusterTracker.mixingFactorTheta", 0.01f);

    {
        setPropertyTooltip("mixingFactorTheta", "how much line cluster angle is turned by a line segment event");
    }

    private float mixingFactorPosition = getPrefs().getFloat("MultiLineClusterTracker.mixingFactorPosition", 0.01f);

    {
        setPropertyTooltip("mixingFactorPosition", "how much line cluster position is moved by segment event");
    }

    private float mixingFactorLength = getPrefs().getFloat("MultiLineClusterTracker.mixingFactorLength", 0.01f);

    {
        setPropertyTooltip("mixingFactorLength", "how much line length is changed by segment events");
    }

    private boolean lengthEnabled = getPrefs().getBoolean("MultiLineClusterTracker.lengthEnabled", false);

    {
        setPropertyTooltip("lengthEnabled", "line cluster length determined by line segments");
    }

    private boolean colorClustersDifferentlyEnabled = getPrefs().getBoolean("MultiLineClusterTracker.colorClustersDifferentlyEnabled", false);

    {
        setPropertyTooltip("colorClustersDifferentlyEnabled", "each cluster gets assigned a random color, otherwise color indicates ages");
    }

    private boolean showVelocity = getPrefs().getBoolean("MultiLineClusterTracker.showVelocity", true);

    {
        setPropertyTooltip("showVelocity", "computes and shows cluster velocity");
    }

    private boolean logDataEnabled = false;

    {
        setPropertyTooltip("logDataEnabled", "writes a cluster log file");
    }

    private PrintStream logStream = null;

    private boolean showAllClusters = getPrefs().getBoolean("MultiLineClusterTracker.showAllClusters", false);

    {
        setPropertyTooltip("showAllClusters", "shows all clusters, not just those with sufficient support");
    }

    private boolean clusterLifetimeIncreasesWithAge = getPrefs().getBoolean("MultiLineClusterTracker.clusterLifetimeIncreasesWithAge", false);

    {
        setPropertyTooltip("clusterLifetimeIncreasesWithAge", "older clusters can live longer without support, good for jumpy objects");
    }

    private int thresholdEventsForVisibleCluster = getPrefs().getInt("MultiLineClusterTracker.thresholdEventsForVisibleCluster", 10);

    {
        setPropertyTooltip("thresholdEventsForVisibleCluster", "Cluster needs this many events to be visible");
    }

    private int clusterLifetimeWithoutSupportUs = getPrefs().getInt("MultiLineClusterTracker.clusterLifetimeWithoutSupport", 10000);

    {
        setPropertyTooltip("clusterLifetimeWithoutSupportUs", "Cluster lives this long in ticks (e.g. us) without events before pruning");
    }

    private int maxNumClusters = getPrefs().getInt("MultiLineClusterTracker.maxNumClusters", 3);

    {
        setPropertyTooltip("maxNumClusters", "Sets the maximum potential number of clusters");
    }

    private boolean weightLengthEnabled = getPrefs().getBoolean("MultiLineClusterTracker.weightLengthEnabled", false);

    {
        setPropertyTooltip("weightLengthEnabled", "weight influence of segment on cluster inversely with length of segment");
    }

    private boolean renderInputEvents = getPrefs().getBoolean("MultiLineClusterTracker.renderInputEvents", false);

    {
        setPropertyTooltip("renderInputEvents", "render input events and not results of enclosed filter chain");
    }

    /**
     * Creates a new instance of PairedEventLinearEdgeClusterTracker
     * @param chip the chip we are tracking for
     */
    public PairedEventLinearEdgeClusterTracker(AEChip chip) {
        super(chip);
        initFilter();
        chip.addObserver(this);
    }

    public void initFilter() {
        initBuffers();
        chipSize = chip.getMaxSize();
        sizex = chip.getSizeX();
        sizey = chip.getSizeY();
        resetFilter();
    }

    ArrayList<LineSegment> segList = new ArrayList<LineSegment>();

    protected LinkedList<LinearFeatureModel> pruneList = new LinkedList<LinearFeatureModel>();

    private LineSegment segment = new LineSegment();

    public EventPacket filterPacket(EventPacket in) {
        if (in == null) {
            return null;
        }
        if (!isFilterEnabled()) {
            return in;
        }
        if (getEnclosedFilter() == null) {
            track(in);
            return in;
        }
        EventPacket enclosedFilterOutputPacket = getEnclosedFilter().filterPacket(in);
        track(enclosedFilterOutputPacket);
        if (!renderInputEvents) {
            return enclosedFilterOutputPacket;
        } else {
            return in;
        }
    }

    OrientationEvent oriEvent = new OrientationEvent();

    private synchronized void track(EventPacket<BasicEvent> packet) {
        int n = packet.getSize();
        if (n == 0) {
            return;
        }
        if (showLineSegments) {
            synchronized (segList) {
                segList.clear();
            }
        }
        for (BasicEvent event : packet) {
            for (OrientationEvent oldEvent : eventBuffer) {
                if (isValidSegment(oldEvent, event)) {
                    if (showLineSegments) {
                        segment = new LineSegment(oldEvent, event);
                        segList.add(segment);
                    } else {
                        segment.set(oldEvent, event);
                    }
                    LinearFeatureModel closest = null;
                    closest = getNearestCluster(segment);
                    if (closest != null) {
                        if (showLineSegments) {
                            segment.lineCluster = closest;
                        }
                        closest.addSegment(segment);
                    } else if (clusters.size() < maxNumClusters) {
                        LinearFeatureModel newCluster = new LinearFeatureModel(segment);
                        clusters.add(newCluster);
                    }
                }
            }
            oriEvent.copyFrom(event);
            eventBuffer.add(oriEvent);
        }
        pruneList.clear();
        for (LinearFeatureModel c : clusters) {
            int t0 = c.getLastEventTimestamp();
            int t1 = packet.getLastTimestamp();
            int timeSinceSupport = t1 - t0;
            boolean killOff = false;
            if (clusterLifetimeIncreasesWithAge) {
                int age = c.getLifetime();
                int supportTime = clusterLifetimeWithoutSupportUs;
                if (age < clusterLifetimeWithoutSupportUs) {
                    supportTime = age;
                }
                if (timeSinceSupport > supportTime) {
                    killOff = true;
                }
            } else {
                if (timeSinceSupport > clusterLifetimeWithoutSupportUs) {
                    killOff = true;
                }
            }
            if (t0 > t1 || killOff || timeSinceSupport < 0) {
                pruneList.add(c);
            }
        }
        clusters.removeAll(pruneList);
        boolean mergePending;
        LinearFeatureModel c1 = null, c2 = null;
        do {
            mergePending = false;
            int nc = clusters.size();
            outer: for (int i = 0; i < nc; i++) {
                c1 = clusters.get(i);
                for (int j = i + 1; j < nc; j++) {
                    c2 = clusters.get(j);
                    if (c1.isOverlapping(c2)) {
                        mergePending = true;
                        break outer;
                    }
                }
            }
            if (mergePending && c1 != null && c2 != null) {
                clusters.remove(c1);
                clusters.remove(c2);
                clusters.add(new LinearFeatureModel(c1, c2));
            }
        } while (mergePending);
        if (isLogDataEnabled() && getNumClusters() > 0) {
            if (logStream != null) {
                for (LinearFeatureModel c : clusters) {
                    if (!c.isVisible()) {
                        continue;
                    }
                    logStream.println(String.format("%d %d %f %f", c.getClusterNumber(), c.lastTimestamp, c.location.x, c.location.y));
                    if (logStream.checkError()) {
                        log.warning("eroror logging data");
                    }
                }
            }
        }
        computedClusterOrdering = false;
        LinearFeatureModel strongest = getStrongestCluster();
        if (strongest != null) {
            computeOutputLineParameters(strongest, packet);
        }
    }

    public int getNumClusters() {
        return clusters.size();
    }

    public String toString() {
        String s = clusters != null ? Integer.toString(clusters.size()) : null;
        String s2 = "MultiLineClusterTracker with " + s + " clusters ";
        return s2;
    }

    /**
     * Computes signed distance to-from between two angles with cut at -PI,PI. E.g.
     *     if e is from small angle and from=PI-e, to=-PI+e, then angular distance to-from is
     *     -2e rather than (PI-e)-(-PI+e)=2PI-2e.
     *     This minimum angle difference is useful to push an angle in the correct direction
     *     by the correct amount. For this example, we want to push an angle hovering around PI-e.
     *     We don't want angles of -PI+e to push the angle from lot, just from bit towards PI. 
     *     If we have angle <code>from</code> and new angle <code>to</code> and
     *     mixing factor m<<1, then new angle <code>c=from+m*angleDistance(from,to)</code>.
     *
     *
     * @param from the first angle
     * @param to the second angle
     * @return the smallest difference to-from, ordinarily positive if to>from
     */
    private double angleDistance(double from, double to) {
        double d = to - from;
        if (d > Math.PI) {
            return d - Math.PI;
        }
        if (d < -Math.PI) {
            return d + Math.PI;
        }
        return d;
    }

    /** A LineSegment is formed from a pair of events and has a rho and theta that characterize it, along with 
    other parameters. Note that rho theta originate in LL chip image corner.
     */
    private class LineSegment {

        /** theta is angle in radians from 0 to PI with 0 and PI horizontal normals to line segment */
        double thetaRad = 0;

        /** rhoPixels is distance of line segment closest passage to origin of chip image LL corner */
        double rhoPixels = 0;

        int dx = 0, dy = 0, dt = 0;

        int ax = 0, bx = 0, ay = 0, by = 0;

        float x = 0, y = 0;

        int timestamp = 0;

        double length;

        LinearFeatureModel lineCluster = null;

        /** makes an empty segment */
        LineSegment() {
        }

        /** Make a new LineSegment with first event a and second (later) event b */
        LineSegment(BasicEvent a, BasicEvent b) {
            set(a, b);
        }

        /**
        computes the rho (pixels distance from origin at LL corner)
         * and theta (radian angle CCW from 0 horizontal) for the segment given the two events.
         *This is confusing because a raw calculation can give negative rho and angle that spans -PI to PI.
         *This values are unambiguous about specifying the line segment but similar segments can have very different rho/theta values
         *depending on the quadrant that dx,dy are in. Also, the rho values -PI and PI are identical 
        although they differ by 2*PI. This makes
         *combining values confusing. Therefore the values are rationalized by ensuring positive rho and forcing 
        theta between -PI to PI.
         */
        void set(BasicEvent a, BasicEvent b) {
            this.timestamp = b.timestamp;
            ax = a.x;
            bx = b.x;
            ay = a.y;
            by = b.y;
            x = (ax + bx) / 2;
            y = (ay + by) / 2;
            dt = b.timestamp - a.timestamp;
            dx = ax - bx;
            dy = ay - by;
            length = Math.sqrt(dx * dx + dy * dy);
            thetaRad = Math.atan2(dx, -dy);
            rhoPixels = x * Math.cos(thetaRad) + y * Math.sin(thetaRad);
            if (rhoPixels < 0) {
                rhoPixels = -rhoPixels;
                if (thetaRad > 0) {
                    thetaRad -= Math.PI;
                } else {
                    thetaRad += Math.PI;
                }
            }
        }

        public String toString() {
            return String.format("LineSegment a x,y=%d,%d, b x,y=%d,%d, rho=%.1f pix theta=%.0f deg x,y=%.1f,%.1f", ax, ay, bx, by, rhoPixels, Math.toDegrees(thetaRad), x, y);
        }

        private void draw(GL gl) {
            if (lineCluster != null) {
                gl.glColor3fv(lineCluster.rgb, 0);
            } else {
                gl.glColor3f(.2f, .2f, .2f);
            }
            gl.glPushMatrix();
            gl.glLineWidth(1);
            gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex2i(ax, ay);
            gl.glVertex2i(bx, by);
            gl.glEnd();
            gl.glPopMatrix();
        }
    }

    /** Checks two events to see if they could form a valid line segment.
    The events need to have the same Polarity is the event is a PolarityEvent.
    @return segment if line segment is long enough but not too long and is not based on events too far apart in time. Returns null if not
    a goog segment*/
    private boolean isValidSegment(BasicEvent older, BasicEvent newer) {
        if (newer instanceof OrientationEvent) {
            OrientationEvent oldOri = (OrientationEvent) older, newOri = (OrientationEvent) newer;
            if (Math.abs(oldOri.orientation - newOri.orientation) > oriDiffAllowed) {
                return false;
            }
        }
        if (newer instanceof PolarityEvent) {
            if (((PolarityEvent) older).polarity != ((PolarityEvent) newer).polarity) {
                return false;
            }
        }
        int dx = Math.abs(newer.x - older.x), dy = Math.abs(older.y - newer.y);
        if (dx < minSegmentLength && dy < minSegmentLength) {
            return false;
        }
        if (dx > maxSegmentLength || dy > maxSegmentLength) {
            return false;
        }
        int dt = newer.timestamp - older.timestamp;
        if (dt < 0) {
            return false;
        }
        if (dt > maxSegmentDt) {
            return false;
        }
        return true;
    }

    /**
     * Method that given segment event, returns closest cluster and distance to it. The actual computation returns the first cluster that is within the
     * minDistance of the event, which reduces the computation at the cost of reduced precision.
     * @param segment the segment event
     * @return closest cluster object (a cluster with a distance - that distance is the distance between the given event and the returned cluster).
     */
    private LinearFeatureModel getNearestCluster(LineSegment segment) {
        boolean useFirst = false;
        double minDistance = Double.MAX_VALUE;
        LinearFeatureModel closest = null;
        double currentDistance;
        for (LinearFeatureModel c : clusters) {
            if (!useFirst) {
                double d = c.distanceAbsTo(segment);
                if (d < minDistance) {
                    minDistance = d;
                    closest = c;
                }
            } else {
                if (c.contains(segment)) {
                    closest = c;
                    return closest;
                }
            }
        }
        if (!useFirst) {
            if (minDistance < minDistanceNormalized) {
                return closest;
            } else {
                return null;
            }
        } else {
            return closest;
        }
    }

    protected int clusterCounter = 0;

    /** Represents a single tracked line. It is characterized mainly by the normal to it from the chip image LL corner.
    rhoPixels is the length of the normal in pixels and thetaRad is the angle of the normal in radians CCW from x-axis.
     */
    public class LinearFeatureModel {

        /** center or average line location of cluster in pixels */
        public Point2D.Float location = new Point2D.Float();

        /** Distance of line from passing origin in pixels */
        private double rhoPixels = 0;

        /** Angle of normal to line CCW from x axis with 0 and PI being horizontal and PI/2 being vertical.
        thetaRad spans -PI to PI.
         */
        private double thetaRad = 0;

        /** velocity of cluster in pixels/tick, where tick is timestamp tick (usually microseconds) */
        public Point2D.Float velocity = new Point2D.Float();

        protected final int MAX_PATH_LENGTH = 100;

        protected Color color = null;

        float[] rgb = new float[4];

        protected int numEvents;

        protected int lastTimestamp, firstTimestamp;

        protected float instantaneousEventRate;

        private float avgEventRate = 0;

        protected float instantaneousISI;

        private float avgISI;

        private int clusterNumber;

        public double length = 0;

        private LowpassFilter rhoFilter = new LowpassFilter(), thetaFilter = new LowpassFilter();

        public LinearFeatureModel() {
            float hue = random.nextFloat();
            Color color = Color.getHSBColor(hue, 1f, 1f);
            setColor(color);
            setClusterNumber(clusterCounter++);
            rhoFilter = new LowpassFilter();
            thetaFilter = new LowpassFilter();
            rhoFilter.setTauMs(tauMs);
            thetaFilter.setTauMs(tauMs);
        }

        public LinearFeatureModel(LineSegment s) {
            this();
            rhoPixels = s.rhoPixels;
            thetaRad = s.thetaRad;
            location.x = s.x;
            location.y = s.y;
            lastTimestamp = s.timestamp;
            firstTimestamp = lastTimestamp;
            numEvents = 1;
            s.lineCluster = this;
        }

        /** Constructs a cluster by merging two clusters. All parameters of the resulting cluster should be reasonable combinations of the
         * source cluster parameters. For example, the merged location values are weighted by the number of events that have supported each
         * source cluster, so that older clusters weigh more heavily in the resulting cluster location. Subtle bugs or poor performance can result
         * from not properly handling the merging of parameters.
         *
         * @param one the first cluster
         * @param two the second cluster
         */
        public LinearFeatureModel(LinearFeatureModel one, LinearFeatureModel two) {
            this();
            LinearFeatureModel older = one.firstTimestamp < two.firstTimestamp ? one : two;
            int sumEvents = one.numEvents + two.numEvents;
            location.x = (one.location.x * one.numEvents + two.location.x * two.numEvents) / (sumEvents);
            location.y = (one.location.y * one.numEvents + two.location.y * two.numEvents) / (sumEvents);
            lastTimestamp = (one.lastTimestamp + two.lastTimestamp) / 2;
            numEvents = sumEvents;
            firstTimestamp = older.firstTimestamp;
            lastTimestamp = older.lastTimestamp;
            avgEventRate = older.avgEventRate;
            avgISI = older.avgISI;
            rhoPixels = older.rhoPixels;
            thetaRad = older.thetaRad;
            length = older.length;
            setColor(older.getColor());
        }

        public int getLastEventTimestamp() {
            return lastTimestamp;
        }

        private double getWeightedMixingFactor(LineSegment seg, float mixingFactor) {
            mixingFactor /= seg.length;
            return mixingFactor;
        }

        /** Adds the segment to this LinearFeatureModel
        @param seg the line segment
         */
        public void addSegment(LineSegment seg) {
            double m, m1;
            float dt = seg.timestamp - lastTimestamp;
            m = getWeightedMixingFactor(seg, mixingFactorRho);
            m1 = 1 - m;
            rhoPixels = m * seg.rhoPixels + m1 * rhoPixels;
            m = getWeightedMixingFactor(seg, mixingFactorTheta);
            double dTheta = angleDistance(thetaRad, seg.thetaRad);
            thetaRad = thetaRad + m * dTheta;
            float mixFacPos1 = 1 - mixingFactorPosition;
            location.x = (mixFacPos1) * location.x + mixingFactorPosition * seg.x;
            location.y = (mixFacPos1) * location.y + mixingFactorPosition * seg.y;
            if (!lengthEnabled) {
                length = maxSegmentLength;
            } else {
                length = (1 - mixingFactorLength) * length + mixingFactorLength * seg.length;
            }
            int prevLastTimestamp = lastTimestamp;
            lastTimestamp = seg.timestamp;
            numEvents++;
            instantaneousISI = lastTimestamp - prevLastTimestamp;
            if (instantaneousISI <= 0) {
                instantaneousISI = 1;
            }
            avgISI = mixFacPos1 * avgISI + mixingFactorPosition * instantaneousISI;
            instantaneousEventRate = 1f / instantaneousISI;
            avgEventRate = mixFacPos1 * avgEventRate + mixingFactorPosition * instantaneousEventRate;
        }

        /** computes metric of distance in rho/theta space,
        normalizing each by appropriate size: rho by chip.getMaxSize() and theta
         *by Math.PI. Returns normalized distance metric
        which can be used for searching for closest LinearFeatureModel to a LineSegment
        @param dRhoPixels distance in pixels, assumed non negative
        @param dThetaRad distance in radians, assumed non negative
         **/
        public double distanceMetric(double dRhoPixels, double dThetaRad) {
            dRhoPixels = (dRhoPixels / chipSize);
            dThetaRad = (dThetaRad / Math.PI);
            return dThetaRad + dRhoPixels;
        }

        /** @return distance in x direction of this cluster to the event */
        private double distanceToX(BasicEvent event) {
            float distance = Math.abs(event.x - location.x);
            return distance;
        }

        /** @return distance in y direction of this cluster to the event */
        private double distanceToY(BasicEvent event) {
            float distance = Math.abs(event.y - location.y);
            return distance;
        }

        /** @return distance in pixels between line segment and line cluster rho metric */
        private double distanceAbsToRho(LineSegment seg) {
            return Math.abs(rhoPixels - seg.rhoPixels);
        }

        private double distanceAbsToTheta(LineSegment seg) {
            double d = angleDistance(thetaRad, seg.thetaRad);
            return Math.abs(d);
        }

        /** @return distance in pixels between line segment and line cluster rho metric */
        private double distanceAbsToRho(LinearFeatureModel c) {
            return Math.abs(rhoPixels - c.rhoPixels);
        }

        private double distanceAbsToTheta(LinearFeatureModel c) {
            double d = angleDistance(thetaRad, c.thetaRad);
            return Math.abs(d);
        }

        /** @return distance of this cluster to the other cluster, abs value in normalized units combining rho and theta */
        public double distanceAbsTo(LinearFeatureModel c) {
            double dRho = distanceAbsToRho(c);
            double dTheta = distanceAbsToTheta(c);
            return distanceMetric(dRho, dTheta);
        }

        /** @return distance of this cluster to the other cluster, abs value in normalized units combining rho and theta  */
        public double distanceAbsTo(LineSegment c) {
            double dRho = distanceAbsToRho(c);
            double dTheta = distanceAbsToTheta(c);
            double d = distanceMetric(dRho, dTheta);
            return d;
        }

        /**
         * Does this cluster contain a LineSegment?
         *
         * @return true if LinearFeatureModel contains segment, i.e., rho is within rhoRadius and thetaRad is within thetaRadiusRad
         */
        private boolean contains(PairedEventLinearEdgeClusterTracker.LineSegment segment) {
            double dRho = distanceAbsToRho(segment);
            if (dRho > getRhoRadius()) {
                return false;
            }
            double dTheta = distanceAbsToTheta(segment);
            if (dTheta > thetaRadiusRad) {
                return false;
            }
            return true;
        }

        /** Returns true if cluster overlapped another cluster
        @param c the other cluster
         */
        public boolean isOverlapping(LinearFeatureModel c) {
            double d1 = distanceAbsToRho(c);
            double d2 = distanceAbsToTheta(c);
            if (d1 < rhoRadius * 2 && d2 < thetaRadiusRad * 2) {
                return true;
            }
            return false;
        }

        public final Point2D.Float getLocation() {
            return location;
        }

        public void setLocation(Point2D.Float l) {
            this.location = l;
        }

        /** @return true if cluster has enough support */
        public final boolean isVisible() {
            boolean ret = true;
            if (numEvents < getThresholdEventsForVisibleCluster()) {
                ret = false;
            }
            return ret;
        }

        /** @return lifetime of cluster in timestamp ticks */
        public final int getLifetime() {
            return lastTimestamp - firstTimestamp;
        }

        public final void updatePath() {
        }

        public String toString() {
            return String.format("Cluster #%d with %d events near x,y=%d,%d rho=%.0f, theta=%.0f deg, visible=%s", getClusterNumber(), numEvents, (int) location.x, (int) location.y, rhoPixels, thetaRad * 180 / Math.PI, isVisible());
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
            color.getRGBComponents(rgb);
        }

        /** @return averaged velocity of cluster in pixels per second. The velocity is instantaneously
         * computed from the movement of the cluster caused by the last event, then this velocity is mixed
         * with the the old velocity by the mixing factor. Thus the mixing factor is appplied twice: once for moving
         * the cluster and again for changing the velocity.
         */
        public Point2D.Float getVelocity() {
            return velocity;
        }

        /** Sets color according to age of cluster */
        public void setColorAccordingToAge() {
            float brightness = (float) Math.max(0f, Math.min(1f, getLifetime() / fullbrightnessLifetime));
            Color color = Color.getHSBColor(.5f, 1f, brightness);
            setColor(color);
        }

        public int getClusterNumber() {
            return clusterNumber;
        }

        public void setClusterNumber(int clusterNumber) {
            this.clusterNumber = clusterNumber;
        }

        /** @return average ISI for this cluster in timestamp ticks. Average is computed using cluster location mising factor.
         */
        public float getAvgISI() {
            return avgISI;
        }

        public void setAvgISI(float avgISI) {
            this.avgISI = avgISI;
        }

        /** @return average event rate in spikes per timestamp tick. Average is computed using location mixing factor. Note that this measure
         * emphasizes the high spike rates because a few events in rapid succession can rapidly push up the average rate.
         */
        public float getAvgEventRate() {
            return avgEventRate;
        }

        public void setAvgEventRate(float avgEventRate) {
            this.avgEventRate = avgEventRate;
        }

        /** draws LinearFeatureModel in current GL context assuming 1 pixel to 1 graphics unit with chip LL corner at LL canvas corner */
        private void draw(GL gl) {
            double x0, y0, x1, y1;
            double absTheta = Math.abs(thetaRad);
            boolean isVert = ((absTheta < Math.PI / 4) || (absTheta > 3 * Math.PI / 4)) ? true : false;
            double cTheta = Math.cos(thetaRad), sTheta = Math.sin(thetaRad);
            if (isVert) {
                y0 = location.y - length;
                y1 = location.y + length;
                x0 = ((rhoPixels - y0 * sTheta) / cTheta);
                x1 = ((rhoPixels - y1 * sTheta) / cTheta);
            } else {
                x0 = location.x - length;
                x1 = location.x + length;
                y0 = ((rhoPixels - x0 * cTheta) / sTheta);
                y1 = ((rhoPixels - x1 * cTheta) / sTheta);
            }
            getColor().getRGBComponents(rgb);
            float f = getLifetime() / clusterLifetimeWithoutSupportUs;
            if (f > 1) {
                f = 1;
            }
            for (int i = 0; i < 3; i++) {
                rgb[i] *= f;
            }
            if (getStrongestCluster() == this) {
                gl.glColor3fv(rgb, 0);
                gl.glLineWidth(12);
            } else if (isVisible()) {
                gl.glColor3fv(rgb, 0);
                gl.glLineWidth(6);
            } else {
                gl.glColor3f(.3f, .3f, .3f);
                gl.glLineWidth(.5f);
            }
            gl.glBegin(GL.GL_LINE_LOOP);
            {
                gl.glVertex2d(x0, y0);
                gl.glVertex2d(x1, y1);
            }
            gl.glEnd();
        }

        /** Angle of normal to line CCW from x axis with 0 and PI being horizontal and PI/2 being vertical.
        thetaRad spans -PI to PI.
         */
        public double getThetaRad() {
            return thetaRad;
        }

        public void setThetaRad(double thetaRad) {
            this.thetaRad = thetaRad;
        }

        /** Angle of normal to line CCW from x axis with 0 and 180 being horizontal and 90 being vertical.
        thetaDeg spans -180 to 180.
         */
        public double getThetaDeg() {
            return Math.toDegrees(getThetaRad());
        }

        /** length of normal to line in pixels from chip LL corner
         */
        public double getRhoPixels() {
            return rhoPixels;
        }

        public void setRhoPixels(double rhoPixels) {
            this.rhoPixels = rhoPixels;
        }
    }

    /** Returns list of all clusters, including those not "visible" yet.
    @return List of LinearFeatureModel
     */
    public java.util.List<PairedEventLinearEdgeClusterTracker.LinearFeatureModel> getClusters() {
        return this.clusters;
    }

    private boolean computedClusterOrdering = false;

    private volatile LinearFeatureModel strongestCluster = null;

    /** returns cluster with maximum average event rate. Thread safe.
    @return strongest supported cluster in events
     */
    public synchronized LinearFeatureModel getStrongestCluster() {
        if (computedClusterOrdering) {
            return strongestCluster;
        } else {
            float maxRate = Float.NEGATIVE_INFINITY;
            for (LinearFeatureModel c : clusters) {
                if (c.avgEventRate > maxRate) {
                    maxRate = c.avgEventRate;
                    strongestCluster = c;
                }
            }
            computedClusterOrdering = true;
        }
        return strongestCluster;
    }

    protected static final float fullbrightnessLifetime = 1000000;

    private Random random = new Random();

    private static final int clusterColorChannel = 2;

    /** lifetime of cluster in ms without support */
    public final int getClusterLifetimeWithoutSupportUs() {
        return clusterLifetimeWithoutSupportUs;
    }

    /** lifetime of cluster in ms without support */
    public void setClusterLifetimeWithoutSupportUs(final int clusterLifetimeWithoutSupport) {
        this.clusterLifetimeWithoutSupportUs = clusterLifetimeWithoutSupport;
        getPrefs().putInt("MultiLineClusterTracker.clusterLifetimeWithoutSupport", clusterLifetimeWithoutSupport);
    }

    /** max number of clusters */
    public final int getMaxNumClusters() {
        return maxNumClusters;
    }

    /** max number of clusters */
    public void setMaxNumClusters(final int maxNumClusters) {
        this.maxNumClusters = maxNumClusters;
        getPrefs().putInt("MultiLineClusterTracker.maxNumClusters", maxNumClusters);
    }

    /** number of events to make a potential cluster visible */
    public final int getThresholdEventsForVisibleCluster() {
        return thresholdEventsForVisibleCluster;
    }

    /** number of events to make a potential cluster visible */
    public void setThresholdEventsForVisibleCluster(final int thresholdEventsForVisibleCluster) {
        this.thresholdEventsForVisibleCluster = thresholdEventsForVisibleCluster;
        getPrefs().putInt("MultiLineClusterTracker.thresholdEventsForVisibleCluster", thresholdEventsForVisibleCluster);
    }

    public Object getFilterState() {
        return null;
    }

    private boolean isGeneratingFilter() {
        return false;
    }

    public synchronized void resetFilter() {
        clusters.clear();
    }

    public float getMixingFactorRho() {
        return mixingFactorRho;
    }

    public void setMixingFactorRho(float mixingFactor) {
        if (mixingFactor < 0) {
            mixingFactor = 0;
        }
        if (mixingFactor > 1) {
            mixingFactor = 1f;
        }
        this.mixingFactorRho = mixingFactor;
        getPrefs().putFloat("MultiLineClusterTracker.mixingFactorRho", mixingFactorRho);
    }

    public float getMixingFactorTheta() {
        return mixingFactorTheta;
    }

    public void setMixingFactorTheta(float mixingFactor) {
        if (mixingFactor < 0) {
            mixingFactor = 0;
        }
        if (mixingFactor > 1) {
            mixingFactor = 1f;
        }
        this.mixingFactorTheta = mixingFactor;
        getPrefs().putFloat("MultiLineClusterTracker.mixingFactorTheta", mixingFactorTheta);
    }

    /**@see #setColorClustersDifferentlyEnabled */
    public boolean isColorClustersDifferentlyEnabled() {
        return colorClustersDifferentlyEnabled;
    }

    /** @param colorClustersDifferentlyEnabled true to color each cluster a different color. false to color each cluster
     * by its age
     */
    public void setColorClustersDifferentlyEnabled(boolean colorClustersDifferentlyEnabled) {
        this.colorClustersDifferentlyEnabled = colorClustersDifferentlyEnabled;
        getPrefs().putBoolean("MultiLineClusterTracker.colorClustersDifferentlyEnabled", colorClustersDifferentlyEnabled);
    }

    public void update(Observable o, Object arg) {
        initFilter();
    }

    public void annotate(Graphics2D g) {
    }

    public void annotate(float[][][] frame) {
    }

    float[] rgb = new float[4];

    public synchronized void annotate(GLAutoDrawable drawable) {
        final float BOX_LINE_WIDTH = 5f;
        final float PATH_LINE_WIDTH = 3f;
        if (!isFilterEnabled()) {
            return;
        }
        GL gl = drawable.getGL();
        if (gl == null) {
            log.warning("null GL in MultiLineClusterTracker.annotate");
            return;
        }
        gl.glPushMatrix();
        try {
            {
                if (showLineSegments) {
                    for (LineSegment s : segList) {
                        s.draw(gl);
                    }
                }
                for (LinearFeatureModel c : clusters) {
                    if (showAllClusters || c.isVisible()) {
                        c.draw(gl);
                    }
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            log.warning(e.getMessage());
        }
        gl.glPopMatrix();
    }

    public synchronized boolean isLogDataEnabled() {
        return logDataEnabled;
    }

    public synchronized void setLogDataEnabled(boolean logDataEnabled) {
        this.logDataEnabled = logDataEnabled;
        if (!logDataEnabled) {
            logStream.flush();
            logStream.close();
            logStream = null;
        } else {
            try {
                logStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("classTrackerData.txt"))));
                logStream.println("# clusterNumber lasttimestamp x y avergeEventDistance");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isShowAllClusters() {
        return showAllClusters;
    }

    /**Sets annotation visibility of clusters that are not "visible"
     * @param showAllClusters true to show all clusters even if there are not "visible"
     */
    public void setShowAllClusters(boolean showAllClusters) {
        this.showAllClusters = showAllClusters;
        getPrefs().putBoolean("MultiLineClusterTracker.showAllClusters", showAllClusters);
    }

    public boolean isClusterLifetimeIncreasesWithAge() {
        return clusterLifetimeIncreasesWithAge;
    }

    /**
     * If true, cluster lifetime withtout support increases proportional to the age of the cluster relative to the clusterLifetimeWithoutSupportUs time
     */
    public void setClusterLifetimeIncreasesWithAge(boolean clusterLifetimeIncreasesWithAge) {
        this.clusterLifetimeIncreasesWithAge = clusterLifetimeIncreasesWithAge;
        getPrefs().putBoolean("MultiLineClusterTracker.clusterLifetimeIncreasesWithAge", clusterLifetimeIncreasesWithAge);
    }

    public int getEventBufferLength() {
        return eventBufferLength;
    }

    /** Sets tne number of events to consider for forming line segments for clustering
     *@param eventBufferLength the length of the buffer in events
     **/
    public synchronized void setEventBufferLength(int eventBufferLength) {
        if (eventBufferLength < 2) {
            eventBufferLength = 2;
        } else if (eventBufferLength > 1000) {
            eventBufferLength = 1000;
        }
        this.eventBufferLength = eventBufferLength;
        getPrefs().putInt("MultiLineClusterTracker.eventBufferLength", eventBufferLength);
        initBuffers();
    }

    public float getRhoRadius() {
        return rhoRadius;
    }

    public void setRhoRadius(float rhoRadius) {
        this.rhoRadius = rhoRadius;
        getPrefs().putFloat("MultiLineClusterTracker.rhoRadius", rhoRadius);
    }

    public float getThetaRadiusDeg() {
        return (float) Math.toDegrees(thetaRadiusRad);
    }

    /** Sets radius of LineClusters; argument is in degrees for user interface */
    public void setThetaRadiusDeg(float thetaRadius) {
        if (thetaRadius < 0) {
            thetaRadius = 0;
        } else if (thetaRadius > 180) {
            thetaRadius = 180;
        }
        this.thetaRadiusRad = (float) Math.toRadians(thetaRadius);
        getPrefs().putFloat("MultiLineClusterTracker.thetaRadiusRad", this.thetaRadiusRad);
    }

    public int getMinSegmentLength() {
        return minSegmentLength;
    }

    public void setMinSegmentLength(int minSegmentLength) {
        this.minSegmentLength = minSegmentLength;
        getPrefs().putInt("MultiLineClusterTracker.minSegmentLength", minSegmentLength);
    }

    public int getMaxSegmentLength() {
        return maxSegmentLength;
    }

    public void setMaxSegmentLength(int maxSegmentLength) {
        this.maxSegmentLength = maxSegmentLength;
        getPrefs().putInt("MultiLineClusterTracker.maxSegmentLength", maxSegmentLength);
    }

    public int getMaxSegmentDt() {
        return maxSegmentDt;
    }

    public void setMaxSegmentDt(int maxSegmentDt) {
        this.maxSegmentDt = maxSegmentDt;
        getPrefs().putInt("MultiLineClusterTracker.maxSegmentDt", maxSegmentDt);
    }

    public float getMixingFactorPosition() {
        return mixingFactorPosition;
    }

    public void setMixingFactorPosition(float mixingFactorPosition) {
        if (mixingFactorPosition > 1) {
            mixingFactorPosition = 1;
        } else if (mixingFactorPosition < 0) {
            mixingFactorPosition = 0;
        }
        this.mixingFactorPosition = mixingFactorPosition;
        getPrefs().putFloat("MultiLineClusterTracker.mixingFactorPosition", mixingFactorPosition);
    }

    class LineClusterCanvas extends GLCanvas implements GLEventListener {

        GLU glu = new GLU();

        GLUT glut = new GLUT();

        LineClusterCanvas(GLCapabilities caps) {
            super(caps);
            setName("LineClusterCanvas");
            addGLEventListener(this);
            setPreferredSize(new Dimension(400, 200));
        }

        public void init(GLAutoDrawable gLAutoDrawable) {
            GL gl = gLAutoDrawable.getGL();
            log.info("INIT GL IS: " + gl.getClass().getName() + "\nGL_VENDOR: " + gl.glGetString(GL.GL_VENDOR) + "\nGL_RENDERER: " + gl.glGetString(GL.GL_RENDERER) + "\nGL_VERSION: " + gl.glGetString(GL.GL_VERSION));
            gl.setSwapInterval(1);
            gl.glShadeModel(GL.GL_FLAT);
            gl.glClearColor(0, 0, 0, 0f);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);
            gl.glLoadIdentity();
            gl.glRasterPos3f(0, 0, 0);
            gl.glColor3f(1, 1, 1);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Initialized display");
        }

        public void display(GLAutoDrawable gLAutoDrawable) {
            float[] rgb = new float[4];
            float w = gLAutoDrawable.getWidth();
            float h = gLAutoDrawable.getHeight();
            GL gl = gLAutoDrawable.getGL();
            gl.glClearColor(0, 0, 0, 0f);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);
            gl.glPushMatrix();
            gl.glPointSize(10);
            gl.glColor3f(1, 1, 1);
            gl.glBegin(GL.GL_POINTS);
            for (LinearFeatureModel c : clusters) {
                float rho = (int) ((float) c.rhoPixels / chip.getMaxSize() * h);
                float theta = (float) ((Math.PI + c.thetaRad) / Math.PI / 2 * w);
                gl.glColor3fv(c.rgb, 0);
                gl.glVertex2f(rho, theta);
            }
            gl.glPopMatrix();
            gl.glEnd();
        }

        public void reshape(GLAutoDrawable gLAutoDrawable, int x, int y, int w, int h) {
            GL gl = getGL();
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glViewport(0, 0, w, h);
        }

        public void displayChanged(GLAutoDrawable gLAutoDrawable, boolean b, boolean b0) {
        }

        /** Utility method to check for GL errors
        @param g the GL context
        @param glu the GLU used to obtain the error strings
        @param msg an error message to log to e.g., show the context
         */
        public void checkGLError(GL g, GLU glu, String msg) {
            int error = g.glGetError();
            int nerrors = 10;
            while (error != GL.GL_NO_ERROR && nerrors-- != 0) {
                log.warning("GL error number " + error + " " + glu.gluErrorString(error) + " : " + msg);
                error = g.glGetError();
            }
        }
    }

    public boolean isShowLineSegments() {
        return showLineSegments;
    }

    public void setShowLineSegments(boolean showLineSegments) {
        this.showLineSegments = showLineSegments;
    }

    public float getMinDistanceNormalized() {
        return minDistanceNormalized;
    }

    public void setMinDistanceNormalized(float minDistanceNormalized) {
        if (minDistanceNormalized < 0) {
            minDistanceNormalized = 0;
        } else if (minDistanceNormalized > 2) {
            minDistanceNormalized = 2;
        }
        this.minDistanceNormalized = minDistanceNormalized;
        getPrefs().putFloat("MultiLineClusterTracker.minDistanceNormalized", minDistanceNormalized);
    }

    public float getMixingFactorLength() {
        return mixingFactorLength;
    }

    public void setMixingFactorLength(float mixingFactorLength) {
        this.mixingFactorLength = mixingFactorLength;
        getPrefs().putFloat("MultiLineClusterTracker.mixingFactorLength", mixingFactorLength);
    }

    public boolean isLengthEnabled() {
        return lengthEnabled;
    }

    public void setLengthEnabled(boolean lengthEnabled) {
        this.lengthEnabled = lengthEnabled;
        getPrefs().putBoolean("MultiLineClusterTracker.lengthEnabled", lengthEnabled);
    }

    public boolean isWeightLengthEnabled() {
        return weightLengthEnabled;
    }

    public void setWeightLengthEnabled(boolean weightLengthEnabled) {
        this.weightLengthEnabled = weightLengthEnabled;
        getPrefs().putBoolean("MultiLineClusterTracker.weightLengthEnabled", weightLengthEnabled);
    }

    private synchronized void initBuffers() {
        OrientationEvent[] a = new OrientationEvent[eventBufferLength];
        Arrays.fill(a, new OrientationEvent());
        eventBuffer = new LIFOEventBuffer<OrientationEvent>(a);
    }

    public boolean isRenderInputEvents() {
        return renderInputEvents;
    }

    public void setRenderInputEvents(boolean renderInputEvents) {
        this.renderInputEvents = renderInputEvents;
        getPrefs().putBoolean("MultiLineClusterTracker.renderInputEvents", renderInputEvents);
    }

    class LineClusterComparator implements Comparator<LinearFeatureModel> {

        public int compare(PairedEventLinearEdgeClusterTracker.LinearFeatureModel o1, PairedEventLinearEdgeClusterTracker.LinearFeatureModel o2) {
            final float f1 = o1.getAvgEventRate(), f2 = o2.getAvgEventRate();
            if (f1 > f2) {
                return 1;
            } else if (f1 < f2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * returns the filtered Hough line radius estimate - the closest distance from the middle of the chip image.
     *
     * @return the distance in pixels. If the chip size is sx by sy, can range over +-Math.sqrt( (sx/2)^2 + (sy/2)^2).
     *     This number is positive if the line is above the origin (center of chip)
     */
    public float getRhoPixelsFiltered() {
        return rhoPixelsFiltered;
    }

    /**
     *     returns the filtered angle of the line cluster normal.
     *
     * @return angle in degrees. Ranges from -180 to 180 degrees, 
    where -180 and 180 represent a vertical line (horizontal line normal)
    and 90 and -90 is a horizontal line (??? not consistent with LineDetector interface definition!!! - should fix) TO-DO
     */
    public float getThetaDegFiltered() {
        return thetaDegFiltered;
    }

    public float getTauMs() {
        return tauMs;
    }

    public synchronized void setTauMs(float tauMs) {
        this.tauMs = tauMs;
        getPrefs().putFloat("MultiLineClusterTracker.tauMs", tauMs);
    }

    /** computes the output parameters using the given (assumed non null) line cluster.
    Transforms the ooordinates of the line to the chip image center
    accordiing to the LineDetector interface.

     */
    private void computeOutputLineParameters(LinearFeatureModel strongest, EventPacket packet) {
        int tx = -sizex / 2, ty = -sizey / 2;
        double rad = strongest.getThetaRad();
        double rho = strongest.getRhoPixels() + tx * Math.cos(rad) + ty * Math.sin(rad);
    }

    public int getOriDiffAllowed() {
        return oriDiffAllowed;
    }

    public void setOriDiffAllowed(int oriDiffAllowed) {
        if (oriDiffAllowed < 0) {
            oriDiffAllowed = 0;
        } else if (oriDiffAllowed > 4) {
            oriDiffAllowed = 4;
        }
        this.oriDiffAllowed = oriDiffAllowed;
        getPrefs().putInt("MultiLineClusterTracker.oriDiffAllowed", oriDiffAllowed);
    }
}
