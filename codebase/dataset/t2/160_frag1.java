        if (st.nextToken() != ',') throw new IOException("Missing ','");

        if (st.nextToken() != '"') throw new IOException("Invalid string");

        String oldc = st.sval;

        if (st.nextToken() != ',') throw new IOException("Missing ','");

        if (st.nextToken() != '"') throw new IOException("Invalid string");
