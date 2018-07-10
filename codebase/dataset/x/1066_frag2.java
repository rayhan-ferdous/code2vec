            public Object run() {

                try {

                    InetAddress a1 = InetAddress.getByName(h1);

                    InetAddress a2 = InetAddress.getByName(h2);

                    result[0] = a1.equals(a2);

                } catch (UnknownHostException e) {

                } catch (SecurityException e) {

                }

                return null;

            }
