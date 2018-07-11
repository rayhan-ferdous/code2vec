    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {

        if (headers == null) {

            loadHeaders();

        }

        return headers.getNonMatchingHeaderLines(names);

    }
