    private org.omg.CORBA.portable.OutputStream _OB_op_disconnect_pull_supplier(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        disconnect_pull_supplier();

        out = handler.createReply();

        return out;

    }
