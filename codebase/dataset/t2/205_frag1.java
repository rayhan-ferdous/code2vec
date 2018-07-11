    public Map getImports() {

        if (isDebugEnabled) {

            log.debug(myClassName + ".getImports()");

        }

        if (wsdlDefinition != null) {

            return wsdlDefinition.getImports();

        }

        return null;

    }
