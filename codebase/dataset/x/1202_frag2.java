    public void cosUpdateCorbaServerOperationMode(java.lang.String uniqueKey, tcg.syscontrol.cos.CosOperationModeEnum operationMode) {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("cosUpdateCorbaServerOperationMode", true);

                    _os.write_string(uniqueKey);

                    tcg.syscontrol.cos.CosOperationModeEnumHelper.write(_os, operationMode);

                    _is = _invoke(_os);

                    return;

                } catch (org.omg.CORBA.portable.RemarshalException _rx) {

                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();

                    throw new RuntimeException("Unexpected exception " + _id);

                } finally {

                    this._releaseReply(_is);

                }

            } else {

                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cosUpdateCorbaServerOperationMode", _opsClass);

                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");

                ICosProcessManagerOperations _localServant = (ICosProcessManagerOperations) _so.servant;

                try {

                    _localServant.cosUpdateCorbaServerOperationMode(uniqueKey, operationMode);

                } finally {

                    _servant_postinvoke(_so);

                }

                return;

            }

        }

    }
