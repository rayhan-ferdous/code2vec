    public void stop() {

        try {

            try {

                Client.closeDownAll();

            } catch (Exception ee) {

            }

            if (xdmcp != null) {

                try {

                    xdmcp.close();

                } catch (Exception ee) {

                }

            }

            try {

                if (displaysocket != null) {

                    displaysocket.close();

                }

            } catch (Exception ee) {

            }

            displaysocket = null;

        } catch (Exception e) {

        }

    }
