    private org.omg.CORBA.portable.OutputStream _OB_att_get_pid_service(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.PersonIdService.IdentificationComponent _ob_r = pid_service();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        org.omg.DsObservationAccess.IdentificationComponentHelper.write(out, _ob_r);

        return out;

    }
