    private org.omg.CORBA.portable.OutputStream _OB_op_obtain_notification_pull_consumer(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            ClientType _ob_a0 = ClientTypeHelper.read(in);

            org.omg.CORBA.IntHolder _ob_ah1 = new org.omg.CORBA.IntHolder();

            ProxyConsumer _ob_r = obtain_notification_pull_consumer(_ob_a0, _ob_ah1);

            out = handler.createReply();

            ProxyConsumerHelper.write(out, _ob_r);

            ProxyIDHelper.write(out, _ob_ah1.value);

        } catch (AdminLimitExceeded _ob_ex) {

            out = handler.createExceptionReply();

            AdminLimitExceededHelper.write(out, _ob_ex);

        }

        return out;

    }
