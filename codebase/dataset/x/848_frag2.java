        retval.start();

        return retval;

    }



    public void echo() throws InterruptedException, IOException, GeneralSecurityException {

        long t1 = System.currentTimeMillis();

        int count = 0;

        for (int i = 0; i < repeatWhole; ++i) {

            ActiveAssociation active = openAssoc();

            if (active != null) {

                if (active.getAssociation().getAcceptedTransferSyntaxUID(PCID_ECHO) == null) {
