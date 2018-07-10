        Properties props = new Properties();

        DataInputStream dis = readS3BinaryHeader(location, path, props);

        String version = props.getProperty("version");

        boolean doCheckSum;

        if (version == null || !version.equals(MIXW_FILE_VERSION)) {

            throw new IOException("Unsupported version in " + path);

        }

        String checksum = props.getProperty("chksum0");

        doCheckSum = (checksum != null && checksum.equals("yes"));

        Pool pool = new Pool(path);

        numStates = readInt(dis);

        numStreams = readInt(dis);

        numGaussiansPerState = readInt(dis);

        numValues = readInt(dis);
