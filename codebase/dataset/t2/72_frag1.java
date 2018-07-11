    public boolean supportsInput(ContentType contentType) {

        switch(contentType) {

            case XML:

                return true;

            case ByteStream:

            case CharStream:

            case ResultSet:

            default:

                return false;

        }

    }
