    private org.omg.CORBA.portable.OutputStream _OB_op_get_admin(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        org.omg.CosNotification.Property[] _ob_r = get_admin();

        out = handler.createReply();

        org.omg.CosNotification.AdminPropertiesHelper.write(out, _ob_r);

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_op_get_all_consumeradmins(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
