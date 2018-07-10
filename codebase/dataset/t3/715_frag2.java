    public void cosSynchronizeManagedProcess(java.lang.String peerName, java.lang.String entity, short weightage, tcg.syscontrol.cos.CosProcessStatusEnum status) {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("cosSynchronizeManagedProcess", true);

                    _os.write_string(peerName);

                    _os.write_string(entity);

                    _os.write_short(weightage);

                    tcg.syscontrol.cos.CosProcessStatusEnumHelper.write(_os, status);

                    _is = _invoke(_os);

                    return;

                } catch (org.omg.CORBA.portable.RemarshalException _rx) {

                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();
