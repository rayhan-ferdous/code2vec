    private int daqDataPort = DEFAULT_DAQ_DATA_PORT;



    private static final int DEFAULT_CACHE_SIZE = 900;



    private int cacheSize = DEFAULT_CACHE_SIZE;



    private static final int DEFAULT_ARCHIVE_SIZE = 0;



    private int archiveSize = DEFAULT_ARCHIVE_SIZE;



    private static final boolean USE_TIME = true;



    private static final long GROUPING_TIME = 100;



    private static final long GROUPING_COUNT = 100;



    /** LJM 060519

    * variable to hold the time (in hours) desired for the length of the ring buffer

    * user to calculate cache and archive.

    */

    private double rbTime = -1.0;



    /** a variable to set what percentage of the archived frames are to be

    * cached by the rbnb server.

    */

    private static final double DEFAULT_CACHE_PERCENT = 10;
