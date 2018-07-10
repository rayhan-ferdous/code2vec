        mySecureRand = new SecureRandom();

        long secureInitializer = mySecureRand.nextLong();

        myRand = new Random(secureInitializer);

        try {

            s_id = InetAddress.getLocalHost().toString();

        } catch (UnknownHostException e) {

            e.printStackTrace();

        }

    }
