    private void closeConnection() {

        try {

            ftp.logout();

            ftp.disconnect();

        } catch (IOException a_excp) {

            throw new RuntimeException(a_excp);

        } catch (Throwable a_th) {

            throw new RuntimeException(a_th);

        }

    }
