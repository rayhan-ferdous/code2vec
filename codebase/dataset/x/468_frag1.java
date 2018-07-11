    private org.omg.CORBA.portable.OutputStream _OB_att_get_type_repos(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.Object _ob_r = type_repos();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        TypeRepositoryHelper.write(out, _ob_r);

        return out;

    }
