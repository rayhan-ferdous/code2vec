    public PortletNameBean getPortletName(Long portletNameId) {

        if (portletNameId == null) {

            return null;

        }

        DatabaseAdapter db = null;

        ResultSet rs = null;

        PreparedStatement ps = null;

        try {

            db = DatabaseAdapter.getInstance();

            String sql = "select ID_SITE_CTX_TYPE, TYPE " + "from 	WM_PORTAL_PORTLET_NAME " + "where  ID_SITE_CTX_TYPE=? ";

            ps = db.prepareStatement(sql);

            ps.setLong(1, portletNameId);

            rs = ps.executeQuery();

            PortletNameBean bean = null;

            if (rs.next()) {

                bean = loadPortletNameFromResultSet(rs);

            }

            return bean;

        } catch (Exception e) {

            String es = "Error load portlet name for id: " + portletNameId;

            throw new IllegalStateException(es, e);

        } finally {

            DatabaseManager.close(db, rs, ps);

            db = null;

            rs = null;

            ps = null;

        }

    }
