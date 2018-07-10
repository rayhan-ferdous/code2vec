    public tcg.syscontrol.cos.ICosManagedProcess cosGetActiveManagedProcess2(java.lang.String entity) throws tcg.syscontrol.cos.CosUnknownProcessException {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("cosGetActiveManagedProcess2", true);

                    _os.write_string(entity);

                    _is = _invoke(_os);

                    tcg.syscontrol.cos.ICosManagedProcess _result = tcg.syscontrol.cos.ICosManagedProcessHelper.read(_is);

                    return _result;

                } catch (org.omg.CORBA.portable.RemarshalException _rx) {

                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();

                    if (_id.equals("IDL:tcg/syscontrol/cos/CosUnknownProcessException:1.0")) {

                        throw tcg.syscontrol.cos.CosUnknownProcessExceptionHelper.read(_ax.getInputStream());

                    }

                    throw new RuntimeException("Unexpected exception " + _id);

                } finally {

                    this._releaseReply(_is);

                }

            } else {

                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cosGetActiveManagedProcess2", _opsClass);

                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");

                ICosProcessManagerOperations _localServant = (ICosProcessManagerOperations) _so.servant;

                tcg.syscontrol.cos.ICosManagedProcess _result;

                try {

                    _result = _localServant.cosGetActiveManagedProcess2(entity);

                } finally {

                    _servant_postinvoke(_so);

                }

                return _result;

            }

        }

    }
