                } catch (org.omg.CORBA.portable.ApplicationException _ax) {

                    String _id = _ax.getId();

                    throw new RuntimeException("Unexpected exception " + _id);

                } finally {

                    this._releaseReply(_is);

                }

            } else {

                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cosGetProcessManagerName", _opsClass);

                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
