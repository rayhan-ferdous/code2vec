    public static void main(String[] args) throws Exception {

        XMLConfigurator.getInstance().getProperties();

        Map<String, String> properties = new HashMap<String, String>();

        properties.put("username", args[1]);

        properties.put("password", args[2]);

        properties.put("server", args[0]);

        ClusterUtil util = ClusterUtilFactory.newInstance(RocksClusterFactoryImpl.class.getName()).createUtil(properties);

        util.scheduleJob(new Long(35), YourOwnCalucation3.class.getName());

        util.scheduleJob(new Long(40), YourOwnCalucation3.class.getName());

        util.scheduleJob(new Long(45), YourOwnCalucation3.class.getName());

        util.scheduleJob(new Long(50), YourOwnCalucation3.class.getName());

        System.out.println(util.startNode());

        System.out.println(util.startNode());

        System.out.println(util.startNode());

        System.out.println(util.startNode());

        util.destroy();

    }
