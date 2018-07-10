    protected Object getTypeObject() {

        Object typeObject = getParseToIndexTypeObject();

        if (typeObject != null) {

            return typeObject;

        }

        java.util.Map<?, ?> options = getOptions();

        if (options != null) {

            typeObject = options.get(org.emftext.language.OCL.resource.OCL.IOCLOptions.RESOURCE_CONTENT_TYPE);

        }

        return typeObject;

    }
