    @Override

    protected HostValue[] handleGetHostsForSite(int siteId) throws Exception {

        HostValue[] hv = null;

        try {

            hv = getAdministrationServiceSpring().getHostsForSite(Integer.valueOf(siteId));

            if (hv == null) hv = new HostValue[0];

        } catch (UserException e) {

            log.error(e.getMessage());

        }

        return hv;

    }
