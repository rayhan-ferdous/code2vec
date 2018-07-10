        String myport = java.util.prefs.Preferences.systemRoot().get("portno", "8080");

        if (myport == null || myport.trim().equals("")) {

            myport = "80";

        }

        if (this.serverURL == null) {

            try {

                java.net.URL codebase = newgen.presentation.NewGenMain.getAppletInstance().getCodeBase();

                if (codebase != null) serverURL = codebase.getHost(); else serverURL = "localhost";

            } catch (Exception exp) {

                exp.printStackTrace();

                serverURL = "localhost";

            }

            newgen.presentation.component.IPAddressPortNoDialog ipdig = new newgen.presentation.component.IPAddressPortNoDialog(myurl, myport);

            ipdig.show();

            serverURL = myurl = ipdig.getIPAddress();

            myport = ipdig.getPortNo();

            java.util.prefs.Preferences.systemRoot().put("serverurl", serverURL);

            java.util.prefs.Preferences.systemRoot().put("portno", myport);

            System.out.println(serverURL);

        }
