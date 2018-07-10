    private org.omg.CORBA.portable.OutputStream _OB_op_get_property_mode(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            String _ob_a0 = PropertyNameHelper.read(in);

            PropertyModeType _ob_r = get_property_mode(_ob_a0);

            out = handler.createReply();

            PropertyModeTypeHelper.write(out, _ob_r);

        } catch (PropertyNotFound _ob_ex) {

            out = handler.createExceptionReply();

            PropertyNotFoundHelper.write(out, _ob_ex);

        } catch (InvalidPropertyName _ob_ex) {

            out = handler.createExceptionReply();

            InvalidPropertyNameHelper.write(out, _ob_ex);

        }

        return out;

    }
