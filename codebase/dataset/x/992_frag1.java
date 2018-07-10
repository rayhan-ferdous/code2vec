    boolean isTransferEncodingChunked(HttpMessage m) {

        List<String> chunked = m.getHeaders(HttpHeaders.Names.TRANSFER_ENCODING);

        if (chunked.isEmpty()) {

            return false;

        }

        for (String v : chunked) {

            if (v.equalsIgnoreCase(HttpHeaders.Values.CHUNKED)) {

                return true;

            }

        }

        return false;

    }
