    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {

        org.omg.CORBA.portable.OutputStream _out = null;

        java.lang.Integer opsIndex = (java.lang.Integer) m_opsHash.get(method);

        if (null == opsIndex) throw new org.omg.CORBA.BAD_OPERATION(method + " not found");

        switch(opsIndex.intValue()) {

            case 0:

                {

                    try {
