    public Map getServices() {

        if (isDebugEnabled) {

            log.debug(myClassName + ".getServices()");

        }

        if (wsdlDefinition != null) {

            return wsdlDefinition.getServices();

        }

        return null;

    }
