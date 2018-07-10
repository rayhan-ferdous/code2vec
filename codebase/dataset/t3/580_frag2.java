    public fr.esrf.Tango.DevCmdInfo_2 command_query_2(java.lang.String command) throws fr.esrf.Tango.DevFailed {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("command_query_2", true);

                    _os.write_string(command);

                    _is = _invoke(_os);

                    fr.esrf.Tango.DevCmdInfo_2 _result = fr.esrf.Tango.DevCmdInfo_2Helper.read(_is);

                    return _result;

                } catch (org.omg.CORBA.portable.RemarshalException _rx) {

                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();

                    if (_id.equals("IDL:Tango/DevFailed:1.0")) {

                        throw fr.esrf.Tango.DevFailedHelper.read(_ax.getInputStream());

                    } else throw new RuntimeException("Unexpected exception " + _id);

                } finally {

                    this._releaseReply(_is);

                }

            } else {

                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("command_query_2", _opsClass);

                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");

                Device_4Operations _localServant = (Device_4Operations) _so.servant;

                fr.esrf.Tango.DevCmdInfo_2 _result;

                try {

                    _result = _localServant.command_query_2(command);

                } finally {

                    _servant_postinvoke(_so);

                }

                return _result;

            }

        }

    }
