    private org.omg.CORBA.portable.OutputStream _OB_op_say_hello(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        String _ob_r = say_hello();

        out = handler.createReply();

        out.write_string(_ob_r);

        return out;

    }
