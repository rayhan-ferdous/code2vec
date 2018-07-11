    private org.omg.CORBA.portable.OutputStream _OB_att_get_MyAdmin(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        SupplierAdmin _ob_r = MyAdmin();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        SupplierAdminHelper.write(out, _ob_r);

        return out;

    }
