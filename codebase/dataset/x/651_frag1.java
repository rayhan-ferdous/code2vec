    public String valueBeforeMD5 = "";



    public String valueAfterMD5 = "";



    private static Random myRand;



    private static SecureRandom mySecureRand;



    private static String s_id;



    /**

	 * Static block to take care of one time {@link SecureRandom} seed. It takes

	 * a few seconds to initialize {@code SecureRandom}. You might want to

	 * consider removing this static block or replacing it with a

	 * "time since first loaded" seed to reduce this time. This block will run

	 * only once per JVM instance.

	 */

    static {

        mySecureRand = new SecureRandom();

        long secureInitializer = mySecureRand.nextLong();

        myRand = new Random(secureInitializer);

        try {

            s_id = InetAddress.getLocalHost().toString();

        } catch (UnknownHostException e) {

            e.printStackTrace();
