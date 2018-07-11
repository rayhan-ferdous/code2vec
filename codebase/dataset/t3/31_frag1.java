    public byte[] retrieveResourceFileBytes(String resourceFileReference) {

        if (!ResourceFileSupport.isValidResourceFileName(resourceFileReference)) {

            System.out.println("ResourceFileReference not in acceptable form: " + resourceFileReference);

            return null;

        }

        boolean wasDisconnected = !this.isConnected();

        if (wasDisconnected && !this.connect()) {

            return null;

        }

        try {

            return this.basicRetrieveResourceFileBytes(resourceFileReference);

        } finally {

            if (wasDisconnected && !this.disconnect()) {

                System.out.println("Disconnection failed");

            }

        }

    }
