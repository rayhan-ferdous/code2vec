    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {

        if (headers == null) {

            loadHeaders();

        }

        return headers.getMatchingHeaders(names);

    }
