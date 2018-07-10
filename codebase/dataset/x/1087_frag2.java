    public static String getLocationType(Model model, String uri) {

        String ret = null;

        Resource resURI = null;

        initModelHash();

        if (model != null) {

            resURI = model.getResource(uri);

            if (resURI != null) {

                NodeIterator niter = model.listObjectsOfProperty(resURI, RDF.type);

                if (niter.hasNext()) {

                    String type = niter.nextNode().toString();

                    ret = checkIsLocation(type);

                    if (ret == null) {

                        ret = getLocationHelper(model, type);

                    }

                }

            }

        }

        return ret;

    }
