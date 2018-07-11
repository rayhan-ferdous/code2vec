    private org.omg.CORBA.portable.OutputStream _OB_att_get_admin_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Admin _ob_r = admin_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        AdminHelper.write(out, _ob_r);

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_att_get_link_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Link _ob_r = link_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        LinkHelper.write(out, _ob_r);

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_att_get_lookup_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Lookup _ob_r = lookup_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        LookupHelper.write(out, _ob_r);

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_att_get_proxy_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
