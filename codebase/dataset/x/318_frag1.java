        } catch (IOException ex) {

            logger.log(Level.SEVERE, "IO exception occurs", ex);

            ldstTO = new LOCKSSDaemonStatusTableTO();

            ldstTO.setBoxHttpStatusOK(false);

            if (httpget != null) {

                httpget.abort();

            }

            return ldstTO;

        } finally {

            if (entity != null) {
