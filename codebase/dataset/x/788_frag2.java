    private void removeDeviceIdentifiers(String deviceName) {

        Element identifiers = retrieveDeviceIdentificationElement(deviceName);

        if (identifiers != null) {

            identifiers.detach();

        }

    }
