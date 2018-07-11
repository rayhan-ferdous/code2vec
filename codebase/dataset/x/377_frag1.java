    private org.omg.CORBA.portable.OutputStream _OB_att_get_lookup_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Lookup _ob_r = lookup_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        LookupHelper.write(out, _ob_r);

        return out;

    }
