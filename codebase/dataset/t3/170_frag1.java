    private org.omg.CORBA.portable.OutputStream _OB_op_get_number_of_properties(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        int _ob_r = get_number_of_properties();

        out = handler.createReply();

        out.write_ulong(_ob_r);

        return out;

    }
