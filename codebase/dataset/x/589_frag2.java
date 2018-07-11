    public InputStream getResourceAsStream(String name) throws IOException, MalformedURLException {

        try {

            synchronized (privilegedExceptionActionWrapper) {

                privilegedExceptionActionWrapper.setNextActionGetResourceAsStream(name);

                return (InputStream) AccessController.doPrivileged(privilegedExceptionActionWrapper);

            }

        } catch (PrivilegedActionException e) {

            Exception ne = e.getException();

            if (ne instanceof IOException) {

                throw (IOException) ne;

            }

            if (ne instanceof MalformedURLException) {

                throw (MalformedURLException) ne;

            }

            throw new RuntimeException("unexpected error retrieving resource as stream", ne);

        }

    }
