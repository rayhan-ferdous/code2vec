    public Object getTotalValueAt(String pColumn) {

        if (fTotal != null) {

            if ("id".equals(pColumn)) return new Long(fTotal.getId());

            if ("trxID".equals(pColumn)) return new Long(fTotal.getTrxID());

            if ("typeID".equals(pColumn)) return new Long(fTotal.getTypeID());

            if ("typeName".equals(pColumn)) return fTotal.getTypeName();

            if ("statusID".equals(pColumn)) return new Long(fTotal.getStatusID());

            if ("statusName".equals(pColumn)) return fTotal.getStatusName();

            if ("dbUser".equals(pColumn)) return fTotal.getDbUser();

            if ("userID".equals(pColumn)) return new Long(fTotal.getUserID());

            if ("modified".equals(pColumn)) return fTotal.getModified();

        }

        return null;

    }
