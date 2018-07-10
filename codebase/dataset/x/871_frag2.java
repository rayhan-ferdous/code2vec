    private org.omg.CORBA.portable.OutputStream _OB_op_for_suppliers(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        org.omg.CosEventChannelAdmin.SupplierAdmin _ob_r = for_suppliers();

        out = handler.createReply();

        org.omg.CosEventChannelAdmin.SupplierAdminHelper.write(out, _ob_r);

        return out;

    }
