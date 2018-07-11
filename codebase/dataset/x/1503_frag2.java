    private org.omg.CORBA.portable.OutputStream _OB_att_get_MyAdmin(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CosNotifyChannelAdmin.ConsumerAdmin _ob_r = MyAdmin();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        org.omg.CosNotifyChannelAdmin.ConsumerAdminHelper.write(out, _ob_r);

        return out;

    }
