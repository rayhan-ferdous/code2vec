    private final void pingIpAddr() {

        try {

            String ipAddress = "74.125.47.104";

            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ipAddress);

            int status = p.waitFor();

            if (status == 0) {

                mPingIpAddrResult = "Pass";

            } else {

                mPingIpAddrResult = "Fail: IP addr not reachable";

            }

        } catch (IOException e) {

            mPingIpAddrResult = "Fail: IOException";

        } catch (InterruptedException e) {

            mPingIpAddrResult = "Fail: InterruptedException";

        }

    }
