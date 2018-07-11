    private org.omg.CORBA.portable.OutputStream _OB_op_remove_filter(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            int _ob_a0 = org.omg.CosNotifyFilter.FilterIDHelper.read(in);

            remove_filter(_ob_a0);

            out = handler.createReply();

        } catch (org.omg.CosNotifyFilter.FilterNotFound _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosNotifyFilter.FilterNotFoundHelper.write(out, _ob_ex);

        }

        return out;

    }
