    public String getGroupId() {

        try {

            return comm.getMyGroup();

        } catch (Exception ex) {

            return "-1";

        }

    }
