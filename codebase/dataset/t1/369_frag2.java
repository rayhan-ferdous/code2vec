    public byte[] formerrMessage(byte[] in) {

        Header header;

        try {

            header = new Header(in);

        } catch (IOException e) {

            return null;

        }

        return buildErrorMessage(header, Rcode.FORMERR, null);

    }
