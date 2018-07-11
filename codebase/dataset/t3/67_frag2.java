    private org.omg.CORBA.portable.OutputStream _OB_op_obtain_notification_push_supplier(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            ClientType _ob_a0 = ClientTypeHelper.read(in);

            org.omg.CORBA.IntHolder _ob_ah1 = new org.omg.CORBA.IntHolder();

            ProxySupplier _ob_r = obtain_notification_push_supplier(_ob_a0, _ob_ah1);

            out = handler.createReply();

            ProxySupplierHelper.write(out, _ob_r);

            ProxyIDHelper.write(out, _ob_ah1.value);

        } catch (AdminLimitExceeded _ob_ex) {

            out = handler.createExceptionReply();

            AdminLimitExceededHelper.write(out, _ob_ex);

        }

        return out;

    }
