            subscription_change(_ob_a0, _ob_a1);

            out = handler.createReply();

        } catch (org.omg.CosNotifyComm.InvalidEventType _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosNotifyComm.InvalidEventTypeHelper.write(out, _ob_ex);

        }

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_op_validate_qos(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            org.omg.CosNotification.Property[] _ob_a0 = org.omg.CosNotification.QoSPropertiesHelper.read(in);

            org.omg.CosNotification.NamedPropertyRangeSeqHolder _ob_ah1 = new org.omg.CosNotification.NamedPropertyRangeSeqHolder();
