            int p = position(suite, connectionSuites);

            if (p < 0 || p >= connectionPos) {

                logger.log(Level.FINEST, "connection did not try all better suites");

                return false;

            }

        }

        if (clientPrincipal != null) {

            Exception exception;

            try {

                authManager.checkAuthentication();

                exception = null;
