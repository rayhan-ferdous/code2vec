    private org.omg.CORBA.portable.OutputStream _OB_op_add_filter(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        org.omg.CosNotifyFilter.Filter _ob_a0 = org.omg.CosNotifyFilter.FilterHelper.read(in);

        int _ob_r = add_filter(_ob_a0);

        out = handler.createReply();

        org.omg.CosNotifyFilter.FilterIDHelper.write(out, _ob_r);

        return out;

    }
