    public static boolean isSoftPhonePluginInstalled(XMPPConnection con) {

        if (!con.isConnected()) {

            return false;

        }

        ServiceDiscoveryManager disco = ServiceDiscoveryManager.getInstanceFor(con);

        try {

            DiscoverItems items = disco.discoverItems(con.getServiceName());

            Iterator<DiscoverItems.Item> iter = items.getItems();

            while (iter.hasNext()) {

                DiscoverItems.Item item = iter.next();

                if ("SIP Controller".equals(item.getName())) {

                    Log.debug("SIP Controller Found");

                    return true;

                }

            }

        } catch (XMPPException e) {

            Log.error("isSparkPluginInstalled", e);

        }

        return false;

    }
