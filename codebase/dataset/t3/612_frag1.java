    private org.omg.CORBA.portable.OutputStream _OB_op_delete_properties(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            String[] _ob_a0 = PropertyNamesHelper.read(in);

            delete_properties(_ob_a0);

            out = handler.createReply();

        } catch (MultipleExceptions _ob_ex) {

            out = handler.createExceptionReply();

            MultipleExceptionsHelper.write(out, _ob_ex);

        }

        return out;

    }
