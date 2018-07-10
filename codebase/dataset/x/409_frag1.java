    private org.omg.CORBA.portable.OutputStream _OB_op_get_all_filters(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        int[] _ob_r = get_all_filters();

        out = handler.createReply();

        org.omg.CosNotifyFilter.FilterIDSeqHelper.write(out, _ob_r);

        return out;

    }
