    public void set_attribute_config_4(fr.esrf.Tango.AttributeConfig_3[] new_conf, fr.esrf.Tango.ClntIdent cl_ident) throws fr.esrf.Tango.DevFailed {

        while (true) {

            if (!this._is_local()) {

                org.omg.CORBA.portable.InputStream _is = null;

                try {

                    org.omg.CORBA.portable.OutputStream _os = _request("set_attribute_config_4", true);

                    fr.esrf.Tango.AttributeConfigList_3Helper.write(_os, new_conf);

                    fr.esrf.Tango.ClntIdentHelper.write(_os, cl_ident);

                    _is = _invoke(_os);

                    return;

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

                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("set_attribute_config_4", _opsClass);

                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");

                Device_4Operations _localServant = (Device_4Operations) _so.servant;

                try {

                    _localServant.set_attribute_config_4(new_conf, cl_ident);

                } finally {

                    _servant_postinvoke(_so);

                }

                return;

            }

        }

    }
