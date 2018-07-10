package com.sun.corba.se.spi.activation;

/**
* com/sun/corba/se/spi/activation/_RepositoryImplBase.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../../../../src/share/classes/com/sun/corba/se/spi/activation/activation.idl
* Monday, March 9, 2009 1:55:37 AM GMT-08:00
*/
public abstract class _RepositoryImplBase extends org.omg.CORBA.portable.ObjectImpl implements com.sun.corba.se.spi.activation.Repository, org.omg.CORBA.portable.InvokeHandler {

    public _RepositoryImplBase() {
    }

    private static java.util.Hashtable _methods = new java.util.Hashtable();

    static {
        _methods.put("registerServer", new java.lang.Integer(0));
        _methods.put("unregisterServer", new java.lang.Integer(1));
        _methods.put("getServer", new java.lang.Integer(2));
        _methods.put("isInstalled", new java.lang.Integer(3));
        _methods.put("install", new java.lang.Integer(4));
        _methods.put("uninstall", new java.lang.Integer(5));
        _methods.put("listRegisteredServers", new java.lang.Integer(6));
        _methods.put("getApplicationNames", new java.lang.Integer(7));
        _methods.put("getServerID", new java.lang.Integer(8));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler $rh) {
        org.omg.CORBA.portable.OutputStream out = null;
        java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
        if (__method == null) throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        switch(__method.intValue()) {
            case 0:
                {
                    try {
                        com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef serverDef = com.sun.corba.se.spi.activation.RepositoryPackage.ServerDefHelper.read(in);
                        int $result = (int) 0;
                        $result = this.registerServer(serverDef);
                        out = $rh.createReply();
                        out.write_long($result);
                    } catch (com.sun.corba.se.spi.activation.ServerAlreadyRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerAlreadyRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.spi.activation.BadServerDefinition $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.BadServerDefinitionHelper.write(out, $ex);
                    }
                    break;
                }
            case 1:
                {
                    try {
                        int serverId = com.sun.corba.se.spi.activation.ServerIdHelper.read(in);
                        this.unregisterServer(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.spi.activation.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 2:
                {
                    try {
                        int serverId = com.sun.corba.se.spi.activation.ServerIdHelper.read(in);
                        com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef $result = null;
                        $result = this.getServer(serverId);
                        out = $rh.createReply();
                        com.sun.corba.se.spi.activation.RepositoryPackage.ServerDefHelper.write(out, $result);
                    } catch (com.sun.corba.se.spi.activation.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 3:
                {
                    try {
                        int serverId = com.sun.corba.se.spi.activation.ServerIdHelper.read(in);
                        boolean $result = false;
                        $result = this.isInstalled(serverId);
                        out = $rh.createReply();
                        out.write_boolean($result);
                    } catch (com.sun.corba.se.spi.activation.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            case 4:
                {
                    try {
                        int serverId = com.sun.corba.se.spi.activation.ServerIdHelper.read(in);
                        this.install(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.spi.activation.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.spi.activation.ServerAlreadyInstalled $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerAlreadyInstalledHelper.write(out, $ex);
                    }
                    break;
                }
            case 5:
                {
                    try {
                        int serverId = com.sun.corba.se.spi.activation.ServerIdHelper.read(in);
                        this.uninstall(serverId);
                        out = $rh.createReply();
                    } catch (com.sun.corba.se.spi.activation.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerNotRegisteredHelper.write(out, $ex);
                    } catch (com.sun.corba.se.spi.activation.ServerAlreadyUninstalled $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerAlreadyUninstalledHelper.write(out, $ex);
                    }
                    break;
                }
            case 6:
                {
                    int $result[] = null;
                    $result = this.listRegisteredServers();
                    out = $rh.createReply();
                    com.sun.corba.se.spi.activation.ServerIdsHelper.write(out, $result);
                    break;
                }
            case 7:
                {
                    String $result[] = null;
                    $result = this.getApplicationNames();
                    out = $rh.createReply();
                    com.sun.corba.se.spi.activation.RepositoryPackage.StringSeqHelper.write(out, $result);
                    break;
                }
            case 8:
                {
                    try {
                        String applicationName = in.read_string();
                        int $result = (int) 0;
                        $result = this.getServerID(applicationName);
                        out = $rh.createReply();
                        out.write_long($result);
                    } catch (com.sun.corba.se.spi.activation.ServerNotRegistered $ex) {
                        out = $rh.createExceptionReply();
                        com.sun.corba.se.spi.activation.ServerNotRegisteredHelper.write(out, $ex);
                    }
                    break;
                }
            default:
                throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
        }
        return out;
    }

    private static String[] __ids = { "IDL:activation/Repository:1.0" };

    public String[] _ids() {
        return (String[]) __ids.clone();
    }
}
