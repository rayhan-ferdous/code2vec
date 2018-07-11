    private ActiveAssociation openAssoc() throws IOException, GeneralSecurityException {

        Association assoc = aFact.newRequestor(newSocket(url.getHost(), url.getPort()));

        assoc.setAcTimeout(acTimeout);

        assoc.setDimseTimeout(dimseTimeout);

        assoc.setSoCloseDelay(soCloseDelay);

        assoc.setPackPDVs(packPDVs);

        PDU assocAC = assoc.connect(assocRQ);

        if (!(assocAC instanceof AAssociateAC)) {

            return null;

        }

        ActiveAssociation retval = aFact.newActiveAssociation(assoc, null);

        retval.start();

        return retval;

    }
