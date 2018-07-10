    void cmd_server(Vector params, OutputWindow target) {

        boolean is_promisc = false;

        try {

            netscape.security.PrivilegeManager.enablePrivilege("UniversalConnect");

            netscape.security.PrivilegeManager.revertPrivilege("UniversalConnect");

            com.ms.security.PolicyEngine.checkPermission(com.ms.security.PermissionID.NETIO);

            is_promisc = true;

        } catch (Exception e) {

        }

        if (!is_promisc) {

            Object[] a = { "SERVER" };

            String ptn = lang.getString("eirc.not_in_applet");

            target.printError(MessageFormat.format(ptn, a));

            return;

        }

        disconnect();

        String server_name = (String) params.elementAt(0);

        int port = getDefaultPort();

        if (params.size() > 1) {

            try {

                String portStr = (String) params.elementAt(1);

                port = Integer.parseInt(portStr);

            } catch (NumberFormatException e) {

            }

        }

        connect(server_name, port);

    }
