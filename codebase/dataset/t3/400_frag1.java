                        com.sun.corba.se.spi.activation.ServerNotActiveHelper.write(out, $ex);

                    } catch (com.sun.corba.se.spi.activation.ServerNotRegistered $ex) {

                        out = $rh.createExceptionReply();

                        com.sun.corba.se.spi.activation.ServerNotRegisteredHelper.write(out, $ex);

                    }

                    break;

                }

            case 5:
