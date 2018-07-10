    private org.omg.CORBA.portable.OutputStream _OB_op_push_structured_event(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            org.omg.CosNotification.StructuredEvent _ob_a0 = org.omg.CosNotification.StructuredEventHelper.read(in);

            push_structured_event(_ob_a0);

            out = handler.createReply();

        } catch (org.omg.CosEventComm.Disconnected _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosEventComm.DisconnectedHelper.write(out, _ob_ex);

        }

        return out;

    }
