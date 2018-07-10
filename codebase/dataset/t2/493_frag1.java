    private org.omg.CORBA.portable.OutputStream _OB_op_get_qos(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        org.omg.CosNotification.Property[] _ob_r = get_qos();

        out = handler.createReply();

        org.omg.CosNotification.QoSPropertiesHelper.write(out, _ob_r);

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_op_obtain_notification_pull_consumer(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
