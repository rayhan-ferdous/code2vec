    private final void pingHostname() {

        try {

            Process p = Runtime.getRuntime().exec("ping -c 1 www.google.com");

            int status = p.waitFor();

            if (status == 0) {

                mPingHostnameResult = "Pass";

            } else {

                mPingHostnameResult = "Fail: Host unreachable";

            }

        } catch (UnknownHostException e) {

            mPingHostnameResult = "Fail: Unknown Host";

        } catch (IOException e) {

            mPingHostnameResult = "Fail: IOException";

        } catch (InterruptedException e) {

            mPingHostnameResult = "Fail: InterruptedException";

        }

    }
