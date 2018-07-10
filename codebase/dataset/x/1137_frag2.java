    public byte[] getStdout() {

        if (stdout != null) {

            try {

                stdoutReq.waitFor();

            } catch (InterruptedException ie) {

            }

            return stdout.out.toByteArray();

        }

        return null;

    }
