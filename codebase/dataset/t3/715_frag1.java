    public void cosProcessTerminating(java.lang.String entity, tcg.syscontrol.cos.CosTerminationCodeEnum p_code) {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("cosProcessTerminating", true);

                    _os.write_string(entity);

                    tcg.syscontrol.cos.CosTerminationCodeEnumHelper.write(_os, p_code);

                    _is = _invoke(_os);

                    return;

                } catch (org.omg.CORBA.portable.RemarshalException _rx) {

                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();
